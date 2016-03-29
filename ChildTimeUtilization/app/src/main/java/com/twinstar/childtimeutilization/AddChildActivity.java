package com.twinstar.childtimeutilization;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

public class AddChildActivity extends AppCompatActivity {

    ChildInfo childInfo;

    Button btnRefresh;

    Spinner spParents;
    Spinner spChildren;
    TableRow trChildSpinner;
    EditText etFirstName;
    EditText etLastName;
    EditText etGender;
    EditText etDOB;
    protected ChildInfoApp app;
    int nEditOrCreate = 0;
    String strSelectedParent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_add);
        app = ChildInfoApp.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        trChildSpinner = (TableRow) findViewById(R.id.spRowChildName);
        etFirstName = (EditText) findViewById(R.id.firstName);
        etLastName = (EditText) findViewById(R.id.lastName);
        etGender = (EditText) findViewById(R.id.gender);
        etDOB = (EditText) findViewById(R.id.dob);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        spParents = (Spinner) findViewById(R.id.parentNames);
        spParents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        setupParentSpinner();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupParentSpinner() {
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
        spParents.setSelection(spParents.getCount() - 1);
        strSelectedParent = spParents.getSelectedItem().toString();

    }

    public void onDone(View v) {
        if (prepareAndWriteToChildInfoTable(true).equals(true)) {
            Toast.makeText(this, "SuccessFully added", Toast.LENGTH_SHORT).show();
            finish();
        } else
            Toast.makeText(this, "Not all fields have been entered. Please try again.", Toast.LENGTH_LONG).show();
    }


    private String makeChildName() {
        String strBeforeStrip = etFirstName.getText().toString() + app.getDelimiter() + etLastName.getText().toString();
        strBeforeStrip.replaceAll("\\s+", "");
        return strBeforeStrip;

    }


    private Boolean prepareAndWriteToChildInfoTable(Boolean bAdd) {

        if (spParents.getCount() == 0 || etFirstName.getText().toString().equals("")
                || etLastName.getText().toString().equals("")
                || etGender.getText().toString().equals("")
                || etDOB.getText().toString().equals("")) {
            return false;
        }
        // In any activity just pass the context and use the singleton method
        ChildDBHandler helper = ChildDBHandler.getInstance(this);
        helper.addOrUpdateChildInfo(makeChildInfo(), bAdd);
        return true;
    }

    private ChildInfo makeChildInfo() {

        ChildInfo ci = new ChildInfo(app.getCurrentId(), spParents.getSelectedItem().toString(), etFirstName.getText().toString(),
                etLastName.getText().toString(), etGender.getText().toString(), etDOB.getText().toString());

        return ci;
    }
}