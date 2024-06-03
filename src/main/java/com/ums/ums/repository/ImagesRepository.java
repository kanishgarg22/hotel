package com.ums.ums.repository;

import com.ums.ums.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    public List<Images> findByPropertyId(long propertyId);

}