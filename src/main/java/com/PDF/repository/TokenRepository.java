package com.PDF.repository;

import com.PDF.model.Token;
import com.PDF.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by martanase on 7/3/2017.
 */
@Repository
public interface TokenRepository extends CrudRepository<Token, String> {


}
