package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.MedicalDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.MedicalRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class MedicalRepository implements MedicalDAO {
    private final MedicalRepo medicalRepo;

    @Override
    public Optional<MedicalDTO> retrieveByFiscalCode(String fiscalCode) {
        return medicalRepo.findByFiscalCode(fiscalCode)
                .map(entity -> MedicalDTO.builder()
                        .name(entity.getName())
                        .surname(entity.getSurname())
                        .fiscalCode(entity.getFiscalCode())
                        .memberId(entity.getId())
                        .commaSeparatedRoles(entity.getCommaSeparatedRoles())
                        .build());
    }

    @Override
    public Optional<MedicalDTO> retrieveById(String medicalId) {
        return medicalRepo.findById(medicalId)
                .map(entity -> MedicalDTO.builder()
                        .name(entity.getName())
                        .surname(entity.getSurname())
                        .fiscalCode(entity.getFiscalCode())
                        .memberId(entity.getId())
                        .commaSeparatedRoles(entity.getCommaSeparatedRoles())
                        .build());
    }
}
