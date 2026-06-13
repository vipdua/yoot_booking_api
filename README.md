# Booking API - Service Booking Platform

## Giới Thiệu Tổng Quan

**Booking API** là một nền tảng backend RESTful được xây dựng bằng **Spring Boot 4.0.5** và **Java 21**, cung cấp các tính năng đầy đủ để quản lý và đặt lịch các dịch vụ. Hệ thống hỗ trợ xác thực người dùng, quản lý lịch làm việc của nhân viên, đặt lịch hẹn, thanh toán qua VNPay, cũng như gửi thông báo và trao đổi tin nhắn realtime thông qua WebSocket.

Mục tiêu của dự án là cung cấp một nền tảng hoàn chỉnh cho các doanh nghiệp cung cấp dịch vụ (salon, spa, trung tâm y tế, v.v.) để quản lý lịch hẹn và tương tác với khách hàng.

---

## Công Nghệ Sử Dụng

### Backend
- **Framework**: Spring Boot 4.0.5
- **Ngôn Ngữ**: Java 21
- **Build Tool**: Maven
- **Cơ Sở Dữ Liệu**: PostgreSQL
- **ORM**: Spring Data JPA + Hibernate

### Xác Thực & Bảo Mật
- **JWT (JSON Web Token)**: Xác thực người dùng và quản lý phiên
- **Spring Security**: Bảo mật ứng dụng
- **OTP (One-Time Password)**: Xác thực hai yếu tố

### Tích Hợp Bên Ngoài
- **Cloudinary**: Lưu trữ và quản lý hình ảnh đám mây
- **VNPay**: Xử lý thanh toán trực tuyến (Việt Nam)
- **Gmail SMTP**: Gửi email thông báo
- **WebSocket**: Giao tiếp realtime cho chat và thông báo

### Công Cụ Hỗ Trợ
- **MapStruct**: Ánh xạ dữ liệu giữa Entity và DTO
- **Lombok**: Giảm boilerplate code
- **Flyway**: Quản lý migration cơ sở dữ liệu
- **SpringDoc OpenAPI**: Tài liệu API tự động (Swagger)
- **Thymeleaf**: Template engine cho email HTML
- **dotenv**: Quản lý biến môi trường

---

## Cấu Trúc Project

