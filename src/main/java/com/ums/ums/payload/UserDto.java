package com.ums.ums.payload;

import lombok.Data;

@Data
public class UserDto {

    private long id;
    private String name;
    private String username;
    private String emailId;
    private String password;

}
