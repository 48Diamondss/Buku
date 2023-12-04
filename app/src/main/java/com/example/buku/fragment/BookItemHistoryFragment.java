package com.example.buku.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.buku.R;

public class BookItemHistoryFragment extends Fragment {

    // inilisiasi
    LinearLayout llProduct;
    ImageView imgBuku;
    TextView tvAuthor,tvJudulBuku, tvHargaBuku, tvQty, tvCON;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_book_item, container, false);

        inisialisasi(v);

//        int nomor = Integer.parseInt(tvCON.getText().toString());
//        int conditionColor;
//        if(nomor == 1){
//            tvCON.setText("Payment Successful");
//            conditionColor = Color.parseColor("#689f38");
//        } else{
//            tvCON.setText("Pending");
//            conditionColor = Color.parseColor("#ff5722");
//        }
//        tvCON.setTextColor(conditionColor);

        return v;
    }

    private void inisialisasi(View v){
        llProduct = v.findViewById(R.id.ll_product);
        imgBuku = v.findViewById(R.id.img_buku);
        tvAuthor = v.findViewById(R.id.tv_author);
//        tvCON = v.findViewById(R.id.tv_status);
        tvJudulBuku = v.findViewById(R.id.tv_judul_buku);
        tvQty = v.findViewById(R.id.tv_qty);
        tvHargaBuku = v.findViewById(R.id.tv_harga_buku);
    }

}