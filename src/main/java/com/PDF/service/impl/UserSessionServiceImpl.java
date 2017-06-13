package com.PDF.service.impl;

import com.PDF.exception.ResourceAlreadyExistsException;
import com.PDF.model.User;
import com.PDF.security.UserTokenState;
import com.PDF.service.UserService;
import com.PDF.service.UserSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martanase on 6/5/2017.
 */
@Service
public class UserSessionServiceImpl implements UserSessionService {

    private static final Logger logger = Logger.getLogger(UserSessionServiceImpl.class.getName());

    private String username;

    @Autowired
    private UserService userService;

    public User checkCredentialsAndLogin(User credentials, HttpServletResponse response) throws Exception {
        User user = userService.checkCredentials(credentials);
        if (user != null){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            username = user.getUsername();
            user.setPassword("");
        }


        //return user == null ? null : user;

        String jws = Jwts.builder()
                .setIssuer("app_name")
                .setSubject("user2")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600_000))
                .signWith(SignatureAlgorithm.HS512, "queenvictoria")
                .compact();

        Cookie authCookie = new Cookie("my-cookie-Go4PDF", jws);
        authCookie.setComment("user authentication. keep session");
        authCookie.setPath("/");
        authCookie.setHttpOnly(true);
        authCookie.setMaxAge(600);
        System.out.println(authCookie.getValue());

        UserTokenState userTokenState = new UserTokenState(jws, 600);
        String jwtResponse = new ObjectMapper().writeValueAsString(userTokenState);

        response.addCookie(authCookie);
        response.setContentType("application/json");
        //response.getWriter().write(jwtResponse);

        return user == null ? null : user;

    }

    public User checkUserInDB(User credentials) {
        return userService.checkCredentials(credentials);
    }

    @Override
    public User createUserAndLogin(User credentials, HttpServletResponse response) throws ResourceAlreadyExistsException {
        User user = userService.create(credentials);
        if (user != null){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            user.setPassword("");
        }
        return user == null ? null : user;
    }

    public void logout(){
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }

    /**
     * Check if user is login by remember me cookie, refer
     * org.springframework.security.authentication.AuthenticationTrustResolverImpl
     */
    public boolean isRememberMeAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        //return (String) authentication.getCredentials();
        return RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }

    public String getUsername(){
        return username;
    }

}
