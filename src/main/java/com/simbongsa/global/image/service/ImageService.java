package com.simbongsa.global.image.service;

import com.simbongsa.global.common.constant.ImageType;
import com.simbongsa.global.image.entity.Image;

import java.util.List;

public interface ImageService {
    List<Image> saveImages(ImageType imageType, Object entity, List<String> imageUrls);
}