```
booking-api/
├── src/
│   ├── main/
│   │   ├── java/com/yoot/booking/api/
│   │   │   ├── BookingApiApplication.java          # Entry point của ứng dụng
│   │   │   ├── common/                             # Utilities chung
│   │   │   │   ├── exception/                      # Custom exceptions
│   │   │   │   ├── otp/                            # Logic OTP
│   │   │   │   ├── response/                       # Định dạng response chung
│   │   │   │   └── validation/                     # Custom validators
│   │   │   ├── config/                             # Cấu hình ứng dụng
│   │   │   │   ├── AsyncConfig.java                # Cấu hình async processing
│   │   │   │   ├── CloudinaryConfig.java           # Cấu hình Cloudinary
│   │   │   │   ├── JacksonConfig.java              # Cấu hình JSON serialization
│   │   │   │   ├── JpaConfig.java                  # Cấu hình JPA
│   │   │   │   ├── OpenApiConfig.java              # Cấu hình Swagger
│   │   │   │   ├── SecurityConfig.java             # Cấu hình Spring Security
│   │   │   │   ├── WebSocketConfig.java            # Cấu hình WebSocket
│   │   │   │   ├── UserHandshakeHandler.java       # Handler cho WebSocket
│   │   │   │   ├── WebSocketAuthInterceptor.java   # Interceptor xác thực WebSocket
│   │   │   │   ├── SocketPrincipal.java            # Principal object cho WebSocket
│   │   │   │   └── VnPayConfig.java                # Cấu hình VNPay
│   │   │   ├── controller/                         # REST Controllers
│   │   │   │   ├── AppointmentController.java      # API đặt lịch hẹn
│   │   │   │   ├── AuthController.java             # API xác thực
│   │   │   │   ├── BannerController.java           # API banner/quảng cáo
│   │   │   │   ├── BlockedSlotController.java      # API khóa khung giờ
│   │   │   │   ├── BookingServiceController.java   # API dịch vụ đặt lịch
│   │   │   │   ├── ChatSocketController.java       # WebSocket chat
│   │   │   │   ├── MenuController.java             # API menu dịch vụ
│   │   │   │   ├── MessageController.java          # API tin nhắn
│   │   │   │   ├── NotificationController.java     # API thông báo
│   │   │   │   ├── ScheduleController.java         # API lịch làm việc
│   │   │   │   ├── ServiceCategoryController.java  # API danh mục dịch vụ
│   │   │   │   ├── SlotController.java             # API khung giờ
│   │   │   │   ├── StaffController.java            # API quản lý nhân viên
│   │   │   │   └── UserController.java             # API quản lý người dùng
│   │   │   ├── dto/                                # Data Transfer Objects
│   │   │   │   ├── appointment/                    # DTO cho lịch hẹn
│   │   │   │   ├── auth/                           # DTO cho xác thực
│   │   │   │   ├── banner/                         # DTO cho banner
│   │   │   │   ├── schedule/                       # DTO cho lịch làm việc
│   │   │   │   ├── slot/                           # DTO cho khung giờ
│   │   │   │   ├── staff/                          # DTO cho nhân viên
│   │   │   │   ├── user/                           # DTO cho người dùng
│   │   │   │   └── ...                             # Các DTO khác
│   │   │   ├── entity/                             # JPA Entities (Models)
│   │   │   │   ├── Appointment.java                # Entity lịch hẹn
│   │   │   │   ├── AppointmentStatus.java          # Enum trạng thái lịch hẹn
│   │   │   │   ├── Banner.java                     # Entity banner
│   │   │   │   ├── BookingService.java             # Entity dịch vụ
│   │   │   │   ├── Schedule.java                   # Entity lịch làm việc
│   │   │   │   ├── Staff.java                      # Entity nhân viên
│   │   │   │   ├── User.java                       # Entity người dùng
│   │   │   │   ├── Conversation.java               # Entity cuộc trò chuyện
│   │   │   │   ├── Message.java                    # Entity tin nhắn
│   │   │   │   ├── Notification.java               # Entity thông báo
│   │   │   │   ├── PaymentStatus.java              # Enum trạng thái thanh toán
│   │   │   │   ├── Role.java                       # Enum vai trò người dùng
│   │   │   │   └── ...                             # Các entity khác
│   │   │   ├── mapper/                             # MapStruct Mappers
│   │   │   │   └── [Các mapper interface để chuyển đổi Entity <-> DTO]
│   │   │   ├── repository/                         # JPA Repositories (Data Access Layer)
│   │   │   │   └── [Các repository interface cho từng Entity]
│   │   │   ├── service/                            # Business Logic Layer
│   │   │   │   ├── AppointmentService.java
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── BannerService.java
│   │   │   │   ├── BookingServiceService.java
│   │   │   │   ├── EmailService.java               # Gửi email
│   │   │   │   ├── FileStorageService.java         # Lưu trữ tập tin
│   │   │   │   ├── MenuService.java
│   │   │   │   ├── MessageService.java
│   │   │   │   ├── NotificationService.java
│   │   │   │   ├── OtpService.java
│   │   │   │   ├── ScheduleService.java
│   │   │   │   ├── SlotService.java
│   │   │   │   ├── StaffService.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── VnPayService.java               # Xử lý thanh toán VNPay
│   │   │   │   └── impl/                           # Triển khai của các Service
│   │   │   ├── security/                           # Security-related classes
│   │   │   │   └── [JWT filters, custom authorities, etc.]
│   │   │   └── ...
│   │   └── resources/
│   │       ├── application.properties              # Cấu hình ứng dụng
│   │       ├── templates/
│   │       │   └── email/                          # Email templates
│   │       │       ├── appointment-confirmed.html  # Email xác nhận lịch hẹn
│   │       │       ├── appointment-paid.html       # Email xác nhận thanh toán
│   │       │       └── otp-email.html              # Email OTP
│   │       └── static/                             # Static resources
│   └── test/
│       └── java/com/yoot/booking/api/
│           └── [Các test cases]
├── pom.xml                                          # Maven configuration
├── mvnw                                             # Maven wrapper (Linux/Mac)
├── mvnw.cmd                                         # Maven wrapper (Windows)
└── HELP.md                                          # Tài liệu hỗ trợ
```

