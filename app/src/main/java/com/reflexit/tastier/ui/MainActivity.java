package com.reflexit.tastier.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.reflexit.tastier.R;
import com.reflexit.tastier.database.MakeItTastyDB;
import com.reflexit.tastier.database.entities.FaceEntity;
import com.reflexit.tastier.database.entities.Person;
import com.reflexit.tastier.database.entities.PersonFaces;
import com.reflexit.tastier.model.Candidate;
import com.reflexit.tastier.model.Face;
import com.reflexit.tastier.model.FaceCandidate;
import com.reflexit.tastier.model.MainMenuSelector;
import com.reflexit.tastier.model.UserInfoBody;
import com.reflexit.tastier.utils.FileUtils;
import com.reflexit.tastier.utils.ImageHelper;
import com.reflexit.tastier.utils.TimeUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 100;
    private List<String> personFacesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personFacesList = new ArrayList<>();
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rv_main_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ArrayList<MainMenuSelector> mainMenuSelectors = new ArrayList<>();
        mainMenuSelectors.add(new MainMenuSelector(R.drawable.capture, getString(R.string.customer_detection), v -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        {
                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
                        }
                        else
                        {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, MY_CAMERA_PERMISSION_CODE);
                        }
                    }
                })
        );

        mainMenuSelectors.add(new MainMenuSelector(R.drawable.complaints, getString(R.string.customer_profile), v -> {
                   startActivity(new Intent(this, CustomersActivity.class));
                })
        );


        mainMenuSelectors.add(new MainMenuSelector(R.drawable.foods, getString(R.string.foods), v -> {
                    startActivity(new Intent(this, FoodActivity.class));
                })
        );

        recyclerView.setAdapter(new MainMenuAdapter(this, mainMenuSelectors));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, getString(R.string.camera_permission), Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, getString(R.string.camera_permission), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            showLoader("Training Data...");
            getApplicationContext().repositories.train().observe(this, integer -> {
                personFacesList.clear();
                if (data.getExtras() != null && data.getExtras().get("data") != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    showLoader("Uploading the photo...");
                    LiveData<Face[]> facesArray = getApplicationContext().repositories.getFacesFromBitMap(photo);
                    facesArray.observe(MainActivity.this, faces -> {
                        if (faces != null && faces.length != 0) {
                            showLoader(getString(R.string.search_for_faces));
                            LiveData<FaceCandidate[]> faceCandidateMutableLiveData = getApplicationContext().repositories.getFaceCandidate(faces);
                            faceCandidateMutableLiveData.observe(MainActivity.this, faceCandidates -> {
                                showLoader("Analyzing Face/s  Candidates...");
                                StringBuilder personFound = new StringBuilder();
                                MutableLiveData<Integer> indexLiveData = new MutableLiveData();
                                indexLiveData.postValue(0);
                                indexLiveData.observe(MainActivity.this, index -> {
                                    if (index >= faceCandidates.length){
                                        if (personFacesList.size() > 0) {
                                                LiveData<List<PersonFaces>> perListLiveData = MakeItTastyDB
                                                        .getDatabase(MainActivity.this).personDao().getPersons(personFacesList);
                                                perListLiveData.observe(MainActivity.this, personFaces -> {
                                                        IdentificationDialog identificationDialog = new IdentificationDialog(MainActivity.this
                                                                ,personFaces);
                                                        identificationDialog.show();
                                                    perListLiveData.removeObservers(MainActivity.this);
                                                });

                                        }
                                        indexLiveData.removeObservers(MainActivity.this);
                                        return;
                                    }
                                    Face currentFace = faces[index];
                                    FaceCandidate faceCandidate = faceCandidates[index];
                                    if (faceCandidate != null && faceCandidate.getCandidates() != null && faceCandidate.getCandidates().size() > 0 ) {
                                        Candidate candidate = faceCandidate.getCandidates().get(0);
                                        personFound.append(candidate.getPersonId()).append("\n");
                                        getApplicationContext().repositories.addFaceToPerson(currentFace, candidate.getPersonId(), photo)
                                                .observe(MainActivity.this, s -> {
                                                    Toast.makeText(getApplicationContext(),"Identified" + candidate.getPersonId(), Toast.LENGTH_LONG).show();
                                                    try {
                                                        Bitmap thumbnail = ImageHelper.generateFaceThumbnail(photo, currentFace.getFaceRectangle());
                                                        showLoader("Adding Faces To the person...");
                                                        LiveData<String> faceLiveData =  getApplicationContext().repositories.addFaceToPerson(currentFace, candidate.getPersonId(), photo);
                                                        faceLiveData.observeForever(faceId -> {
                                                            LiveData<Person> personLiveData = getApplicationContext().getDb().personDao().getSinglePerson(candidate.getPersonId());
                                                            personLiveData.observe(MainActivity.this,person -> {
                                                                double totalPoints;
                                                                Calendar calendar = Calendar.getInstance();
                                                                if (person == null) {
                                                                    person = new Person();
                                                                    person.setPersonId(candidate.getPersonId());
                                                                    totalPoints = 1d;
                                                                }
                                                                else {
                                                                    totalPoints = person.getPoints() + 1;
                                                                }
                                                                person.setPoints(totalPoints);
                                                                if (person.getLastVisit() == 0f) {

                                                                    person.setRank("Low");
                                                                }
                                                                else {
                                                                    int daysDifference = TimeUtils.daysDifference(calendar.getTimeInMillis());

                                                                    if (daysDifference < 3f) {
                                                                        person.setRank("High");
                                                                    }
                                                                    else if (daysDifference < 7f) {
                                                                        person.setRank("Medium");
                                                                    }
                                                                    else if (daysDifference < 30f) {
                                                                        person.setRank("Low");
                                                                    }
                                                                    else if (daysDifference > 30f) {
                                                                        person.setRank("Extremely Low");
                                                                    }
                                                                }
                                                                person.setLastVisit(calendar.getTimeInMillis());
                                                                Person finalPerson = person;
                                                                getApplicationContext().repositories.insertTransaction(() -> {
                                                                    getApplicationContext().getDb().personDao().insert(finalPerson);
                                                                    FileUtils.savePersonImage(thumbnail, candidate.getPersonId(), faceId);
                                                                    FaceEntity faceEntity = new FaceEntity();
                                                                    faceEntity.setFaceID(faceId);
                                                                    faceEntity.setOwnerID(candidate.getPersonId());
                                                                    getApplicationContext().getDb().faceEntityDao().insert(faceEntity);
                                                                    personFacesList.add(candidate.getPersonId());
                                                                    indexLiveData.postValue(index + 1);

                                                                });
                                                                personLiveData.removeObservers(MainActivity.this);
                                                                faceLiveData.removeObservers(MainActivity.this);
                                                                dismissLoader();
                                                            });
                                                        });
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                });
                                    }
                                    else {
                                        showLoader("Adding the person...");
                                        LiveData<String> personIdData = getApplicationContext().repositories.addPerson("Unknown", new UserInfoBody());
                                        personIdData.observe(MainActivity.this, personId -> {
                                            try {
                                                Person person = new Person();
                                                person.setPersonId(personId);
                                                Calendar calendar = Calendar.getInstance();
                                                person.setLastVisit(calendar.getTimeInMillis());
                                                person.setPoints(1d);
                                                person.setRank("Low");
                                                Bitmap thumbnail = ImageHelper.generateFaceThumbnail(photo, currentFace.getFaceRectangle());
                                                LiveData<String> faceLiveData = getApplicationContext().repositories.addFaceToPerson(currentFace, personId, photo);
                                                faceLiveData.observe(MainActivity.this, faceId -> {
                                                    getApplicationContext().repositories.insertTransaction(() -> {
                                                        getApplicationContext().getDb().personDao().insert(person);
                                                        FileUtils.savePersonImage(thumbnail, personId, faceId);
                                                        FaceEntity faceEntity = new FaceEntity();
                                                        faceEntity.setOwnerID(personId);
                                                        faceEntity.setFaceID(faceId);
                                                        getApplicationContext().getDb()
                                                                .faceEntityDao().insert(faceEntity);
                                                    });
                                                    faceLiveData.removeObservers(MainActivity.this);
                                                    personIdData.removeObservers(MainActivity.this);
                                                    faceCandidateMutableLiveData.removeObservers(MainActivity.this);
                                                    dismissLoader();
                                                    personFacesList.add(personId);
                                                    indexLiveData.postValue(index + 1);
                                                });
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        });
                                    }
                                });

                            });
                            return;
                        }
                        dismissLoader();
                        Toast.makeText(getApplicationContext(), getString(R.string.no_face), Toast.LENGTH_LONG).show();
                        facesArray.removeObservers(MainActivity.this);
                    });
                }
            });

        }
    }

}

