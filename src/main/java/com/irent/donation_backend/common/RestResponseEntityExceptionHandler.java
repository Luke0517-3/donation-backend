package com.irent.donation_backend.common;

import com.irent.donation_backend.model.RespBodyDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.irent.donation_backend.model.RespBodyDTO.getErrRespBodyDTO;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public @ResponseBody RespBodyDTO handleExceptionForInternalServerError(final Exception ex) {
        log.error("Unexpected Exception:", ex);
        return handleThrowableForInternalServerError(ex);
    }

    public RespBodyDTO handleThrowableForInternalServerError(final Exception ex) {

        String errMsg = ex.getMessage();

        if (log.isDebugEnabled()) {
            log.error(ExceptionUtils.getStackTrace(ex));
        }

        return getErrRespBodyDTO(ex.getClass().getSimpleName(), errMsg);
    }

}
