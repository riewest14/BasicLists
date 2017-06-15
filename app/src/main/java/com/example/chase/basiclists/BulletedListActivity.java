package com.example.chase.basiclists;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chase.basiclists.control.BulletedListFile;
import com.example.chase.basiclists.control.ListFile;
import com.example.chase.basiclists.control.PublicListFile;

import java.util.ArrayList;

public class BulletedListActivity extends ListActivity implements View.OnClickListener {

    private static final String BULLET_STRING = "\u2022 ";

    private BulletedListFile fileToView;
    private boolean newFile;
    private boolean changedFile;

    private ListView bulletList;
    private Button btnAdd, btnSave, btnCancel;
    private EditText etxtFileName;
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int startSize;
    private String startName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulleted_list);

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
                    fileToView = new BulletedListFile(this,fileName, folder);
                else
                    fileToView = new BulletedListFile(this, null, null);
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);
        initializeControls();
    }

    private void initializeControls()
    {

        btnCancel = (Button)findViewById(R.id.btnCancelBull);
        btnCancel.setOnClickListener(this);

        btnSave = (Button)findViewById(R.id.btnSaveBull);
        btnSave.setOnClickListener(this);

        etxtFileName = (EditText)findViewById(R.id.etxtFileNameBull);
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

        if(!newFile) {
            setItems(fileToView.readFileBulletList());
            startSize = listItems.size();
            startName = fileToView.getFileName();
        }
        else {
            startSize = 0;
            startName = "";
        }

        bulletList = getListView();
        bulletList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lineText = listItems.get(position);
                lineText = lineText.substring(2);
                if(lineText.trim().length() == 0)
                {
                    newLine(null, position);
                } else {
                    newLine(lineText, position);
                }
            }
        });
        addItem(" ");

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancelBull:
                    cancel();
                break;
            case R.id.btnSaveBull:
                    save();
                break;
            default:
                break;
        }
    }

    private void save()
    {
        if(!changedFile) {
            finish();
            return;
        }
        listItems.remove(listItems.size() - 1);
        if(newFile)
            fileToView = new BulletedListFile(this, etxtFileName.getText().toString(), null);
        else
            fileToView.deleteFile();
        if(fileToView.saveFile(listItems))
            finish();
        else
            Toast.makeText(this, "FILE NOT SAVED", Toast.LENGTH_SHORT).show();
    }

    private void cancel()
    {
        if(!changedFile) {
            finish();
            return;
        }
        if (listItems.size() > startSize || etxtFileName.getText().toString() != startName )
        {
            new AlertDialog.Builder(this)
                    .setTitle("Discard")
                    .setMessage("Do you really want to discard the changes/list")
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

    private void setItems(ArrayList<String> newListItems)
    {
        for(String line : newListItems)
            listItems.add(line);
        adapter.notifyDataSetChanged();
    }

    private void addItem(String newLineText) {
        if(newLineText.length() > 0) {
            listItems.add(BULLET_STRING + newLineText);
            adapter.notifyDataSetChanged();
        }
    }

    private void updateItem(String newText, int position) {
        listItems.set(position, BULLET_STRING + newText);
        adapter.notifyDataSetChanged();
        if(position == listItems.size() - 1)
            addItem(" ");
    }

    private void newLine(String oldText, final int position)
    {
        if(!changedFile)
            changedFile = true;
        if(oldText == null) {
            oldText = "";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Line");
        final EditText input = new EditText(this);
        input.setText(oldText);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateItem(input.getText().toString(), position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.show();
    }
}
