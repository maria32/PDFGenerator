package com.PDF.controller;

import com.PDF.exception.ResourceAlreadyExistsException;
import com.PDF.model.User;
import com.PDF.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
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
        return userSessionService.checkCredentialsAndLogin(credentials, response);
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

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public void logout() {
        userSessionService.logout();
    }

    @RequestMapping(value="/user/authenticate", method = RequestMethod.GET)
    public Principal user(Principal user) {
        System.out.println("Principal:" + user.getName() + user.toString());
        return user;
    }
}