package com.ums.ums.repository;

import com.ums.ums.entity.AppUser;
import com.ums.ums.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    // Add entity alias "f" in the query
    @Query("select f from Favourite f where f.appUser = :user")
    public List<Favourite> getFavourites(@Param("user") AppUser user);
}
