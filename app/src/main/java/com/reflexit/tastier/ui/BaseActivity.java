package com.reflexit.tastier.ui;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.reflexit.tastier.FaceAIApp;
import com.reflexit.tastier.database.MakeItTastyDB;


public class BaseActivity extends AppCompatActivity {


    protected LoaderDialog loaderDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (loaderDialog == null) {
            loaderDialog = new LoaderDialog(this, "Loading");

        }
    }


    @Override
    public FaceAIApp getApplicationContext() {
        return FaceAIApp.getInstance();
    }

    public MakeItTastyDB getDB() {
        return getApplicationContext().getDb();
    }

    protected void showLoader(String text) {
        if (loaderDialog == null) {
            loaderDialog = new LoaderDialog(this, "Loading");
        }
        loaderDialog.setText(text);
        if (loaderDialog.isShowing()) {
            return;
        }
        loaderDialog.show();
    }


    protected  void dismissLoader() {
        loaderDialog.dismiss();
    }
}
