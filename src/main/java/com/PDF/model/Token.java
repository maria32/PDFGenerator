package com.PDF.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by martanase on 7/3/2017.
 */
@Entity
@Table(name = "token")
public class Token {

    @Id
    private String value;
    @NotNull
    private String username;

    public Token() {
    }

    public Token(String value, String username) {
        this.value = value;
        this.username = username;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
