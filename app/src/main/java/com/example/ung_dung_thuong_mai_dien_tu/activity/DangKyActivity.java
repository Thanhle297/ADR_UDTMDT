package com.example.ung_dung_thuong_mai_dien_tu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ung_dung_thuong_mai_dien_tu.R;

public class DangKyActivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtPassword, edtConfirmPassword, edtAddress;
    private Button btnRegister;
    private TextView txtGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dang_ky);

        // Ánh xạ view (đúng theo id mà m đã dùng trong XML)
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword); // nếu chưa có trong XML thì sửa lại cho đúng
        edtAddress = findViewById(R.id.edtAddress); // thêm địa chỉ nếu m muốn

        btnRegister = findViewById(R.id.btnRegister);
        txtGoToLogin = findViewById(R.id.txtGoToLogin);

        // Bắt sự kiện nút Đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        // Chuyển sang màn đăng nhập
        txtGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // quay lại màn trước (Đăng nhập)
            }
        });
    }

    private void handleRegister() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        String confirm = edtConfirmPassword.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        // Kiểm tra rỗng
        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(pass) ||
                TextUtils.isEmpty(confirm) ||
                TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra số điện thoại đơn giản
        if (phone.length() < 9 || phone.length() > 11) {
            Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu
        if (pass.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirm)) {
            Toast.makeText(this, "Mật khẩu nhập lại không trùng khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Sau này m gọi API / lưu database
        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

        // Quay lại đăng nhập
        finish();
    }
}
