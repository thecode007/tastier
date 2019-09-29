package com.reflexit.tastier.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.reflexit.tastier.database.entities.PersonFaces;


@Dao
public interface PersonFacesDao {

    @Query("SELECT * FROM Person")
    public PersonFaces loadAllPersonsWithFaces(long companyId);
}
