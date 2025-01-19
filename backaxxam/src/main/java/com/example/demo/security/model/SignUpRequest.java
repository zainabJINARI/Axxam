package com.example.demo.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
public class SignUpRequest {
	private String username;
    private String email;
    private String password;
    private String role;
}
