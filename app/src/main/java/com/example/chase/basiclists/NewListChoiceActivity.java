package com.example.chase.basiclists;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chase.basiclists.control.BulletedListFile;
import com.example.chase.basiclists.control.ListFile;

import java.util.ArrayList;
import java.util.Arrays;

public class NewListChoiceActivity extends AppCompatActivity {
    private Resources res;
    private ArrayList<String> fileTypes;
    private ArrayAdapter<String> adapter;

    private ListView lvFileChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list_choice);
        initializeControls();
    }

    private void initializeControls()
    {
        lvFileChoice = (ListView)findViewById(R.id.lvFileChoice);
        getListTypes();
        final Context context = this;
        lvFileChoice.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 if(generateNewFileActivity(fileTypes.get(position)))
                     finish();
            }
        });
    }

    private void getListTypes()
    {
        res = getResources();
        fileTypes = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.list_types)));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileTypes);
        lvFileChoice.setAdapter(adapter);
    }

    private boolean generateNewFileActivity(String fileType)
    {
        Intent createList = null;
        switch (fileType)
        {
            case "BasicList":
                createList = new Intent(this, BasicListActivity.class);
                break;
            case "BulletedList":
                createList = new Intent(this, BulletedListActivity.class);
                break;
            case "NumberedList":
                Toast.makeText(this, fileType + " Not Implemented", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Not A File Type", Toast.LENGTH_SHORT).show();
                break;

        }
        if (createList == null)
            return false;
        createList.putExtra("NewList", true);
        startActivity(createList);
        return true;
    }
}
