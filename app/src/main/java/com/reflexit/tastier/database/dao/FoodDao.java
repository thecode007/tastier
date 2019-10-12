package com.reflexit.tastier.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.reflexit.tastier.database.entities.Food;
import java.util.List;

@Dao
public interface FoodDao {

    @Query("SELECT * FROM Food ")
    LiveData<List<Food>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Food... foods);

}
