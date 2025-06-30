package com.irent.donation_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * ClassName:ComBodyDTO
 * Package:com.iisigroup.ocapi.dto
 * Description:
 *
 * @Date:2024/3/19 上午 09:45
 * @Author:2208021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqBodyDTO<T> {

    private Map<String, Object> header;

    @Valid
    @NotNull(message = "RequestBodySkeleton is required")
    private RequestBodySkeleton<T> body;
}
