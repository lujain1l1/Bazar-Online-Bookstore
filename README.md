# 📚 Bazar.com — Distributed Online Book Store

Bazar.com is a **multi-tier, microservices-based online bookstore** developed for the **Distributed Operating Systems** course.
The system evolved from a simple client-server architecture into a highly available, replicated, and cached distributed system.

---

# 🏗️ System Architecture

The project follows a **two-tier web architecture**:

## Front-end Tier

A single microservice responsible for:

* Handling user requests
* Implementing load balancing
* Managing an in-memory cache

## Back-end Tier

Consists of replicated services:

* Catalog Servers
* Order Servers

---

# 🔑 Key Components

## Front-end Server

Acts as a REST gateway between clients and backend services.

### Features

* Round-Robin load balancing
* In-memory caching
* Request forwarding

## Catalog Server

Responsible for managing:

* Book titles
* Topics
* Prices
* Stock availability

Data is stored using a persistent CSV database.

## Order Server

Handles purchase requests and ensures consistency between replicated catalog servers.

## In-Memory Cache

Integrated into the front-end service to reduce latency for:

* `/search`
* `/info`

requests.

---

# 🚀 Features Implemented

## ✅ Microservices Architecture

Each service runs independently and communicates through HTTP REST APIs.

## ✅ Replication

The system includes:

* 2 Catalog replicas
* 2 Order replicas

This improves scalability and fault tolerance.

## ✅ Caching & Consistency

### In-Memory Cache

Stores results for:

* Book information
* Topic searches

### Cache Invalidation

Implements a **Server-Push invalidation strategy** where cache entries are removed immediately after a purchase operation to maintain **Strong Consistency**.

### Database Synchronization

Backend replicas use internal synchronization protocols during write operations.

## ✅ Dockerization

The entire project is containerized using:

* Docker
* Docker Compose

---

# 🛠️ Tech Stack

| Technology     | Usage                     |
| -------------- | ------------------------- |
| Java           | Main programming language |
| Spark Java     | Lightweight web framework |
| REST APIs      | Communication between services |
| JSON           | REST API responses        |
| CSV Files      | Persistent storage        |
| Docker         | Containerization          |
| Docker Compose | Service orchestration     |

---

# 🚦 Getting Started

## Prerequisites

Make sure the following are installed:

* Docker
* Docker Compose
* Maven

---

# ⚙️ Installation & Deployment

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/bazar-bookstore.git
cd bazar-bookstore
```

## 2️⃣ Build the Project

```bash
mvn clean package
```

## 3️⃣ Run Using Docker Compose

```bash
docker-compose up --build
```

---

# 🔗 API Endpoints

## Front-end Server — Port 8080

| Method | Endpoint             | Description                                     |
| ------ | -------------------- | ----------------------------------------------- |
| GET    | `/search/:topic`     | Returns books belonging to a specific topic     |
| GET    | `/info/:item_id`     | Returns stock and cost for a specific book      |
| POST   | `/purchase/:item_id` | Triggers a purchase request to the order server |

---

# 📊 Experimental Evaluation

## Performance Measurements

| Request Type | Status               | Average Response Time |
| ------------ | -------------------- | --------------------- |
| info/search  | Cache MISS           | ~5–15 ms              |
| info/search  | Cache HIT            | ~0 ms                 |
| purchase     | Invalidation + Write | ~20–40 ms             |

## Conclusion

Caching significantly improves read latency, reducing response time to nearly zero for frequently requested data.

---

# 📂 Project Structure

```plaintext
/frontend   → Front-end source code and Dockerfile
/catalog    → Catalog service source code, Dockerfile, and catalog.csv
/order      → Order service source code, Dockerfile, and orders.csv
```

---

# 📝 Design Document Summary

The documentation inside `/docs` includes:

* Round-Robin load balancing logic
* Concurrency handling strategy
* Replication design
* Cache consistency mechanisms
* Trade-offs between latency and Strong Consistency

---

# 👥 Contributors

- Lujain Toma
- Alaa Zetawi

Developed for the **Distributed and Operating Systems Course**
(Spring 2026)
