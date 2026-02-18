package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

/**
 * Base Controller
 */
public abstract class BaseController {

    public <T extends BaseResponse> ResponseEntity<T> handleResponse(T responseService) {
        return new ResponseEntity<>(responseService, this.httpStatusFromCode(responseService.getResponseCode()));
    }

    /**
     * Method to handle unimplemented method and return custom response entity
     *
     * @return {@link ResponseEntity} with unimplemented responseCode and message
     */
    public ResponseEntity<?> handleNotImplemented() {
        return new ResponseEntity<>(BaseResponse.builder()
                .status(BaseResponse.Status.WARNING)
                .responseMessage(ResponseCodesEnum.NOT_IMPLEMENTED.getDescription())
                .responseCode(ResponseCodesEnum.NOT_IMPLEMENTED.getErrorCode())
                .build(), ResponseCodesEnum.NOT_IMPLEMENTED.getHttpErrorCode());
    }

    /**
     * Method to retrieve status code from nh response code.
     * Method check into {@link ResponseCodesEnum} enum with contains specific code
     *
     * @param code integer value for response code
     * @return specific http status if response code exists or else {@code UNPROCESSABLE_ENTITY}
     */
    protected HttpStatus httpStatusFromCode(Integer code) {
        Optional<HttpStatus> httpStatusOptional = Arrays.stream(ResponseCodesEnum.values())
                .filter(e -> e.getErrorCode()
                        .equals(code))
                .findFirst()
                .map(ResponseCodesEnum::getHttpErrorCode);
        return httpStatusOptional.orElse(HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
