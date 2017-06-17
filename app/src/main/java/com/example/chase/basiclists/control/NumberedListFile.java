package com.example.chase.basiclists.control;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by chase on 6/11/2017.
 */

public class NumberedListFile extends BasicListFile {
    public NumberedListFile(Activity activity, String fileName, String folder) {
        super(activity, fileName, folder);
    }

    public boolean saveFile(ArrayList<String> fileText)
    {
        if (!getExternalStorageWritable())
            return false;
        fileText.add(0, "NumberedList");
        File file = getNotesStorageDir(folderPath);
        FileOutputStream fos = null;
        try {
            for(String line : fileText) {
                fos = new FileOutputStream(file.getPath() + File.separator + fileName, true);
                byte[] buffer = line.getBytes();
                fos.write(buffer, 0, buffer.length);
                if (line != fileText.get(fileText.size() - 1))
                    fos.write(System.getProperty("line.separator").getBytes());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            try{
                if (fos != null){
                        fos.flush();
                        fos.close();
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public ArrayList<String> readFileBulletList()
    {
        if (!isExternalStorageReadable())
            return null;
        ArrayList<String> fileText = new ArrayList<>();
        File file = getNotesStorageDir(folderPath);
        FileInputStream fis = null;
        BufferedReader reader = null;
        try {
            fis = new FileInputStream(file.getPath() + File.separator + fileName);
            reader = new BufferedReader(new InputStreamReader(fis));
            String line = reader.readLine();
            while(line != null)
            {
                fileText.add(line);
                line = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if (fis != null)
                    fis.close();
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if(fileText != null) {
            fileText.remove(0);
            return fileText;
        }
        return null;
    }
}
