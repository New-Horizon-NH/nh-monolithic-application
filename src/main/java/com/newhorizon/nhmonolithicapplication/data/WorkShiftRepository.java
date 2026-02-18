package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.WorkShiftDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.projections.MonthlyShiftProjectionImpl;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.WorkShiftRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class WorkShiftRepository implements WorkShiftDAO {
    private final WorkShiftRepo workShiftRepo;

    @Override
    public Optional<WorkShiftDTO> retrieveByShiftCode(Integer shiftCode) {
        return workShiftRepo.findByShiftCode(shiftCode)
                .map(entity -> WorkShiftDTO.builder()
                        .workShiftId(entity.getId())
                        .name(entity.getName())
                        .shiftCode(shiftCode)
                        .startTime(entity.getStartTime())
                        .endTime(entity.getEndTime())
                        .build());
    }

    @Override
    public List<MonthlyShiftProjectionImpl> retrieveMonthlyShifts(String medicalId, Integer month, Integer year) {
        return workShiftRepo.findMonthlyShifts(medicalId,
                        LocalDate.of(year, month, 1),
                        LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth()))
                .stream()
                .map(entity -> MonthlyShiftProjectionImpl.builder()
                        .shiftCode(entity.getShiftCode())
                        .shiftDate(entity.getShiftDate())
                        .build())
                .toList();
    }

    @Override
    public Optional<WorkShiftDTO> findById(String workShiftId) {
        return workShiftRepo.findById(workShiftId)
                .map(entity -> WorkShiftDTO.builder()
                        .workShiftId(entity.getId())
                        .name(entity.getName())
                        .shiftCode(entity.getShiftCode())
                        .startTime(entity.getStartTime())
                        .endTime(entity.getEndTime())
                        .build());
    }
}
