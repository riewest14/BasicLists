package com.example.chase.basiclists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSeeLists, btnCreateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        initializeControls();
    }

    private void initializeControls()
    {
        btnSeeLists = (Button)findViewById(R.id.btnSeeLists);
        btnSeeLists.setOnClickListener(this);

        btnCreateList = (Button)findViewById(R.id.btnCreateList);
        btnCreateList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSeeLists)
        {
            Intent viewLists = new Intent(this, ShowAvailableFiles.class);
            startActivity(viewLists);
        }
        if(v.getId() == R.id.btnCreateList)
        {
            Intent newList = new Intent(this, NewListChoiceActivity.class);
            startActivity(newList);
        }
    }

    private void test()
    {
        int i = 1+1;
    }

}
