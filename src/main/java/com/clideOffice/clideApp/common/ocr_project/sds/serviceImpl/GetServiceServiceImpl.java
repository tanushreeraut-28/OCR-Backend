package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.GetServiceProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.GetServiceRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetServiceResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.GetServiceService;

@Service
public class GetServiceServiceImpl implements GetServiceService {

    private final GetServiceRepository getServiceRepository;

    public GetServiceServiceImpl(GetServiceRepository getServiceRepository) {
        this.getServiceRepository = getServiceRepository;
    }

    @Override
    public List<GetServiceResponseDto> getAllServices() {

        List<GetServiceProjection> services =
                getServiceRepository.getAllServices();

        return services.stream()
                .map(service -> GetServiceResponseDto.builder()
                        .serviceId(service.getServiceId())
                        .serviceName(service.getServiceName())
                        .build())
                .collect(Collectors.toList());
    }
}