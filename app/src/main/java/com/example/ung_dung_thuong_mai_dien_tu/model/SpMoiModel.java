package com.example.ung_dung_thuong_mai_dien_tu.model;

import java.util.List;

public class SpMoiModel {
    boolean success;
    String message;
    List<SpMoi> result;

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

    public List<SpMoi> getResult() {
        return result;
    }

    public void setResult(List<SpMoi> result) {
        this.result = result;
    }
}
