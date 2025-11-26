package com.example.ung_dung_thuong_mai_dien_tu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ung_dung_thuong_mai_dien_tu.R;
import com.example.ung_dung_thuong_mai_dien_tu.adapter.GioHangAdapter;
import com.example.ung_dung_thuong_mai_dien_tu.model.EventBus.TinhTongEvent;
import com.example.ung_dung_thuong_mai_dien_tu.model.GioHang;
import com.example.ung_dung_thuong_mai_dien_tu.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView txtTongTien, txtGioHangTrong;
    Button btnMuaHang;
    GioHangAdapter adapter;
    long tongtien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        AnhXa();
        ActionToolbar();

        // Load giỏ hàng từ SharedPreferences
        Utils.loadGioHang(getApplicationContext());

        // Nếu giỏ hàng rỗng -> hiện thông báo
        if (Utils.manggiohang == null || Utils.manggiohang.isEmpty()) {
            txtGioHangTrong.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtGioHangTrong.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            setupRecyclerView();
        }

        TinhTongTien();
        initControl();

    }

    private void initControl() {
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                intent.putExtra("tongtien", tongtien);
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GioHangAdapter(getApplicationContext(), Utils.manggiohang);
        recyclerView.setAdapter(adapter);
    }

    private void TinhTongTien() {
        tongtien = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            tongtien  = tongtien + (Utils.manggiohang.get(i).getGiasp()*Utils.manggiohang.get(i).getSoluong());
        }
        DecimalFormat df = new DecimalFormat("###,###,###");
        txtTongTien.setText(df.format(tongtien) + " VNĐ");
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerviewgiohang);
        txtTongTien = findViewById(R.id.txttongtien);
        txtGioHangTrong = findViewById(R.id.txtgiohangtrong);
        btnMuaHang = findViewById(R.id.btn_muahang);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Lưu lại khi thoát
        Utils.saveGioHang(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public  void EventTinhTong(TinhTongEvent event){
        if(event != null){
            TinhTongTien();
        }
    }
}
