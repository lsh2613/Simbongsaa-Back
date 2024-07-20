package com.simbongsa.global.image.service;

import com.simbongsa.feed.entity.Feed;
import com.simbongsa.global.common.constant.ImageType;
import com.simbongsa.global.image.Repository.ImageRepository;
import com.simbongsa.global.image.entity.Image;
import com.simbongsa.group.entity.Group;
import com.simbongsa.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ImageServiceImpl implements ImageService{
    private final ImageRepository imageRepository;

    @Override
    public List<Image> saveImages(ImageType imageType, Object entity, List<String> imageUrls) {
        if (imageUrls == null)
            imageUrls = new ArrayList<>();

        List<Image> images = imageUrls.stream()
                .map(url -> createImage(imageType, entity, url))
                .collect(Collectors.toList());
        return imageRepository.saveAll(images);
    }

    private Image createImage(ImageType imageType, Object entity, String url) {
        Image.ImageBuilder imageBuilder = Image.builder().url(url);
        switch (imageType) {
            case GROUP -> imageBuilder.group((Group) entity);
            case MEMBER -> imageBuilder.member((Member) entity);
            case FEED -> imageBuilder.feed((Feed) entity);
            default -> throw new IllegalArgumentException("Unknown image type: " + imageType);
        }
        return imageBuilder.build();
    }
}
