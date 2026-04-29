package com.clideOffice.clideApp.common.ocr_project.sds.Section10.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section10.Service.Section10Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section10.Section10Item;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section10RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section10ResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Section10Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Section10ServiceImpl implements Section10Service {

    private final Section10Repository section10Repository;

    // ================= POST =================
    @Override
    public Section10ResponseDTO addItem(Section10RequestDTO requestDTO) {

        Section10Item entity = Section10Item.builder()
                .sdsId(requestDTO.getSdsId())
                .itemName(requestDTO.getItemName())
                .information(requestDTO.getInformation())
                .remarks(requestDTO.getRemarks())
                .isDeleted(false)
                .build();

        Section10Item saved = section10Repository.save(entity);

        Section10ResponseDTO response = new Section10ResponseDTO();
        response.setId(saved.getId());
        response.setSdsId(saved.getSdsId());
        response.setItemName(saved.getItemName());
        response.setInformation(saved.getInformation());
        response.setRemarks(saved.getRemarks());

        return response;
    }

    // ================= DELETE (SOFT DELETE) =================
    @Override
    public void deleteItem(Long id) {

        Section10Item entity = section10Repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section10 item not found with id: " + id));

        entity.setIsDeleted(true); // ✅ Soft delete
        section10Repository.save(entity);
    }
}