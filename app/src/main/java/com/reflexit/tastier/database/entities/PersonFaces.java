package com.reflexit.tastier.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

public class PersonFaces {

    @Embedded
    private Person person;
    @Relation(parentColumn = "personId", entityColumn = "ownerID", entity = FaceEntity.class)
    private List<FaceEntity> faces;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<FaceEntity> getFaces() {
        return faces;
    }

    public void setFaces(List<FaceEntity> faces) {
        this.faces = faces;
    }
}
