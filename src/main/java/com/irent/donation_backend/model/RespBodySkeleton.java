package com.irent.donation_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName:AAA
 * Package:com.iisigroup.ocapi.dto
 * Description:
 *
 * @Date:2024/3/19 上午 09:50
 * @Author:2208021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespBodySkeleton<T> {
    private Integer responseCode;
    private String responseMsg;
    private String type;
    private T data;
}
