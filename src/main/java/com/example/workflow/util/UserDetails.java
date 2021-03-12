package com.example.workflow.util;

import java.io.Serializable;

public class UserDetails implements Serializable
{
    private static final long serialVersionUID = 1L;
    String firstName;
    String secondName;
    String email;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public void setSecondName(final String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}