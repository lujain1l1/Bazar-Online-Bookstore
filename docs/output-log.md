# 📌 Bazar.com — Runtime Output Logs

This file contains sample runtime outputs from Postman and terminal logs to demonstrate system functionality.

---

# 🔗 1. Search Request

## 📥 Request

```
GET frontend/search/distributed
```
## 📤 Response (200 Ok)
```json
[
  {
    "id": 1,
    "title": "How to get a good grade in DOS in 40 minutes a day"
  },
  {
    "id": 2,
    "title": "RPCs for Noobs"
  }
]
```
## 📥 Request
```
GET frontend/search/undergraduate
```
## 📤 Response (200 Ok)
```json
[
{
"id": 3,
"title": "Xen and the Art of Surviving Undergraduate School"
},
{
"id": 4,
"title": "Cooking for the Impatient Undergrad"
},
{
"id": 5,
"title": "How to finish Project 3 on time"
},
{
"id": 6,
"title": "Why theory classes are so hard"
},
{
"id": 7,
"title": "Spring in the Pioneer Valley"
}
]
```
## 🖥️ Terminal Log
```
catalog-app    | CATALOG NODE = 1
catalog-app-2  | CATALOG NODE = 2
```
![img_1.png](images/img_1.png)

# 🔗 2. Info Request
## 📥 Request
```
GET frontend/info/7
```
## 📤 Response
```json
{
  "title": "Spring in the Pioneer Valley",
  "quantity": 0,
  "price": 35
}
```

## 🖥️ Terminal Log
```
catalog-app    | CATALOG NODE = 1

frontend-app   | CACHE MISS - Response time: 8 ms
frontend-app   | REQUEST arriveddddddd
frontend-app   | CACHE HIT - Response time: 0 ms

```

# 🔗 3. Purchase Request
## 📥 Request
```
POST frontend/purchase/5
```
## 📤 Response
```json
{
  "message": "bought book How to finish Project 3 on time",
  "status": "success"
}
```

## 🖥️ Terminal Log
```
order-app      | ORDER NODE = 1
catalog-app    | CATALOG NODE = 1
catalog-app    | UPDATE request for book id: 5
order-app      | Successfully synced with: http://catalog-service:4567
catalog-app-2  | UPDATE request for book id: 5
order-app      | Successfully synced with: http://catalog-service-2:4567
order-app      | bought book How to finish Project 3 on time item_id: 5
```

# 📊 4. Cache Behavior Test
## 📥 Request (Cache MISS)
```
GET /info/5
```
## 🖥️ Terminal Log
```
frontend-app   | REQUEST arriveddddddd
catalog-app-2  | CATALOG NODE = 2
frontend-app   | CACHE MISS - Response time: 36 ms
```
## 📥 Request (Cache HIT - same request again)
## 🖥️ Terminal Log
```
frontend-app   | REQUEST arriveddddddd
frontend-app   | CACHE HIT - Response time: 0 ms
```


## 📥 Request (Purchase same book id then request info)
cache will be invalidated
## 🖥️ Terminal Log

```
order-app-2    | ORDER NODE = 2
catalog-app    | CATALOG NODE = 1
catalog-app    | UPDATE request for book id: 5
order-app-2    | Successfully synced with: http://catalog-service:4567
catalog-app-2  | UPDATE request for book id: 5
order-app-2    | Successfully synced with: http://catalog-service-2:4567
order-app-2    | bought book How to finish Project 3 on time item_id: 5
catalog-app    | CATALOG NODE = 1
frontend-app   | REQUEST arriveddddddd
frontend-app   | CACHE MISS - Response time: 7 ms
```
![img.png](images/img.png)