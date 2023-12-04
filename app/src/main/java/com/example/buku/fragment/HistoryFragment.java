package com.example.buku.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.buku.Handler.Constant;
import com.example.buku.R;
import com.example.buku.activity.HomeActivity;
import com.example.buku.adapter.HistoryItemAdapter;
import com.example.buku.dto.BookHistoryDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryFragment extends Fragment {

    // delklarasi awal.
    RecyclerView rvHistoryTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        inisialisasi(v, getContext());
        loadData(container.getContext());

        return v;
    }

    private void inisialisasi(View v, Context context){

        //buat layout  menjadi vertical
        rvHistoryTransaction = v.findViewById(R.id.rv_history_transaction);
        rvHistoryTransaction.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,true));
    }

    private void loadData(Context context) {
        // digunakan buat ambil id_user dari session !!
        HomeActivity activity = (HomeActivity) getActivity();

        String id_data_User = activity.getUSERDATA_ID();

        ArrayList<BookHistoryDTO> data = new ArrayList<>(); // deklarasi arraylist pad class bookhistoryDTO

        /** mulailah menggunakan library volley dibawah ini untuk mengubungkan ke api lalu ke database,
         * dibawah ini menggunakan metode post, yang berarti  metode HTTP Request
         * yang digunakan untuk membuat data baru dengan menyisipkan data dalam body saat request dilakukan

         */

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_HISTORI_CART_PEMBELIANN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                data.add(new BookHistoryDTO(product));

                                HistoryItemAdapter adapter = new HistoryItemAdapter(context, data);
                                rvHistoryTransaction.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) { // jika eror
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { // jika eror
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: ", error.getMessage());

                    }
                }){

            /**
             * metode dibawah ini adalah yang digunakan untuk memasukkan data imputan dari aplikasi kedalam api,
             * dengan mencocokkan pada apa yang telah dideklarasikan pada methode post di api (php - nya), sehingga
             * bisa terhubung.... kedalam api dan bisa mennyampaikannya kedalam database bahwa terdapat insert - baru

             */

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                // digunakan untuk mengambil data user id dari sessionn yaa!!!
                params.put("id_user", id_data_User);

                return params;
            }
        };

        // di request deh
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }
}