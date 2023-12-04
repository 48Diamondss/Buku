package com.example.buku.fragment;


import static com.example.buku.Handler.Constant.URL_NEW_BOOK;
import static com.example.buku.Handler.Constant.URL_POPULAR;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.buku.R;
import com.example.buku.activity.DetailBookActivity;
import com.example.buku.activity.SearchActivity;
import com.example.buku.adapter.NewBookAdapter;
import com.example.buku.dto.BookDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    // deklarasi awal
    LinearLayout ll_searchView;
    ViewFlipper v_flipper;
    RecyclerView rvNewBook;
    RecyclerView rvPopularBook;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        inisialisasi(v, container.getContext());
        mainButton();


        // buat load di awal awal, berdasarkan buku baru dan juga buku yang populer pada recyleview tampilan awal

        loadData_bukuNEW_BOOK(container.getContext());
        LoadData_buku_POPULAR_BOOK(container.getContext());
        return v;
    }

    private void inisialisasi(View v, Context context){ // inisiasi awal

        ll_searchView = v.findViewById(R.id.ll_searchView);

        // ini buat bannerrrr
        int[] images = new int[] {R.drawable.carousel1, R.drawable.carousel2, R.drawable.carousel3, R.drawable.carousel4};
        v_flipper = v.findViewById(R.id.v_flipper);
        for (int image:images){
            flipperImages(image);
        }

        rvPopularBook = v.findViewById(R.id.rv_popular_book);
        rvPopularBook.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,true));

        rvNewBook = v.findViewById(R.id.rv_new_book);
        rvNewBook.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,true));
    }

    private void mainButton(){ // ke search activity ketika ditekan
        ll_searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SearchActivity.class);
                startActivity(i);
            }
        });
    }

    public void flipperImages(int image){ // buat function agar banner bisa geser geser
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
    }

    private void loadData_bukuNEW_BOOK(Context context){ // load data buku baru dri api yg diambil dri database

        ArrayList<BookDTO> data = new ArrayList<>(); // deklarasi arraylist Bookdto

        /** mulailah menggunakan library volley dibawah ini untuk mengubungkan ke api lalu ke database,
         * dibawah ini menggunakan metode GET, GET digunakan untuk request data,
         * State server tidak berubah walaupun di-GET terus-terusan kecuali data di server dirubah oleh client yang lain.
         */

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_NEW_BOOK,
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

                                // masukin ke arraylistnya berdasarkan dri object api product
                                data.add(new BookDTO(product));
                            }

//                            // ISI_Datanya();
                            NewBookAdapter adapter = new NewBookAdapter(context, data);
                            adapter.setClickListener(new NewBookAdapter.ItemClickListener(){

                                /**
                                 * ketika bukunya ditekan maka recyleviewnya akan intent data, berdasarkan bukunya
                                 * sehingga di detail activity bisa ngebaca bahwa ini buku apa
                                 * @param view
                                 * @param position
                                 */

                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent i = new Intent(getActivity(), DetailBookActivity.class);
                                    i.putExtra("data",data.get(position));
                                    startActivity(i);
                                }
                            });

                            // disenggol adapternya biar sadar, (masukin datanya kedalam recyleview dri adapter tdi)
                            rvNewBook.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

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

                });
        // adding our stringrequest to queue

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    public void LoadData_buku_POPULAR_BOOK(Context context){  // LOAD API DRI database buku popular book
        ArrayList<BookDTO> data = new ArrayList<>(); // deklarasi awal arraylist bookdto

        /** mulailah menggunakan library volley dibawah ini untuk mengubungkan ke api lalu ke database,
         * dibawah ini menggunakan metode GET, GET digunakan untuk request data,
         * State server tidak berubah walaupun di-GET terus-terusan kecuali data di server dirubah oleh client yang lain.
         */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POPULAR,
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
                                data.add(new BookDTO(product));
                            }

                            // ISI_Datanya();
                            NewBookAdapter adapter = new NewBookAdapter(context, data);
                            adapter.setClickListener(new NewBookAdapter.ItemClickListener(){

                                /**
                                 * ketika bukunya ditekan maka recyleviewnya akan intent data, berdasarkan bukunya
                                 * sehingga di detail activity bisa ngebaca bahwa ini buku apa
                                 * @param view
                                 * @param position
                                 */
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent i = new Intent(getActivity(), DetailBookActivity.class);
                                    i.putExtra("data",data.get(position));
                                    startActivity(i);
                                }
                            });

                            // disenggol adapternya biar sadar, (masukin datanya kedalam recyleview dri adapter tdi)
                            rvPopularBook.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { // jika eror
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: ", error.getMessage());

                    }
                });
        // direquest dah
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}