package com.example.chase.basiclists;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chase.basiclists.control.ListFile;
import com.example.chase.basiclists.control.PublicListFile;

public class BasicListActivity extends AppCompatActivity implements View.OnClickListener {

    private ListFile fileToView;
    private boolean newFile;

    private EditText etxtFileName, etxtFile;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_activitiy);

        String fileName, folder;
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                fileName = null;
                folder = null;
            } else {
                fileName = extras.getString("fileName");
                folder = extras.getString("fileFolder");
                newFile = extras.getBoolean("NewList");
                if(!newFile)
                    fileToView = new PublicListFile(this,fileName, folder);
                else
                    fileToView = new PublicListFile(this, null, null);
            }



        }
        initializeControls();

    }

    private void initializeControls()
    {
        etxtFileName = (EditText)findViewById(R.id.etxtFileName);
        if (!newFile)
        {
            etxtFileName.setText(fileToView.getFileName().substring(0, fileToView.getFileName().lastIndexOf('.')));
            etxtFileName.setEnabled(false);
        }
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }

        };
        etxtFileName.setFilters(new InputFilter[] {filter});

        etxtFile = (EditText)findViewById(R.id.etxtFile);
        if (!newFile)
            etxtFile.setText(fileToView.readFile());

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave)
        {
            if(newFile)
                fileToView = new PublicListFile(this, etxtFileName.getText().toString(), null);
            if(fileToView.saveFile(etxtFile.getText().toString()))
                finish();
            else
                Toast.makeText(this, "FILE NOT SAVED", Toast.LENGTH_SHORT).show();
        }
        if(v.getId() == R.id.btnCancel)
        {
            if (etxtFile.getText().toString().length() > 0 || etxtFileName.getText().toString().length() > 0)
            {
                new AlertDialog.Builder(this)
                        .setTitle("Discard")
                        .setMessage("Do you really want to discard the list")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
            else
            {
                finish();
            }
        }
        if(v.getId() ==R.id.etxtFileName )
        {

        }
    }

    private void finishAct()
    {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
