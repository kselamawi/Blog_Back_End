package com.programming.SoloMedia.MyBlog.service;

import com.programming.SoloMedia.MyBlog.model.Bloger;
import com.programming.SoloMedia.MyBlog.repo.BlogerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private BlogerRepository blogerRepository;

    /*inside this method we should query our database based on this username and return the user-details to spring. so to do that
    * first we need to 'Autowired' our BlogerRepository*/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Bloger bloger= blogerRepository.findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("No user found " + username));
    /* here we have a class by the name User and this User object also expect us to provide what authorities this user is having ,and we passed
    a basic an authority called 'ROLE_USER' to the method and inside this method,a simple granted authority object with this role("ROLE_USER")
    and returns back as a singleton list.Now the custom User Detail Service that we have created needed to be wired it to "SecurityConfig"
    class and spring will use this service for authentication.*/
        return new org.springframework.security.core.userdetails.User(bloger.getUserName(),
                bloger.getPassword(),
                true, true, true, true,
                //getAuthorities("ROLE_USER"));
        getAuthorities("USER_ROLE"));
    }
    private Collection<? extends GrantedAuthority> getAuthorities(String role_user) {
        return Collections.singletonList(new SimpleGrantedAuthority(role_user));
    }
}