---

## Cơ Sở Dữ Liệu (Database Schema)

### Các Entities Chính

#### 1. **User** - Người Dùng
- `id`: UUID của người dùng
- `email`: Email (unique)
- `phoneNumber`: Số điện thoại
- `name`: Tên người dùng
- `password`: Mật khẩu (được hash)
- `role`: Vai trò (CUSTOMER, STAFF, ADMIN)
- `avatar`: URL ảnh đại diện trên Cloudinary
- `status`: Trạng thái (ACTIVE, INACTIVE, BANNED)
- `createdAt`, `updatedAt`: Timestamp

#### 2. **Staff** - Nhân Viên
- `id`: UUID của nhân viên
- `user`: Tham chiếu đến User
- `specialization`: Chuyên môn
- `bio`: Tiểu sử
- `rating`: Đánh giá trung bình
- `isAvailable`: Trạng thái khả dụng
- `createdAt`, `updatedAt`: Timestamp

#### 3. **BookingService** - Dịch Vụ Đặt Lịch
- `id`: UUID của dịch vụ
- `name`: Tên dịch vụ
- `description`: Mô tả
- `price`: Giá dịch vụ
- `duration`: Thời lượng dịch vụ (phút)
- `category`: Danh mục dịch vụ
- `image`: Hình ảnh dịch vụ
- `createdAt`, `updatedAt`: Timestamp

#### 4. **ServiceCategory** - Danh Mục Dịch Vụ
- `id`: UUID
- `name`: Tên danh mục
- `description`: Mô tả
- `icon`: Icon danh mục

#### 5. **Schedule** - Lịch Làm Việc
- `id`: UUID
- `staff`: Tham chiếu đến Staff
- `dayOfWeek`: Ngày trong tuần (0-6)
- `startTime`: Giờ bắt đầu
- `endTime`: Giờ kết thúc
- `isActive`: Trạng thái hoạt động

#### 6. **Slot** - Khung Giờ Có Sẵn
- `id`: UUID
- `staff`: Tham chiếu đến Staff
- `service`: Tham chiếu đến BookingService
- `date`: Ngày
- `startTime`: Giờ bắt đầu
- `endTime`: Giờ kết thúc
- `isAvailable`: Trạng thái khả dụng
- `createdAt`: Timestamp

#### 7. **BlockedSlot** - Khung Giờ Bị Khóa
- `id`: UUID
- `staff`: Tham chiếu đến Staff
- `date`: Ngày bị khóa
- `startTime`: Giờ bắt đầu
- `endTime`: Giờ kết thúc
- `reason`: Lý do khóa

#### 8. **Appointment** - Lịch Hẹn
- `id`: UUID
- `user`: Tham chiếu đến User (khách hàng)
- `staff`: Tham chiếu đến Staff
- `service`: Tham chiếu đến BookingService
- `appointmentDate`: Ngày hẹn
- `startTime`: Giờ bắt đầu
- `endTime`: Giờ kết thúc
- `status`: Trạng thái (PENDING, CONFIRMED, COMPLETED, CANCELLED)
- `paymentStatus`: Trạng thái thanh toán (UNPAID, PAID, REFUNDED)
- `notes`: Ghi chú
- `cancellationReason`: Lý do hủy (nếu có)
- `cancellationTime`: Thời gian hủy
- `createdAt`, `updatedAt`: Timestamp

#### 9. **Message** - Tin Nhắn
- `id`: UUID
- `conversation`: Tham chiếu đến Conversation
- `sender`: Tham chiếu đến User
- `content`: Nội dung tin nhắn
- `mediaType`: Loại media (TEXT, IMAGE, FILE)
- `mediaUrl`: URL của media (nếu có)
- `isRead`: Đã đọc hay chưa
- `createdAt`: Timestamp

#### 10. **Conversation** - Cuộc Trò Chuyện
- `id`: UUID
- `participant1`: Tham chiếu đến User
- `participant2`: Tham chiếu đến User
- `lastMessage`: Tin nhắn cuối cùng
- `lastMessageTime`: Thời gian tin nhắn cuối cùng
- `createdAt`, `updatedAt`: Timestamp

