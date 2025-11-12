package com.example.ung_dung_thuong_mai_dien_tu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ung_dung_thuong_mai_dien_tu.R;
import com.example.ung_dung_thuong_mai_dien_tu.Retrofit.ApiBanHang;
import com.example.ung_dung_thuong_mai_dien_tu.Retrofit.RetrofitClient;
import com.example.ung_dung_thuong_mai_dien_tu.adapter.ManHinhAdapter;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoi;
import com.example.ung_dung_thuong_mai_dien_tu.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ManHinhActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    ManHinhAdapter adaptermanhinh;
    List<SpMoi> sanPhamMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai",4);
        Anhxa();
        ActionToolbar();
        Getdata(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoiList.size() - 1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.add(null);
                adaptermanhinh.notifyItemInserted(sanPhamMoiList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.remove(sanPhamMoiList.size() - 1);
                adaptermanhinh.notifyItemRemoved(sanPhamMoiList.size());
                page  = page +1;
                Getdata(page);
                adaptermanhinh.notifyDataSetChanged();
//                isLoading = false;
            }
        },2000);
    }

    private void Getdata(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page,loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        SpMoiModel -> {
                            if(SpMoiModel.isSuccess()){
                                if(adaptermanhinh == null){
                                    sanPhamMoiList = SpMoiModel.getResult();
                                    adaptermanhinh = new ManHinhAdapter(getApplicationContext(),sanPhamMoiList);
                                    recyclerView.setAdapter(adaptermanhinh);
                                }else {
                                    int vitri = sanPhamMoiList.size() - 1;
                                    int soluongadd = SpMoiModel.getResult().size();
                                    for(int i = 0; i < soluongadd; i++){
                                        sanPhamMoiList.add(SpMoiModel.getResult().get(i));
                                    }
                                    adaptermanhinh.notifyItemRangeInserted(vitri,soluongadd);
                                }
                                isLoading = false;
                                Log.d("isloading", String.valueOf(isLoading));
                            }else {
                                Toast.makeText(getApplicationContext(), "Đã hết sản phẩm", Toast.LENGTH_SHORT).show();
                                isLoading = true;
                                Log.d("Isloading: ", String.valueOf(isLoading));
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Server not conected" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            isLoading=false;
                        }
                ));
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Xử lý sự kiện khi ấn nút mũi tên
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ManHinhActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });


    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview_manhinh);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}