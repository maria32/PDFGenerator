package com.PDF.controller;

import com.PDF.model.User;
import com.PDF.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by martanase on 6/7/2017.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public User create(@RequestBody User user) { return userService.create(user);}

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User getOne(@PathVariable("id") Long id) { return userService.getOne(id); }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable("id") Long id) { userService.delete(id); }

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    @ResponseBody
//    public User checkCredentials(@RequestBody User user){
//        return userService.checkCredentials(user);
//    }

}
