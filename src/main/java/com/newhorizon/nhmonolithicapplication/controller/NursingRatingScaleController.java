package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.RegisterScaleRecordRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveScaleListRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterScaleRecordResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveScaleListResponseBean;
import org.springframework.http.ResponseEntity;

public interface NursingRatingScaleController {
    /**
     * Register a rating scale with value/values
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RegisterScaleRecordResponseBean}
     */
    ResponseEntity<?> registerScale(RegisterScaleRecordRequestBean requestBean);

    /**
     * Retrieve all scales contained in medical chart
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RetrieveScaleListResponseBean}
     */
    ResponseEntity<?> retrieveScale(RetrieveScaleListRequestBean requestBean);

}
