package com.example.ung_dung_thuong_mai_dien_tu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ung_dung_thuong_mai_dien_tu.R;
import com.example.ung_dung_thuong_mai_dien_tu.activity.ChiTietActivity;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoi;

import java.text.DecimalFormat;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    Context context;
    List<SpMoi> list;

    public SearchAdapter(Context context, List<SpMoi> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dienthoai, parent, false); // dùng layout điện thoại
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SpMoi sp = list.get(position);

        holder.tensp.setText(sp.getTensp());
        holder.motasp.setText(sp.getMota());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.giasp.setText("Giá: " + decimalFormat.format(sp.getGiasp()) + " VNĐ");

        Glide.with(context)
                .load(sp.getHinhanh())
                .into(holder.hinhanh);

        // CLICK ITEM
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietActivity.class);
            intent.putExtra("chitiet", sp);
            intent.putExtra("from", "timkiem");  // QUAN TRỌNG
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tensp, giasp, motasp;
        ImageView hinhanh;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tensp = itemView.findViewById(R.id.itemdt_tensp);
            giasp = itemView.findViewById(R.id.itemdt_giasp);
            motasp = itemView.findViewById(R.id.itemdt_motasp);
            hinhanh = itemView.findViewById(R.id.itemdt_image);
        }
    }
}
