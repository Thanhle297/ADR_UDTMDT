package com.example.ung_dung_thuong_mai_dien_tu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ung_dung_thuong_mai_dien_tu.R;
import com.example.ung_dung_thuong_mai_dien_tu.model.GioHang;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    private Context context;
    private List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        holder.item_giohang_tensp.setText(gioHang.getTensp());
        holder.item_giohang_soluong.setText(String.valueOf(gioHang.getSoluong()));
        holder.item_giohang_gia.setText(decimalFormat.format(gioHang.getGiasp()) + " ₫");
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);

        // Cập nhật tổng giá ban đầu
        updateTotalPrice(holder, gioHang);

        // Xử lý nút cộng
        holder.item_giohang_cong.setOnClickListener(v -> {
            int sl = gioHang.getSoluong();
            if (sl < 99) { // giới hạn hợp lý
                gioHang.setSoluong(sl + 1);
                holder.item_giohang_soluong.setText(String.valueOf(gioHang.getSoluong()));
                updateTotalPrice(holder, gioHang);
            }
        });

        // Xử lý nút trừ
        holder.item_giohang_tru.setOnClickListener(v -> {
            int sl = gioHang.getSoluong();
            if (sl > 1) {
                gioHang.setSoluong(sl - 1);
                holder.item_giohang_soluong.setText(String.valueOf(gioHang.getSoluong()));
                updateTotalPrice(holder, gioHang);
            }
        });
    }

    private void updateTotalPrice(MyViewHolder holder, GioHang gioHang) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        long thanhTien = gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format(thanhTien) + " ₫");
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView item_giohang_image, item_giohang_tru, item_giohang_cong;
        TextView item_giohang_tensp, item_giohang_gia, item_giohang_giasp2, item_giohang_soluong;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_tru = itemView.findViewById(R.id.item_giohang_tru);
            item_giohang_cong = itemView.findViewById(R.id.item_giohang_cong);
        }
    }
}
