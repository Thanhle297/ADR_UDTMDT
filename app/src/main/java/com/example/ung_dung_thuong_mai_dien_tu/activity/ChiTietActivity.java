package com.example.ung_dung_thuong_mai_dien_tu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ung_dung_thuong_mai_dien_tu.R;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoi;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tvTenSanPham, tvGiaSanPham, tvMoTaChiTiet;
    ImageView imgChiTiet;
    Spinner spinner;
    Button btnThemGioHang;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        Anhxa();
        ActionToolbar();
        initData();
    }

    private void initData() {
        SpMoi sanPhamMoi = (SpMoi) getIntent().getSerializableExtra("chitiet");
        tvTenSanPham.setText(sanPhamMoi.getTensp());
        tvMoTaChiTiet.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imgChiTiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvGiaSanPham.setText("Giá: " +decimalFormat.format(sanPhamMoi.getGiasp()) + " VNĐ");
        Integer[] so = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapterspin);
    }

    private void Anhxa() {
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = findViewById(R.id.tvGiaSanPham);
        tvMoTaChiTiet = findViewById(R.id.tvMoTaChiTiet);
        imgChiTiet = findViewById(R.id.imgchitiet);
        spinner = findViewById(R.id.spinner);
        btnThemGioHang = findViewById(R.id.btnThemGioHang);
        toolbar = findViewById(R.id.toolbar);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            String from = getIntent().getStringExtra("from");
            Intent intent;

            if ("dien_thoai".equals(from)) {
                intent = new Intent(ChiTietActivity.this, DienThoaiActivity.class);
            } else if ("laptop".equals(from)) {
                intent = new Intent(ChiTietActivity.this, LaptopActivity.class);
            } else {
                intent = new Intent(ChiTietActivity.this, MainActivity.class);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}