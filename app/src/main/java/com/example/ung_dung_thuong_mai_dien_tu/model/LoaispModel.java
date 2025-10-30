package com.example.ung_dung_thuong_mai_dien_tu.model;

import java.util.List;

public class LoaispModel {
    boolean success;
    String message;
    List<Loaisp> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Loaisp> getResult() {
        return result;
    }

    public void setResult(List<Loaisp> result) {
        this.result = result;
    }
}
