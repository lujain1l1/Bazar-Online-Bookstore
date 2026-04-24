import static spark.Spark.*;

public class OrderServer {
    public static void main(String[] args) {
        port(8081); // تحديد المنفذ الخاص بخادم الطلبات

        // نقطة النهاية (Endpoint) لعملية الشراء
        post("/purchase/:item_number", (req, res) -> {
            String itemNumber = req.params(":item_number");
            return "Checking order for item: " + itemNumber;
        });
    }
}