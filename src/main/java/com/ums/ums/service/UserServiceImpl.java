package com.ums.ums.service;

import com.ums.ums.entity.AppUser;
import com.ums.ums.payload.LoginDto;
import com.ums.ums.payload.UserDto;
import com.ums.ums.repository.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {

    private JWTService jwtService;

    private AppUserRepository userRepository;

    public UserServiceImpl(JWTService jwtService, AppUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }


    public UserDto addUser() {
        return addUser(null);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        AppUser user = mapToEntity(userDto);

        AppUser savedUser = userRepository.save(user);
        UserDto dto = mapToDto(savedUser);
        //dto.setPassword(savedUser.getPassword());
        return dto;
    }

    @Override
    public String verifyLogin(LoginDto loginDto) {
        Optional<AppUser> opUser = userRepository.findByUsername(loginDto.getUsername());
        if (opUser.isPresent()){
            AppUser user = opUser.get();
            if(BCrypt.checkpw(loginDto.getPassword(),user.getPassword())){
                return jwtService.generateToken(user);
            }
        }
        return null;
    }

    UserDto mapToDto(AppUser user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setUsername(user.getUsername());
        dto.setEmailId(user.getEmailId());
        return dto;
    }

    AppUser mapToEntity(UserDto userDto){
        AppUser user = new AppUser();
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setEmailId(userDto.getEmailId());
        user.setUsername("ROLE_USER");
        user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10)));
        return user;
    }
}
