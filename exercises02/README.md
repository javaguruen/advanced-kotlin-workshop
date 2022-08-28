# The application
This is a SpringBoot application with one REST controller supporting one method
`POST http://localhost:8080/api/v1/Users`

## Testing with curl
`curl -v -X POST http://localhost:8080/api/v1/users/  -H "Content-Type: application/json" -d '{"email": "uname@gmail.com", "username": "uname"}'`

## Testing with IntelliJ
- Open a new scratch file (command-shift-n) and choose type http
- Type
 
```
POST http://localhost:8080/api/v1/users/
Content-Type: application/json

{
  "email": "uname@gmail.com",
  "username": "uname"
}
```
