package com.example.buku.activity;

import static com.example.buku.Handler.Constant.url_upload_bukti_pembayaran_bukan_gambar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.ims.RegistrationManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.buku.Handler.Constant;
import com.example.buku.Handler.RequestHandler;
import com.example.buku.Handler.SQLite;
import com.example.buku.R;
import com.example.buku.dto.BookCartDTO;
import com.example.buku.dto.BookDTO;
import com.example.buku.fragment.CartFragment;
import com.example.buku.fragment.HistoryFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    // Inisial awal buat edit-text -/dkk
    TextView Total_payment_id,tv_summary_item;
    Button btnCheckout;
    EditText edt_address;

    String HARGA_KESELURUHAN;
    String TOTAL_BUKU_YANG_DI_BELI;

    ImageView img;
    String address;
    Bitmap bitmap;

    String id_data_User;

    BookCartDTO bookCartDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // jangan lupa ambil intent buat datanya
        bookCartDTO = (BookCartDTO) getIntent().getSerializableExtra("data_123");

        setContentView(R.layout.activity_checkout);
        inisialisasi();

        mainButton();
        getSupportActionBar().hide();

        img = findViewById(R.id.image_piew);

        /**
         * dibawah ini digunakan untuk intent kedalam media store storage masing masing - dari user
         * gunanya untuk mengambil gambar untuk proses upload di checkout buku
         */

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if(result.getResultCode() == Activity.RESULT_OK) {

                            Intent data = result.getData();

                            Uri uri = data.getData();

                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                img.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                activityResultLauncher.launch(intent);
            }
        });



    }

    private boolean pengubahan(){ //penampungan sementara

        address = edt_address.getText().toString().trim();

        return false;
    }


    private void inisialisasi(){ // inisialisasi

        btnCheckout = findViewById(R.id.btn_checkout);

        //Buyer_id = findViewById(R.id.Buyer_id);
        Total_payment_id = findViewById(R.id.Total_payment_id);
        tv_summary_item = findViewById(R.id.Summary_item_id);

        Intent intent = getIntent();
        HARGA_KESELURUHAN = intent.getStringExtra("loadsPosition");
        TOTAL_BUKU_YANG_DI_BELI = intent.getStringExtra("loads_summary");

        Total_payment_id.setText("Rp." +HARGA_KESELURUHAN + "00");

        tv_summary_item.setText(TOTAL_BUKU_YANG_DI_BELI);

        edt_address = findViewById(R.id.edt_addresss);


        HomeActivity activity = new HomeActivity();

        id_data_User = activity.getUSERDATA_ID(); // buat id_user



    }

    private void mainButton(){

        // button checkout dikasi listener biar bisa di tekan dan menjalankan aksinya

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pengubahan();

                if (edt_address.length()<1) { // jika address blm di isi, maka notif dibawah ini akan keluar " please input your address corectly"


                    edt_address.requestFocus();

                    Toast.makeText(CheckoutActivity.this,"Please input your address correctly",Toast.LENGTH_SHORT).show();

                }

                else{

                    // menjalankan sebagaimana proses - penguploadtan gambar - beserta data pada public void dibawah
                    kedalam_transaction_item();
                    upload_gambar();

                }

            }
        });

    }

    // upload book_id, id_user, id_cart, qty (TOTAL), total_price, Address.

    public void kedalam_transaction_item(){


        /** mulailah menggunakan library volley dibawah ini untuk mengubungkan ke api lalu ke database,
         * dibawah ini menggunakan metode post, yang berarti  metode HTTP Request
         * yang digunakan untuk membuat data baru dengan menyisipkan data dalam body saat request dilakukan

         */

        StringRequest request = new StringRequest(Request.Method.POST, Constant.url_upload_bukti_pembayaran_bukan_gambar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    // mengambil json object untuk mendapatkan nilai dari api yang telah dibuat
                    int sukses = jObj.getInt("success");
                    String pesan = jObj.getString("message");


                    if (sukses == -1) {


                        Toast.makeText(CheckoutActivity.this, pesan, Toast.LENGTH_SHORT).show();

                        return;

                    }

                    // Error in registration!  // user failed to store
                    // jika gagal didaftarkan
                    if (sukses == -2){


                        Toast.makeText(CheckoutActivity.this, pesan, Toast.LENGTH_SHORT).show();
                        return;

                    }

                    // jika ada kesalan dalam PENGISIAN KE DALAM CART
                    if (sukses == -3) {


                        Toast.makeText(CheckoutActivity.this, pesan, Toast.LENGTH_SHORT).show();

                        return;
                    }

                } catch (Exception ex) { // ketika eror
                    Toast.makeText(CheckoutActivity.this, "Something Wrong with your connection", Toast.LENGTH_SHORT).show();

                    Log.e("Error: ", ex.toString());
                    ex.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) { // ketika eror

                Toast.makeText(CheckoutActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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

             /**   yang dipake dibawha ini (nyocokin php(api))
              * upload book_id, id_user, id_cart, qty (TOTAL), total_price, Address.

                $bookid = $_POST['book_id'];
                $userid = $_POST['user_id'];
                $cartid = $_POST['cart_id'];
                $qty      = ($_POST['qty']);
                $total_price    = ($_POST['total_price']);
                $Address    = ($_POST['address']);
              */

                params.put("book_id", bookCartDTO.id);

                params.put("user_id", id_data_User);

                params.put("cart_id", bookCartDTO.id_cart);

                params.put("qty", TOTAL_BUKU_YANG_DI_BELI);

                params.put("total_price", HARGA_KESELURUHAN);
                params.put("address", address);

                return params;

            }
        };
// baru bisa dijalankan
        RequestHandler.getInstance((Context) this).addToRequestQueue(request);

    }

    // upload gambar ke payment.
    public void upload_gambar(){

        // deklarasi dulu

        ByteArrayOutputStream byteArrayOutputStream;
        byteArrayOutputStream = new ByteArrayOutputStream();

        // bitmap itu gambarnya yang diambil dari storage yee.,..
        if(bitmap != null) {

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte [] bytes = byteArrayOutputStream.toByteArray();
            final  String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
            //RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait..");

            // dibuat progress dialognya tidak bisa di cancel, agar keren :v, maksudnya biar tau kalo proses penguploadsedang jalan
            progressDialog.setCancelable(false);
            progressDialog.show();

            /** mulailah menggunakan library volley dibawah ini untuk mengubungkan ke api lalu ke database,
             * dibawah ini menggunakan metode post, yang berarti  metode HTTP Request
             * yang digunakan untuk membuat data baru dengan menyisipkan data dalam body saat request dilakukan

             */

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_upload_bukti_pembayaran,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                // sama seperti yang diatass ya kurang lebihh
                                JSONObject jObj = new JSONObject(response);
                                int sukses = jObj.getInt("success");

                                if (sukses == 1) {

                                    Toast.makeText(getApplicationContext(), "Upload Payment Success ^.^", Toast.LENGTH_SHORT).show();
                                    // masukin ke home page
                                    progressDialog.dismiss();

                                    Intent i = new Intent(getApplication(), HomeActivity.class);
                                    startActivity(i);

                                } else

                                    Toast.makeText(CheckoutActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                protected Map<String, String> getParams(){
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("img", base64Image);
                    paramV.put("user_id",id_data_User);
                    paramV.put("total_harga",HARGA_KESELURUHAN);

                    return paramV;
                }
            };


            /**
             * dibawah ini digunakan untuk menghandle klo internet slow, jadi dia timeoutnya di  x 0
             * retriesnya ga usah diulang, jdi ga ngebug,  baru setelah itu bisa request dehh....
             */
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ));


            Volley.newRequestQueue(this).add(stringRequest);
        }
        else {
            Toast.makeText(getApplicationContext(), "Select Image First! ", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}