package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.exception.ResourceNotFoundException;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.appointment.*;
import com.yoot.booking.api.entity.*;
import com.yoot.booking.api.mapper.AppointmentMapper;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.repository.*;
import com.yoot.booking.api.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public ResultDTO<AppointmentResponseDTO> create(AppointmentCreateDTO dto) {

        Staff staff = staffRepository.findByIdAndIsActiveTrue(dto.staffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff", dto.staffId()));

        BookingService service = serviceRepository.findById(dto.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service", dto.serviceId()));

        // check schedule
        boolean validSchedule = scheduleRepository
                .existsByStaffIdAndWorkDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        dto.staffId(),
                        dto.startTime().toLocalDate(),
                        dto.startTime().toLocalTime(),
                        dto.endTime().toLocalTime()
                );

        if (!validSchedule) {
            throw new IllegalArgumentException("Slot không thuộc lịch làm việc");
        }

        boolean overlap = appointmentRepository
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
            throw new IllegalStateException("Slot đã được đặt");
        }

        User user = getCurrentUser();

        Appointment appointment = Appointment.builder()
                .staff(staff)
                .service(service)
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .status(AppointmentStatus.PENDING)
                .paymentStatus(PaymentStatus.UNPAID)
                .user(user)
                .build();

        return ResultDTO.success(
                mapper.toDTO(appointmentRepository.save(appointment)),
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