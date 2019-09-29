package com.reflexit.tastier.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.reflexit.tastier.database.entities.Comment;
import java.util.List;

@Dao
public interface CommentDao {

    @Query("SELECT * FROM Comment ")
    LiveData<List<Comment>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment... comments);
}
