package com.example.ung_dung_thuong_mai_dien_tu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ung_dung_thuong_mai_dien_tu.R;
import com.example.ung_dung_thuong_mai_dien_tu.Retrofit.ApiBanHang;
import com.example.ung_dung_thuong_mai_dien_tu.Retrofit.RetrofitClient;
import com.example.ung_dung_thuong_mai_dien_tu.model.PingModel;
import com.example.ung_dung_thuong_mai_dien_tu.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiBanHang apiBanHang;

    private TextView txtStatus;
    private Button btnRetry;
    private LottieAnimationView animationView;

    // Typing effect
    private final String typingText = "Đang kiểm tra kết nối server...";
    private int typingIndex = 0;
    private Handler typingHandler = new Handler();

    // Retry logic
    private static final int RETRY_DELAY = 2000;
    private static final int MAX_RETRY = 5;
    private int retryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Paper.init(this);

        txtStatus = findViewById(R.id.txt_status);
        btnRetry = findViewById(R.id.btn_retry);
        animationView = findViewById(R.id.animation_view);

        animationView.playAnimation();

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        btnRetry.setOnClickListener(v -> {
            retryCount = 0;
            btnRetry.setVisibility(Button.GONE);
            startTypingEffect();
            checkServerConnection();
        });

        startTypingEffect();
        checkServerConnection();
    }

    /**
     * Hiệu ứng typing + blinking cho chữ "Đang kiểm tra kết nối server..."
     */
    private void startTypingEffect() {
        typingIndex = 0;
        txtStatus.setText("");

        typingHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (typingIndex < typingText.length()) {
                    txtStatus.append(String.valueOf(typingText.charAt(typingIndex)));
                    typingIndex++;
                    typingHandler.postDelayed(this, 40);
                } else {
                    startBlinking();
                }
            }
        }, 40);
    }

    /**
     * Hiệu ứng nhấp nháy
     */
    private void startBlinking() {
        txtStatus.animate()
                .alpha(0.3f)
                .setDuration(500)
                .withEndAction(() -> txtStatus.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .withEndAction(this::startBlinking)
                        .start())
                .start();
    }

    /**
     * Hiệu ứng lắc khi retry
     */
    private void shakeText() {
        txtStatus.animate()
                .translationX(10)
                .setDuration(50)
                .withEndAction(() -> txtStatus.animate()
                        .translationX(-10)
                        .setDuration(50)
                        .withEndAction(() -> txtStatus.animate()
                                .translationX(0)
                                .setDuration(50)
                                .start())
                        .start())
                .start();
    }

    /**
     * Hiệu ứng fade-in khi kết nối thành công
     */
    private void fadeInText(String text) {
        txtStatus.setAlpha(0f);
        txtStatus.setText(text);
        txtStatus.animate()
                .alpha(1f)
                .setDuration(500)
                .start();
    }

    private void checkServerConnection() {
        compositeDisposable.add(
                apiBanHang.pingServer()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handlePingSuccess,
                                e -> handleRetry()
                        )
        );
    }

    private void handlePingSuccess(PingModel model) {
        if (model.isSuccess()) {

            fadeInText("Kết nối thành công!");

            new Handler(Looper.getMainLooper()).postDelayed(
                    this::checkLoginStatus,
                    700
            );
        } else {
            handleRetry();
        }
    }

    private void handleRetry() {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            txtStatus.setText("Thử lại (" + retryCount + "/" + MAX_RETRY + ")...");
            shakeText();

            new Handler(Looper.getMainLooper()).postDelayed(
                    this::checkServerConnection,
                    RETRY_DELAY
            );
        } else {
            txtStatus.setText("Không thể kết nối đến server.");
            btnRetry.setVisibility(Button.VISIBLE);
        }
    }

    private void checkLoginStatus() {
        Object user = Paper.book().read("user");

        // toán tử 3 ngôi
        // nếu user != null thì chuyển sang MainActivity, ngược lại chuyển sang DangNhapActivity
        Intent intent = (user == null)
                ? new Intent(this, DangNhapActivity.class)
                : new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