#### 11. **Notification** - Thông Báo
- `id`: UUID
- `user`: Tham chiếu đến User
- `title`: Tiêu đề
- `message`: Nội dung thông báo
- `targetType`: Loại đối tượng (APPOINTMENT, MESSAGE, PROMOTION)
- `targetId`: ID của đối tượng
- `isRead`: Đã đọc hay chưa
- `createdAt`: Timestamp

#### 12. **Banner** - Banner/Quảng Cáo
- `id`: UUID
- `title`: Tiêu đề
- `description`: Mô tả
- `image`: Hình ảnh
- `position`: Vị trí hiển thị (HOME_TOP, HOME_BOTTOM, PROMO)
- `isActive`: Kích hoạt
- `createdAt`, `updatedAt`: Timestamp

#### 13. **Menu** - Menu Dịch Vụ
- `id`: UUID
- `name`: Tên menu
- `description`: Mô tả
- `type`: Loại menu (CATEGORY, FEATURED)
- `services`: Danh sách dịch vụ
- `createdAt`, `updatedAt`: Timestamp

---

## Các Chức Năng Chính

### 1. **Xác Thực & Phân Quyền** (AuthController)
- Đăng ký tài khoản (Register)
- Đăng nhập (Login) - trả về JWT Token
- Refresh Token
- Đăng xuất (Logout)
- Xác thực OTP qua email
- Quên mật khẩu & đặt lại mật khẩu
- Phân quyền: CUSTOMER, STAFF, ADMIN

### 2. **Quản Lý Người Dùng** (UserController)
- Lấy thông tin cá nhân
- Cập nhật hồ sơ người dùng
- Tải lên ảnh đại diện (Cloudinary)
- Thay đổi mật khẩu
- Quản lý địa chỉ
- Xem lịch sử đặt lịch

### 3. **Quản Lý Dịch Vụ** (BookingServiceController & ServiceCategoryController)
- Tạo/cập nhật/xóa dịch vụ (Admin & Staff)
- Danh sách dịch vụ theo danh mục
- Tìm kiếm dịch vụ
- Quản lý danh mục dịch vụ
- Lọc dịch vụ theo giá, đánh giá, v.v.

### 4. **Quản Lý Lịch Làm Việc** (ScheduleController & SlotController)
- Tạo lịch làm việc cho nhân viên (theo ngày trong tuần)
- Cập nhật giờ làm việc
- Tạo khung giờ có sẵn (Slot)
- Xem các khung giờ trống
- Quản lý khung giờ bị khóa (BlockedSlot)

### 5. **Đặt Lịch Hẹn** (AppointmentController)
- Đặt lịch hẹn dịch vụ
- Xem lịch hẹn của khách hàng
- Xem lịch hẹn của nhân viên
- Xác nhận lịch hẹn (Staff)
- Hoàn thành lịch hẹn
- Hủy lịch hẹn (với lý do)
- Tìm kiếm và lọc lịch hẹn

### 6. **Thanh Toán** (VnPayService)
- Tích hợp VNPay để thanh toán trực tuyến
- Tạo URL thanh toán
- Xác minh kết quả thanh toán
- Cập nhật trạng thái thanh toán
- Hoàn tiền (Refund)

### 7. **Tin Nhắn & Chat** (MessageController & ChatSocketController)
- Gửi tin nhắn giữa khách hàng và nhân viên
- WebSocket realtime chat
- Hỗ trợ text, image, file
- Đánh dấu tin nhắn là đã đọc
- Xem lịch sử cuộc trò chuyện

### 8. **Thông Báo** (NotificationController)
- Gửi thông báo cho người dùng
- Thông báo lịch hẹn sắp tới
- Thông báo khuyến mãi
- Thông báo tin nhắn mới
- Đánh dấu thông báo là đã đọc

### 9. **Email** (EmailService)
- Gửi email xác nhận đặt lịch
- Gửi email xác nhận thanh toán
- Gửi email OTP
- Gửi email nhắc nhở lịch hẹn
- Template email HTML (Thymeleaf)

