package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.exception.ResourceNotFoundException;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.appointment.*;
import com.yoot.booking.api.entity.*;
import com.yoot.booking.api.mapper.AppointmentMapper;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.repository.*;
import com.yoot.booking.api.service.AppointmentService;
import com.yoot.booking.api.service.EmailService;
import com.yoot.booking.api.service.VnPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final StaffRepository staffRepository;
    private final BookingServiceRepository serviceRepository;
    private final ScheduleRepository scheduleRepository;
    private final AppointmentMapper mapper;
    private final PaginationMapper paginationMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final VnPayService vnPayService;

    // ================= GET CURRENT USER =================
    private User getCurrentUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    // ================= CREATE =================
    @Override
    @Transactional
    public ResultDTO<AppointmentResponseDTO> create(
            AppointmentCreateDTO dto
    ) {

        Staff staff = staffRepository
                .findByIdAndIsActiveTrue(dto.staffId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Staff",
                                dto.staffId()
                        )
                );

        BookingService service = serviceRepository
                .findById(dto.serviceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service",
                                dto.serviceId()
                        )
                );

        // ================= VALIDATE TIME =================
        if (
                dto.endTime()
                        .isBefore(dto.startTime())
        ) {

            throw new IllegalArgumentException(
                    "Thời gian kết thúc phải sau thời gian bắt đầu"
            );
        }

        // ================= VALIDATE STAFF SERVICE =================
        boolean supportsService =
                staff.getServices()
                        .stream()
                        .anyMatch(s ->
                                s.getId()
                                        .equals(service.getId())
                        );

        if (!supportsService) {

            throw new IllegalStateException(
                    "Nhân viên không hỗ trợ dịch vụ này"
            );
        }

        // ================= CHECK SCHEDULE =================
        boolean validSchedule =
                scheduleRepository
                        .existsByStaffIdAndWorkDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                                dto.staffId(),
                                dto.startTime().toLocalDate(),
                                dto.startTime().toLocalTime(),
                                dto.endTime().toLocalTime()
                        );

        if (!validSchedule) {

            throw new IllegalArgumentException(
                    "Slot không thuộc lịch làm việc"
            );
        }

        // ================= CHECK OVERLAP =================
        boolean overlap =
                appointmentRepository
                        .existsByStaffIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
                                dto.staffId(),
                                List.of(
                                        AppointmentStatus.PENDING,
                                        AppointmentStatus.CONFIRMED
                                ),
                                dto.endTime(),
                                dto.startTime()
                        );

        if (overlap) {

            throw new IllegalStateException(
                    "Slot đã được đặt"
            );
        }

        User user = getCurrentUser();

        // ================= CREATE APPOINTMENT =================
        Appointment appointment =
                Appointment.builder()

                        // USER
                        .user(user)

                        // STAFF
                        .staff(staff)

                        // SERVICE
                        .service(service)

                        // CUSTOMER
                        .customerName(dto.customerName())

                        .customerPhone(dto.customerPhone())

                        .customerEmail(dto.customerEmail())

                        .note(dto.note())

                        // PRICE
                        .totalPrice(service.getPrice())

                        // TIME
                        .startTime(dto.startTime())

                        .endTime(dto.endTime())

                        // STATUS
                        .status(AppointmentStatus.PENDING)

                        .paymentStatus(PaymentStatus.UNPAID)

                        .build();

        var saved =
                appointmentRepository.save(appointment);

        return ResultDTO.success(
                mapper.toDTO(saved),
                "Đặt lịch thành công"
        );
    }

    // CONFIRM (STAFF / ADMIN)
    @Transactional
    public ResultDTO<AppointmentResponseDTO> confirm(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Chỉ có thể xác nhận lịch PENDING");
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);

        appointmentRepository.save(appointment);

        emailService.sendAppointmentConfirmedEmail(appointment);

        return ResultDTO.success(mapper.toDTO(appointment), "Xác nhận lịch thành công");
    }

    // MARK PAID (STAFF xác nhận đã nhận tiền)
    @Transactional
    public ResultDTO<AppointmentResponseDTO> markPaid(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Phải xác nhận trước");
        }

        if (appointment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Đã thanh toán rồi");
        }

        appointment.setPaymentStatus(PaymentStatus.PAID);

        try {
            emailService.sendAppointmentPaidEmail(appointment);
        } catch (Exception e) {
            // chỉ log, không làm fail business
            System.err.println("Send mail failed: " + e.getMessage());
        }

        return ResultDTO.success(mapper.toDTO(appointment), "Đã xác nhận thanh toán");
    }

    // COMPLETE (kết thúc dịch vụ)
    @Transactional
    public ResultDTO<AppointmentResponseDTO> complete(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Chưa xác nhận lịch");
        }

        if (appointment.getPaymentStatus() != PaymentStatus.PAID) {
            throw new IllegalStateException("Chưa thanh toán");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        return ResultDTO.success(mapper.toDTO(appointment), "Hoàn thành dịch vụ");
    }

    @Transactional
    public ResultDTO<VnPayPaymentResponseDTO> createPayment(Long id, String clientIp) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));

        User user = getCurrentUser();

        // check owner
        if (!appointment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bạn không có quyền thanh toán lịch này");
        }

        if (appointment.getPaymentStatus() != PaymentStatus.UNPAID) {
            throw new IllegalStateException("Lịch đã thanh toán");
        }

        String paymentUrl = vnPayService.createPaymentUrl(
                "APT_" + appointment.getId(),
                appointment.getTotalPrice().longValue(),
                "Thanh toan lich hen #" + appointment.getId(),
                clientIp
        );

        return ResultDTO.success(
                new VnPayPaymentResponseDTO(paymentUrl),
                "Tạo link thanh toán thành công"
        );
    }

    @Transactional
    public String handleVnPayCallback(Map<String, String> params) {

        boolean valid = vnPayService.verifyCallback(params);

        if (!valid) {
            return "Thanh toán thất bại (Sai chữ ký)";
        }

        String responseCode = params.get("vnp_ResponseCode");
        String txnRef = params.get("vnp_TxnRef"); // APT_1

        Long appointmentId = Long.parseLong(txnRef.replace("APT_", ""));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        if ("00".equals(responseCode)) {
            appointment.setPaymentStatus(PaymentStatus.PAID);

            try {
                emailService.sendAppointmentPaidEmail(appointment);
            } catch (Exception e) {
                System.err.println("Send email failed");
            }

            return "Thanh toán thành công";
        } else {
            // FAIL
            appointment.setPaymentStatus(PaymentStatus.FAILED);

            return "Thanh toán thất bại: " + responseCode;
        }
    }

    // ================= GET MY =================
    @Override
    public ResultListDTO<AppointmentResponseDTO> getMyAppointments(PagingRequestDTO request) {

        var pageable = request.toPageable();

        User user = getCurrentUser();

        Page<Appointment> page =
                appointmentRepository.findAllByUserId(user.getId(), pageable);

        var data = page.getContent()
                .stream()
                .map(mapper::toDTO)
                .toList();

        var pagination = paginationMapper.toPagination(page);

        return ResultListDTO.success(data, "Lấy lịch của bạn thành công", pagination);
    }

    // ================= GET ALL (ADMIN) =================
    @Override
    public ResultListDTO<AppointmentResponseDTO> getAll(PagingRequestDTO request) {

        var page = appointmentRepository.findAll(request.toPageable());


        var data = page.getContent()
                .stream()
                .map(mapper::toDTO)
                .toList();

        var pagination = paginationMapper.toPagination(page);

        return ResultListDTO.success(data, "Lấy tất cả lịch thành công", pagination);
    }

    // ================= CANCEL =================
    @Override
    @Transactional
    public ResultNoDataDTO cancel(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));

        User user = getCurrentUser();

        // check quyền
        if (!appointment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bạn không có quyền hủy lịch này");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return ResultNoDataDTO.success("Hủy lịch thành công");
    }

    // ================= RESCHEDULE =================
    @Override
    @Transactional
    public ResultDTO<AppointmentResponseDTO> reschedule(Long id, AppointmentRescheduleDTO dto) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));

        User user = getCurrentUser();

        // check quyền
        if (!appointment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bạn không có quyền đổi lịch này");
        }

        // check rule
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Chỉ được đổi lịch khi chưa xác nhận");
        }

        // check schedule
        boolean validSchedule = scheduleRepository
                .existsByStaffIdAndWorkDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        appointment.getStaff().getId(),
                        dto.startTime().toLocalDate(),
                        dto.startTime().toLocalTime(),
                        dto.endTime().toLocalTime()
                );

        if (!validSchedule) {
            throw new IllegalArgumentException("Slot không thuộc lịch làm việc");
        }

        boolean overlap = appointmentRepository
                .existsByStaffIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
                        appointment.getStaff().getId(),
                        List.of(
                                AppointmentStatus.PENDING,
                                AppointmentStatus.CONFIRMED
                        ),
                        dto.endTime(),
                        dto.startTime()
                );

        if (overlap) {
            throw new IllegalStateException("Slot mới bị trùng");
        }

        appointment.setStartTime(dto.startTime());
        appointment.setEndTime(dto.endTime());

        return ResultDTO.success(mapper.toDTO(appointment), "Đổi lịch thành công");
    }
}