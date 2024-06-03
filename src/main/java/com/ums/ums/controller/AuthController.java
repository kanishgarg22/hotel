package com.ums.ums.controller;

import com.ums.ums.entity.AppUser;
import com.ums.ums.payload.JwtResponse;
import com.ums.ums.payload.LoginDto;
import com.ums.ums.payload.UserDto;
import com.ums.ums.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/{addUser}")
    public ResponseEntity<?>  addUser(
            @RequestBody UserDto userDto,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        UserDto dto = userService.addUser(userDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        String token = userService.verifyLogin(loginDto);
        if(token != null){
            JwtResponse response = new JwtResponse();
            response.setToken(token);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid credentials",HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/profile")
    public AppUser getCurrentProfile(AppUser user){
        return user;
    }


}