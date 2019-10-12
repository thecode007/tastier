package com.reflexit.tastier.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.reflexit.tastier.FaceAIApp;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.Person;
import com.reflexit.tastier.database.entities.PersonFaces;
import com.reflexit.tastier.utils.TimeUtils;
import java.util.ArrayList;
import java.util.List;

public class IdentificationDialog  extends Dialog implements RadioAdapter.OnPictureSelectedListener {

    private List<PersonFaces> personFaces;
    public IdentificationDialog(@NonNull Context context, List<PersonFaces> personFaces) {
        super(context);
        this.personFaces = personFaces;
    }

    private RecyclerView rvFaces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_identification);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        rvFaces = findViewById(R.id.rv_faces);
        rvFaces.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL,
                false));
        List<PersonFacesSelector> personFacesSelectors = new ArrayList<>();
        for (PersonFaces person: personFaces) {
            PersonFacesSelector personFacesSelector = new PersonFacesSelector();
            personFacesSelector.setPersonFaces(person);
            personFacesSelectors.add(personFacesSelector);
        }
        rvFaces.setAdapter(new RadioAdapter(getContext(), personFacesSelectors, this));
    }

    @Override
    public void onPictureSelected(PersonFaces personFaces) {
        Person person = personFaces.getPerson();
        TextView tvId = findViewById(R.id.tv_id);
        TextView tvRank = findViewById(R.id.tv_rank);
        TextView tvLastVisit = findViewById(R.id.tv_last_visit);
        TextView etName = findViewById(R.id.et_name);
        TextView etMobile = findViewById(R.id.et_mobile);
        TextView etEmail = findViewById(R.id.et_email);
        tvId.setText(personFaces.getPerson().getPersonId());
        tvLastVisit.setText(TimeUtils.dateDifference(personFaces.getPerson().getLastVisit()));
        etName.setText(personFaces.getPerson().getName());
        etMobile.setText(personFaces.getPerson().getMobileNumber());
        etEmail.setText(personFaces.getPerson().getEmail());
        tvRank.setText(personFaces.getPerson().getRank());
        findViewById(R.id.ll_form).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_update).setOnClickListener(view -> {
            person.setName(etName.getText().toString());
            person.setEmail(etEmail.getText().toString());
            person.setMobileNumber(etMobile.getText().toString());
            FaceAIApp.getInstance().repositories.insertTransaction(() -> {
                FaceAIApp.getInstance().getDb().personDao().insert(person);
            });

            if (rvFaces.getAdapter().getItemCount() == 1) {
                dismiss();
            }
        });

    }



}
