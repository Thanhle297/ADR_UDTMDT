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
import com.example.ung_dung_thuong_mai_dien_tu.model.Item;

import java.util.List;

public class ChiTietAdapter extends  RecyclerView.Adapter<ChiTietAdapter.MyViewHolder> {
    Context context;
    List<Item> itemList;

    public ChiTietAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.tenspchitiet.setText(item.getTensp());
        holder.soluongchitiet.setText("Số lượng: " + item.getSoluong());

        // Format số
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("###,###,###");

        // Giá
        holder.giachitiet.setText("Giá: " + decimalFormat.format(item.getGia()) + " đ");

        // Thành tiền = giá * số lượng
        int thanhtien = item.getGia() * item.getSoluong();
        holder.thanhtienchitiet.setText("Thành tiền: " + decimalFormat.format(thanhtien) + " đ");

        Glide.with(context).load(item.getHinhanh()).into(holder.imgchitiet);
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        ImageView imgchitiet;
        TextView tenspchitiet, soluongchitiet, giachitiet,thanhtienchitiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgchitiet = itemView.findViewById(R.id.item_imgchitiet);
            tenspchitiet = itemView.findViewById(R.id.item_tenspchitiet);
            soluongchitiet = itemView.findViewById(R.id.item_soluongchitiet);
            giachitiet = itemView.findViewById(R.id.item_giachitiet);
            thanhtienchitiet = itemView.findViewById(R.id.item_thanhtienchitiet);
        }
    }
}
