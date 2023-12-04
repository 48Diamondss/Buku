package com.example.buku.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.buku.Handler.Constant;
import com.example.buku.Handler.RequestHandler;
import com.example.buku.R;
import com.example.buku.dto.BookDTO;
import com.example.buku.fragment.HomeFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailBookActivity extends AppCompatActivity implements View.OnClickListener{

    // Inisial awal buat txtview -/dkk
    ImageView imgBuku;
    TextView tvAuthor, tvJudulBuku, tvHargaBuku, tvDescBuku, tvJmlHal, tvTglTerbit, tvIsbn, tvPenerbit, tvStock;
    Button btnAddCart;
    BookDTO bookDTO;

    int qty = 1;

    String id_buku,id_data_User, harga_buku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);

        // ambil intent data dari bookdto
        bookDTO = (BookDTO) getIntent().getSerializableExtra("data");

        inisialisasi();

        getSupportActionBar().hide();

        btnAddCart.setOnClickListener(this);

    }

    private void inisialisasi(){ // inilisilisasi awal

        imgBuku = findViewById(R.id.img_buku);
        tvAuthor = findViewById(R.id.tv_author);
        tvJudulBuku = findViewById(R.id.tv_judul_buku);
        tvHargaBuku = findViewById(R.id.tv_harga_buku);
        tvDescBuku = findViewById(R.id.tv_desc_buku);
        tvJmlHal = findViewById(R.id.tv_jml_hal);
        tvTglTerbit = findViewById(R.id.tv_tgl_terbit);
        tvIsbn = findViewById(R.id.tv_isbn);
        tvPenerbit = findViewById(R.id.tv_penerbit);
        tvStock = findViewById(R.id.tv_stock);
        btnAddCart = findViewById(R.id.btn_add_cart);

        pengubahan();

        // set textnya ya bro dari book dto. apa apa , yang mau diambil datanya berdasarkan tempatnya
        tvDescBuku.setText(bookDTO.description);
        tvJmlHal.setText(bookDTO.total_pages);
        tvTglTerbit.setText(bookDTO.published_at);
        tvIsbn.setText(bookDTO.isbn);
        tvPenerbit.setText(bookDTO.rating);

        tvAuthor.setText(bookDTO.author);
        tvJudulBuku.setText(bookDTO.name);
        tvHargaBuku.setText(bookDTO.price);
        tvStock.setText(bookDTO.stock);

        // picaso dipakai biar bisa load image, ini library yang keren
        Picasso.get().load(bookDTO.image).into(imgBuku);

    }

    public void pengubahan(){
        HomeActivity activity = new HomeActivity();

        // user_id, book_id, qty, price

        id_data_User = activity.getUSERDATA_ID(); // buat id_user

        id_buku = bookDTO.id;  // buat id_buku

        harga_buku = tvHargaBuku.getText().toString().trim();// buat harga_buku
    }


    public void memasukkan_kedalam_cart(){

        /**
         * progress dialog, display the progress of any running task or an operation. An Alert Dialog is a type of alert
         * message displayed over the screen that lets users choose between options to respond to the message of the alert.
         */

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();

        /** mulailah menggunakan library volley dibawah ini untuk mengubungkan ke api lalu ke database,
         * dibawah ini menggunakan metode post, yang berarti  metode HTTP Request
         * yang digunakan untuk membuat data baru dengan menyisipkan data dalam body saat request dilakukan

         */

        StringRequest request = new StringRequest(Request.Method.POST, Constant.URL_ADD_CART_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    // ambil dari database ( json)
                    int sukses = jObj.getInt("success");
                    String pesan = jObj.getString("message");

                    if (sukses == -1) {

                        progressDialog.dismiss();
                        Toast.makeText(DetailBookActivity.this, pesan, Toast.LENGTH_SHORT).show();

                        return;

                    }

                    // Error in registration!  // user failed to store
                    // jika gagal didaftarkan
                    if (sukses == -2){

                        progressDialog.dismiss();
                        Toast.makeText(DetailBookActivity.this, pesan, Toast.LENGTH_SHORT).show();
                        return;

                    }

                    // jika ada kesalan dalam PENGISIAN KE DALAM CART
                    if (sukses == -3) {

                        progressDialog.dismiss();
                        Toast.makeText(DetailBookActivity.this, pesan, Toast.LENGTH_SHORT).show();

                        return;
                    }

                    else{
                        progressDialog.dismiss();

                        Toast.makeText(DetailBookActivity.this, "Cool, you have successfully added to your cart ! ", Toast.LENGTH_SHORT).show();

                        // masukin ke sign in
                        Intent i = new Intent(getApplication(), HomeActivity.class);
                        startActivity(i);

                        // menyatakan selesai
                        finish();
                    }
                } catch (Exception ex) { // jika eror
                    Log.e("Error: ", ex.toString());
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) { // jika eror

                Toast.makeText(DetailBookActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Log.e("Error: ", error.getMessage());

            }
        }

        ) {

            /**
             * metode dibawah ini adalah yang digunakan untuk memasukkan data imputan dari aplikasi kedalam api,
             * dengan mencocokkan pada apa yang telah dideklarasikan pada methode post di api (php - nya), sehingga
             * bisa terhubung.... kedalam api dan bisa mennyampaikannya kedalam database bahwa terdapat insert - baru

             */

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_id", id_data_User);
                params.put("book_id", id_buku);
                params.put("qty", String.valueOf(qty)); // default
                params.put("price", harga_buku);

                return params;

            }
        };
// baru bisa dijalankan
        RequestHandler.getInstance((Context) this).addToRequestQueue(request);

    }

    @Override
    public void onClick(View v) { // karena dibawah kita implement view on click listener
        if(v.getId()==R.id.btn_add_cart){

            try { // dihandle jika ade yang eror ya beb maka pake tangkep dan coba
                memasukkan_kedalam_cart();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}