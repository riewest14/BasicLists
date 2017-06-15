package com.example.chase.basiclists;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.chase.basiclists.control.BulletedListFile;
import com.example.chase.basiclists.control.ListFile;
import com.example.chase.basiclists.control.ManageLists;
import com.example.chase.basiclists.control.PublicListFile;

import java.util.ArrayList;

public class ShowAvailableFiles extends AppCompatActivity implements View.OnClickListener {

    private static final String DELETE_TEXT = "Delete Files";
    private static final String DELETE_CONFIRM = "Confirm Delete";
    private static final String DELETE_BULLET = "\u0058 ";

    private ArrayList<String> fileNames;
    private ArrayAdapter<String> adapter;
    private boolean deleteMode = false;
    private ArrayList<Integer> deletePositions;

    private ManageLists ListManager;
    private ListView lvFileList;
    private Button btnNewList, btnDeleteFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_available_files);
        ListManager = new ManageLists(this);
        initializeControls();
    }

    private void initializeControls()
    {
        lvFileList = (ListView)findViewById(R.id.lvFileList);
        setFileList();
        final Context context = this;
        lvFileList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!deleteMode) {
                    ListFile file = ListManager.getFile(position);
                    viewFile(file);
                }
                else if (deleteMode)
                {
                    if(!deletePositions.contains(position)) {
                        deletePositions.add(position);
                        updateFileListItem(DELETE_BULLET + fileNames.get(position), position);
                    }
                    else {
                        ArrayList<Integer> temp = new ArrayList<Integer>();
                        for(Integer i : deletePositions)
                        {
                            if( i != position)
                                temp.add(i);
                        }
                        deletePositions = temp;
                        updateFileListItem(ListManager.getFile(position).getFileName(), position);
                    }
                }
            }
        });

        btnDeleteFiles = (Button)findViewById(R.id.btnDeleteFiles);
        btnDeleteFiles.setOnClickListener(this);

        btnNewList = (Button)findViewById(R.id.btnNewList);
        btnNewList.setOnClickListener(this);
    }

    private void updateFileListItem(String newText, int position)
    {
        fileNames.set(position, newText);
        adapter.notifyDataSetChanged();
    }

    private void setFileList()
    {
        ListManager.readExistingFiles(null);
        fileNames = ListManager.getFileNames();
        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, fileNames);
        lvFileList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnNewList)
        {
            Intent newList = new Intent(this, NewListChoiceActivity.class);
            startActivity(newList);
        }
        if(v.getId() == R.id.btnDeleteFiles)
        {
            if(!deleteMode) {
                deleteMode = true;
                btnDeleteFiles.setText(DELETE_CONFIRM);
                deletePositions = new ArrayList<>();
            }
            else {
                processDelete();
                btnDeleteFiles.setText(DELETE_TEXT);
                deleteMode = false;
            }
        }
    }

    @Override
    public void onResume()    {
        super.onResume();
        setFileList();
    }

    private boolean viewFile(ListFile file)
    {
        Intent main = null;

        if (file instanceof BulletedListFile)
            main = new Intent(this, BulletedListActivity.class);
        else if (file instanceof PublicListFile)
            main = new Intent(this, BasicListActivity.class);
        else
            return false;
        main.putExtra("fileName", file.getFileName().substring(0, file.getFileName().lastIndexOf('.')));
        main.putExtra("fileFolder", file.getFolder());
        main.putExtra("NewList", false);
        startActivity(main);
        return true;
    }

    private void processDelete()
    {
        if(deletePositions.size() == 0)
            return;
        String filesToDelete = "";
        for(Integer i : deletePositions)
        {
            filesToDelete += ListManager.getFile(i).getFileName() + ", ";
        }
        filesToDelete = filesToDelete.substring(0, filesToDelete.lastIndexOf(','));
        new AlertDialog.Builder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Do you really want to delete the files:  " + filesToDelete)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        ListManager.deleteFiles(deletePositions);
                        setFileList();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setFileList();
                    }
                }).show();

    }

}
