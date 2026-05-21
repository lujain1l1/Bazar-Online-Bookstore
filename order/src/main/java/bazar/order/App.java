package bazar.order;
import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.FileWriter;
import java.io.PrintWriter;

public class App {

    private static final Gson gson = new Gson();
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String CATALOG_URL = "http://catalog-service:4567";

    private static final String[] CATALOG_URLS = {
            "http://catalog-service:4567",
            "http://catalog-service-2:4567"
    };
    public static void main(String[] args) {
        port(8081);

        System.out.println("Order Server started on port 8081...");

        get("/hello", (req, res) -> {
            res.type("application/json");
            return "{\"message\": \"Order Server is running!\"}";
        });

        post("/purchase/:item_id", (req, res) -> {
            System.out.println("ORDER NODE = " + System.getenv("NODE"));
            String itemId = req.params(":item_id");
            res.type("application/json");

            int quantity = checkStockFromCatalog(itemId);

            Map<String, Object> responseMap = new HashMap<>();
            JsonObject bookInfo = getBookInfoFromCatalog(itemId);

            if (bookInfo != null && bookInfo.get("quantity").getAsInt() > 0) {
                boolean updated = updateCatalogStock(Integer.parseInt(itemId), -1);

                if (updated) {
                    String title = bookInfo.get("title").getAsString();

                    System.out.println("bought book " + title +" item_id: " + itemId);

                    saveOrderToCSV(itemId, title);

                    responseMap.put("status", "success");
                    responseMap.put("message", "bought book " + title);
                    return gson.toJson(responseMap);
                }
            }


            res.status(410);
            responseMap.put("status", "fail");
            responseMap.put("message", "Item out of stock or error occurred");
            return gson.toJson(responseMap);
        });
    }

    private static int checkStockFromCatalog(String itemId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CATALOG_URL + "/info/" + itemId))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            return jsonResponse.get("quantity").getAsInt();
        } catch (Exception e) {
            System.err.println("Error calling Catalog Server: " + e.getMessage());
            return 0;
        }
    }

    private static JsonObject getBookInfoFromCatalog(String itemId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CATALOG_URL + "/info/" + itemId))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return JsonParser.parseString(response.body()).getAsJsonObject();
            }
        } catch (Exception e) {
            System.err.println("Error calling Catalog: " + e.getMessage());
        }
        return null;
    }

//    private static boolean updateCatalogStock(int id, int value) {
//
//        try {
//            Map<String, Object> updateData = new HashMap<>();
//            updateData.put("id", id);
//            updateData.put("type", "quantity");
//            updateData.put("value", value);
//
//            String jsonBody = gson.toJson(updateData);
//
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(CATALOG_URL + "/update"))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
//                    .build();
//
//
//
//            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//            return response.statusCode() == 200;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    private static boolean updateCatalogStock(int id, int value) {
        boolean allUpdated = true;

        for (String url : CATALOG_URLS) {
            try {
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("id", id);
                updateData.put("type", "quantity");
                updateData.put("value", value);

                String jsonBody = gson.toJson(updateData);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url + "/update")) // يرسل لكل نسخة على حدة
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    allUpdated = false;
                    System.err.println("Failed to update catalog at: " + url);
                } else {
                    System.out.println("Successfully synced with: " + url);
                }
            } catch (Exception e) {
                allUpdated = false;
                System.err.println("Error syncing with " + url + ": " + e.getMessage());
            }
        }
        return allUpdated;
    }

    private static void saveOrderToCSV(String id, String title) {
        try (FileWriter fw = new FileWriter("orders.csv", true);
             PrintWriter out = new PrintWriter(fw)) {
            out.println(System.currentTimeMillis() + "," + id + "," + title);
        } catch (Exception e) {
            System.err.println("Could not write to CSV file");
        }
    }

}
