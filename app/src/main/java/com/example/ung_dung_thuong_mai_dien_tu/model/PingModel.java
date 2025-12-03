package com.example.ung_dung_thuong_mai_dien_tu.model;

/**
 * Model dùng để nhận phản hồi từ API ping.php
 *
 * JSON mẫu từ server:
 * {
 *     "success": true,
 *     "message": "Server OK"
 * }
 */
public class PingModel {

    private boolean success;   // true = server hoạt động
    private String message;    // thông điệp phản hồi từ server

    public PingModel() {
        // Constructor mặc định
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message != null ? message : "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PingModel{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
