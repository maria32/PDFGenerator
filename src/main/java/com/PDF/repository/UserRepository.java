package com.PDF.repository;

import com.PDF.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by martanase on 6/7/2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    User findByUsername(String username);

    User findByEmail(String email);
}
