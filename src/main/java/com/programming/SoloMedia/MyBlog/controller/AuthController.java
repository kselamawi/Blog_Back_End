package com.programming.SoloMedia.MyBlog.controller;
import com.programming.SoloMedia.MyBlog.service.AuthenticationResponse;

import com.programming.SoloMedia.MyBlog.dto.LoginRequest;
import com.programming.SoloMedia.MyBlog.dto.RegisterRequest;
import com.programming.SoloMedia.MyBlog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;
    @PostMapping("/signup")
    /*the "ResponseEntity" is a built-in class inside spring to send response to the client.so now if the "Post"
    * is successful then it sends OK(http status of 200) response to the client */
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    /*Here, wer are creating login authentication.Here we have a method called login with a parameter 'LoginRequest' and
    * let's create class called 'LoginRequest' inside the 'dto' package and define some fields required for login.In order a user
    * to login that user needs authentication token and this authentication token was created in the "JwtProvider" class.the token was return to
    "AuthService" class and from there return to this class(AuthController), and from here it will be sent to the client  */
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        System.out.println(loginRequest);
         return authService.login(loginRequest);

    }

}
