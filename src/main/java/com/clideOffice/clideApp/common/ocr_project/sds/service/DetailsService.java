package com.clideOffice.clideApp.common.ocr_project.sds.service;

import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DetailsResponseDto;

public interface DetailsService {

	DetailsResponseDto getSdsDetails(Long sdsId);
}