### 10. **Quản Lý Banner/Quảng Cáo** (BannerController)
- Tạo/cập nhật/xóa banner
- Kích hoạt/vô hiệu hóa banner
- Quản lý vị trí hiển thị

### 11. **Quản Lý Menu** (MenuController)
- Tạo menu dịch vụ
- Thêm dịch vụ vào menu
- Sắp xếp dịch vụ trong menu

### 12. **Quản Lý Nhân Viên** (StaffController)
- Thêm nhân viên mới
- Cập nhật thông tin nhân viên
- Xem danh sách nhân viên
- Quản lý chuyên môn
- Xem đánh giá nhân viên

---

## Các Trường Hợp Sử Dụng (Use Cases)

### **1. Khách Hàng Đặt Lịch Hẹn**
1. Khách hàng đăng nhập vào ứng dụng
2. Xem danh sách dịch vụ và nhân viên
3. Chọn dịch vụ và nhân viên mong muốn
4. Xem các khung giờ trống (Slots)
5. Chọn ngày và giờ hẹn
6. Xem lại thông tin và xác nhận
7. Thanh toán qua VNPay (tùy chọn)
8. Nhận email xác nhận lịch hẹn
9. Nhận thông báo nhắc nhở trước lịch hẹn

### **2. Nhân Viên Quản Lý Lịch Làm Việc**
1. Nhân viên đăng nhập
2. Cấu hình lịch làm việc (ngày/giờ hoạt động)
3. Tạo khung giờ có sẵn cho mỗi dịch vụ
4. Khóa khung giờ khi không có sẵn (nghỉ, bận)
5. Xem lịch hẹn của mình
6. Xác nhận lịch hẹn
7. Cập nhật trạng thái lịch hẹn

### **3. Quản Trị Viên Quản Lý Hệ Thống**
1. Quản lý người dùng (khóa, kích hoạt)
2. Quản lý dịch vụ và danh mục
3. Quản lý nhân viên
4. Xem báo cáo thống kê
5. Quản lý banner/quảng cáo
6. Cấu hình cài đặt hệ thống

### **4. Khách Hàng Giao Tiếp với Nhân Viên**
1. Khách hàng mở chat với nhân viên
2. Gửi tin nhắn text hoặc hình ảnh
3. Nhân viên trả lời tin nhắn realtime
4. Cả hai nhận thông báo tin nhắn mới
5. Xem lịch sử cuộc trò chuyện

### **5. Thanh Toán Lịch Hẹn**
1. Khách hàng chọn thanh toán
2. Hệ thống tạo URL thanh toán VNPay
3. Khách hàng được chuyển hướng đến VNPay
4. Nhập thông tin thẻ/tài khoản và thanh toán
5. VNPay trả về kết quả thanh toán
6. Hệ thống cập nhật trạng thái thanh toán
7. Khách hàng nhận email xác nhận thanh toán

---

## Hướng Dẫn Chạy Source Code

### **Yêu Cầu Tiên Quyết**
- **Java 21** trở lên (JDK 21+)
- **Maven 3.6+**
- **PostgreSQL 12+** (hoặc H2 Database cho test)
- **Git** (tùy chọn)

### **Bước 1: Clone hoặc Tải Project**
```bash
# Clone từ Git (nếu có)
git clone https://github.com/yourusername/booking-api.git
cd booking-api

# Hoặc tải file zip và giải nén
```

### **Bước 2: Cấu Hình Biến Môi Trường**
Tạo file `.env` ở thư mục gốc project và thêm các biến sau:

```properties
# JWT Configuration
JWT_SECRET=your-secret-key-here-min-32-chars-long
JWT_ISSUER=booking-api
JWT_AUDIENCE=booking-app
JWT_ACCESS_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Frontend URLs (CORS)
FRONTEND_URLS=http://localhost:3000,http://localhost:5173

# Email Configuration (Gmail)
EMAIL_USERNAME=your-gmail@gmail.com
EMAIL_PASSWORD=your-app-password

# Cloudinary Configuration
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret

# VNPay Configuration (Vietnam Payment Gateway)
VNPAY_URL=https://sandbox.vnpayment.vn/paygate/pay
VNPAY_API=https://sandbox.vnpayment.vn/merchant_webapi/api/transaction
VNPAY_TMN_CODE=your-terminal-code
VNPAY_HASH_SECRET=your-hash-secret
VNPAY_RETURN_URL=http://localhost:8080/api/v1/vnpay/return
VNPAY_FRONTEND_RETURN_URL=http://localhost:3000/payment-result
```

