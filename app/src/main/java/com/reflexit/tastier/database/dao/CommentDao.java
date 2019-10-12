package com.reflexit.tastier.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.reflexit.tastier.database.entities.Comment;
import com.reflexit.tastier.database.entities.CommentFoods;

import java.util.List;

@Dao
public interface CommentDao {

    @Query("SELECT * FROM Comment ")
    LiveData<List<Comment>> getAll();

    @Query("SELECT * FROM Comment where personID = :id")
    LiveData<List<CommentFoods>> getAllCommentsPerUser(String id);

    @Query("SELECT * FROM Comment where personID = :personId and foodID = :foodId")
    LiveData<List<Comment>> getAllCommentsByPersonFood(String personId, int foodId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment... comments);
}
