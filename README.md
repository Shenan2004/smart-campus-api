📄 2. README (VERY IMPORTANT FOR MARKS)
Your README must include:
🔹 1. Overview
Explain:
	•	what your API does
	•	no database (used HashMap)
	•	JAX-RS architecture

🔹 2. How to run
Example:

1. Open project in NetBeans
2. Build using Maven
3. Run project
4. Access API at http://localhost:8080/api/v1


🔹 3. Sample curl commands (MIN 5)
Example:

curl -X GET http://localhost:8080/api/v1/rooms


curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"LAB-101","name":"Computer Lab","capacity":30}'


curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"


curl -X POST http://localhost:8080/api/v1/sensors/TEMP-002/readings \
-H "Content-Type: application/json" \
-d '{"value":25.5}'


🔹 4. Answers to coursework questions
Each part has theory questions. Write short structured answers (not too long, not too small).

🎥 3. Video (don’t overcomplicate)
Structure:
⏱️ 1. Intro (30 sec)
	•	name
	•	what API does
⏱️ 2. Show code structure
	•	packages
	•	DataStore
	•	mappers
⏱️ 3. Postman demo (main part)
	•	happy paths
	•	error cases (VERY IMPORTANT)
⏱️ 4. Quick wrap-up

🧠 Viva-ready explanation (memorize this)
If they ask:
👉 “Why didn’t you use a database?”
Say:
“The coursework required using in-memory data structures, so I implemented a centralized DataStore using static HashMaps to simulate persistence across API requests.”

🏁 Final state
You now have:
	•	REST API
	•	layered structure
	•	exception handling
	•	validation
	•	nested resources
	•	filtering
	•	proper HTTP responses
That’s not beginner level anymore. That’s solid backend engineering.

🚀 If you want next
I can:
	•	generate your README fully written
	•	give perfect viva answers
	•	or help you polish for full marks
Just tell me 👍

generate a README a fully wrriten one

Show moreShow less

Here’s a clean, submission-ready README you can copy directly into your GitHub. It’s written professionally but still clear and exam-friendly.

📄 Smart Campus Sensor & Room Management API
📌 Overview
This project is a RESTful API developed using Java (JAX-RS) to manage rooms, sensors, and sensor readings in a smart campus environment.
The system allows clients to:
	•	Create and manage rooms
	•	Register sensors linked to rooms
	•	Record and retrieve sensor readings
	•	Handle error scenarios using custom exceptions
Instead of using a database, the application uses in-memory data structures (HashMap, ArrayList) to store and manage data during runtime, as required by the coursework.

🛠️ Technologies Used
	•	Java (JAX-RS / Jakarta RESTful Web Services)
	•	Maven
	•	NetBeans IDE
	•	JSON (for request/response)
	•	Postman (for testing)

🏗️ Project Structure

com.mycompany.mavenproject1
│
├── model        → Data models (Room, Sensor, SensorReading, ErrorResponse)
├── repository   → DataStore (in-memory storage using HashMaps)
├── resources    → REST API endpoints
├── exception    → Custom exception classes
├── mapper       → Exception mappers (convert errors to HTTP responses)
└── config       → JAXRSConfiguration (API base path setup)


🚀 How to Run the Project
	1	Open the project in NetBeans
	2	Build the project using Maven
	3	Run the project
	4	Access the API at:

http://localhost:8080/api/v1


🔗 API Endpoints
🔹 Discovery
	•	GET /api/v1
	◦	Returns API metadata and available resources

🔹 Rooms
	•	GET /api/v1/rooms → Get all rooms
	•	POST /api/v1/rooms → Create a new room
	•	GET /api/v1/rooms/{roomId} → Get a specific room
	•	DELETE /api/v1/rooms/{roomId} → Delete a room

🔹 Sensors
	•	GET /api/v1/sensors → Get all sensors
	•	GET /api/v1/sensors?type=Temperature → Filter sensors by type
	•	POST /api/v1/sensors → Create a new sensor

🔹 Sensor Readings
	•	GET /api/v1/sensors/{sensorId}/readings → Get all readings
	•	POST /api/v1/sensors/{sensorId}/readings → Add a new reading

⚙️ Sample cURL Commands
1. Get all rooms

curl -X GET http://localhost:8080/api/v1/rooms

2. Create a room

curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"LAB-101","name":"Computer Lab","capacity":30}'

3. Create a sensor

curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":25.5,"roomId":"LAB-101"}'

4. Get sensors by type

curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"

5. Add a sensor reading

curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"value":27.3}'


⚠️ Error Handling
The API uses custom exceptions and mappers to return meaningful HTTP responses.
Scenario
Exception
Status Code
Deleting a room with sensors
RoomNotEmptyException
409 Conflict
Creating sensor with invalid room
LinkedResourceNotFoundException
422 Unprocessable Entity
Posting reading to maintenance sensor
SensorUnavailableException
403 Forbidden
Unexpected errors
GlobalExceptionMapper
500 Internal Server Error
Example error response:

{
  "status": 409,
  "error": "Conflict",
  "message": "Cannot delete room because it still has assigned sensors."
}


🧠 Key Design Decisions
🔹 In-Memory Data Store
A DataStore class using static HashMap and ArrayList is used instead of a database. This ensures:
	•	Simplicity
	•	Fast access
	•	Compliance with coursework requirements

🔹 RESTful Design
	•	Proper use of HTTP methods (GET, POST, DELETE)
	•	Resource-based URL structure
	•	JSON request/response handling

🔹 Exception Handling
Custom exceptions are used to represent business logic errors, while Exception Mappers convert them into proper HTTP responses.

🔹 Nested Resources
Sensor readings are implemented as nested resources:

/api/v1/sensors/{sensorId}/readings

This reflects real-world relationships between sensors and their readings.

📚 Coursework Questions (Summary Answers)
Why use /api/v1?
To version the API and allow future updates without breaking existing clients.

Why use HashMaps instead of a database?
The coursework required in-memory storage. HashMaps provide efficient key-based access and simulate persistence during runtime.

Why use Exception Mappers?
To convert Java exceptions into meaningful HTTP responses with proper status codes and JSON messages.

How are resources related?
	•	A Room contains multiple Sensors
	•	A Sensor contains multiple SensorReadings

🎯 Conclusion
This project demonstrates:
	•	REST API development using Java
	•	Proper layering (models, resources, mappers, exceptions)
	•	Error handling with HTTP standards
	•	Data management without a database
The system is scalable, maintainable, and follows best practices for backend API design.
