package com.reflexit.tastier.ui;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.Food;

import java.util.List;

public class FoodActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        RecyclerView recyclerView = findViewById(R.id.rv_food_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
       LiveData<List<Food>> foodLiveData =  getDB().foodDao().getAll();

       foodLiveData.observe(this, foods -> {
           recyclerView.setAdapter(new FoodAdapter(this, foods));
           foodLiveData.removeObservers(this);
       });;
    }
}
