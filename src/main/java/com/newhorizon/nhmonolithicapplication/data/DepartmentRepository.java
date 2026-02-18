package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.DepartmentDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DepartmentEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DepartmentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class DepartmentRepository implements DepartmentDAO {
    private final DepartmentRepo departmentRepo;

    @Override
    public Optional<DepartmentDTO> retrieveByCode(String code) {
        Optional<DepartmentEntity> entity = departmentRepo.findByCode(code);
        return entity.map(departmentEntity -> DepartmentDTO.builder()
                .departmentName(departmentEntity.getDepartmentName())
                .code(departmentEntity.getCode())
                .description(departmentEntity.getDepartmentDescription())
                .location(departmentEntity.getLocation())
                .director(departmentEntity.getDirector())
                .phone(departmentEntity.getPhone())
                .email(departmentEntity.getEmail())
                .coordinator(departmentEntity.getCoordinator())
                .build());
    }

    @Override
    public Optional<DepartmentDTO> createDepartment(DepartmentDTO build) {
        DepartmentEntity entity = DepartmentEntity.builder()
                .departmentName(build.getDepartmentName())
                .code(build.getCode())
                .departmentDescription(build.getDescription())
                .location(build.getLocation())
                .director(build.getDirector())
                .phone(build.getPhone())
                .email(build.getEmail())
                .coordinator(build.getCoordinator())
                .build();
        departmentRepo.saveAndFlush(entity);
        return Optional.of(build);
    }
}
