package com.programming.SoloMedia.MyBlog.service;

import com.programming.SoloMedia.MyBlog.dto.LoginRequest;
import com.programming.SoloMedia.MyBlog.dto.RegisterRequest;
import com.programming.SoloMedia.MyBlog.model.Bloger;
import com.programming.SoloMedia.MyBlog.repo.BlogerRepository;
import com.programming.SoloMedia.MyBlog.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private BlogerRepository blogerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;


    /* when a user/blogger signup, provides the following information, and to send this information,
            to database, we autowired the repository and using the instance of the "BlogerRepository" ,we save it*/
    public void signup(RegisterRequest registerRequest) {
        Bloger bloger =new Bloger();
        bloger.setUserName(registerRequest.getUsername());
        bloger.setPassword( encodePassword(registerRequest.getPassword()));
        bloger.setEmail(registerRequest.getEmail());
        blogerRepository.save(bloger);
    }
/*when the bloger set the password,we used 'encodePassword' to encrypt it,and then created a method called 'encoderPassword'
then return encoded password using the instance of the "PasswordEncoder" and then the encrypted password will be saved in the database*/
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /* here we have successfully implemented authentication process,now we have to implement the process to
    create the JSON web token after successful authentication.Before creating JSON web token for that we need to add gradle the dependency for the
    JSON web tokens */

    public AuthenticationResponse  login(LoginRequest loginRequest) {
       Authentication authenticate= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String authenticationToken =jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(authenticationToken,loginRequest.getUsername());
    }

    public Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
        org.springframework.security.core.userdetails.User principal =(org.springframework.security.core.userdetails.User)SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return  Optional.of(principal);
    }
}
