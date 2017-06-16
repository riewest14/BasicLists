package com.example.chase.basiclists;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chase.basiclists.control.ListFile;

/**
 * Created by chase on 6/15/2017.
 */

abstract class BasicListActivitySuperclass extends AppCompatActivity implements View.OnClickListener {
    protected ListFile fileToView;
    protected boolean newFile;
    protected String startName;
    protected int startSize;
    protected EditText etxtFileName;
    protected Button btnSave;
    protected Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                initializeList(fileName, folder);
            }
        }
        initializeControls();
    }

    private void initializeControls()
    {
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

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
    }

    protected abstract void initializeList(String fileName, String folder);

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnSave:
                save();
                break;
            default:
                break;
        }
    }

    protected abstract void save();

    protected abstract void cancel();
}
