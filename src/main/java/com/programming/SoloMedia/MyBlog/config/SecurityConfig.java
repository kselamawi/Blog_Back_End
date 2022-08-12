package com.programming.SoloMedia.MyBlog.config;

import com.programming.SoloMedia.MyBlog.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /*to make use of the 'UserDetailService' for authentication we need to autowired it here.since we already have an implementation
    *of,it will pick our 'UserDetailServiceImpl' class ,and now we can use this service inside the 'configureGlobal' method*/

    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }
    // after create a bean here,we need to go the 'AuthService' class and add the logic to do the authentication.
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity  httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers("/api/posts/all")
                .permitAll()
                .anyRequest()
                .authenticated();
       httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    /*this method is used to solve the CORS issue from the frontend.*/

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



    /* Here we are going to create a method called 'configreGlobal'for login authentication ,and we'll pass as a parameter a class called
    'AuthenticationManagerBuilder' which is basically responsible for handling authentication functionality in spring.
    There are several kinds of authentication inside this 'AuthenticationManagerBuilder' such as in memory authentication,LDAP authentication,
    and JDBC based authentication.
   To do all this spring provide us an interface called "UserDetailService" which is used to load user specific data. Therefore,what we will
   do now is that creating our own "UserDetailService" interface ,and we will read the user from database and pass it to spring.
   so let's  implement  'UserDetailService interface' by 'UserDetailServiceImpl' class inside the service package'
   */

    /*Here,we're configuring spring to read users/bloger from the database and we will add a method called 'passwordEncoder' to called it from here
    * because as we're encoding our password spring also has to decode the password before performing authentication. we also need to create a 'Bean'
    * which will create an authentication manager so*/

    /* we have changed this method to the method below b/c we had error 'Error creating bean with name 'securityConfig':
    Requested bean is currently in creation:'*/
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//
//    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /*at this point the password is in simple text format,in which it can be easily read by any-one who got access to our database.
    * so now we're going to make the password encrypted so no one can read.There are several methods available with spring that can be used
    * to encrypt password,and one of the best is called 'BCryptPasswordEncoder' so we are going it here */

   @Override
   public void configure(WebSecurity web){
       web.ignoring().antMatchers(HttpMethod.OPTIONS,"/**");
   }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

        /* after defining "password-encoder" then we need to go to 'AuthService' class where we are getting the password
        from the user/Blogger when signup ,and we're going to encode it
        */
    }
}
