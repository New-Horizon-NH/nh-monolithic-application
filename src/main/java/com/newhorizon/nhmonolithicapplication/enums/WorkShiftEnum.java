package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum WorkShiftEnum {
    MORNING(1000, "Morning", LocalTime.of(7, 0), LocalTime.of(13, 30)),
    AFTERNOON(1001, "Afternoon", LocalTime.of(13, 30), LocalTime.of(20, 0)),
    NIGHT(1002, "Night", LocalTime.of(20, 0), LocalTime.of(7, 30)),
    END_OF_SHIFT(1003, "EndOfShift", LocalTime.of(7, 0), LocalTime.of(23, 59, 59)),
    DAY_OFF(1004, "DayOff", LocalTime.of(0, 0), LocalTime.of(23, 59, 59));

    private final Integer workShiftCode;
    private final String workShiftName;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public static List<WorkShiftEnum> workShiftFromInitial(WorkShiftEnum start,
                                                           Integer workShiftMonthDays) {
        WorkShiftEnum[] values = WorkShiftEnum.values(); // Get all enum values
        int startIndex = start.ordinal(); // Get the index of the given start value
        return IntStream.range(0, workShiftMonthDays)
                .mapToObj(index -> values[(startIndex + index) % values.length])
                .toList();
    }

    public static WorkShiftEnum retrieveByCode(Integer code) {
        Optional<WorkShiftEnum> responseCode = Arrays.stream(WorkShiftEnum.values())
                .filter(workShiftEnum -> code.equals(workShiftEnum.getWorkShiftCode()))
                .findFirst();
        return responseCode.orElse(null);
    }
}
