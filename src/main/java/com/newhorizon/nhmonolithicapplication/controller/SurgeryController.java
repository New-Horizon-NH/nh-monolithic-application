package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateSurgeryRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSurgeryTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RescheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.ScheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnscheduleSurgeryRequestBean;
import org.springframework.http.ResponseEntity;

public interface SurgeryController {
    /**
     * Create surgery room
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.CreateSurgeryRoomResponseBean}
     */
    ResponseEntity<?> createSurgeryRoom(CreateSurgeryRoomRequestBean requestBean);

    /**
     * CreateSurgery room type
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.CreateSurgeryTypeResponseBean}
     */
    ResponseEntity<?> createSurgeryType(CreateSurgeryTypeRequestBean requestBean);

    /**
     * Schedule surgery
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.ScheduleSurgeryResponseBean}
     */
    ResponseEntity<?> scheduleSurgery(ScheduleSurgeryRequestBean requestBean);

    /**
     * Remove scheduled surgery
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.UnscheduleSurgeryResponseBean}
     */
    ResponseEntity<?> unscheduleSurgery(UnscheduleSurgeryRequestBean requestBean);

    /**
     * Reschedule surgery
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.RescheduleSurgeryResponseBean}
     */
    ResponseEntity<?> rescheduleSurgery(RescheduleSurgeryRequestBean requestBean);

}
