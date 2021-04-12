package com.example.sharedpreferencetask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);

        Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {
            editText.setText(cursor.getString(0));
        }
        cursor.close();
    }

    public void updateValue(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyContentProvider.PREFERENCES_NAME, editText.getText().toString());
        getContentResolver().update(MyContentProvider.CONTENT_URI, contentValues, null, null);
    }
}