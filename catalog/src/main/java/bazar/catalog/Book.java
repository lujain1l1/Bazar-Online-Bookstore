package bazar.catalog;
public class Book {
    private int id;
    private String title;
    private int quantity;
    private int price;
    private String topic;

    public Book(int id, String title, int quantity, int price, String topic) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.topic = topic;
    }


    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getQuantity() { return quantity; }
    public int getPrice() { return price; }
    public String getTopic() { return topic; }
    public void setPrice(int price) {
        this.price = price;
    }



    public void setQuantity(int quantity) { this.quantity = quantity; }
}
