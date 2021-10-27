package com.shopme.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.apiresponse.ApiResponse;
import com.shopme.admin.security.JwtUtils;
import com.shopme.admin.security.ShopmeUserDetailsService;
import com.shopme.common.entity.LoginRequest;
import com.shopme.common.entity.LoginResponse;

@RestController
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManagerBean;
    @Autowired
    ShopmeUserDetailsService shopmeUserDetailsService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    ApiResponse apiResponse;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            authenticate(loginRequest.getEmail() ,loginRequest.getPassword());
        }catch (BadCredentialsException e){
            return apiResponse.errorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, false);
        }
        UserDetails u = shopmeUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtUtils.generateToken(u);
        return ResponseEntity.ok(new LoginResponse(token, "Login successful", true));
    }

    private void authenticate(String name, String pass) {
        authenticationManagerBean.authenticate(new UsernamePasswordAuthenticationToken(name,pass));
    }
}
