package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.CreateVersionProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.CreateVersionRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.CreateVersionRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.CreateVersionResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.CreateVersionService;
import com.clideOffice.clideApp.common.ocr_project.sds.service.UploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateVersionServiceImpl implements CreateVersionService {

    private final CreateVersionRepository repository;
    private final UploadService uploadService;

    @Override
    @Transactional
    public CreateVersionResponseDto createVersion(Long sdsId, CreateVersionRequestDto request) {

        // get current version
        CreateVersionProjection current = repository.getCurrentVersion(sdsId);

        if (current == null) {
            throw new RuntimeException("SDS not found");
        }

        // calculate next version
        Integer newVersion = current.getCurrentVersion() + 1;

        // upload file
        UploadRequestDto uploadRequest = new UploadRequestDto();
        uploadRequest.setFile(request.getFile());

        UploadResponseDto uploadResponse = uploadService.uploadSds(uploadRequest);
        String fileUrl = uploadResponse.getFileUrl();

        // insert new version
        repository.insertNewVersion(
                sdsId,
                newVersion,
                fileUrl,
                request.getChangeNotes(),
                request.getUploadedBy()
        );

        // update master table
        repository.updateCurrentVersion(sdsId, newVersion);

        return CreateVersionResponseDto.builder()
                .sdsId(sdsId)
                .version(newVersion)
                .message("New version created successfully")
                .build();
    }
}