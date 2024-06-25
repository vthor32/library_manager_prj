package model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("isbn")
    private String isbn;

    @SerializedName("category")
    private String category;

    @SerializedName("isAvailable")
    private boolean isAvailable;

    public Book(String title, String author, String isbn, String category) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.isAvailable = true;
    }

    // Getters e Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }

    @Override
    public String toString() {
        return title + " by " + author;
    }
}
