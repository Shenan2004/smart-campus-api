# 🏫 Smart Campus — Sensor & Room Management API

A JAX-RS (Jersey 2) RESTful API for managing university campus rooms and IoT sensors. Built for the 5COSC022W Client-Server Architectures coursework (University of Westminster, 2025/26).

---

## 📐 API Design Overview

The API follows RESTful architectural principles with a clear, hierarchical resource model that mirrors the physical structure of the campus:

```
/api/v1                          ← Discovery / metadata endpoint
/api/v1/rooms                    ← Room collection resource
/api/v1/rooms/{roomId}           ← Individual room resource
/api/v1/sensors                  ← Sensor collection resource
/api/v1/sensors/{sensorId}       ← Individual sensor resource
/api/v1/sensors/{sensorId}/readings  ← Sub-resource: reading history
```

### Technology Stack

| Component        | Technology                        |
|------------------|-----------------------------------|
| Language         | Java 8                            |
| API Framework    | JAX-RS via Jersey 2.39.1          |
| JSON Binding     | Jackson (via jersey-media-json-jackson) |
| Build Tool       | Apache Maven                      |
| Server           | Apache Tomcat (or any Servlet 3.x container) |
| Data Storage     | In-memory (`HashMap`, `ArrayList`) |

### Key Design Decisions

- **Versioned entry point**: All routes are prefixed with `/api/v1` via `@ApplicationPath("/api/v1")`.
- **In-memory data store**: A static `DataStore` class holds `HashMap<String, Room>`, `HashMap<String, Sensor>`, and `HashMap<String, List<SensorReading>>`, pre-seeded with one room and one sensor.
- **Sub-Resource Locator**: `SensorResource` delegates `/sensors/{id}/readings` to a dedicated `SensorReadingResource` class, keeping concerns separated.
- **Leak-proof error handling**: Every exception scenario is caught by a specific `ExceptionMapper` and returns a structured JSON body. A global catch-all `ExceptionMapper<Throwable>` ensures raw stack traces are never exposed.
- **Cross-cutting observability**: A `LoggingFilter` implementing both `ContainerRequestFilter` and `ContainerResponseFilter` logs every incoming request method/URI and every outgoing status code.

---

## 🚀 Build & Run Instructions

### Prerequisites

- **Java JDK 8** or higher  
- **Apache Maven 3.6+**  
- **Apache Tomcat 9.x** (or any Servlet 3.1-compatible container)

Verify your environment:
```bash
java -version
mvn -version
```

### Step 1 — Clone the Repository

```bash
git clone https://github.com/<your-username>/smart-campus-api.git
cd smart-campus-api/mavenproject1
```

### Step 2 — Build the WAR File

```bash
mvn clean package
```

On success you will see:
```
BUILD SUCCESS
```
The compiled WAR is at:
```
target/mavenproject1-1.0-SNAPSHOT.war
```

### Step 3 — Deploy to Tomcat

**Option A — Copy the WAR manually:**
```bash
cp target/mavenproject1-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/smart-campus.war
$CATALINA_HOME/bin/startup.sh
```

**Option B — Deploy via Tomcat Manager UI:**
1. Open `http://localhost:8080/manager/html`
2. Under *"WAR file to deploy"*, browse to the WAR file and click *Deploy*.

**Option C — Use Apache NetBeans:**  
Open the project in NetBeans, right-click the project → *Clean and Build*, then *Run*.

### Step 4 — Verify the Server is Running

```bash
curl http://localhost:8080/smart-campus/api/v1
```

Expected response:
```json
{
  "version": "v1",
  "adminContact": "admin@smartcampus.local",
  "resources": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}
```

> **Note:** Replace `smart-campus` with whatever context path Tomcat assigned to your WAR (e.g., `mavenproject1-1.0-SNAPSHOT`).

---

## 🧪 Sample `curl` Commands

All examples assume the server is running at `http://localhost:8080/smart-campus`.

### 1. Discovery — Get API Metadata
```bash
curl -X GET http://localhost:8080/smart-campus/api/v1 \
  -H "Accept: application/json"
```

### 2. Create a New Room (POST)
```bash
curl -X POST http://localhost:8080/smart-campus/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"ENG-101","name":"Engineering Lab","capacity":30}'
```
Expected: `201 Created` with `Location: /api/v1/rooms/ENG-101`

