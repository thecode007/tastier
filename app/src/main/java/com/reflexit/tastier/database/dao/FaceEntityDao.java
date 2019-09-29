package com.reflexit.tastier.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.reflexit.tastier.database.entities.FaceEntity;

import java.util.List;

@Dao
public interface FaceEntityDao {

    @Query("SELECT * FROM FaceEntity ")
    LiveData<List<FaceEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FaceEntity... faceEntities);
}
