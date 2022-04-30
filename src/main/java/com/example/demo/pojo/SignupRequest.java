package com.example.demo.pojo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest {
    
    @NotBlank
    @Size(min = 3, max = 50)
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    public SignupRequest() {
    }

    public SignupRequest(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
