package com.reflexit.tastier.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class CommentFoods {

    @Embedded
    private Comment comment;


    @Relation(parentColumn = "personID", entityColumn = "personId", entity = Person.class)
    private List<Person> person;

    @Relation(parentColumn = "foodID", entityColumn = "id", entity = Food.class)
    private  List<Food> food;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public List<Person> getPerson() {
        return person;
    }

    public void setPerson(List<Person> person) {
        this.person = person;
    }

    public List<Food> getFood() {
        return food;
    }

    public void setFood(List<Food> food) {
        this.food = food;
    }
}
