## Spring Boot Playground

This is a simple Spring Boot practice project built using Java Spring Boot.

### Project Structure
```bash
├── src/main/java
│   ├── com.example.Spring_Boot_Playground
│   │   ├── controller
│   │   │   └── NotificationController.java     # Handles REST API requests
│   │   ├── service
│   │   │   ├── NotificationService.java        # Interface for sending notifications
│   │   │   └── NotificationServiceImpl.java    # Implementation of the notification service
│   │   ├── kafka
│   │   │   ├── NotificationProducer.java       # Kafka Producer to send messages
│   │   │   └── NotificationConsumer.java       # Kafka Consumer to consume messages
│   │   └── caffeine
│   │       └── CaffeineService.java            # A sample code of dynamic ttl cache using Caffeine
│   └── resources
│       └── application.yml                     # Application configuration, including Kafka settings
└── pom.xml                                     # Maven dependencies, including Spring Boot and Kafka
```

### Key Features
- Kafka Producer: The producer sends messages to a Kafka topic, with the topic name configurable in application.yml.
- Kafka Consumer: The consumer listens to the topic and logs incoming messages.
  - REST API: Provides an endpoint to trigger sending Kafka messages.
    ```bash
    POST /api/notifications/send
    ```

### How to Run the Project
1. Run the following command to start Kafka and Zookeeper:
```bash
docker-compose up -d
# This will start Kafka on localhost:9092 and Zookeeper on localhost:2181.
```
2. Build and Run the Spring Boot Application:
```bash
./mvnw spring-boot:run 
```
3. To stop and remove the Kafka and Zookeeper containers, run the following command:
```bash
docker-compose down
```


