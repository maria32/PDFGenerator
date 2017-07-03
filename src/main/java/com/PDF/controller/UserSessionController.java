package com.PDF.controller;

import com.PDF.exception.ResourceAlreadyExistsException;
import com.PDF.model.Token;
import com.PDF.model.User;
import com.PDF.repository.TokenRepository;
import com.PDF.service.UserService;
import com.PDF.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by martanase on 6/5/2017.
 */
@Controller
public class UserSessionController {

    {
        System.out.println("Login Controller");
    }
    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenRepository tokenRepository;

    private static final Logger logger = Logger.getLogger(UserSessionController.class.getName());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public void getLoginPage(){
        System.out.println("Taking user to login page");
    }

    @RequestMapping(value="/login/credentials", method = RequestMethod.POST)
    @ResponseBody
    public User checkCredentialsAndLogin(@RequestBody User credentials, HttpServletResponse response) throws Exception{
        System.out.println("In login with credentials controller method");
        User user = userSessionService.checkCredentialsAndLogin(credentials, response);
        System.out.println(user);
        if (user != null){
            Cookie cookie = new Cookie("remember-me", UUID.randomUUID().toString());
            cookie.setMaxAge(60*60*24*14);
            cookie.setPath("/");
            response.addCookie(cookie);

            Token token = new Token(cookie.getValue(), user.getUsername());
            System.out.println(token);
            tokenRepository.save(token);
        }
        return user;
    }

    @RequestMapping(value="/login/sing-up", method = RequestMethod.PUT)
    @ResponseBody
    public User createUser(@RequestBody User credentials, HttpServletResponse response) {
        System.out.println("In sign-up with credentials controller method");

        User newUser = null;

        try{
            newUser = userSessionService.createUserAndLogin(credentials, response);
        } catch (ResourceAlreadyExistsException rae){
            response.addHeader("exception", "User already exists");
        } catch (Exception e){
            response.addHeader("exception", e.getMessage());
        }
        return newUser;
    }

    @RequestMapping(value="/logout/{tokenValue}", method = RequestMethod.GET)
    @ResponseBody
    public void logout(@PathVariable("tokenValue") String tokenValue, HttpServletResponse response) {
        Token token = tokenValue == null ? null : tokenRepository.findOne(tokenValue);
        if (token != null) {
            tokenRepository.delete(tokenValue);
            Cookie cookie = new Cookie("remember-me", token.getValue());
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        userSessionService.logout();
    }

    @RequestMapping(value="/user/authenticate", method = RequestMethod.GET)
    public Principal user(Principal user) {
        System.out.println("Principal:" + user.getName() + user.toString());
        return user;
    }

    @RequestMapping(value="/user/{tokenValue}", method = RequestMethod.GET)
    @ResponseBody
    public User getUserByTokenValue(@PathVariable("tokenValue") String tokenValue) {
        Token token = tokenRepository.findOne(tokenValue);
        User user = null;
        if (token != null){
            user = userService.getOne(token.getUsername());
        }
        return user;
    }

}
