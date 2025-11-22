package com.example.ung_dung_thuong_mai_dien_tu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ung_dung_thuong_mai_dien_tu.R;

public class DangNhapActivity extends AppCompatActivity {

    private EditText edtPhone;
    private Button btnLogin;
    private TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dang_nhap);

        // Ánh xạ view
        edtPhone = findViewById(R.id.edtPhone);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        // Bắt sự kiện nút đăng nhập
        btnLogin.setOnClickListener(view -> {
            String phone = edtPhone.getText().toString().trim();

            if (phone.isEmpty()) {
                Toast.makeText(this,
                        "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.length() < 9 || phone.length() > 11) {
                Toast.makeText(this,
                        "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Thông báo
            Toast.makeText(this,
                    "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            // Chuyển sang MainActivity sau khi đăng nhập
            Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
            startActivity(intent);

            // Nếu không muốn quay lại màn đăng nhập khi bấm Back
            finish();
        });

        // Sự kiện: Chưa có tài khoản → đăng ký
        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
            startActivity(intent);
        });
    }
}
