
package com.twinstar.childtimeutilization;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

public class EditChildActivity extends AppCompatActivity {

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
    String strSelectedParent = "";
    String strSelectedChild = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_edit);
        app = ChildInfoApp.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        trChildSpinner = (TableRow) findViewById(R.id.spRowChildName);
        etFirstName = (EditText) findViewById(R.id.firstName);
        etLastName = (EditText) findViewById(R.id.lastName);
        etGender = (EditText) findViewById(R.id.gender);
        etDOB = (EditText) findViewById(R.id.dob);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        // Parent dropdown combo must be loaded before child ddc for edit to work
        spParents = (Spinner) findViewById(R.id.parentNames);
        spParents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                strSelectedParent = spParents.getSelectedItem().toString();
                setupChildSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spChildren = (Spinner) findViewById(R.id.childNames);
        spChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String strSplitName = parentView.getItemAtPosition(position).toString();
                String[] names = new String[2];
                names = strSplitName.split(",");
                etFirstName.setText(names[0]);
                etLastName.setText(names[1].trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        Toast.makeText(this, "select the child's first and last name, and click Refresh to retrieve additional data", Toast.LENGTH_LONG).show();
        setupParentSpinner();
        setupChildSpinner();
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
                    Log.v("EDITCI", "parent = " + cursor.getString(parentIndex));
                } while (cursor.moveToNext());
            }
        } finally {
            helper.closeDB();
        }
        spParents.setSelection(0);
        strSelectedParent = spParents.getSelectedItem().toString();

    }

    private void setupChildSpinner() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChildren.setAdapter(adapter);
        ChildDBHandler helper = ChildDBHandler.getInstance(this);
        String strSplitName;
        String[] names;
        try {
            Cursor cursor = helper.getChildren(strSelectedParent);
            Log.v("EDITCI", "current strSelectedParent = " + strSelectedParent);
            int childIndex = cursor.getColumnIndexOrThrow(ChildDBHandler.COLUMN_CHILD_NAME);
            if (cursor.moveToFirst()) {
                do {
                    strSplitName = cursor.getString(childIndex);
                    names = strSplitName.split(app.getDelimiter());
                    Log.v("EDITCI", "current child  = " + names[0] + " , " + names[1]);
                    adapter.add(names[0] + " , " + names[1]);
                 } while (cursor.moveToNext());
            }
        } finally {
            helper.closeDB();
        }
     }

    public void onDone(View v) {

        // passes zero so an update is done instead of an insert, which is 1.
        if (prepareAndWriteToChildInfoTable(false).equals(true)) {
            Toast.makeText(this, "SuccessFully replaced", Toast.LENGTH_SHORT).show();
            finish();
        } else
            Toast.makeText(this, "Not all fields have been entered. Please try again.", Toast.LENGTH_LONG).show();
    }

    public void onRefresh(View v) {
        ChildDBHandler helper = ChildDBHandler.getInstance(this);
        ChildInfo ci = helper.findChildInfo(makeChildName(), spParents.getSelectedItem().toString());
        // So, if the child doesn't exist, null is returned, so put up toast
        if (ci == null) {
            Toast.makeText(this, "This child doesn't yet exist. Go to add a child", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        app.setCurrentId(ci.getId());
        etFirstName.setText(ci.getChildFirstName());
        etLastName.setText(ci.getChildLastName());
        etGender.setText(ci.getMale());
        etDOB.setText(ci.getDate());

        // So, after we refresh, the new values are DOB and gender are retrieved and editable.
        // To keep the integrity of this table row, the names are not allowed to be changed.
        // If the intent is to make another child, they need to click Done and return to the NavigationView to do the new add.
        // Because of this, the name fields are being set to non editable ...
        etFirstName.setEnabled(false);
        etLastName.setEnabled(false);

    }

    private String makeChildName() {
        String strBeforeStrip = etFirstName.getText().toString() + app.getDelimiter() + etLastName.getText().toString();
        return strBeforeStrip = strBeforeStrip.replaceAll("\\s+", "");

    }


    private Boolean prepareAndWriteToChildInfoTable(Boolean bAdd) {

        Log.v("EDITCI", "top of  prepareAndWriteToChildInfoTable" + bAdd);
        if (spParents.getCount() == 0 || etFirstName.getText().toString().equals("")
                || etLastName.getText().toString().equals("")
                || etGender.getText().toString().equals("")
                || etDOB.getText().toString().equals("")) {
            return false;
        }
        // In any activity just pass the context and use the singleton method
        ChildDBHandler helper = ChildDBHandler.getInstance(this);
        helper.addOrUpdateChildInfo(makeChildInfo(), bAdd);
        Log.v("EDITCI", "bottom of  prepareAndWriteToChildInfoTable" + bAdd);
        return true;
    }

    private ChildInfo makeChildInfo() {
        String strParent = spParents.getSelectedItem().toString();
        ChildInfo ci = new ChildInfo(app.getCurrentId(), strParent, etFirstName.getText().toString(),
                etLastName.getText().toString(), etGender.getText().toString(), etDOB.getText().toString());

        return ci;
    }
}