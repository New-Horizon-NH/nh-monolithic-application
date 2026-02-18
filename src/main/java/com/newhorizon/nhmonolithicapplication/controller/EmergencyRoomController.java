package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.RegisterERDocumentRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterERDocumentResponseBean;
import org.springframework.http.ResponseEntity;

public interface EmergencyRoomController {
    /**
     * Register any type of document from the ER
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RegisterERDocumentResponseBean}
     */
    ResponseEntity<? extends BaseResponse> registerDocument(RegisterERDocumentRequestBean requestBean);
}
