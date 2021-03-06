package com.example.chase.basiclists;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chase.basiclists.control.BulletedListFile;

import java.util.ArrayList;

public class BulletedListActivity extends BasicListActivitySuperclass implements View.OnClickListener {

    private static final String BULLET_STRING = "\u2022 ";
    private ListView bulletList;
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_bulleted_list);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeList(String fileName, String folder)
    {
        bulletList = (ListView)findViewById(R.id.lvBulletList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        bulletList.setAdapter(adapter);
        if(!newFile) {
            fileToView = new BulletedListFile(this,fileName, folder);
            setItems( ((BulletedListFile)fileToView).readFileBulletList());
            startSize = listItems.size();
            startName = fileName;
        }
        else {
            startSize = 0;
            startName = "";
            fileToView = new BulletedListFile(this, null, null);
        }

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
    protected void save()
    {
        if(startSize == listItems.size() - 1 && startName.equals(etxtFileName.getText().toString())) {
            finish();
            return;
        }
        listItems.remove(listItems.size() - 1);
        if(newFile)
            fileToView = new BulletedListFile(this, etxtFileName.getText().toString(), null);
        else
            fileToView.deleteFile();
        if(((BulletedListFile)fileToView).saveFile(listItems))
            finish();
        else
            Toast.makeText(this, "FILE NOT SAVED", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void cancel()
    {
        if(startSize == listItems.size() - 1 && startName.equals(etxtFileName.getText().toString())) {
            finish();
            return;
        }
        if (etxtFileName.getText().toString() != startName )
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
