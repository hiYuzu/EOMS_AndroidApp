package com.a712.emos_androidapp;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MyEomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        System.out.println("BaseActivity.onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
        Intent mainActivityIntent = new Intent(MyEomsActivity.this, LoginActivity.class);
        startActivity(mainActivityIntent);
        MyEomsActivity.this.finish();
    }

}
