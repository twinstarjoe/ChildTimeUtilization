package com.twinstar.childtimeutilization;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ParentInfoActivity extends AppCompatActivity {
    Button bDone;
    protected ChildInfoApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText et = (EditText) findViewById(R.id.ecParentName);
        final ChildDBHandler helper = ChildDBHandler.getInstance(this);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                         @Override
                                         public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
         if (actionId == EditorInfo.IME_ACTION_DONE) {
             app = ChildInfoApp.getInstance();
             EditText et = (EditText) findViewById(R.id.ecParentName);
             String strParentName = et.getText().toString();
             try {
                 if (helper.parentNameAlreadyExists(strParentName)) {
                     Toast.makeText(getApplicationContext(), "Parent name already exists. Try another name", Toast.LENGTH_LONG).show();
                     return false;
                 } else {
                     helper.addParent(strParentName);
                 }
             } catch (Exception e) {
                 int x = 1;
             }
             if (strParentName.equals("")) {
                 Toast.makeText(getApplicationContext(), "Parent name has not been entered. Please retry", Toast.LENGTH_LONG).show();
             } else
                 finish();
         }

         return false;
     }
 }

);

    }
}
