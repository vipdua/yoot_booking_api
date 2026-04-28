# Architecture

Flow:
Controller → Service → Repository → Entity

DTO:
- CreateDTO
- UpdateDTO
- ResponseDTO

Mapper:
- MapStruct

Response:
- ResultDTO<T>
- ResultListDTO<T>

Exception:
- GlobalExceptionHandler
- ResourceNotFoundException