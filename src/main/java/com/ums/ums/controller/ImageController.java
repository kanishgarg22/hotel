package com.ums.ums.controller;

import com.ums.ums.entity.Images;
import com.ums.ums.entity.Property;
import com.ums.ums.repository.ImagesRepository;
import com.ums.ums.repository.PropertyRepository;
import com.ums.ums.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    private ImagesRepository imagesRepository;
    private PropertyRepository propertyRepository;

    private BucketService bucketService;

    public ImageController(ImagesRepository imagesRepository, PropertyRepository propertiesRepository, PropertyRepository propertyRepository, BucketService bucketService) {
        this.imagesRepository = imagesRepository;
        this.propertyRepository = propertyRepository;
        this.bucketService = bucketService;
    }
    @PostMapping("/addImages")
    public ResponseEntity<Images> addImages(
            @RequestParam long propertyId,
            @RequestParam String bucketName,
            MultipartFile file
    ){
        String imageUrl = bucketService.uploadFile(file, bucketName);
        Optional<Property> byId = propertyRepository.findById(propertyId);
        Property property = byId.get();
        Images images = new Images();
        images.setImageUrl(imageUrl);
        images.setProperty(property);
        imagesRepository.save(images);
        return new ResponseEntity<>(images, HttpStatus.CREATED);
    }
    @GetMapping("/propertyImages")
    public ResponseEntity<List<Images>> fetchPropertyImages(
            @RequestParam long propertyId
    ){
        List<Images> images = imagesRepository.findByPropertyId(propertyId);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }


}
