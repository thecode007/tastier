package com.reflexit.tastier.ui;

import android.content.Intent;
import android.os.Bundle;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.Food;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getApplicationContext().getDb().foodDao().getAll().observe(this, foods -> {
            if (foods == null || foods.size() >0){
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                return;
            }

            getApplicationContext().repositories.insertTransaction(() -> {
                Food[] foodsArr = new Food[4];
                Food food = new Food();
                food.setImage("https://cdn.pixabay.com/photo/2017/02/15/10/57/pizza-2068272__340.jpg");
                food.setName("Pizza");
                food.setPrice(15);
                foodsArr[0] = food;
                food = new Food();
                food.setImage("https://www.seriouseats.com/recipes/images/2017/03/20170224-fettucine-alfredo-vicky-wasik-8-1500x1125.jpg");
                food.setName("Fettuccine Alfredo");
                food.setPrice(10);
                foodsArr[1] = food;
                food = new Food();
                food.setImage("https://www.plated.com/uploads/culinary/recipe_image/file/69141/menu_small_tpQGuNy0T0igQG5UIKGD_Mushroom_Risotto_with_Mascarpone__Gruye_re__and_Chives_003_THUMB.jpg");
                food.setName("Rice Risotto");
                food.setPrice(9);
                foodsArr[2] = food;
                food = new Food();
                food.setImage("https://www.thespruceeats.com/thmb/dl-9Ul-g-RGABH0QScAt0k8FVdM=/4494x3000/filters:fill(auto,1)/recipe-for-three-cheese-ravioli-909235-hero-5c3800ca46e0fb000133eed7.jpg");
                food.setName("Ravioli");
                food.setPrice(11);
                foodsArr[3] = food;
                getDB().foodDao().insert(foodsArr);
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                finish();
            });
        });


    }
}
