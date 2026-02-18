package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.DrugPackageDTO;
import com.newhorizon.nhmonolithicapplication.mapper.DrugPackageMapper;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugPackageEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugPackageWithdrawalRegistryEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DrugPackageRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DrugPackageWithdrawalRegistryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class DrugPackageRepository implements DrugPackageDAO {
    private final DrugPackageRepo drugPackageRepo;
    private final DrugPackageWithdrawalRegistryRepo drugPackageWithdrawalRegistryRepo;
    private final DrugPackageMapper drugPackageMapper;

    @Override
    public Optional<DrugPackageDTO> retrieveByPackageId(String packageId) {
        return drugPackageRepo.findByPackageId(packageId)
                .map(entity -> DrugPackageDTO.builder()
                        .drugPackageId(entity.getId())
                        .drugId(entity.getDrugId())
                        .packageId(entity.getPackageId())
                        .name(entity.getName())
                        .aicCode(entity.getAicCode())
                        .fornitureClass(entity.getFornitureClass())
                        .fornitureClassDescription(entity.getFornitureClassDescription())
                        .refundabilityClass(entity.getRefundabilityClass())
                        .quantity(entity.getQuantity())
                        .build());
    }

    @Override
    public Optional<DrugPackageDTO> createPackage(DrugPackageDTO build) {
        DrugPackageEntity entity = DrugPackageEntity.builder()
                .drugId(build.getDrugId())
                .packageId(build.getPackageId())
                .name(build.getName())
                .aicCode(build.getAicCode())
                .fornitureClass(build.getFornitureClass())
                .fornitureClassDescription(build.getFornitureClassDescription())
                .refundabilityClass(build.getRefundabilityClass())
                .quantity(0L)
                .build();
        entity = drugPackageRepo.saveAndFlush(entity);
        build.setDrugPackageId(entity.getId());
        return Optional.of(build);
    }

    /**
     * This method checks if the quantity of required drug is available in the system
     *
     * @param packageId id of the package, not table id
     * @param quantity  quantity of needed drug
     * @return {@code true} is quantity is more or equal else {@code false}
     */
    @Override
    public Boolean isQuantityAvailable(String packageId, Long quantity) {
        return drugPackageRepo.findByPackageId(packageId).orElseThrow().getQuantity() >= quantity;
    }

    /**
     * This method register a record in {@link com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugPackageWithdrawalRegistryEntity}
     * with the info of package, quantity and user.
     * Consequently is updated the quantity inside {@link com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugPackageEntity}
     */
    @Override
    public Optional<DrugPackageDTO> registerWithdraw(String packageId, Long quantity, String userId) {
        DrugPackageWithdrawalRegistryEntity entity = DrugPackageWithdrawalRegistryEntity.builder()
                .packageId(drugPackageRepo.findByPackageId(packageId).orElseThrow().getId())
                .quantity(quantity)
                .userId(userId)
                .timestamp(Instant.now())
                .build();
        drugPackageWithdrawalRegistryRepo.saveAndFlush(entity);
        DrugPackageEntity drugPackageEntity = drugPackageRepo.findByPackageId(packageId).orElseThrow();
        drugPackageEntity.setQuantity(Long.sum(drugPackageEntity.getQuantity(), -quantity));
        drugPackageEntity = drugPackageRepo.saveAndFlush(drugPackageEntity);
        return Optional.of(DrugPackageDTO.builder()
                .drugPackageId(drugPackageEntity.getId())
                .drugId(drugPackageEntity.getDrugId())
                .packageId(drugPackageEntity.getPackageId())
                .name(drugPackageEntity.getName())
                .aicCode(drugPackageEntity.getAicCode())
                .fornitureClass(drugPackageEntity.getFornitureClass())
                .fornitureClassDescription(drugPackageEntity.getFornitureClassDescription())
                .refundabilityClass(drugPackageEntity.getRefundabilityClass())
                .build());
    }

    @Override
    public Optional<DrugPackageDTO> addQuantity(String packageId, Long quantity) {
        DrugPackageEntity entity = drugPackageRepo.findByPackageId(packageId).orElseThrow();
        entity.setQuantity(Long.sum(entity.getQuantity(), quantity));
        entity = drugPackageRepo.saveAndFlush(entity);
        return Optional.of(drugPackageMapper.parseEntity(entity));
    }
}
