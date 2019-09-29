package com.reflexit.tastier.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Food.class,
                parentColumns = "id", childColumns = "foodID"),
        @ForeignKey(entity = Person.class,
                parentColumns = "personId", childColumns = "personID")}
)
public class Comment {

    @PrimaryKey
    @ColumnInfo(name = "commentID")
    private int commentID;
    @ColumnInfo(name = "comment")
    private String comment;
    @ColumnInfo(name = "foodID")
    private String foodID;
    @ColumnInfo(name = "personID")
    private String personID;

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
