package com.reflexit.tastier.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.Person;
import com.reflexit.tastier.database.entities.PersonFaces;
import com.reflexit.tastier.databinding.ActivityProfileBinding;

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
            getApplicationContext().repositories.insertTransaction(() -> {
                getApplicationContext().getDb().personDao().insert(person);
            });
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        });

    }
}