**Lưu ý**: 
- Để lấy Gmail App Password, cần bật 2FA và tạo app password tại https://myaccount.google.com/apppasswords
- Lấy Cloudinary credentials từ https://cloudinary.com
- Lấy VNPay credentials từ https://sandbox.vnpayment.vn

### **Bước 3: Cấu Hình Cơ Sở Dữ Liệu**
Chỉnh sửa file `src/main/resources/application.properties`:

```properties
# PostgreSQL Configuration (mặc định)
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/booking_api
spring.datasource.username=postgres
spring.datasource.password=123456

# Hoặc sử dụng H2 Database cho test
# spring.datasource.url=jdbc:h2:mem:testdb
# spring.datasource.driver-class-name=org.h2.Driver
```

**Tạo cơ sở dữ liệu PostgreSQL**:
```bash
# Kết nối tới PostgreSQL
psql -U postgres

# Tạo database
CREATE DATABASE booking_api;

# Thoát
\q
```

### **Bước 4: Build Project**
```bash
# Sử dụng Maven để build project
mvn clean install

# Hoặc sử dụng Maven Wrapper (không cần cài Maven)
mvnw clean install

# Trên Windows
mvnw.cmd clean install
```

### **Bước 5: Chạy Ứng Dụng**

**Cách 1: Sử dụng Maven**
```bash
mvn spring-boot:run
```

**Cách 2: Chạy file JAR**
```bash
mvn clean package
java -jar target/booking-api-0.0.1-SNAPSHOT.jar
```

**Cách 3: Sử dụng IDE (IntelliJ IDEA / Eclipse)**
- Mở project trong IDE
- Right-click vào `BookingApiApplication.java`
- Chọn `Run 'BookingApiApplication.main()'`

### **Bước 6: Truy Cập Ứng Dụng**

Ứng dụng sẽ chạy tại: **http://localhost:8080**

**API Documentation (Swagger UI)**: 
- URL: **http://localhost:8080/swagger**
- JSON API Docs: **http://localhost:8080/v3/api-docs**

**Các endpoints chính**:
- `POST /api/v1/auth/register` - Đăng ký
- `POST /api/v1/auth/login` - Đăng nhập
- `GET /api/v1/services` - Lấy danh sách dịch vụ
- `POST /api/v1/appointments` - Đặt lịch hẹn
- `GET /api/v1/appointments/my-appointments` - Lịch hẹn của tôi
- `POST /api/v1/messages/send` - Gửi tin nhắn
- `WS /ws/chat` - WebSocket chat endpoint

## Troubleshooting

### **1. Lỗi "PostgreSQL connection refused"**
- Kiểm tra PostgreSQL đang chạy
- Kiểm tra host, port, username, password đúng
- Tạo database `booking_api`

### **2. Lỗi "JWT_SECRET not found"**
- Đảm bảo file `.env` tồn tại ở thư mục gốc project
- Kiểm tra tất cả biến môi trường đã được thiết lập

### **3. Lỗi "Cloudinary credentials invalid"**
- Kiểm tra CLOUDINARY_CLOUD_NAME, API_KEY, API_SECRET
- Lấy lại credentials từ Cloudinary dashboard

### **4. Lỗi "Port 8080 already in use"**
```bash
# Sử dụng port khác
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

---

## Tài Liệu Bổ Sung

- **Spring Boot Documentation**: https://spring.io/projects/spring-boot
- **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
- **Spring Security**: https://spring.io/projects/spring-security
- **JWT**: https://jwt.io
- **VNPay**: https://sandbox.vnpayment.vn
- **Cloudinary**: https://cloudinary.com/documentation
- **OpenAPI/Swagger**: https://springdoc.org/

---

## Team & Contributors

**Dự án được phát triển bởi**: Trần Hoà Long