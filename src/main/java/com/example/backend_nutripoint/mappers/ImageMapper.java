package com.example.backend_nutripoint.mappers;

import com.example.backend_nutripoint.DTO.responses.ImageResponseDTO;
import com.example.backend_nutripoint.models.ImgProd;

public class ImageMapper {

    private ImageMapper() {}

    public static ImageResponseDTO imageToDTO(ImgProd imgProd) {
        return ImageResponseDTO.builder()
                .idImage(imgProd.getIdImg())
                .url(imgProd.getImageUrl())
                .build();
    }
}
