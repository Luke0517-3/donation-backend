package com.irent.donation_backend.model.lark;

import lombok.Data;

@Data
public class LarkResponse<T> {
    private int code;
    private String msg;
    private DataWrapper<T> data;
}
