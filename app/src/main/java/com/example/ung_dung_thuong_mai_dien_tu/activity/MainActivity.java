package com.example.ung_dung_thuong_mai_dien_tu.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ung_dung_thuong_mai_dien_tu.R;
import com.example.ung_dung_thuong_mai_dien_tu.Retrofit.ApiBanHang;
import com.example.ung_dung_thuong_mai_dien_tu.Retrofit.RetrofitClient;
import com.example.ung_dung_thuong_mai_dien_tu.adapter.LoaispAdapter;
import com.example.ung_dung_thuong_mai_dien_tu.adapter.SanPhamMoiAdapter;
import com.example.ung_dung_thuong_mai_dien_tu.model.GioHang;
import com.example.ung_dung_thuong_mai_dien_tu.model.Loaisp;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoi;
import com.example.ung_dung_thuong_mai_dien_tu.model.User;
import com.example.ung_dung_thuong_mai_dien_tu.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarHome;
    ViewFlipper viewSlideshow;
    RecyclerView recyclerViewHome;
    TextView textviewHome;
    NavigationView navigationview;
    ListView listviewHome;
    DrawerLayout drawer_layout;
    LoaispAdapter loaispAdapter;
    List<Loaisp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SpMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        Paper.init(this);
        if(Paper.book().read("user") != null) {
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }

        Anhxa();
        ActionBar();

        if (isConnected(this)) {
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        } else {
            Toast.makeText(getApplicationContext(), "Không có kết nối Internet, vui lòng kết nối!", Toast.LENGTH_LONG).show();
        }

        // ===== KHỞI TẠO GIỎ HÀNG =====
        if (Utils.manggiohang == null) {
            Utils.manggiohang = new ArrayList<>();
        }
        Utils.loadGioHang(this); // ✅ đọc lại dữ liệu đã lưu
        updateBadge(); // ✅ hiển thị số lượng
    }

    private void getEventClick() {
        listviewHome.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0:
                    drawer_layout.closeDrawer(GravityCompat.START, false);
                    if (!(this instanceof MainActivity)) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    break;
                case 1:
                    drawer_layout.closeDrawer(GravityCompat.START, false);
                    Intent dienthoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                    dienthoai.putExtra("loai", 1);
                    startActivity(dienthoai);
                    break;
                case 2:
                    drawer_layout.closeDrawer(GravityCompat.START, false);
                    Intent laptop = new Intent(getApplicationContext(), LaptopActivity.class);
                    laptop.putExtra("loai", 2);
                    startActivity(laptop);
                    break;
                case 3:
                    drawer_layout.closeDrawer(GravityCompat.START, false);
                    Intent tablet = new Intent(getApplicationContext(), TabletActivity.class);
                    tablet.putExtra("loai", 3);
                    startActivity(tablet);
                    break;
                case 4:
                    drawer_layout.closeDrawer(GravityCompat.START, false);
                    Intent pc = new Intent(getApplicationContext(), PCActivity.class);
                    pc.putExtra("loai", 4);
                    startActivity(pc);
                    break;
                case 5:
                    drawer_layout.closeDrawer(GravityCompat.START, false);
                    Intent manhinh = new Intent(getApplicationContext(), ManHinhActivity.class);
                    manhinh.putExtra("loai", 5);
                    startActivity(manhinh);
                    break;
                case 6:
                    drawer_layout.closeDrawer(GravityCompat.START, false);
                    Intent thongtin = new Intent(getApplicationContext(),XemDonActivity.class);
                    startActivity(thongtin);
                    break;
                case 9:
                    // đăng xuất
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Xác nhận");
                    builder.setMessage("Bạn có chắc chắn muốn đăng xuất không?");

                    // Nút Đồng ý
                    builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // xóa key user
                            Paper.book().delete("user");
                            Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                            startActivity(dangnhap);
                            finish();
                        }
                    });

                    // Nút Hủy
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();

            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        SpMoiModel -> {
                            if (SpMoiModel.isSuccess()) {
                                mangSpMoi = SpMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                recyclerViewHome.setAdapter(spAdapter);
                            }
                        },
                        throwable -> Toast.makeText(getApplicationContext(),
                                "Server not connected: " + throwable.getMessage(),
                                Toast.LENGTH_LONG).show()
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaisp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(LoaispModel -> {
                    if (LoaispModel.isSuccess()) {
                        mangloaisp = LoaispModel.getResult();
//                        mangloaisp.add(new Loaisp("Đăng xuất",""));
                        loaispAdapter = new LoaispAdapter(getApplicationContext(), mangloaisp);
                        listviewHome.setAdapter(loaispAdapter);
                    }
                }));
    }

    private void ActionViewFlipper() {
        List<String> Mangquangcao = new ArrayList<>();
        Mangquangcao.add("http://103.90.224.183/uploads/banner/banner1.jpg");
        Mangquangcao.add("http://103.90.224.183/uploads/banner/banner2.jpg");
        Mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");

        for (String url : Mangquangcao) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(url).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewSlideshow.addView(imageView);
        }

        viewSlideshow.setFlipInterval(3000);
        viewSlideshow.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);
        viewSlideshow.setInAnimation(slide_in);
        viewSlideshow.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbarHome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarHome.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbarHome.setNavigationOnClickListener(v -> drawer_layout.openDrawer(GravityCompat.START));
    }

    private void Anhxa() {
        toolbarHome = findViewById(R.id.toolbarHome);
        viewSlideshow = findViewById(R.id.viewSlideshow);
        recyclerViewHome = findViewById(R.id.recyclerViewHome);
        recyclerViewHome.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewHome.setHasFixedSize(true);
        textviewHome = findViewById(R.id.textviewHome);
        navigationview = findViewById(R.id.navigationview);
        listviewHome = findViewById(R.id.listviewHome);
        drawer_layout = findViewById(R.id.drawer_layout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();

        frameLayout.setOnClickListener(v -> {
            Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
            startActivity(giohang);
        });

        imgsearch = findViewById(R.id.img_search);
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.loadGioHang(this); // ✅ tải lại khi quay về
        updateBadge();
    }

    private void updateBadge() {
        int totalItem = 0;
        if (Utils.manggiohang != null) {
            for (GioHang item : Utils.manggiohang) {
                totalItem += item.getSoluong();
            }
        }
        badge.setNumber(totalItem); // ✅ đúng thư viện NotificationBadge
        badge.setVisibility(View.VISIBLE);
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
