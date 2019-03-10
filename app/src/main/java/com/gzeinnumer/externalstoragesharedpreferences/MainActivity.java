package com.gzeinnumer.externalstoragesharedpreferences;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.read)
    TextView read;
    @BindView(R.id.editNama)
    EditText editNama;

    private static final int PERMISION_WRITE_EXTERNAL_STORAGE = 123;
    String fileName = "myFile";
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn1, R.id.btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                int permitCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permitCheck == PackageManager.PERMISSION_GRANTED){
                    saveData();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISION_WRITE_EXTERNAL_STORAGE);
                }
                break;
            case R.id.btn2:
                readData();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISION_WRITE_EXTERNAL_STORAGE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    saveData();
                } else {
                    Toast.makeText(this, "Izinkan dulu!!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void saveData() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(),fileName);
            FileOutputStream fos = new FileOutputStream(file);
            name = editNama.getText().toString();
            fos.write(name.getBytes());
            fos.close();
            Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData() {
        try{
            File file = new File(Environment.getExternalStorageDirectory(),fileName);
            FileInputStream fin = new FileInputStream(file);
            InputStreamReader inputStream = new InputStreamReader(fin);
            BufferedReader buffered = new BufferedReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line=buffered.readLine())!=null){
                stringBuilder.append(line);
            }
            fin.close();
            inputStream.close();
            read.setText("Name= "+stringBuilder.toString());
            Toast.makeText(this, "Readed", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

