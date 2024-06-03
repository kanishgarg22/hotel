package com.ums.ums.service;

import com.ums.ums.payload.LoginDto;
import com.ums.ums.payload.UserDto;

public interface UserService {
    public UserDto addUser(UserDto userDto);

    String verifyLogin(LoginDto loginDto);
}
