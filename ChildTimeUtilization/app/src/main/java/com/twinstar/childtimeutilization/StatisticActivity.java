package com.twinstar.childtimeutilization;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupSpinners();
    }

    private void setupSpinners() {
        Spinner spParents = (Spinner) findViewById(R.id.parentNames);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spParents.setAdapter(adapter);
        ChildDBHandler helper = ChildDBHandler.getInstance(this);
        try {
            Cursor cursor = helper.getParents();
            int parentIndex = cursor.getColumnIndexOrThrow(ChildDBHandler.COLUMN_PARENT_NAME);
            if (cursor.moveToFirst()) {
                do {
                    adapter.add(cursor.getString(parentIndex));
                } while (cursor.moveToNext());
            }
        } finally {
            helper.closeDB();
        }
    }

    public void onDone(View v) {
        finish();
    }
}
