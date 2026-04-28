# API Contract

All API must return:

ResultDTO<T>
ResultListDTO<T>

Error:
- success = false
- message = error message

Success:
- success = true
- data != null