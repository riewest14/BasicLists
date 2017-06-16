package com.example.chase.basiclists;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class TestActivity extends ListActivity implements View.OnClickListener {
    private Button btnAdd;
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);

        initializeControls();
    }

    private void initializeControls()
    {

    }

    @Override
    public void onClick(View v)
    {

    }

    private void addItem(String newLineText) {
        if(newLineText.length() > 0) {
            listItems.add("\u2022 " + newLineText);
            adapter.notifyDataSetChanged();
        }
    }

    private void newLine()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Line");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addItem(input.getText().toString());
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
