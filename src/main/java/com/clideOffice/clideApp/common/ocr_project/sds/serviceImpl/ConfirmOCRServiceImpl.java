package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import org.springframework.stereotype.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.repository.ConfirmOCRRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ConfirmOCRResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.ConfirmOCRService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfirmOCRServiceImpl implements ConfirmOCRService {

    private final ConfirmOCRRepository repository;

    @Override
    public ConfirmOCRResponseDto confirmOCR(Long sdsId) {

        // update OCR status to CONFIRMED
        repository.confirmOcr(sdsId);

        return ConfirmOCRResponseDto.builder()
                .sdsId(sdsId)
                .message("OCR confirmed successfully")
                .build();
    }
}