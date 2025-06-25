package com.irent.donation_backend.model;

import lombok.Data;

@Data
public class NGOEnvListItem {
    private String id;
    private String record_id;
    private NGOEnvFields fields;
}
