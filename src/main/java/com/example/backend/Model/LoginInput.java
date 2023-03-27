package com.example.backend.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginInput {
    private String email;
    private String password;

    public LoginInput(String email, String password) {
        this.email = email;
        this.password = password;
    }
}