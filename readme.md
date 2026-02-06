# Steps to run:
1. Build the application: `./gradlew clean build`
1. Build the docker image: `docker build -t usermetadata:1.0 .`
1. Run the image on port(8282 in this case): `docker run -d -p 8282:8282 --name usermetadata usermetadata:1.0`

# Endpoints:
1. Create User:
```
POST 'http://localhost:8282/users/' \
  --header 'Idempotency-Key: #{idempotency_key}#' \
  --header 'Content-Type: application/json' \
  --body '{
    "name": "#{name}#",
    "email": "#{email}#",
    "phone": "#{phone}#"
}'
```

1. Get User:
```
GET 'http://localhost:8282/users/#{user_id}#'
```

1. Health Check:
```
GET 'http://localhost:8282/actuator/health'
```

1. Metrics
```
GET 'http://localhost:8282/actuator/prometheus'
```