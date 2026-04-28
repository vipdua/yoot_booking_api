# BookingService Flow

Controller:
- receive request
- return ResultDTO

Service:
- business logic

Repository:
- JpaRepository

Entity:
- BookingService (id, name, duration, price, isActive)

Mapper:
- convert DTO <-> Entity

Paging:
- PagingRequestDTO
- PaginationDTO