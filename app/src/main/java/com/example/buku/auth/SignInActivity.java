package com.example.buku.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.buku.Handler.SQLite;
import com.example.buku.R;
import com.example.buku.activity.HomeActivity;
import com.example.buku.adapter.Session;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    // Inisial awal buat text-piew -/dkk
    Button btnSignIn;
    TextInputEditText edtEmail, edtPassword;
    TextView tvBtnSignup;
    TextView tvErrorTextLogin;

    String email, passwordw;
    String pesan;



    // pembatasan buat nama
    final Pattern NAMA_PATTERN =
            Pattern.compile("^" +
                    ".{5,}"               //at least 5 characters

            );

    // pembatasan buat password
    final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    private SQLite dbsqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        // SqLite database handler
        dbsqlite = new SQLite(getApplicationContext());

        /**
         *  pengecekan dilakukan agar tau apakah user ini  login apa belum, jadi klo udah login ia akan ke home activity
         *
         *  sebaliknya maka dia akan stey di activity ini untuk log - in terlebih dahulu
         */

        if(Session.getInstance(this).isLoggedin()){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
           // return;
        }

        inisialisasi();

        // ngehilangin support action barnya
        getSupportActionBar().hide();
        Sign_up_Button();

    }

    private void inisialisasi() {
        btnSignIn = findViewById(R.id.btn_sign_in);//ga gw pake karena gw lanngsung pake di xml ( Sign_in)
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        tvBtnSignup = findViewById(R.id.tv_btn_signup);

        tvErrorTextLogin = findViewById(R.id.tv_errortext_login);

    }

    public void Sign_In (View view) {

        // PENAMPUNGAN VARIABLE YANG TELAH DIRUBAH DIBAWAH
        pengubahan();

        // JIKA EMAIL KOSONG MAKA DIA TURUN MENEGAMBIL CLASS YANG MENGURUSNYA DIBAWAH
        if (email.isEmpty()) {
            LoginEmail(edtEmail); // ambil kebawah
            return;

        }

        // JIKA EMAIL/username TIDAK SESUAI DENGAN KETENTUAN bagaimana dia login menggunakan fullname/email
        // seperti @BLABLA.COM DIA AKAN MENGELUARKAN NOTIF
        if (! NAMA_PATTERN.matcher(email).matches()) {

            tvErrorTextLogin.setText("The email address or full name you entered is invalid !");
            edtEmail.requestFocus();

            return;
        }


        // JIKA PASSWORD TIDAK SESUAI DENGAN KETENTUAN YAITU DIKIT ISINYA (MINIMAL 8 KARAKTER)
        // MAKA DIA EROR (MENAMPILKAN PESAN)
        if (edtPassword.length()<1) {

            tvErrorTextLogin.setText("Password field is required!");

            edtPassword.requestFocus();
            return;

        }

        // JIKA PASSWROD TIDAK SESUAI DENGAN KETENTUAN DIA NGACO ISINYA
        //MAKA PESAN NOTIF AKAN KELUAR
        if (! (PASSWORD_PATTERN).matcher(passwordw).matches()){
            tvErrorTextLogin.setText("At least with 8 Characters," +
                    " 1 Lower case and 1 Upper case and have 1 Special character in your Password !");
            edtPassword.requestFocus();
        }


         // jika sqlite belum memiliki data berupa email dan password ini, dia akan  menjalankan ke public void menjalankan aksi 1

        if(!dbsqlite.checkUser(email, passwordw)){

            menjalankan_aksi1();

        }

       else {
           // jika  memiliki data berupa email dan password ini, dia akan  menjalankan ke public void menjalankan aksi 2


            menjalankan_aksi2();
       }

    }


    public void menjalankan_aksi1(){ // kalo sqlite blom ada data email dn password

         /**
          * progress dialog, display the progress of any running task or an operation. An Alert Dialog is a type of alert
          * message displayed over the screen that lets users choose between options to respond to the message of the alert.
          */
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        pengubahan();

        /** mulailah menggunakan library volley dibawah ini untuk mengubungkan ke api lalu ke database,
         * dibawah ini menggunakan metode post, yang berarti  metode HTTP Request
         * yang digunakan untuk membuat data baru dengan menyisipkan data dalam body saat request dilakukan

         */

        StringRequest request = new StringRequest(Request.Method.POST, Constant.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);


                    // mengambil json object untuk mendapatkan nilai dari api yang telah dibuat

                    String namauser = jObj.getString("namee"); // ambil dari database ( json)
                    String emailuser = jObj.getString("email");
                    String nohpuser = jObj.getString("nohp");
                    String id_user = jObj.getString("id_user123"); // ambil dari database ( json)

                    int sukses = jObj.getInt("success");
                    pesan = jObj.getString("message");

                    if (sukses == -1){ // jika respon - 1 dia akan keluarin pesan dri json

                        progressDialog.dismiss();
                        tvErrorTextLogin.setText(pesan);
                        edtEmail.requestFocus();
                        edtPassword.requestFocus();

                        return;

                    }

                    if (sukses == -2){ // sama

                        progressDialog.dismiss();
                        tvErrorTextLogin.setText(pesan);
                        edtEmail.requestFocus();
                        edtPassword.requestFocus();

                        return;

                    }


                    if (sukses == 1) { // ini jika berhasil maka responnya adalah 1 dari json dan akan toast "apa"

                        progressDialog.dismiss();

                        edtEmail.setText("");
                        edtPassword.setText("");
                        tvErrorTextLogin.setText("");


                        Toast.makeText(SignInActivity.this, pesan, Toast.LENGTH_SHORT).show();

                        /**
                         *  masukin data kedalam sqlite
                         */
                        dbsqlite.addUser(namauser, emailuser, passwordw);

                        // masukin ke home page
                        Intent i = new Intent(getApplication(), HomeActivity.class);
                        startActivity(i);

                        // masukin session ye ges !
                        Session.getInstance(getApplicationContext())
                                .userlogin(
                                        id_user, namauser, emailuser, nohpuser
                                );

                        // menyatakan selesai
                        finish();

                    }


                } catch (Exception ex) { // jika eror
                    progressDialog.dismiss();

                    tvErrorTextLogin.setText("Login not matched. Please try again!");
                    Log.e("Error: ", ex.toString());
                    ex.printStackTrace();
                    Log.e("anyText",response);

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {// jika eror
                Log.e("Error: ", error.getMessage());

                Toast.makeText(SignInActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {

            /**
             * metode dibawah ini adalah yang digunakan untuk memasukkan data imputan dari aplikasi kedalam api,
             * dengan mencocokkan pada apa yang telah dideklarasikan pada methode post di api (php - nya), sehingga
             * bisa terhubung.... kedalam api dan bisa mennyampaikannya kedalam database bahwa terdapat insert - baru

             */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                // kondisi dibawah ini digunakan apabila login dengan menggunakan email
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    params.put("email",email);
                    params.put("password", passwordw);

                }

                else {

                    // kondisi dibawah ini digunakan apabila login dengan menggunakan nama panjang / username
                    params.put("name", email);
                    params.put("password", passwordw);

                }
                return params;

            }

        };

        // baru di request deh

        RequestHandler.getInstance(this).addToRequestQueue(request);

    }


    public void menjalankan_aksi2(){// kalo sqlite blom ada data email dn password

        /**
         * progress dialog, display the progress of any running task or an operation. An Alert Dialog is a type of alert
         * message displayed over the screen that lets users choose between options to respond to the message of the alert.
         */

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        pengubahan();

        /** mulailah menggunakan library volley dibawah ini untuk mengubungkan ke api lalu ke database,
         * dibawah ini menggunakan metode post, yang berarti  metode HTTP Request
         * yang digunakan untuk membuat data baru dengan menyisipkan data dalam body saat request dilakukan

         */

        StringRequest request = new StringRequest(Request.Method.POST, Constant.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response); // ambil respon dari json

                    String namauser = jObj.getString("namee"); // ambil dari database ( json)
                    String emailuser = jObj.getString("email");
                    String nohpuser = jObj.getString("nohp");
                    String id_user = jObj.getString("id_user123"); // ambil dari database ( json)

                    int sukses = jObj.getInt("success");
                    pesan = jObj.getString("message");

                    if (sukses == -1){ // jika respon == -1 maka blm bisa

                        progressDialog.dismiss();
                        tvErrorTextLogin.setText(pesan);
                        edtEmail.requestFocus();
                        edtPassword.requestFocus();

                        return;

                    }

                    if (sukses == -2){ // sama belum bisa

                        progressDialog.dismiss();
                        tvErrorTextLogin.setText(pesan);
                        edtEmail.requestFocus();
                        edtPassword.requestFocus();

                        return;
                    }

                    if (sukses == 1) { // ini respon yang berhasil

                        progressDialog.dismiss();

                        edtEmail.setText("");
                        edtPassword.setText("");
                        tvErrorTextLogin.setText("");

                        Toast.makeText(SignInActivity.this, pesan, Toast.LENGTH_SHORT).show();

                        // masukin ke home page
                        Intent i = new Intent(getApplication(), HomeActivity.class);
                        startActivity(i);

                        // masukin session ye ges !
                        Session.getInstance(getApplicationContext())
                                .userlogin(
                                        id_user, namauser, emailuser, nohpuser

                                );

                        // menyatakan selesai
                        finish();

                    }


                } catch (Exception ex) {
                    progressDialog.dismiss();

                    tvErrorTextLogin.setText("Login not matched. Please try again!");
                    Log.e("Error: ", ex.toString());
                    ex.printStackTrace();
                    Log.e("anyText",response);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());


                Toast.makeText(SignInActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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

                // kondisi dibawah ini digunakan apabila login dengan menggunakan email
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    params.put("email",email);
                    params.put("password", passwordw);

                }

                else {

                    // kondisi dibawah ini digunakan apabila login dengan menggunakan nama panjang / username
                    params.put("name", email);
                    params.put("password", passwordw);

                }
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(request);
    }

    private boolean pengubahan(){

        email = edtEmail.getText().toString().trim();
        passwordw = edtPassword.getText().toString().trim();

        return false;
    }
    private boolean LoginEmail(EditText edtEmail) {

        pengubahan();

        if (email.length()<5) {
            tvErrorTextLogin.setText("Email field is required ! ");
            edtEmail.requestFocus();
            return false;
        }

        else {
            tvErrorTextLogin.setText("");
            edtEmail.requestFocus();
            return true;
        }

    }

    private void Sign_up_Button() {

        tvBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), SignUpActivity.class);
                startActivity(i);
            }
        });
    }
}