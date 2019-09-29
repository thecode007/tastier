package com.reflexit.tastier.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.dao.CommentDao;
import com.reflexit.tastier.database.dao.FaceEntityDao;
import com.reflexit.tastier.database.dao.FoodDao;
import com.reflexit.tastier.database.dao.PersonDao;
import com.reflexit.tastier.database.entities.Comment;
import com.reflexit.tastier.database.entities.FaceEntity;
import com.reflexit.tastier.database.entities.Food;
import com.reflexit.tastier.database.entities.Person;

@Database(entities = {Food.class, Person.class, FaceEntity.class, Comment.class}, version = 1,
          exportSchema = false)
public abstract class MakeItTastyDB extends RoomDatabase {

    private static MakeItTastyDB instance;
    public abstract PersonDao personDao();
    public abstract FoodDao foodDao();
    public abstract CommentDao commentDao();
    public abstract FaceEntityDao faceEntityDao();

    public static MakeItTastyDB getDatabase(Context context) {
        if (instance == null) {
            synchronized (MakeItTastyDB.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            MakeItTastyDB.class ,
                            context.getString(R.string.app_name) + "_database").build();
                }
            }
        }
        return instance;
    }




}
