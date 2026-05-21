package bazar.frontend;
//package org.example;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class App {
    //private static final String CATALOG_URL = "http://catalog-service:4567";
    //private static final String ORDER_URL = "http://order-service:8081";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    private static final Map<String, String> cache = new HashMap<>();
    private static int catalogCounter = 0;
    private static int orderCounter = 0;
    public static void main(String[] args) {
        port(8080);
        System.out.println("FrontEnd Server started on port 8080...");
        System.out.println("FrontEnd START WORKS");
        System.out.flush();

        get("/search/:topic", (req, res) -> {

            String topic = req.params(":topic");
            long start = System.currentTimeMillis();

            System.out.println("REQUEST arriveddddddd - SEARCH");

            // CACHE HIT
            if (cache.containsKey(topic)) {
                long end = System.currentTimeMillis();

                System.out.println("CACHE HIT - SEARCH - Response time: " + (end - start) + " ms");

                res.type("application/json");
                return cache.get(topic);
            }

            try {
                // CACHE MISS → call catalog service
                String response = sendGetRequest(
                        getCatalogServiceUrl() + "/search/" + topic
                );

                cache.put(topic, response);

                long end = System.currentTimeMillis();

                System.out.println("CACHE MISS - SEARCH - Response time: " + (end - start) + " ms");

                res.type("application/json");
                return response;

            } catch (Exception e) {
                res.status(500);
                return "{\"error\": \"Catalog server connection failed\"}";
            }
        });

        get("/info/:id", (req, res) -> {
            System.out.println("REQUEST arriveddddddd");
            String id = req.params(":id");
            long start = System.currentTimeMillis();

            if (cache.containsKey(id)) {
                long end = System.currentTimeMillis();
                System.out.println("CACHE HIT - Response time: " + (end - start) + " ms");
                res.type("application/json");
                return cache.get(id);
            }

            try {
                String response = sendGetRequest(getCatalogServiceUrl() + "/info/" + id);

                cache.put(id, response);
                long end = System.currentTimeMillis();
                System.out.println("CACHE MISS - Response time: " + (end - start) + " ms");


                res.type("application/json");
                return response;

            } catch (Exception e) {
                res.status(500);
                return "{\"error\": \"Catalog server connection failed\"}";
            }
        });

        post("/purchase/:id", (req, res) -> {
            String id = req.params(":id");
            System.out.println("REQUEST arrived - PURCHASE");
            long start = System.currentTimeMillis();
            try {
                String response = sendPostRequest(getOrderServiceUrl() + "/purchase/" + id);
                cache.remove(id);

                long end = System.currentTimeMillis();

                System.out.println("CACHE INVALIDATION - ITEM " + id);
                System.out.println("PURCHASE RESPONSE TIME: " + (end - start) + " ms");
                res.type("application/json");
                return response;
            } catch (Exception e) {
                res.status(500);
                return "{\"error\": \"Order server connection failed\"}";
            }
        });
    }

    private static String sendGetRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static String sendPostRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    private static String getCatalogServiceUrl() {
        if (catalogCounter % 2 == 0) {
            catalogCounter++;
            return "http://catalog-app:4567";
        } else {
            catalogCounter++;
            return "http://catalog-app-2:4567";
        }
    }
    private static String getOrderServiceUrl() {
        if (orderCounter % 2 == 0) {
            orderCounter++;
            return "http://order-app:8081";
        } else {
            orderCounter++;
            return "http://order-app-2:8081";
        }
    }
}