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
import com.example.ung_dung_thuong_mai_dien_tu.adapter.MayTinhBangAdapter;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoi;
import com.example.ung_dung_thuong_mai_dien_tu.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MayTinhBangActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    MayTinhBangAdapter adapter;
    List<SpMoi> spMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_may_tinh_bang);

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai", 3);
        Anhxa();
        ActionToolbar();
        getData(page);
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
                if (!isLoading) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == spMoiList.size() - 1) {
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
                // Thêm null để hiển thị progress bar
                spMoiList.add(null);
                adapter.notifyItemInserted(spMoiList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Xóa progress bar
                spMoiList.remove(spMoiList.size() - 1);
                adapter.notifyItemRemoved(spMoiList.size());
                page = page + 1;
                getData(page);
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        spMoiModel -> {
                            if (spMoiModel.isSuccess()) {
                                if (adapter == null) {
                                    spMoiList = spMoiModel.getResult();
                                    adapter = new MayTinhBangAdapter(getApplicationContext(), spMoiList);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    int vitri = spMoiList.size() - 1;
                                    int soluongAdd = spMoiModel.getResult().size();
                                    for (int i = 0; i < soluongAdd; i++) {
                                        spMoiList.add(spMoiModel.getResult().get(i));
                                    }
                                    adapter.notifyItemRangeInserted(vitri, soluongAdd);
                                }
                                isLoading = false;
                                Log.d("MayTinhBang", "isLoading: " + isLoading);
                            } else {
                                Toast.makeText(getApplicationContext(), "Đã hết sản phẩm", Toast.LENGTH_SHORT).show();
                                isLoading = true;
                                Log.d("MayTinhBang", "Hết dữ liệu - isLoading: " + isLoading);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            isLoading = false;
                            Log.e("MayTinhBang", "Error: " + throwable.getMessage());
                        }
                ));
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Xử lý sự kiện khi ấn nút mũi tên
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(MayTinhBangActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbar_maytinhbang);
        recyclerView = findViewById(R.id.recyclerview_maytinhbang);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        spMoiList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}

