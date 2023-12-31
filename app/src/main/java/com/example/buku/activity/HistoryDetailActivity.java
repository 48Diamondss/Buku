package com.example.buku.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buku.R;

public class HistoryDetailActivity extends AppCompatActivity {

    // Inisial awal buat txtview -/dkk
    ImageView imgBuku;
    TextView tvJudulBuku, tvHargaBuku, tvDescBuku, tvJmlHal, tvTglTerbit, tvIsbn, tvPenerbit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        // dipanggil
        inisialisasi();

    }

    private void inisialisasi(){ // inilisasisasiiiii
        imgBuku = findViewById(R.id.img_buku);
        tvJudulBuku = findViewById(R.id.tv_judul_buku);
        tvHargaBuku = findViewById(R.id.tv_harga_buku);
        tvDescBuku = findViewById(R.id.tv_desc_buku);
        tvJmlHal = findViewById(R.id.tv_jml_hal);
        tvTglTerbit = findViewById(R.id.tv_tgl_terbit);
        tvIsbn = findViewById(R.id.tv_isbn);
        tvPenerbit = findViewById(R.id.tv_penerbit);
    }

}