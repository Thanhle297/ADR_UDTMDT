package com.example.ung_dung_thuong_mai_dien_tu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.ung_dung_thuong_mai_dien_tu.R;
import com.example.ung_dung_thuong_mai_dien_tu.model.GioHang;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoi;
import com.example.ung_dung_thuong_mai_dien_tu.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {

    TextView tvTenSanPham, tvGiaSanPham, tvMoTaChiTiet;
    ImageView imgChiTiet;
    Spinner spinner;
    Button btnThemGioHang;
    Toolbar toolbar;
    SpMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);

        Anhxa();
        ActionToolbar();
        initData();
        initControl();
    }

    private void initControl() {
        btnThemGioHang.setOnClickListener(v -> {
            themGioHang();
            Utils.saveGioHang(getApplicationContext());
            sendBroadcast(new Intent("update_badge"));
            Toast.makeText(ChiTietActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }

    private void themGioHang() {
        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
        boolean exists = false;

        for (GioHang g : Utils.manggiohang) {
            if (g.getIdsp() == sanPhamMoi.getId()) {
                g.setSoluong(g.getSoluong() + soluong);
                g.setGiasp(sanPhamMoi.getGiasp() * g.getSoluong());
                exists = true;
                break;
            }
        }

        if (!exists) {
            GioHang gioHang = new GioHang();
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            gioHang.setSoluong(soluong);
            gioHang.setGiasp(sanPhamMoi.getGiasp() * soluong);
            Utils.manggiohang.add(gioHang);
        }

        updateBadge();
    }

    private void updateBadge() {
        int total = 0;
        for (GioHang g : Utils.manggiohang) total += g.getSoluong();
        badge.setNumber(total);
        badge.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
    }

    private void initData() {
        sanPhamMoi = (SpMoi) getIntent().getSerializableExtra("chitiet");

        tvTenSanPham.setText(sanPhamMoi.getTensp());
        tvMoTaChiTiet.setText(sanPhamMoi.getMota());
        Glide.with(this).load(sanPhamMoi.getHinhanh()).into(imgChiTiet);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvGiaSanPham.setText("Giá: " + decimalFormat.format(sanPhamMoi.getGiasp()) + " VNĐ");

        Integer[] soluong = {1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterSpin = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, soluong);
        spinner.setAdapter(adapterSpin);
    }

    private void Anhxa() {
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = findViewById(R.id.tvGiaSanPham);
        tvMoTaChiTiet = findViewById(R.id.tvMoTaChiTiet);
        imgChiTiet = findViewById(R.id.imgchitiet);
        spinner = findViewById(R.id.spinner);
        btnThemGioHang = findViewById(R.id.btnThemGioHang);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_sl);

        FrameLayout frameLayoutgiohang = findViewById(R.id.framegiohang);
        frameLayoutgiohang.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), GioHangActivity.class));
        });

        updateBadge();
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            String from = getIntent().getStringExtra("from");
            Intent intent;

            if ("dien_thoai".equals(from)) {
                intent = new Intent(this, DienThoaiActivity.class);
            } else if ("laptop".equals(from)) {
                intent = new Intent(this, LaptopActivity.class);
            } else if ("timkiem".equals(from)) {
                intent = new Intent(this, SearchActivity.class);
            } else {
                intent = new Intent(this, MainActivity.class);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
