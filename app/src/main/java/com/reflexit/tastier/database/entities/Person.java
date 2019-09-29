package com.reflexit.tastier.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity
public class Person {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "personId")
    private String personId;
    @ColumnInfo(name = "personName")
    private String name;
    @ColumnInfo(name = "points")
    private double points;
    @ColumnInfo(name = "mobileNumber")
    private String mobileNumber;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "last_visit")
    private long lastVisit;
    @ColumnInfo(name = "rank")
    private String rank;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public long getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(long date) {
        lastVisit = date;
    }

    public Person() {
        name = "Unknown";
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(@NotNull String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String personName) {
        this.name = personName;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
