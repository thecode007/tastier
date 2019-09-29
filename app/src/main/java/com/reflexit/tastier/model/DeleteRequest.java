package com.reflexit.tastier.model;

public class DeleteRequest {

    private String personGroupId;

    public DeleteRequest() {
        this.personGroupId = "";
    }

    public String getPersonGroupId() {
        return personGroupId;
    }

    public void setPersonGroupId(String personGroupId) {
        this.personGroupId = personGroupId;
    }
}
