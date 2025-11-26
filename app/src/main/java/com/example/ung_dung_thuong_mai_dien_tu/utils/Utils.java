package com.example.ung_dung_thuong_mai_dien_tu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ung_dung_thuong_mai_dien_tu.model.GioHang;
import com.example.ung_dung_thuong_mai_dien_tu.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Utils {
//    public  static final String BASE_URL="http://103.90.224.183/banhang/";
    public static final String BASE_URL="http://192.168.1.49:8080/banhang/";
//    public static final String BASE_URL="http://10.15.67.18:8080/banhang/";
//    public static final String BASE_URL="http://192.168.1.51:8080/banhang/";
    public static List<GioHang> manggiohang = new ArrayList<>();

    // Lưu giỏ hàng vào SharedPreferences
    public static void saveGioHang(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GIOHANG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(manggiohang);
        editor.putString("data", json);
        editor.apply();
    }

    // Tải lại giỏ hàng khi mở ứng dụng
    public static void loadGioHang(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GIOHANG", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("data", null);
        Type type = new TypeToken<ArrayList<GioHang>>() {}.getType();
        List<GioHang> savedList = gson.fromJson(json, type);

        manggiohang.clear();
        if (savedList != null) {
            manggiohang.addAll(savedList);
        }
    }

    // gửi tk mk lại đăng nhập sau khi đăng ký
    public static User user_current = new User();
}
