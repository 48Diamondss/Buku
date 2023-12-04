package com.example.buku.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BookHistoryDTO {

    /**
     * Tempat Penampungan data PADA BUKU HISTORI PEMBELIAN USER
     */

    public String id;
    public String book_id;
    public String id_user;
    public String id_cart;
    public String name;
    public String image;
    public String qty;
    public String total_price;
    public String Address;
    public String status;

    public BookHistoryDTO(JSONObject product) { // json api
        try {
            id = product.getString("id");
            book_id = product.getString("book_id");
            id_user = product.getString("id_user");
            id_cart = product.getString("id_cart");
            name = product.getString("name");
            image = product.getString("image");
            qty = product.getString("qty");
            total_price = product.getString("total_price");
            Address = product.getString("Address");
            status = product.getString("status");
        } catch(JSONException e){

        }
    }

}
