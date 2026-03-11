package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.GetPlantProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.GetPlantRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetPlantResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.GetPlantService;

@Service
public class GetPlantServiceImpl implements GetPlantService {

    private final GetPlantRepository getPlantRepository;

    public GetPlantServiceImpl(GetPlantRepository getPlantRepository) {
        this.getPlantRepository = getPlantRepository;
    }

    @Override
    public List<GetPlantResponseDto> getPlantsByService(Long serviceId) {

        List<GetPlantProjection> plants =
                getPlantRepository.getPlantsByService(serviceId);

        return plants.stream()
                .map(p -> new GetPlantResponseDto(
                        p.getPlantId(),
                        p.getPlantName()
                ))
                .collect(Collectors.toList());
    }
}