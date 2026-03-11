package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import org.springframework.stereotype.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.DashboardSummaryProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.DashboardSummaryRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DashboardSummaryResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.DashboardSummaryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardSummaryServiceImpl implements DashboardSummaryService {

    private final DashboardSummaryRepository dashboardSummaryRepository;

    // Fetch dashboard summary counts
    @Override
    public DashboardSummaryResponseDto getDashboardSummary() {

        DashboardSummaryProjection projection =
                dashboardSummaryRepository.getDashboardSummary();

        return DashboardSummaryResponseDto.builder()
                .totalSds(projection.getTotalSds())
                .pendingReview(projection.getPendingReview())
                .duplicates(projection.getDuplicates())
                .build();
    }
}