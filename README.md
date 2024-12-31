# Flowchart Manager API

A Spring Boot application for managing flowcharts with CRUD operations and graph-related queries. It uses an H2 in-memory database for data storage.

## Features

- **CRUD Operations**: Create, read, update, and delete flowcharts.
- **Graph Queries**:
  - Fetch outgoing edges for a node.
  - Query nodes connected to a specific node (directly or indirectly).
- **Swagger Documentation** for APIs.
- **Unit Tests** for all controller methods.

---

## Prerequisites

1. **Java 17+** installed.
2. **Maven** installed.
3. (Optional) **IDE** like IntelliJ IDEA or Eclipse.

---

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/gagansr/FlowchartCrudOperations.git
cd flowchart-manager
```

### Build and Run

1. Build the project:

   ```bash
   mvn clean install
   ```

2. Start the application:

   ```bash
   mvn spring-boot:run
   ```

   The application runs at `http://localhost:8080`.

---

## API Endpoints

| Method | Endpoint                              | Description                                      |
|--------|---------------------------------------|--------------------------------------------------|
| POST   | `/flowcharts`                         | Create a new flowchart.                         |
| GET    | `/flowcharts/{id}`                    | Fetch details of a flowchart.                   |
| PUT    | `/flowcharts/{id}`                    | Update an existing flowchart.                   |
| DELETE | `/flowcharts/{id}`                    | Delete a flowchart.                             |
| GET    | `/flowcharts/{id}/outgoing/{node}`    | Fetch outgoing edges for a node.                |
| GET    | `/flowcharts/{id}/connected/{node}`   | Fetch all nodes connected to a specific node.   |

---

## Example Request Payloads

### Create Flowchart (POST `/flowcharts`)
```json
{
  "nodes": ["A", "B", "C"],
  "edges": [
    { "source": "A", "target": "B" },
    { "source": "B", "target": "C" }
  ]
}
```

---

## Access H2 Database

- **Console URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:flowchartdb`
- **Username**: `sa`  
- **Password**: *(leave blank)*

---

## Run Unit Tests

Execute all tests with:

```bash
mvn test
```

---

## Swagger Documentation

Access API documentation at:

- **URL**: `http://localhost:8080/swagger-ui.html`

---


