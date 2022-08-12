package com.programming.SoloMedia.MyBlog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailsService userDetailsService;
    /*this will the method executed when call the endpoint in our application*/

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        /*inside the method,first we need retrieve the JWT from the request,using the method below and to do that we have
        to create a method called 'getJwtFromRequest'.
        from the method below we'll have our Jwt,and we need to validate this Jwt and to write this validation logic
         we need to go to JwtProvider class*/
        String jwt =getJwtFromRequest(request);
        if(StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)){
            String username=jwtProvider.getUsernameFromJWT(jwt);
            UserDetails userDetails =userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(userDetails,
                    null,userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);

    }

    private String getJwtFromRequest(HttpServletRequest request) {
        /*inside this method we'll retrieve the authentication token*/
      String bearerToken= request.getHeader("Authorization");
        /*here,we'll have if function that checks if the bearer token has some text*/
if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
    /*if the if function is true then we return the Jwt by using the 'bearerToken.substring(7) b/c in the if condition
    we have used the Bearer and the 'Bearer' string have six characters plus the space after it total of index 6(0to6) and the Jwt starts at
    7th index and we're going to return starting at the 7th index which leave us with just Jwt only*/
    return bearerToken.substring(7);
}
/*outside the if function ,we simply return the bearerToken*/
        return bearerToken;
    }
}
