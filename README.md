. 📡 Smart Campus Sensor & Room Management API

📌 Overview

This project is an API built with Java (JAX-RS) to manage rooms, sensors and sensor readings in a smart campus.

The system lets users:

*. Manage rooms

* Register sensors linked to rooms

* Record and retrieve sensor readings

* Handle errors with HTTP responses and custom exception handling

As required by the coursework the system uses in-memory data structures (HashMap, ArrayList) instead of a database for simplicity and fast access.

🛠️ Technologies Used

* Java (JAX-RS / Web Services)

* Maven

* Apache Tomcat

* JSON (request/response format)

* Postman (testing)

🏗️ Project Structure

* com.mycompany.mavenproject1

+ model        → Data models (Room, Sensor, SensorReading, ErrorResponse)

. Repository   → DataStore (in-memory storage using HashMaps)

+ resources    → REST API endpoints

+ exception    → Custom exception classes

+ mapper       → Exception mappers (convert errors to HTTP responses)

+ config       → JAXRSConfiguration (API base path setup)

🚀 How to Run the Project

* Clone. Download the repository

* Open the project in NetBeans

* Build the project using Maven

* Run the project (Tomcat server)

* Access the API at: http://localhost:8080/mavenproject1/api/v1

🔗 API Endpoints

### Discovery

* GET /api/v1. Returns API metadata and available resources

### Rooms

* GET /api/v1/rooms. Retrieve all rooms

* POST /api/v1/rooms. Create a room

* GET /api/v1/rooms/{roomId}. Retrieve a room

* DELETE /api/v1/rooms/{roomId}. Delete a room

### Sensors

* GET /api/v1/sensors. Retrieve all sensors

* GET /api/v1/sensors?type=Temperature. Filter sensors by type

* POST /api/v1/sensors. Create a sensor

### Sensor Readings

* GET /api/v1/sensors/{sensorId}/readings. Retrieve readings

* POST /api/v1/sensors/{sensorId}/readings. Add a new reading

⚙️ Sample cURL Commands

1. Get all rooms

```bash

curl -X GET http://localhost:8080/mavenproject1/api/v1/rooms

```

2. Create a room

```bash

curl -X POST http://localhost:8080/mavenproject1/api/v1/rooms \

-H "Content-Type: application/json" \

-d '{"id":"LAB-101","name":"Computer Lab","capacity":30}'

```

3. Create a sensor

```bash

curl -X POST http://localhost:8080/mavenproject1/api/v1/sensors \

-H "Content-Type: application/json" \

-d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":25.5,"roomId":"LAB-101"}'

```

4. Filter sensors

```bash

curl -X GET "http://localhost:8080/mavenproject1/api/v1/sensors?type=Temperature"

```

5. Add a sensor reading

```bash

curl -X POST http://localhost:8080/mavenproject1/api/v1/sensors/TEMP-001/readings \

-H "Content-Type: application/json" \

-d '{"'

```

⚠️ Error Handling

The API uses custom exceptions and exception mappers to return meaningful HTTP responses.

| Scenario | Exception | Status Code |

| --- | --- | --- |

Deleting a room with sensors | RoomNotEmptyException | 409 Conflict |

| Creating sensor with invalid room | LinkedResourceNotFoundException | 422 Unprocessable Entity |

| Posting reading to maintenance sensor | SensorUnavailableException | 403 Forbidden |

Unexpected errors | GlobalExceptionMapper | 500 Internal Server Error |

Example Error Response

```json

{

"status": 409

"error": "Conflict"

"message": "Cannot room because it still has assigned sensors."

}

```

🧠 Key Design Decisions

### In-Memory Data Store

A centralized DataStore class using HashMap and ArrayList is used instead of a database to:

* Meet coursework requirements

* Provide data access

* Maintain shared state across requests

### RESTful Architecture

* use of HTTP methods (GET, POST, DELETE)

* Resource-based URL design

* JSON-based communication

### Exception Handling

Custom exceptions are used for business logic errors while Exception Mappers convert them into structured HTTP responses.

### Nested Resources

Sensor readings are implemented as resources: /api/v1/sensors/{sensorId}/readings

This reflects real-world relationships between sensors and their readings.

📚 Coursework Concepts

* Why use /api/v1?

To version the API. Allow future updates without breaking existing clients.

* Why use HashMaps of a database?

The coursework required in-memory storage. HashMaps provide key-based access and simulate persistence during runtime.

* Why use Exception Mappers?

To convert Java exceptions into HTTP responses with proper status codes and JSON messages.

This project demonstrates:

* REST API development using Java

* Clean layered architecture

* Proper error handling using HTTP standards

* Data management without a database

The system is scalable maintainable. Follows industry best practices, for backend API design.
