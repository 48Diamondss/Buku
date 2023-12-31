package com.example.buku.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.buku.Handler.SQLite;
import com.example.buku.R;
import com.example.buku.activity.ChangeProfileActivity;
import com.example.buku.activity.HomeActivity;
import com.example.buku.activity.WebViewActivity;
import com.example.buku.adapter.Session;
import com.example.buku.auth.SignInActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    // inisiasi awal awal
    TextView tvName, tvEmail,tvPhone;
    Button btnChangeProfile, btnSignOut, mapp, button123;
    private Object ProfileFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        inisialisasi(v);
        mainButton();

        return v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void inisialisasi(View v){
        tvName = v.findViewById(R.id.tv_name);
        tvEmail = v.findViewById(R.id.tv_email);
        tvPhone = v.findViewById(R.id.tv_phone);

        btnChangeProfile = v.findViewById(R.id.btn_change_profile);
        btnSignOut = v.findViewById(R.id.btn_sign_out);

        mapp = v.findViewById(R.id.buat_map);
        button123 = v.findViewById(R.id.button123);

        // ambil intent untuk ngisi data profil dari session ()!!
        HomeActivity activity = (HomeActivity) getActivity();

        String noHP_USER = activity.getUSERDATA_NOHAPE();

        // Fetching user details from SQLite
        // Menyiapkan data user dari SQLite

        SQLite dbEntry = new SQLite(getActivity());
        HashMap<String, String> user = dbEntry.getUserDetails();

        // dan yang diambil adalah data nama dan email dari SQLite
        String name = user.get("nama");
        String email = user.get("email");

        // di set text ye bos
        tvName.setText(name);
        tvEmail.setText(email);
        tvPhone.setText(noHP_USER);

    }

    private void mainButton(){
        btnChangeProfile.setOnClickListener(new View.OnClickListener() { // btn buat ke change profil activity
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangeProfileActivity.class);
                startActivity(i);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() { // btn signout
            @Override
            public void onClick(View v) {

                SQLite dbEntry = new SQLite(getActivity());
                HomeActivity activity = (HomeActivity) getActivity();

                // menghapus data di SQLite jika di logout. sehingga tidak ada penyimpanan di local database
                dbEntry.deleteUsers();


                activity.pencet();//panggil method buat log out, krn di fragment sini ga biga gitu
                // dia tempatnya ada di home activity

            }
        });

        mapp.setOnClickListener(new View.OnClickListener() { // buat nampilin map
            @Override
            public void onClick(View v) {

                HomeActivity activity = (HomeActivity) getActivity();

                activity.tombol_map();
            }
        });

        button123.setOnClickListener(new View.OnClickListener() { // buat website
            @Override
            public void onClick(View v) {

                HomeActivity activity = (HomeActivity) getActivity();
                activity.tombol_webview();

            }
        });
    }
}