package com.reflexit.tastier.ui;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.reflexit.tastier.model.Face;
import com.reflexit.tastier.model.FaceCandidate;
import com.reflexit.tastier.model.IdentifyRequest;
import com.reflexit.tastier.model.PersonRequest;
import com.reflexit.tastier.model.UserInfoBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import static com.reflexit.tastier.apiservice.ApiHelper.ADD_FACE;
import static com.reflexit.tastier.apiservice.ApiHelper.ADD_PERSON;
import static com.reflexit.tastier.apiservice.ApiHelper.DELETE_GROUP;
import static com.reflexit.tastier.apiservice.ApiHelper.DETECT;
import static com.reflexit.tastier.apiservice.ApiHelper.IDENTIFY;
import static com.reflexit.tastier.apiservice.ApiHelper.OUTH_KEY;
import static com.reflexit.tastier.apiservice.ApiHelper.TRAIN;

public class Repositories {

    private OkHttpClient client;
    private final MediaType JSON_MEDIA_TYPE
            = MediaType.parse("application/json; charset=utf-8");
    private final MediaType MEDIA_TYPE_PNG = MediaType.parse("application/octet-stream");
    private final String OCP_KEY = "ocp-apim-subscription-key";
    private final String PERSON_GROUP_ID = "tastebuddies";

    public Repositories(OkHttpClient client) {
        this.client = client;
    }

    LiveData<Face[]> getFacesFromUrl(String url) {

        Request request = new Request.Builder()
                .url(DETECT)
                .addHeader(OCP_KEY ,OUTH_KEY)
                .build();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);
            RequestBody body = RequestBody.create(jsonObject.toString(), JSON_MEDIA_TYPE);

            request = new Request.Builder()
                    .url(DETECT)
                    .addHeader(OCP_KEY ,OUTH_KEY)
                    .post(body)
                    .build();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getFace(request);
    }


    private LiveData<Face[]> getFace(Request request) {
        MutableLiveData<Face[]> faces = new MutableLiveData<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.wtf(getClass().getSimpleName(), e.getMessage());
                faces.postValue(new Face[0]);
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseString = response.body().string();
                Log.wtf(getClass().getSimpleName(), responseString);
                if (response.code() == 200 ) {
                    faces.postValue(new Gson().fromJson(responseString, Face[].class));
                }
            }
        });

        return faces;
    }


    LiveData<Face[]> getFacesFromBitMap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_PNG;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
               sink.write(byteArray);
            }

        };

        Request request = new Request.Builder()
                .url(DETECT)
                .addHeader(OCP_KEY ,OUTH_KEY)
                .post(requestBody)
                .build();

        return getFace(request);
    }


    LiveData<String> addFaceToPerson(Face face ,String personId ,Bitmap bitmap) {

        MutableLiveData<String> persistedId = new MutableLiveData<>();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        //manipulating url
        String url = ADD_FACE.replace("personId", personId)
                .replace("userData","userData=" + new Gson().toJson(new UserInfoBody()))
                .replace("targetFace", "targetFace=" + face.getFaceRectangle().getLeft() + "," + face.getFaceRectangle().getTop() +
                        "," + face.getFaceRectangle().getWidth() + "," + face.getFaceRectangle().getHeight());

        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_PNG;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.write(byteArray);
            }

        };

        Request request = new Request.Builder()
                .url(url)
                .addHeader(OCP_KEY ,OUTH_KEY)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    persistedId.postValue("Connection Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String persistedFaceId = jsonObject.getString("persistedFaceId");
                    if (!TextUtils.isEmpty(persistedFaceId)) {
                        persistedId.postValue(persistedFaceId);
                        return;
                    }
                    persistedId.postValue(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return persistedId;
    }



    LiveData<String> addPerson(String name, UserInfoBody userInfoBody) {

        PersonRequest personRequest = new PersonRequest();
        personRequest.setName(name);
        MutableLiveData<String> persistedId = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(new Gson().toJson(personRequest), JSON_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(ADD_PERSON)
                .addHeader(OCP_KEY ,OUTH_KEY)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String personId = new JSONObject(response.body().string()).getString("personId");
                    if (!TextUtils.isEmpty(personId)) {
                        persistedId.postValue(personId);

                    }
                    else {
                        persistedId.postValue("error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return persistedId;
    }


    LiveData<FaceCandidate[]> getFaceCandidate(Face[] faceIDs) {
        MutableLiveData<FaceCandidate[]> faces = new MutableLiveData<>();
        IdentifyRequest identifyRequest = new IdentifyRequest();
        identifyRequest.setConfidenceThreshold(0.5);
        identifyRequest.setMaxNumOfCandidatesReturned(1);
        identifyRequest.setPersonGroupId(PERSON_GROUP_ID);

        for (Face face : faceIDs) {
            identifyRequest.addFaceId(face.getFaceId());
        }

        RequestBody requestBody = RequestBody.create(new Gson().toJson(identifyRequest), JSON_MEDIA_TYPE);


        Request request = new Request.Builder()
                .url(IDENTIFY)
                .addHeader(OCP_KEY ,OUTH_KEY)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.wtf("Exception Tasty", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new Gson();
                String responseString = response.body().string();
                switch (response.code()) {

                    case 200:
                        faces.postValue(gson.fromJson(responseString, FaceCandidate[].class));
                        break;

                    default:
                        faces.postValue(new FaceCandidate[1]);
                }
            }
        });
        return faces;
    }


    public LiveData<Integer> train() {

        MutableLiveData<Integer> status = new MutableLiveData<>();

        Request request = new Request.Builder()
                .url(TRAIN)
                .addHeader(OCP_KEY ,OUTH_KEY)
                .post(new RequestBody() {
                    @Nullable
                    @Override
                    public MediaType contentType() {
                        return JSON_MEDIA_TYPE;
                    }

                    @Override
                    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
                    }
                })
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.wtf("Exception Tasty", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                status.postValue(response.code());
            }
        });

        return status;
    }




    public void insertTransaction (CallBack callBack) {

        new InsertTransactionsAsyncTask(callBack).execute();

    }

    private static class InsertTransactionsAsyncTask extends AsyncTask<Void, Void, Void> {

        CallBack callBack;
        InsertTransactionsAsyncTask(CallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            callBack.onCallBack();
            return null;
        }
    }


    void deleteAllPersons() {

        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_PNG;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
            }

        };

        Request request = new Request.Builder()
                .url(DELETE_GROUP)
                .addHeader(OCP_KEY ,OUTH_KEY)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                response.code();
            }
        });
    }





    public interface CallBack<T>{
        void onCallBack();
    }



}
