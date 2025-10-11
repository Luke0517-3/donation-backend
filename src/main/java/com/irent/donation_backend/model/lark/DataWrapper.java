package com.irent.donation_backend.model.lark;

import lombok.Data;

import java.util.List;

@Data
public class DataWrapper<T> {
    private boolean has_more;
    private List<T> items;
    private int total;
}
