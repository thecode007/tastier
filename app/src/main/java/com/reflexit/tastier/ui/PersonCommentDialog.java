package com.reflexit.tastier.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.reflexit.tastier.FaceAIApp;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.entities.Comment;
import com.reflexit.tastier.database.entities.Food;
import com.reflexit.tastier.database.entities.PersonFaces;
import com.reflexit.tastier.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class PersonCommentDialog extends Dialog {

    private Food food;
    private PersonFaces personFaces;
    Context context;
    private CommentAdapter commentAdapter = null;
    public PersonCommentDialog(@NonNull Context context, Food food) {
        super(context);
        this.food = food;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_person_comment);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        SimpleDraweeView sp_food= findViewById(R.id.sp_food);
        sp_food.setImageURI(food.getImage());
        ImageView persImageView = findViewById(R.id.person);
        RecyclerView rvFaces = findViewById(R.id.persons);
        RecyclerView rvComments = findViewById(R.id.comments);

        rvComments.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        rvFaces.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL,
                false));
        List<PersonFacesSelector> personFacesSelectors = new ArrayList<>();
        LiveData<List<PersonFaces>> perListLiveData = FaceAIApp.getInstance().getDb().personDao().getAllFaces();
        perListLiveData.observe((LifecycleOwner) context, personFaces -> {
            for (PersonFaces person: personFaces) {
                PersonFacesSelector personFacesSelector = new PersonFacesSelector();
                personFacesSelector.setPersonFaces(person);
                personFacesSelectors.add(personFacesSelector);
            }
            rvFaces.setAdapter(new RadioAdapter(getContext(), personFacesSelectors, face -> {
                String imageDirectory = FileUtils.personDirectory + "/" +
                        face.getPerson().getPersonId() + "/"
                        + face.getFaces().get(0).getFaceID() + ".jpg";

                findViewById(R.id.btn_comment).setOnClickListener(view -> {
                    EditText etDetails = findViewById(R.id.et_details);
                    if (!TextUtils.isEmpty(etDetails.getText())) {
                        Comment comment = new Comment();
                        comment.setFoodID(food.getId());
                        comment.setComment(etDetails.getText().toString());
                        comment.setPersonID(face.getPerson().getPersonId());
                        FaceAIApp.getInstance().repositories.insertTransaction(() -> {
                            FaceAIApp.getInstance().getDb().commentDao().insert(comment);
                        });
                        commentAdapter.add(comment);
                    }
                });
                persImageView.setImageDrawable(Drawable.createFromPath(imageDirectory));
                this.personFaces = face;

                LiveData<List<Comment>> commentsLiveData = FaceAIApp.getInstance().getDb().
                        commentDao().
                        getAllCommentsByPersonFood(face.getPerson().
                                        getPersonId(),
                                food.getId());

                commentsLiveData.observe((LifecycleOwner) context, comments -> {
                    commentAdapter = new CommentAdapter(context, comments);
                    rvComments.setAdapter(commentAdapter);
                    commentsLiveData.removeObservers((LifecycleOwner) context) ;
                });
            }));
            perListLiveData.removeObservers((LifecycleOwner) context);
        });
    }
}
