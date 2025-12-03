package com.example.ung_dung_thuong_mai_dien_tu.Retrofit;

import io.reactivex.rxjava3.core.Observable;

import com.example.ung_dung_thuong_mai_dien_tu.model.DonHangModel;
import com.example.ung_dung_thuong_mai_dien_tu.model.LoaispModel;
import com.example.ung_dung_thuong_mai_dien_tu.model.PingModel;
import com.example.ung_dung_thuong_mai_dien_tu.model.SpMoiModel;
import com.example.ung_dung_thuong_mai_dien_tu.model.UserModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    //GET DATA
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
    //REGESTER
    @FormUrlEncoded
    @POST("dangky.php")
    Observable<UserModel> dangky(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("phone") String phone
    );

    //LOGIN
    @FormUrlEncoded
    @POST("dangnhap.php")
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("pass") String pass
    );
    //RESET PASSWORD
    @FormUrlEncoded
    @POST("reset.php")
    Observable<UserModel> resetPass(
            @Field("email") String email

    );
    // create Oder
    @FormUrlEncoded
    @POST("donhang.php")
    Observable<UserModel> createOder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int iduser,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet

    );
    // xem đơn hàng
    @FormUrlEncoded
    @POST("xemdonhang.php")
    Observable<DonHangModel> xemDonHang(
            @Field("iduser") int id

    );

    // tìm kiếm
    @FormUrlEncoded
    @POST("timkiem.php")
    Observable<SpMoiModel> search(
            @Field("search") String search

    );

    //check ping
    @GET("ping.php")
    Observable<PingModel> pingServer();

}
