package com.ums.ums.controller;

import com.ums.ums.entity.AppUser;
import com.ums.ums.entity.Property;
import com.ums.ums.entity.Review;
import com.ums.ums.repository.PropertyRepository;
import com.ums.ums.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private ReviewRepository reviewRepository;
    private PropertyRepository propertyRepository;

    public ReviewController(ReviewRepository reviewRepository, PropertyRepository propertyRepository) {
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
    }
    @PostMapping
    public ResponseEntity<String> addReview(
            AppUser user,
            @RequestParam long propertyID,
            @RequestBody Review review
            ){
        Optional<Property> byId = propertyRepository.findById(propertyID);
        Property property = byId.get();
        review.setAppUser(user);
        review.setProperty(property);
        Review r = reviewRepository.fetchUserReview(property, user);
        if(r != null){
            return new ResponseEntity<>("Review is already given", HttpStatus.BAD_REQUEST);
        }
        reviewRepository.save(review);
        return new ResponseEntity<>("Review is added", HttpStatus.CREATED);
    }
}
