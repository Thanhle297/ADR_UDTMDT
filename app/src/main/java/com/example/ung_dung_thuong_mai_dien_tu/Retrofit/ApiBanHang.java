package com.example.ung_dung_thuong_mai_dien_tu.Retrofit;

import io.reactivex.rxjava3.core.Observable;

import com.example.ung_dung_thuong_mai_dien_tu.model.LoaispModel;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoiModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaispModel> getLoaisp();

    @GET("getspmoi.php")
    Observable<SpMoiModel> getSpMoi();

    @FormUrlEncoded
    @POST("chitiet.php")
    Observable<SpMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );
}
