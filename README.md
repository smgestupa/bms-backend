# bms-backend
The Spring Boot back-end application for our mobile app college project

This uses the Spring Boot framework and acts as a REST API endpoint, which is ran in a cloud server -- such as Linode -- which is then connected to MariaDB.

## Cloning the Repository
> Make sure to clone the [bms-utils repository](https://github.com/laazyCmd/bms-utils) in the same location as this one for the image analysis to work

1. Go to your preferred directory
2. Open your terminal and execute `git@github.com:laazyCmd/bms-backed.git`
3. Import the project, using your preferred IDE
4. Wait for Maven to load dependencies
5. Before running, ensure that in `src/main/resources/application.properties`:
    - You are using your preferred database URL in `spring.datasource.url`
    - You are using your correct driver in `spring.datasource.driver-class-name`
6. Go to `src/main/kotlin/BackendApplication.kt`, then press `SHIFT + F10` to run the application
7. Open your browser, and go see [`http://localhost:8080`](http://localhost:8080) if this is successfully running
