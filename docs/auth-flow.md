# Auth Flow

Register:
- save user
- bcrypt password

Login:
- check password
- generate accessToken + refreshToken

Refresh:
- validate refreshToken
- generate new accessToken

Security:
- JwtAuthenticationFilter
- SecurityContextHolder