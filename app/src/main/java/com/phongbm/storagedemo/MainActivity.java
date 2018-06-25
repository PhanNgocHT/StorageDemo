package com.phongbm.storagedemo;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_PERMISSION = 1000;

    private FileManager fileManager;

    private SharedPreferences pref;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
                if (key.equals("count")) {
                    Toast.makeText(MainActivity.this,
                            String.valueOf(pref.getInt(key, 0)),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        };

        pref.registerOnSharedPreferenceChangeListener(listener);

        /*
        getDataFromAssets();
        fileManager = new FileManager();
        fileManager.showAllFile(fileManager.getRootPath());
        fileManager.writeData();
        */

        // saveDataToLocal();
        getDataFromLocal();
    }

    @Override
    protected void onDestroy() {
        pref.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
    }

    private void initializeComponents() {
        findViewById(R.id.btn_count).setOnClickListener(this);
    }

    private void getDataFromAssets() {
        try {
            AssetManager manager = getAssets();
            InputStream is = manager.open("numbers.txt");

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int sum = 0;
            String line;
            while ((line = br.readLine()) != null) {
                sum += Integer.parseInt(line);
            }

            Toast.makeText(this, "Result: " + sum, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        REQUEST_CODE_PERMISSION
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED
                    || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
    }

    private void saveDataToLocal() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        // pref = getSharedPreferences("settings", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("has_sound", true);

        String username = "t3h";
        String password = "a@123456";
        String username1 = "t3h";
        String password2 = "a@123456";
        editor.putString(username, password);
        editor.putString(username1, password2);

        editor.apply();
    }

    private void getDataFromLocal() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        boolean hasSound = pref.getBoolean("has_sound", false);
        Toast.makeText(this, String.valueOf(hasSound), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_count:
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("count", pref.getInt("count", 0) + 1);
                editor.apply();
                break;

            default:
                break;
        }
    }

}