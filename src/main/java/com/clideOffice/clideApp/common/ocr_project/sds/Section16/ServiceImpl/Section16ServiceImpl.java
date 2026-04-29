package com.clideOffice.clideApp.common.ocr_project.sds.Section16.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section16.OtherInformation;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.Section16Projection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Section16Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section16RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section16ResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.Section16.Service.Section16Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Section16ServiceImpl implements Section16Service {

    private final Section16Repository repository;

    @Override
    @Transactional
    public Section16ResponseDTO addItem(Section16RequestDTO dto) {

        OtherInformation entity = repository.findEntityBySdsId(dto.getSdsId())
                .orElse(OtherInformation.builder().sdsId(dto.getSdsId()).build());

        entity.setIndicationOfChanges(dto.getIndicationOfChanges());
        entity.setAbbreviations(dto.getAbbreviations());
        entity.setReferences(dto.getReferences());
        entity.setClassificationProcedure(dto.getClassificationProcedure());
        entity.setHPhrases(dto.getHPhrases());
        entity.setTrainingAdvice(dto.getTrainingAdvice());
        entity.setAdditionalInformation(dto.getAdditionalInformation());

        entity.setDateOfIssue(dto.getDateOfIssue());
        entity.setDateOfRevision(dto.getDateOfRevision());
        entity.setVersion(dto.getVersion());
        entity.setPreparedBy(dto.getPreparedBy());
        entity.setSdsNumber(dto.getSdsNumber());
        entity.setDisclaimer(dto.getDisclaimer());

        return mapToResponse(repository.save(entity));
    }

    @Override
    public Section16ResponseDTO getBySdsId(Long sdsId) {

        Section16Projection p = repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Section16 not found"));

        return Section16ResponseDTO.builder()
                .id(p.getId())
                .sdsId(p.getSdsId())
                .indicationOfChanges(p.getIndicationOfChanges())
                .abbreviations(p.getAbbreviations())
                .references(p.getReferences())
                .classificationProcedure(p.getClassificationProcedure())
                .hPhrases(p.getHPhrases())
                .trainingAdvice(p.getTrainingAdvice())
                .additionalInformation(p.getAdditionalInformation())
                .dateOfIssue(p.getDateOfIssue())
                .dateOfRevision(p.getDateOfRevision())
                .version(p.getVersion())
                .preparedBy(p.getPreparedBy())
                .sdsNumber(p.getSdsNumber())
                .disclaimer(p.getDisclaimer())
                .build();
    }

    @Override
    @Transactional
    public void deleteBySdsId(Long sdsId) {

        repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Section16 not found"));

        repository.deleteBySdsId(sdsId);
    }

    private Section16ResponseDTO mapToResponse(OtherInformation e) {

        return Section16ResponseDTO.builder()
                .id(e.getId())
                .sdsId(e.getSdsId())
                .indicationOfChanges(e.getIndicationOfChanges())
                .abbreviations(e.getAbbreviations())
                .references(e.getReferences())
                .classificationProcedure(e.getClassificationProcedure())
                .hPhrases(e.getHPhrases())
                .trainingAdvice(e.getTrainingAdvice())
                .additionalInformation(e.getAdditionalInformation())
                .dateOfIssue(e.getDateOfIssue())
                .dateOfRevision(e.getDateOfRevision())
                .version(e.getVersion())
                .preparedBy(e.getPreparedBy())
                .sdsNumber(e.getSdsNumber())
                .disclaimer(e.getDisclaimer())
                .build();
    }
}