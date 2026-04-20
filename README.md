Smart Campus Sensor & Room Management API 

Overview 

This project develops a RESTful API for the university’s ‘Smart Campus’ project via JAX-RS (Jersey). It is meant for facilities managers and automated systems to add, remove, edit Rooms, Sensors and Sensor Readings throughout the buildings on campus. 

The API adheres to REST principles, employs JSON for data transmission, and implements thorough error handling with relevant HTTP status codes. As specified in the coursework specification, all data is stored in-memory using HashMap and ArrayList facilities. 

Technologies Used 

Technology | Function Java 8 | Core programming language JAX-RS (Jersey 2.39) | RESTful web service framework Maven | Build automation and dependency management Apache Tomcat | Servlet container/deployment target JSON | Interchange data format Postman / cURL | API test tools 

Project Structure 

src/main/java/com/mycompany/mavenproject1/ ├── JAXRSConfiguration.java # Application path configuration (/api/v1) ├── model/ │ ├── Room.java │ ├── Sensor.java │ ├── SensorReading.java │ └── ErrorResponse.java └ Repository/ │ └── DataStore.java # In-memory data storage (HashMaps) ├── resources/ │ ├── DiscoveryResource.java # Root discovery endpoint │ ├── RoomResource.java │ └── SensorResource.java │ └── SensorReadingResource.java ├── exception/ │ ├── RoomNotEmptyException.java │ ├── LinkedResourceNotFoundException.java │ └── SensorUnavailableException.java └── mapper/ # Moved to proper package ├── GlobalExceptionMapper.java RoomNotEmptyExceptionMapper.java ├── LinkedResourceNotFoundExceptionMapper.java └── SensorUnavailableExceptionMapper.java 

How to Build and Run the Project 

Prerequisites 

Java JDK 8 or above Apache Maven 3.6+ Apache Tomcat 9+ (or any servlet container) 

Steps 

1. Clone the repository ```bash git clone https://github.com/your-username/your-repo-name.git “cd your-repo-name” ” Use Maven to build the project 

```bash
 mvn clean package ``` This should produce a WAR file in the target/ directory (for example, mavenproject1-1.0-SNAPSHOT.war). 

Deploy to Tomcat Deploy to Tomcat Copy WAR File to Tomcat’s webapps/ folder. 

Start Tomcat using bin/startup.sh (Linux/Mac) or bin/startup.bat (Windows). 

Get API Access Base URL: http://localhost:8080/mavenproject1/api/v1 

Test discovery endpoint: http://localhost:8080/mavenproject1/api/v1 

API Endpoints 

Discovery Method | Endpoint | Description GET | /api/v1 | Returns API information and resources. 

Rooms Method | Endpoint | Description GET | /api/v1/rooms | Retrieve all rooms POST | /api/v1/rooms | Create a room GET | /api/v1/rooms/{roomId} | Get a specific room by ID DELETE | /api/v1/rooms/{roomId} | Delete a room (requires that the room does not have sensors) 

SENSORS Method | Endpoint | Description GET | /api/v1/sensors | Retrieve all sensors GET | /api/v1/sensors?type={type} | Get sensors based on the sensor type (e.g. Temperature). POST | /api/v1/sensors | Create a sensor (valid roomId required). GET | /api/v1/sensors/{sensorId} 

Sensor Readings (Sub Resource) Method | Endpoint | Description GET | /api/v1/sensors/{sensorId}/readings | Retrieve all readings for a sensor POST | /api/v1/sensors/{sensorId}/readings | Add a new reading (sets currentValue) 

Sample cURL Commands 

Note: If you use different hosts/ports, be sure to replace localhost:8080 accordingly. 

Discovery Endpoint 1. Discovery Endpoint ```bash ”Discovery” Endpoint1. ```bash curl -X GET http://localhost:8080/mavenproject1/api/v1 1. Discovery Endpoint ```bash curl -X GET http://localhost:8080/mavenproject1/api/v1 ``` 

2. Create Room 2. Create a Room ```bash 2. Create a Room ”curl –X POST http://localhost:8080/mavenproject1/api/v1/rooms \ -H "Content-Type: application/json" ‘-d’ ‘{“id”: “LAB-101”, “name”: “Computer Lab”, “capacity”: 30}’ ” 

3. Set Up a Sensor 3. Create a Sensor
``` ”bash_curl -X POST http://localhost:8080/mavenproject1/api/v1/sensors \ -H “Content-Type: application/json” \ -d ‘{“id”:”TEMP-001″,”type”:”Temperature”,”status”:”ACTIVE”,”currentValue”:22.5,”roomId”:”LAB-101″}’ ” 

4. Sensors by Type 4. Filter Sensors by Type ```bash curl -X GET “http://localhost:8080/mavenproject1/api/v1/sensors?type=Temperature” 5. Filter Sensors by Type ```bash curl -X GET "http://localhost:8080/mavenproject1/api/v1/sensors?type=Temperature" ```
 

5. Add a Sensor Reading 5. Add Sensor Reading ```bash curl -X POST http://localhost:8080/mavenproject1/api/v1/sensors/TEMP-001/readings \ -H “Content-Type: application/json” \ -d ‘{“value”:23.8}’ ” 

6. Delete Room with Sensors (409 Conflict) ```bash
 curl -X DELETE http://localhost:8080/mavenproject1/api/v1/rooms/LAB-101 “ 

7. Add a Reading to a Sensor with the MAINTENANCE State (403 Forbidden) 7. Try to Add a Reading to a Sensor in MAINTENANCE (403 Forbidden Expected)```bash # First, update sensor status (it’s not endpoint, but you can do it via code or just imagine). curl -X POST http://localhost:8080/mavenproject1/api/v1/sensors/TEMP-001/readings \ -H “Content-Type: application/json” \ -d ‘{“value”:24.0}’ 7. Try to add a reading to a sensor in MAINTENANCE (403 Forbidden) 

Error Handling 

The API returns structured JSON error responses through custom exceptions and Exception Mappers. 

Scenario | Exception Class | HTTP Status Deleting a Room With Sensors Still Attached to It | RoomNotEmptyException | 409 Conflict Creation of a sensor linked to a non-existing roomId | LinkedResourceNotFoundException | 422 Unprocessable Entity Posting a reading to a sensor in MAINTENANCE | SensorUnavailableException | 403 Forbidden Any uncaught runtime exception | GlobalExceptionMapper (catch-all) | 500 Internal Server Error 

Example Error Response (409 Conflict) 

” ( 409;  "error": "Conflict", "message": "Cannot delete room because it is still assigned to sensors." } 
