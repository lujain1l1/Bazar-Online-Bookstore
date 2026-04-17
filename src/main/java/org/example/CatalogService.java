package org.example;

import java.io.*;
import java.util.*;

public class CatalogService {
    private List<Book> books = new ArrayList<>();

    public CatalogService() {
        loadBooksFromCSV();
    }

    private void loadBooksFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("catalog.csv"))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                int price = Integer.parseInt(parts[3]);
                String topic = parts[4];
                books.add(new Book(id, title, quantity, price, topic));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book getBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }


    public List<Book> getBooksByTopic(String topic) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getTopic().toLowerCase().contains(topic.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }
    public void saveToCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("catalog.csv"))) {

            pw.println("id,title,quantity,price,topic");

            for (Book b : books) {
                pw.println(
                        b.getId() + "," +
                                b.getTitle() + "," +
                                b.getQuantity() + "," +
                                b.getPrice() + "," +
                                b.getTopic()
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
