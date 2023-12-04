package com.example.buku.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BookDTO implements Serializable {

    /**
     * Tempat Penampungan data pada BUKU
     */

    public String image;
    public String author;
    public String name;
    public String price;
    public String description;
    public String total_pages;
    public String published_at;
    public String isbn;
    public String rating;
    public String stock;
    public String id;

    public BookDTO(JSONObject product) {
        try {
            image = product.getString("image");
            author = product.getString("author");
            name = product.getString("name");
            price = product.getString("price");
            id = product.getString("id");
            description = product.getString("description");
            total_pages = product.getString("total_pages");
            published_at = product.getString("published_at");
            isbn = product.getString("isbn");
            rating = product.getString("rating");
            stock = product.getString("stock_kun");
        } catch(JSONException e){

        }
    }
}