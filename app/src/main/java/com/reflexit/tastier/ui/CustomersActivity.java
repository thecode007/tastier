package com.reflexit.tastier.ui;


import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.reflexit.tastier.R;

public class CustomersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);


    }


    @Override
    protected void onResume() {
        super.onResume();
        getDB().personDao().getAllFaces().observe(this, personFaces -> {
            RecyclerView recyclerView = findViewById(R.id.rv_customers);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            recyclerView.setAdapter(new CustomersAdapter(this, personFaces));
        });
    }
}