### 3. Retrieve All Rooms (GET)
```bash
curl -X GET http://localhost:8080/smart-campus/api/v1/rooms \
  -H "Accept: application/json"
```

### 4. Retrieve a Specific Room by ID (GET)
```bash
curl -X GET http://localhost:8080/smart-campus/api/v1/rooms/LIB-301 \
  -H "Accept: application/json"
```

### 5. Register a New Sensor (POST) — Valid Room
```bash
curl -X POST http://localhost:8080/smart-campus/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-002","type":"CO2","status":"ACTIVE","currentValue":410.0,"roomId":"LIB-301"}'
```
Expected: `201 Created`

### 6. Register a Sensor with a Non-Existent Room — Triggers 422
```bash
curl -X POST http://localhost:8080/smart-campus/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-003","type":"CO2","status":"ACTIVE","currentValue":400.0,"roomId":"INVALID-999"}'
```
Expected: `422 Unprocessable Entity` with JSON error body

### 7. Filter Sensors by Type (GET with Query Param)
```bash
curl -X GET "http://localhost:8080/smart-campus/api/v1/sensors?type=Temperature" \
  -H "Accept: application/json"
```

### 8. Post a Sensor Reading (POST sub-resource)
```bash
curl -X POST http://localhost:8080/smart-campus/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":23.7}'
```
Expected: `201 Created` — also updates `currentValue` on the parent sensor

### 9. Get Reading History for a Sensor (GET sub-resource)
```bash
curl -X GET http://localhost:8080/smart-campus/api/v1/sensors/TEMP-001/readings \
  -H "Accept: application/json"
```

### 10. Attempt to Delete a Room That Still Has Sensors — Triggers 409
```bash
curl -X DELETE http://localhost:8080/smart-campus/api/v1/rooms/LIB-301
```
Expected: `409 Conflict` with JSON error body

### 11. Delete an Empty Room (DELETE)
```bash
# First create a room with no sensors
curl -X POST http://localhost:8080/smart-campus/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-ROOM","name":"Empty Room","capacity":5}'

# Now delete it
curl -X DELETE http://localhost:8080/smart-campus/api/v1/rooms/TEMP-ROOM
```
Expected: `200 OK`

### 12. Post a Reading to a Sensor in MAINTENANCE — Triggers 403
```bash
# First update sensor status to MAINTENANCE (set manually in DataStore for demo, or POST a new sensor)
curl -X POST http://localhost:8080/smart-campus/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-MAINT","type":"CO2","status":"MAINTENANCE","currentValue":0.0,"roomId":"LIB-301"}'

curl -X POST http://localhost:8080/smart-campus/api/v1/sensors/CO2-MAINT/readings \
  -H "Content-Type: application/json" \
  -d '{"value":500.0}'
```
Expected: `403 Forbidden` with JSON error body

---

## 📁 Project Structure

```
mavenproject1/
├── pom.xml
└── src/main/java/com/mycompany/mavenproject1/
    ├── JAXRSConfiguration.java          # @ApplicationPath("/api/v1")
    ├── exception/
    │   ├── LinkedResourceNotFoundException.java
    │   ├── RoomNotEmptyException.java
    │   └── SensorUnavailableException.java
    ├── filter/
    │   └── LoggingFilter.java           # Request/Response logging
    ├── mapper/
    │   ├── GlobalExceptionMapper.java   # Catch-all → 500
    │   ├── LinkedResourceNotFoundExceptionMapper.java  # 422
    │   ├── RoomNotEmptyExceptionMapper.java            # 409
    │   └── SensorUnavailableExceptionMapper.java       # 403
    ├── model/
    │   ├── ErrorResponse.java
    │   ├── Room.java
    │   ├── Sensor.java
    │   └── SensorReading.java
    ├── repository/
    │   └── DataStore.java               # Static in-memory store
    └── resources/
        ├── DiscoveryResource.java       # GET /api/v1
        ├── RoomResource.java            # /api/v1/rooms
        ├── SensorResource.java          # /api/v1/sensors
        └── SensorReadingResource.java   # /api/v1/sensors/{id}/readings
```
"
---
## REPORT
---
5COSC022W_ Client-Server
Architectures
Smart Campus API
Shenan Dimbukkuwalage
20241042
W2121287


