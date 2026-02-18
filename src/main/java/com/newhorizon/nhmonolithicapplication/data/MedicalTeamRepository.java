package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.MedicalChartMedicalTeamAssociationDTO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalTeamDTO;
import com.newhorizon.nhmonolithicapplication.mapper.MedicalChartMedicalTeamAssociationMapper;
import com.newhorizon.nhmonolithicapplication.mapper.MedicalTeamMapper;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.MedicalChartMedicalTeamAssociationRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.MedicalTeamRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class MedicalTeamRepository implements MedicalTeamDAO {
    private final MedicalTeamRepo medicalTeamRepo;
    private final MedicalChartMedicalTeamAssociationRepo medicalChartMedicalTeamAssociationRepo;
    private final MedicalTeamMapper medicalTeamMapper;
    private final MedicalChartMedicalTeamAssociationMapper medicalChartMedicalTeamAssociationMapper;

    @Override
    public Optional<MedicalTeamDTO> retrieveByFiscalCode(String fiscalCode) {
        return medicalTeamRepo.findByFiscalCode(fiscalCode)
                .map(medicalTeamMapper::parseEntity);
    }

    @Override
    public Optional<MedicalTeamDTO> createMember(MedicalTeamDTO build) {
        return Optional.of(medicalTeamMapper.parseEntity(
                medicalTeamRepo.saveAndFlush(
                        medicalTeamMapper.createMedicalEntity(build)
                )
        ));
    }

    @Override
    public Optional<MedicalChartMedicalTeamAssociationDTO> createPatientAssociation(MedicalChartMedicalTeamAssociationDTO build) {
        return Optional.of(
                medicalChartMedicalTeamAssociationMapper.parseEntity(
                        medicalChartMedicalTeamAssociationRepo.saveAndFlush(
                                medicalChartMedicalTeamAssociationMapper.parseDTO(build)
                        )
                )
        );
    }
}
