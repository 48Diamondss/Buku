package com.example.buku.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.buku.R;
import com.example.buku.dto.BookHistoryDTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> implements Filterable {

    private List<BookHistoryDTO> mData;
    private LayoutInflater mInflater;
    private HistoryItemAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public HistoryItemAdapter(Context context, ArrayList<BookHistoryDTO> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public HistoryItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_book_item_history, parent, false);
        return new HistoryItemAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookHistoryDTO data = mData.get(position);
        Picasso.get().load(data.image).into(holder.imgBuku);
        holder.tvHargaBuku.setText("Rp. " +data.total_price+ ".000");
        holder.tvJudulBuku.setText(data.name);
        holder.tvquantity.setText(data.qty);
        holder.tvstatus.setText(data.status);
        int conditionColor;
        if(data.status.equals("1")){
            holder.tvstatus.setText("Pending");
            conditionColor = Color.parseColor("#FB7181");
        } else{
            holder.tvstatus.setText("Payment Successful");
            conditionColor = Color.parseColor("#4BB543");
        }
        holder.tvstatus.setTextColor(conditionColor);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgBuku;
        TextView tvAuthor,tvHargaBuku, tvstatus, tvquantity, tvJudulBuku;

        ViewHolder(View itemView) {
            super(itemView);
            imgBuku = itemView.findViewById(R.id.img_buku);
            tvJudulBuku = itemView.findViewById(R.id.tv_judul_buku);
            tvHargaBuku = itemView.findViewById(R.id.tv_harga_buku);
            tvstatus = itemView.findViewById(R.id.tv_status);
            tvquantity = itemView.findViewById(R.id.tv_qty);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(HistoryItemAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}