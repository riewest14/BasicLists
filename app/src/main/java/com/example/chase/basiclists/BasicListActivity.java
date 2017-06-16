package com.example.chase.basiclists;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chase.basiclists.control.BasicListFile;

public class BasicListActivity extends BasicListActivitySuperclass {

    private Object fileContainer;
    private EditText etxtFile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_view_list_activitiy);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeList(String fileName, String folder)
    {
        etxtFile = (EditText)findViewById(R.id.etxtFile);
        if(!newFile) {
            fileToView = new BasicListFile(this, fileName, folder);
            etxtFile.setText(fileToView.readFile());
            startSize = etxtFile.getText().length();
            startName = fileName;
        }
        else {
            startSize = 0;
            startName = "";
            fileToView = new BasicListFile(this, null, null);
        }
    }

    @Override
    protected void save()
    {
        if(startSize == etxtFile.getText().length() && startName.equals(etxtFileName.getText().toString())) {
            finish();
            return;
        }
        if(newFile)
            fileToView = new BasicListFile(this, etxtFileName.getText().toString(), null);
        if(fileToView.saveFile(etxtFile.getText().toString()))
            finish();
        else
            Toast.makeText(this, "FILE NOT SAVED", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void cancel()
    {
        if(startSize == etxtFile.getText().length() && startName.equals(etxtFileName.getText().toString())) {
            finish();
            return;
        }
        if (etxtFileName.getText().toString().length() > 0)
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

}
