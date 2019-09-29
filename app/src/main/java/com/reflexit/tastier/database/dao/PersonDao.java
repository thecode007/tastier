package com.reflexit.tastier.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.reflexit.tastier.database.entities.Person;
import com.reflexit.tastier.database.entities.PersonFaces;

import java.util.List;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM Person")
    LiveData<List<Person>> getAll();

    @Query("SELECT * FROM Person order by points desc")
    LiveData<List<PersonFaces>> getAllFaces();

    @Query("SELECT * FROM Person where personId= :id")
    LiveData<PersonFaces> getPerson(String id);

    @Query("SELECT * FROM Person where personId in (:ids)")
    LiveData<List<PersonFaces>> getPersons(List<String> ids);

    @Query("SELECT * FROM Person where personId= :id")
    LiveData<Person> getSinglePerson(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Person... persons);

}