Part 1: Service Architecture & Setup
Q1. Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton?

Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures. When a resource class handles an incoming HTTP request, JAX-RS creates a new instance of the class for every single request (the request-scoped lifecycle). This means
a separate instance of RoomResource or SensorResource will be created for every request received. In our case, although there are multiple instances of the resource, they share the same underlying data structure (static HashMap) in the DataStore class since they are all referencing the same static field. Static fields belong to a class and not to the objects of that class; therefore, the data stored in the static field will persist between requests.

Concurrency implications:
While resource instances are request-scoped, the static maps are shared across all threads. Because there could be concurrent requests in a production environment, it is possible for there to be race conditions or data corruption. In order to maintain safe cross-thread access to the data in the static maps, we would need to implement a thread-safe data structure (e.g. ConcurrentHashMap) or use explicit thread synchronisation (e.g., synchronised blocks). However, for purposes of completing this coursework with single-threaded testing, the current implementation is adequate to demonstrate knowledge of managing in-memory data.

Q2. Why is the provision of "Hypermedia" (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

HATEOAS (Hypermedia As The Engine Of Application State) provides a self-description and discoverability of an API by allowing all response data to contain hyperlinks to related resources or possible next actions rather than requiring out-of-band (i.e., written) documentation.
The benefits of using HATEOAS are:
1. Reduced Coupling: Because clients follow links dynamically, they do not need to have hard-coded values in their application code.
2. Easier Evolution: If a server's URL pattern changes, clients will not be a]ected as long as the relationship between links remains the same.
3. Improved Discoverability: New developers will be able to use links from an entry point (our /api/v1 discovery endpoint) to explore the API.
In our API we implemented the discovery endpoint to return a list of resource paths. A complete HATEOAS implementation would include actual HTML links in each response so that clients can navigate through the API without knowledge of how it is structured.

Part 2: Room Management
Q3. When returning a list of rooms, what are the implications of returning only IDs
versus returning the full room objects? Consider network bandwidth and client-side
processing.

Method of transmission network bandwidth processing cost of client use case Returning Identifiers only: Low (small amount of data transmitted), High (client has to do N more requests to get detailed information). When the client does not need to see any of the details about the rooms, or there is an immense number of rooms. Returning full objects: Higher (more data will use more bandwidth), Lower (all the data
is available right away). When the client needs all of the information at once, such as in an inventory view of rooms on a dashboard.
For the Smart Campus API, we are returning full room objects because the representation of rooms will be moderately large and simplifies the logic of the client. In more advanced versions of an API, field filtering or pagination might be used to address both concerns.

Q4. Is the DELETE operation idempotent in your implementation? Provide a detailed
justification by describing what happens if a client mistakenly sends the exact same
DELETE request for a room multiple times.

The DELETE/Retrieve operation is idempotent/returns a confirmed idempotent result.
Original Request

By deleting the Room after checking the Resources' Sensory-Mapping
- If an identical DELETE request is made again, then the room has been deleted, but all other resources have not changed since the initial request; same 200 OK status returned;

- The Resource has been deleted; therefore, 404 Not Found is the only viable response to a new Resource (since this resource no longer exists).

As such, DELETE Return values have been consistent. This is still confirmable using

HTTP 1.1 Idempotency.

Part 3: Sensors & Filtering

Q5. We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a di]erent format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

JAX-RS will automatically return an HTTP 415 Unsupported Media Type error to the client if a Content-Type header sent with a request does not match application/json (or compatible subtypes). The result is:

The client will immediately receive a standard error indicating that the server cannot process the given format of the payload. When JAX-RS receives a request, it compares the incoming Value of the Content-Type header to the @Consumes value associated with the resource method. If there is a mismatch, the JAX-RS runtime does not call the resource method and instead invokes the appropriate default exception mapper.
Thus, this mechanism provides protection for our API methods by preventing the method from processing unexpected payload formats and ensuring that the API contract is maintained.

Q6. You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g.,
/api/v1/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

Approach Example Advantages Disadvantages Query Parameter /sensors?type=CO2 being identified from the optional filters that can be applied.

