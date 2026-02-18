package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignRoomBedRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterBedRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDepartmentRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnassignRoomBedRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AssignRoomBedResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterDepartmentResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterRoomResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UnassignRoomBedResponseBean;
import org.springframework.http.ResponseEntity;

public interface DepartmentController {
    /**
     * Register a department inside the hospital
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RegisterDepartmentResponseBean}
     */
    ResponseEntity<?> registerDepartment(RegisterDepartmentRequestBean requestBean);

    /**
     * Register a room with beds and bed id
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RegisterRoomResponseBean}
     */
    ResponseEntity<?> registerRoom(RegisterRoomRequestBean requestBean);

    /**
     * Register bed inside specific room
     *
     * @param requestBean request
     * @return Entity with manged response of RegisterBedRoomResponseBean
     */
    ResponseEntity<?> registerBedRoom(RegisterBedRoomRequestBean requestBean);

    /**
     * Assign bed to a patient
     *
     * @param requestBean request
     * @return Entity with managed response of {@link AssignRoomBedResponseBean}
     */
    ResponseEntity<?> assignRoomBed(AssignRoomBedRequestBean requestBean);

    /**
     * Unassign bed to a assigned patient
     *
     * @param requestBean request
     * @return Entity with managed response of {@link UnassignRoomBedResponseBean }
     */
    ResponseEntity<?> unassignRoomBed(UnassignRoomBedRequestBean requestBean);
}
