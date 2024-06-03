package com.ums.ums.controller;

import com.ums.ums.entity.Property;
import com.ums.ums.exceptions.ResourceNotFound;
import com.ums.ums.repository.PropertyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {
    private PropertyRepository propertyRepository;

    public PropertyController(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @GetMapping("/{locationName}")
    public ResponseEntity<List<Property>> getPropertyListings(
            @PathVariable String locationName)
    {
        List<Property> propertiesListing =  propertyRepository.listPropertyByLocationOrCountryName(locationName);
        return new ResponseEntity<>(propertiesListing, HttpStatus.OK);
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<Property> getAllProperty(
            @PathVariable long propertyId)
    {
        Property property = propertyRepository.findById(propertyId).orElseThrow(
                () -> new ResourceNotFound("Property not found with id " + propertyId)
        );
        return new ResponseEntity<>(property, HttpStatus.OK);
    }


    //localhost:8090/api/v1/property?pageSize=2&pageNo=0&sortBy=propertyName&sortDir=desc
    @GetMapping
    public ResponseEntity<List<Property>> getAllProperty(
            @RequestParam(name = "pageSize", defaultValue = "5", required = false)  int pageSize,
            @RequestParam(name = "pageNo", defaultValue = "5", required = false)  int pageNo,
            @RequestParam(name = "sortBy", defaultValue = "id", required = false)  String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false)  String sortDir

    ){
        Pageable pageable=null;
        if(sortDir.equalsIgnoreCase("asc")){
            pageable = PageRequest.of(pageSize, pageNo, Sort.by(sortBy).ascending());
        }else if(sortDir.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(pageSize, pageNo, Sort.by(sortBy).descending());
        }


        Page<Property> all =  propertyRepository.findAll(pageable);
        List<Property> properties = all.getContent();
        Pageable firstPage = pageable.first();
        int pageNumber = pageable.getPageNumber();
        int pageCapacity = pageable.getPageSize();
        int totalPages = pageable.getPageSize();
        long totalElements = all.getTotalElements();
        boolean hasNextPage = all.hasNext();
        boolean hasPrevious = all.hasPrevious();
        boolean last = all.isLast();
        boolean first = all.isFirst();
        //sout statements
        System.out.println(pageNumber);
        System.out.println(pageCapacity);
        System.out.println(totalPages);
        System.out.println(totalElements);
        System.out.println(hasNextPage);
        System.out.println(hasPrevious);
        System.out.println(last);
        System.out.println(first);
        System.out.println(firstPage);
        System.out.println(pageable);
        System.out.println(all);
        System.out.println(properties);
        System.out.println(all.getContent());
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

}
