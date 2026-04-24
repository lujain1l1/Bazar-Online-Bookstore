package bazar.catalog;

import static spark.Spark.*;
import com.google.gson.Gson;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        port(4567);
        CatalogService catalog = new CatalogService();
        Gson gson = new Gson();

        System.out.println("Catalg Server started on port 4567...");


        get("/info/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Book book = catalog.getBookById(id);

            res.type("application/json");

            if (book != null) {
                Map<String, Object> response = new LinkedHashMap<>();

                response.put("title", book.getTitle());
                response.put("quantity", book.getQuantity());
                response.put("price", book.getPrice());

                return gson.toJson(response);
            } else {
                return gson.toJson(Collections.singletonMap("error", "Book not found"));
            }
        });


        get("/search/:topic", (req, res) -> {
            String topic = req.params("topic");
            List<Book> result = catalog.getBooksByTopic(topic);

            res.type("application/json");

            List<Map<String, Object>> response = new ArrayList<>();

            for (Book b : result) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", b.getId());
                item.put("title", b.getTitle());
                response.add(item);
            }

            return gson.toJson(response);
        });




        post("/update", (req, res) -> {

            UpdateRequest request = gson.fromJson(req.body(), UpdateRequest.class);

            System.out.println("UPDATE request for book id: " + request.id);

            Book book = catalog.getBookById(request.id);

            res.type("application/json");

            if (book != null) {

                if (request.type.equalsIgnoreCase("quantity")) {
                    book.setQuantity(book.getQuantity() + request.value);
                } else if (request.type.equalsIgnoreCase("price")) {
                    book.setPrice(request.value);
                } else {
                    return gson.toJson(Collections.singletonMap("error", "invalid type"));
                }

                catalog.saveToCSV();

                return gson.toJson(Collections.singletonMap("status", "updated"));
            }

            return gson.toJson(Collections.singletonMap("error", "book not found"));
        });


    }
}
class UpdateRequest {
    int id;
    String type;
    int value;
}
