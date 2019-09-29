package com.reflexit.tastier.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Person.class,
                parentColumns = "personId", childColumns = "ownerID")})
public class FaceEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "faceID")
    private String faceID;

    @ColumnInfo(name = "ownerID")
    private String ownerID;

    public String getFaceID() {
        return faceID;
    }

    public void setFaceID(String faceID) {
        this.faceID = faceID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
}
