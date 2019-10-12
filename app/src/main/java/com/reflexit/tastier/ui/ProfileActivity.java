package com.reflexit.tastier.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.google.gson.Gson;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.Comment;
import com.reflexit.tastier.database.entities.CommentFoods;
import com.reflexit.tastier.database.entities.Person;
import com.reflexit.tastier.database.entities.PersonFaces;
import com.reflexit.tastier.databinding.ActivityProfileBinding;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class ProfileActivity extends BaseActivity {

    public static Intent newIntent(Context context, PersonFaces personFaces) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(Person.class.getName(), new Gson().toJson(personFaces));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding profileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        String personJson = getIntent().getExtras().getString(Person.class.getName());
        PersonFaces personFaces = new Gson().fromJson(personJson, PersonFaces.class);
        ProfileViewModel profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileBinding.setViewmodel(profileViewModel);
        profileViewModel.setPerson(personFaces.getPerson());
        if (personFaces.getFaces().size() < 2) {
            profileBinding.rvPersonImages.setLayoutManager(new GridLayoutManager(this, personFaces.getFaces().size()));
        }
        else {
            profileBinding.rvPersonImages.setLayoutManager(new GridLayoutManager(this,3));
        }

        FacesAdapter facesAdapter = new FacesAdapter(this, personFaces.getFaces());
        profileBinding.rvPersonImages.setAdapter(facesAdapter);
        profileBinding.btnUpdate.setOnClickListener(view -> {
            if (TextUtils.isEmpty(profileViewModel.name.getValue())) {
                profileViewModel.name.setValue("Unknown");
            }
            Person person = personFaces.getPerson();
            person.setName(profileViewModel.name.getValue());
            person.setEmail(profileViewModel.email.getValue());
            person.setMobileNumber(profileViewModel.mobile.getValue());
            person.setRank(profileViewModel.rank.getValue());
            getApplicationContext().repositories.insertTransaction(() -> {
                getApplicationContext().getDb().personDao().insert(person);
            });
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        });


        LiveData<List<CommentFoods>> cListLiveData = getDB().commentDao().getAllCommentsPerUser(personFaces.getPerson().getPersonId());

        cListLiveData.observe(this, foods -> {
            RecyclerView recyclerView = findViewById(R.id.rv_person_comments);
            PersonFoodCommentAdapter personFoodCommentAdapter = new PersonFoodCommentAdapter(this, foods);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            recyclerView.setAdapter(personFoodCommentAdapter);
        });

        ExpandableLayout expandableLayoutImages = findViewById(R.id.expandable_layout_images);
        ExpandableLayout expandableLayoutComments = findViewById(R.id.expandable_layout_comments);

        findViewById(R.id.tv_gallery).setOnClickListener(view -> {
            if (expandableLayoutImages.isExpanded()){
                expandableLayoutImages.collapse(true);
            }
            else {
                expandableLayoutImages.expand(true);
                expandableLayoutComments.collapse(true);
            }

        });

        findViewById(R.id.tv_comments).setOnClickListener(view -> {
            if (expandableLayoutComments.isExpanded()){
                expandableLayoutComments.collapse(true);
            }
            else {
                expandableLayoutComments.expand(true);
                expandableLayoutImages.collapse(true);
            }
        });
    }
}
