package com.irent.donation_backend.model.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.irent.donation_backend.common.Constants;
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
public class RespBodyDTO<T> {
    private Integer responseCode;
    private String responseMsg;
    private String type;
    private T data;

    public static <T> RespBodyDTO<T> getRespBodyDTO(String type, T data) {
        return RespBodyDTO.<T>builder()
                .type(type)
                .data(data)
                .responseCode(Constants.SUCCESS_CODE)
                .responseMsg(Constants.SUCCESS_MSG).build();
    }

    public static RespBodyDTO getErrRespBodyDTO(String type, String errMsg) {
        return RespBodyDTO.builder()
                .type(type)
                .responseCode(Constants.FAIL_CODE)
                .responseMsg(errMsg).build();
    }
}
