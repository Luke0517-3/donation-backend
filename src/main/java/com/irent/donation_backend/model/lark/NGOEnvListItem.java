package com.irent.donation_backend.model.lark;

import lombok.Data;

@Data
public class NGOEnvListItem<T> {
    private String id;
    private String record_id;
    private T fields;
}
