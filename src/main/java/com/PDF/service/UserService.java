package com.PDF.service;

import com.PDF.model.User;

/**
 * Created by martanase on 6/7/2017.
 */
public interface UserService {

    User create(User user);

    User getOne(Long id);

    void delete(Long id);

    User checkCredentials(User user);
}
