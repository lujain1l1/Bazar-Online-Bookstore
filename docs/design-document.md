# 📄 Design Document — Bazar.com

## 1. Introduction
Bazar.com is a distributed online bookstore built using a multi-tier microservices architecture. The system is designed for handling concurrent user requests efficiently using REST-based communication.

---

## 2. System Architecture

The system is divided into:

### Front-end Service
- Acts as the entry point for all client requests
- Implements load balancing (Round Robin)
- Maintains in-memory cache

### Back-end Services
- Catalog Service (manages books data)
- Order Service (handles purchases)
- Both services are replicated for scalability and fault tolerance

---

## 3. Load Balancing
We use a Round Robin algorithm in the front-end to distribute requests evenly across replicas.

---

## 4. Caching
- In-memory cache is used in the front-end
- Stores results of `/search` and `/info`
- Improves response time significantly for repeated requests

---

## 5. Cache Consistency
We use server-push invalidation:
- When a purchase happens, cache entries are invalidated
- Ensures strong consistency between cache and database

---

## 6. Replication
- 2 replicas for Catalog service
- 2 replicas for Order service
- Improves availability and scalability

---

## 7. Persistence
All data is stored using simple CSV files to ensure persistence without using heavyweight databases.

---

## 8. Trade-offs
- Chose CSV instead of DB → simplicity over performance
- Chose in-memory cache → faster reads but consistency overhead

---

## 9. Conclusion
The system successfully demonstrates key distributed systems concepts including replication, caching, and REST-based microservices.