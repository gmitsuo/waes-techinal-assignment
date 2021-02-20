#Technical Assignment

## Requirements

* Provide 2 http endpoints that accepts JSON base64 encoded binary data on both endpoints.
```
<host>/v1/diff/<ID>/left 
<host>/v1/diff/<ID>/right
```

* The provided data needs to be diff-ed and the results shall be available on a third end point.
```
<host>/v1/diff/<ID>
```
* The results shall provide the following info in JSON format.
    * If equal return that;
    * If not of equal size just return that;
    * If of same size provide insight in where the diffs are, actual diffs are not needed.
        * So mainly offsets + length in the data.
    * Make assumptions in the implementation explicit, choices are good but need to be communicated
    
### Required:
* JDK 11

### Optional:
* Docker

### How to run:
- Tests:
    - `./mvnw clean test`
- Build:
    - Jar with maven: `./mvnw clean package`. Jar available at `./target/waes-technical-assigment-0.1.jar`.
    - Docker image: make sure docker is running and `./mvn clean spring-boot:build-image`
- The app:
    - Maven: `./mvnw spring-boot:run [-Dspring-boot.run.arguments=--server.port=8080]`
    - Jar: `java -jar ./target/waes-technical-assigment-0.1.jar [--server.port=8080]`
    - Docker container: `docker run --name waes-technical-assignment --rm -p 8080:8080 waes-technical-assigment:0.1`