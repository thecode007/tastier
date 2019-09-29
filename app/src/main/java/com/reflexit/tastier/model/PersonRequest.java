package com.reflexit.tastier.model;

public class PersonRequest {

    private String name;
    private UserInfoBody userData;

    public PersonRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserInfoBody getUserInfoBody() {
        return userData;
    }

    public void setUserInfoBody(UserInfoBody userInfoBody) {
        this.userData = userInfoBody;
    }
}
