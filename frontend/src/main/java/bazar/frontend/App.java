package bazar.frontend;
//package org.example;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class App {
    //private static final String CATALOG_URL = "http://catalog-service:4567";
    private static final String ORDER_URL = "http://order-service:8081";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    private static int catalogCounter = 0;
    public static void main(String[] args) {
        port(8080);
        System.out.println("FrontEnd Server started on port 8080...");


        get("/search/:topic", (req, res) -> {
            String topic = req.params(":topic");
            try {
                String response = sendGetRequest(getCatalogServiceUrl() + "/search/" + topic);                res.type("application/json");
                return response;
            } catch (Exception e) {
                res.status(500);
                return "{\"error\": \"Catalog server connection failed\"}";
            }
        });

        get("/info/:id", (req, res) -> {
            String id = req.params(":id");
            try {
                String response = sendGetRequest(getCatalogServiceUrl() + "/info/" + id);                res.type("application/json");
                return response;
            } catch (Exception e) {
                res.status(500);
                return "{\"error\": \"Catalog server connection failed\"}";
            }
        });

        post("/purchase/:id", (req, res) -> {
            String id = req.params(":id");
            try {
                String response = sendPostRequest(ORDER_URL + "/purchase/" + id);
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
}