package com.PDF.service;

import com.PDF.model.User;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by martanase on 6/5/2017.
 */
public interface UserSessionService {

    User checkCredentialsAndLogin(User credentials, HttpServletResponse response) throws Exception;

    void logout();

    boolean isRememberMeAuthenticated();

    String getUsername();

    String createUserAndLogin(User credentials, HttpServletResponse response) throws Exception;
}