✅ Clear, distinct separation of the resource
✅ Query parameters permit several di]erent filters to be combined into one request easily (?type=CO2&status=ACTIVE). Path Parameter /sensors/type/CO2 unique resource, and this is not true.

❌ The path indicates that "type/CO2" is a
❌ It's di]icult to support multiple independent filters or optional parameters without adding to complexity.

Why query parameters for filtering are better: By using query parameters, the path can uniquely identify the resource (all sensors), and then use the query parameters to identify how to represent the resource.

Query parameters adhere to REST, creating an easy-to-understand, consistent, and future extensible API. Query parameters do not clutter the URL hierarchy with pseudo-resources.

Part 4: Deep Nesting with Sub-Resources
Q7. Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path in one massive controller class?

The Sub-Resource Locator pattern (used in SensorResource.getSensorReadingResource()) returns a dedicated resource instance for the nested path segment.

Benefits: The Sub-Resource Locator pattern (as in SensorResource.getSensorReadingResource()) creates a dedicated resource instance for a sub-path segment.

Benefits include: Separation of Concerns: Each sub-resource class (SensorReadingResource) manages only that specific functionality, and thus prevents "god classes" from becoming excessively large and multiple classes of hundreds of lines long.

Encapsulation: The parent resource does not have to understand the internal logic of the sub-resource; it merely provides the route to the appropriate resource.

Reusability: The same sub-resource class can also be called upon as needed by other parent resources.

Testability: Smaller, focused classes can be more easily independently unit tested. 

Readability: As the API expands, the codebase will become easier to navigate and maintain, with additional sub-resource classes being added.
Without this pattern, all methods necessary for providing access to readings and adding readings (e.g., getReadingsForSensor(), addReadingToSensor(), etc.) as methods on a single SensorResource object would bloat the SensorResource and make it di]icult to maintain.

Part 5: Advanced Error Handling, Exception Mapping & Logging
Q8. Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

The error code 404 indicates that the URI (Uniform Resource Identifier) requested is not present. In this case, the endpoint /api/v1/sensors is an actual endpoint, and the routing of the request was done properly. So 404 is not an accurate indication of this scenario.

The 422 Unprocessable Entity code (taken from WebDAV and adopted by some REST APIs) means that the request is understood by the server as to its Content-Type and syntax, but because of a semantic error, the server cannot process the instruction given
(e.g., the roomId in the JSON payload does not refer to an existing room).

By using the 422 response code, the client receives better feedback: "Your request was well-formed; however, the entity(s) you requested do not exist in our system." By providing distinct separation between routing failures and business logic validation failures, the 422 code allows for easier identification by client developers.

Q9. From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Exposing raw stack traces is a serious security vulnerability because it leaks internal implementation details. An attacker can gather: reveal raw stack trace to is a serious security risk because they expose internal implementation information. When an attacker can gather:

1. Package and class names (e.g.com.mycompany.mavenproject1.repository.DataStore), the application’s internal architecture can be determined.

2. Method and line numbers may give an indication as to where there are flaws in business logic.

3. Versions of libraries and frameworks (e.g., Jersey, Jackson) can be cross-referenced with known vulnerabilities (CVE’s).

4. The structure of SQL queries (if a database were used) could reveal the names of tables/columns.

This information can be used to develop more specific types of attacks, such as SQL injection, path traversal and exploiting known library vulnerabilities. Using a general catch-all exception mapper that returns a general "Internal Server Error" response prevents this leakage from occurring, but allows the full stack trace to be logged securely server-side.

Q10. Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

Using Filters (ContainerRequestFilter / ContainerResponseFilter) provides several

advantages: a single location controls the logging function, and all other endpoints will use it the same way.
Separation of concerns; the focus of resource methods is only on business things (more readable and maintainable).
No repetitive LOGGER.info(...) calls were scattered around among dozens of methods. Easier to modify: If you change the log format or want to add additional logging to the filter class (for example, request headers), then you only need to update the filter class to reflect those changes.

Filters are executed for every request/response; for example, if a request fails before reaching a resource method (example: "404" and "415" errors), that means that you can guarantee complete observability. This strategy conforms with the Aspect-Oriented Programming (AOP) philosophy,
therefore producing a cleaner and more robust codebase. "
