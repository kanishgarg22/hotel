package com.ums.ums.repository;

import com.ums.ums.entity.AppUser;
import com.ums.ums.entity.Property;
import com.ums.ums.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
        @Query("Select r from Review r where r.property=:property and r.appUser=:user ")
        Review  fetchUserReview(@Param("property") Property property, @Param("user") AppUser user);
}