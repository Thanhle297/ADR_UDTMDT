package com.example.ung_dung_thuong_mai_dien_tu.activity;

import android.content.Context;
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
import com.example.ung_dung_thuong_mai_dien_tu.model.Loaisp;
import com.example.ung_dung_thuong_mai_dien_tu.model.LoaispModel;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoi;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoiModel;
import com.example.ung_dung_thuong_mai_dien_tu.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        // anh xa
        Anhxa();
        ActionBar();

        if(isConnected(this)){
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }else{
            Toast.makeText(getApplicationContext(), "Không có kết nối Internet, vui lòng kết nối!", Toast.LENGTH_LONG).show();
        }

        if (Utils.manggiohang == null) {
            Utils.manggiohang = new ArrayList<>();
        }
    }

    private void getEventClick() {
        listviewHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        drawer_layout.closeDrawer(GravityCompat.START, false);
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        drawer_layout.closeDrawer(GravityCompat.START, false);
                        Intent dienthoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        drawer_layout.closeDrawer(GravityCompat.START, false);
                        Intent laptop = new Intent(getApplicationContext(), LaptopActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 3:
                        drawer_layout.closeDrawer(GravityCompat.START, false);
                        Intent tablet = new Intent(getApplicationContext(), TabletActivity.class);
                        tablet.putExtra("loai",3);
                        startActivity(tablet);
                        break;
                    case 4:
                        drawer_layout.closeDrawer(GravityCompat.START, false);
                        Intent pc = new Intent(getApplicationContext(), PCActivity.class);
                        pc.putExtra("loai",4);
                        startActivity(pc);
                        break;
                    case 5:
                        drawer_layout.closeDrawer(GravityCompat.START, false);
                        Intent manhinh = new Intent(getApplicationContext(), ManHinhActivity.class);
                        startActivity(manhinh);
                        break;

                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        SpMoiModel -> {
                            if(SpMoiModel.isSuccess()) {
                                mangSpMoi = SpMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                recyclerViewHome.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Server not conected" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }

                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaisp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        LoaispModel -> {
                            if(LoaispModel.isSuccess()) {
                                mangloaisp = LoaispModel.getResult();
                                loaispAdapter = new LoaispAdapter(getApplicationContext(), mangloaisp);
                                listviewHome.setAdapter(loaispAdapter);
                            }
                        }
                ));
    }

    private void ActionViewFlipper() {
        List<String> Mangquangcao = new ArrayList<>();
        Mangquangcao.add("http://103.90.224.183/uploads/banner/banner1.jpg");
        Mangquangcao.add("http://103.90.224.183/uploads/banner/banner2.jpg");
        Mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for (int i = 0; i < Mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(Mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewSlideshow.addView(imageView);
        }
        viewSlideshow.setFlipInterval(3000);
        viewSlideshow.setAutoStart(true);
        Animation slie_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        Animation slie_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);
        viewSlideshow.setInAnimation(slie_in);
        viewSlideshow.setOutAnimation(slie_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbarHome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarHome.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbarHome.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        toolbarHome = findViewById(R.id.toolbarHome);
        viewSlideshow = findViewById(R.id.viewSlideshow);
        recyclerViewHome = findViewById(R.id.recyclerViewHome);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);
        textviewHome = findViewById(R.id.textviewHome);
        navigationview = findViewById(R.id.navigationview);
        listviewHome = findViewById(R.id.listviewHome);
        drawer_layout = findViewById(R.id.drawer_layout);
        badge =findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        // khởi tạo mang
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        // khởi tạo adapter
        if(Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }else{
            int totalItem = 0;
            for (int i =0; i<Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for (int i =0; i<Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private boolean isConnected (Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (drawer_layout != null && drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            // Đóng Drawer sau 100ms để tránh giật khung hình
//            drawer_layout.postDelayed(() ->
//                    drawer_layout.closeDrawer(GravityCompat.START, false), 100);
//        }
//    }


}