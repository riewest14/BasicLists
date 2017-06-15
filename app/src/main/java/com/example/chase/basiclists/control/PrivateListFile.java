package com.example.chase.basiclists.control;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import static android.provider.Telephony.Mms.Part.FILENAME;

/**
 * Created by chase on 6/4/2017.
 */

public class PrivateListFile extends ListFile {

    public PrivateListFile(Activity activity, String fileName) {
        super(activity, fileName, null);
    }

    @Override
    public boolean saveFile(String fileText) {
        try {
            FileOutputStream fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(fileText.getBytes());
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String readFile() {
        try {
            FileInputStream fis = activity.openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return line;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "UNREADABLE";
        }
    }

    @Override
    public boolean deleteFile() {
        return false;
    }

    @Override
    public ListFile getFileType() {
        return null;
    }
}
