package com.example.ung_dung_thuong_mai_dien_tu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ung_dung_thuong_mai_dien_tu.Interface.ItemClickListener;
import com.example.ung_dung_thuong_mai_dien_tu.R;
import com.example.ung_dung_thuong_mai_dien_tu.activity.ChiTietActivity;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoi;

import java.text.DecimalFormat;
import java.util.List;

public class LaptopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SpMoi> array;
    private  static final int VIEW_TYPE_DATA = 0;
    private static final  int VIEW_TYPE_LOADING = 1;

    public LaptopAdapter(Context context, List<SpMoi> array){
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laptop,parent,false);
            return  new MyViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            LaptopAdapter.MyViewHolder myViewHolder = (LaptopAdapter.MyViewHolder) holder;
            SpMoi sanPhamMoi = array.get(position);
            myViewHolder.tensp.setText(sanPhamMoi.getTensp());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.giasp.setText("Giá: " +decimalFormat.format(sanPhamMoi.getGiasp()) + " VNĐ");
            myViewHolder.motasp.setText(sanPhamMoi.getMota());
            Glide.with(context).load(sanPhamMoi.getHinhanh()).into(myViewHolder.hinhanh);
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet", sanPhamMoi);
                    intent.putExtra("from", "laptop");
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp, giasp, motasp, idsp;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.itemlt_tensp);
            giasp = itemView.findViewById(R.id.itemlt_giasp);
            motasp = itemView.findViewById(R.id.itemlt_motasp);
            hinhanh = itemView.findViewById(R.id.itemlt_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
