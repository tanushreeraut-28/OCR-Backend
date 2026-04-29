package com.clideOffice.clideApp.common.ocr_project.sds.Section6.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section6.Service.Section6Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section6.AccidentalReleaseMeasure;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Section6Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section6PartialUpdateRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section6PartialUpdateResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section6ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Section6ServiceImpl implements Section6Service {

    private final Section6Repository repository;

    // Get Section 6 data by SDS ID
    @Override
    public Section6ResponseDTO getBySdsId(Long sdsId) {

        AccidentalReleaseMeasure entity = repository.findAll()
                .stream()
                .filter(e -> e.getSdsId().equals(sdsId) && Boolean.TRUE.equals(e.getIsActive()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Section 6 not found for SDS ID: " + sdsId));

        return Section6ResponseDTO.builder()
                .id(entity.getId())
                .sdsId(entity.getSdsId())
                .personalPrecautions(entity.getPersonalPrecautions())
                .environmentalPrecautions(entity.getEnvironmentalPrecautions())
                .containmentMethods(entity.getContainmentMethods())
                .referenceSections(entity.getReferenceSections())
                .build();
    }

    // PARTIAL UPDATE
    @Override
    public Section6PartialUpdateResponseDto partialUpdate(Section6PartialUpdateRequestDTO requestDTO) {

        AccidentalReleaseMeasure entity = repository.findById(requestDTO.getId())
                .orElseThrow(() -> new RuntimeException("Record not found"));

        String field = requestDTO.getField();
        String value = requestDTO.getValue();

        // 🔥 Dynamic field update
        switch (field) {

            case "personalPrecautions":
                entity.setPersonalPrecautions(value);
                break;

            case "environmentalPrecautions":
                entity.setEnvironmentalPrecautions(value);
                break;

            case "containmentMethods":
                entity.setContainmentMethods(value);
                break;

            case "referenceSections":
                entity.setReferenceSections(value);
                break;

            default:
                throw new RuntimeException("Invalid field: " + field);
        }

        repository.save(entity);

        return Section6PartialUpdateResponseDto.builder()
                .id(entity.getId())
                .field(field)
                .value(value)
                .message("Updated successfully")
                .build();
    }
}