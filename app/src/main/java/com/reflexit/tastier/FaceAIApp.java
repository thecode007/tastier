package com.reflexit.tastier;

import android.app.Application;
import com.reflexit.tastier.database.MakeItTastyDB;
import com.reflexit.tastier.ui.Repositories;
import com.reflexit.tastier.utils.FileUtils;
import java.util.Arrays;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;

public class FaceAIApp extends Application {

    private static OkHttpClient client;
    private MakeItTastyDB db;
    public Repositories repositories;
    private static FaceAIApp faceAIApp;

    @Override
    public void onCreate() {
        super.onCreate();
        client = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                .build();
        repositories = new Repositories(client);
        db = MakeItTastyDB.getDatabase(this);
        faceAIApp = this;
        FileUtils.createAppFileStructure();
    }


    public MakeItTastyDB getDb() {
        return db;
    }

    public static FaceAIApp getInstance() {
        if (faceAIApp == null) {
            faceAIApp = new FaceAIApp();
        }
        return faceAIApp;
    }

    private synchronized static OkHttpClient getFaceServiceClient() {
        return client;
    }

}