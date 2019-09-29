package com.reflexit.tastier.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    private static String rootDirectory = Environment.getExternalStorageDirectory() + "/MakeItTasty";
    public static String personDirectory = rootDirectory + "/Persons";

    public static void createAppFileStructure() {

        File file = new File(rootDirectory);
        if (!file.exists()) {
            if (file.mkdir()) {
                Log.wtf("File Manager ---->", "root directory created");
            }
            file = new File(personDirectory);
            if (file.mkdir()) {
                Log.wtf("File Manager ---->", "person directory created");
            }
        }
    }

    public static void savePersonImage(Bitmap bitmap, String personId, String faceID) {

        File file = new File(personDirectory + "/" + personId);
        if (!file.exists()) {
            file.mkdir();
        }
        try (FileOutputStream out = new FileOutputStream(personDirectory + "/" + personId + "/" + faceID + ".jpg")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            Log.wtf("File Manager ---->", "face saved!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
