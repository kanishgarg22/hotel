package com.ums.ums.controller;

import com.ums.ums.entity.AppUser;
import com.ums.ums.entity.Favourite;
import com.ums.ums.entity.Property;
import com.ums.ums.repository.FavouriteRepository;
import com.ums.ums.repository.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/favourite")
public class FavouriteController {
    private FavouriteRepository favouriteRepository;
    private PropertyRepository propertyRepository;

    public FavouriteController(FavouriteRepository favouriteRepository, PropertyRepository propertyRepository) {
        this.favouriteRepository = favouriteRepository;
        this.propertyRepository = propertyRepository;
    }
    @PostMapping("/addFav")
    public ResponseEntity<Favourite> addFavourite(
            AppUser user,
            @RequestParam long propertyId
            ){
        Optional<Property> byId = propertyRepository.findById(propertyId);
        Property property = byId.get();
        Favourite favourite = new Favourite();
        favourite.setAppUser(user);
        favourite.setProperty(property);
        favourite.setIsFavourite(true);
        Favourite savedFavourite = favouriteRepository.save(favourite);
        return new ResponseEntity<>(savedFavourite, HttpStatus.CREATED);
    }
    @GetMapping("/userFavouriteList")
    public ResponseEntity<List<Favourite>> getAllFavouritesOfUser(
        AppUser user
    ) {
        List<Favourite> favourites = favouriteRepository.getFavourites(user);
        return new ResponseEntity<>(favourites, HttpStatus.OK);
    }
}
