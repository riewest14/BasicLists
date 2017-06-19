package com.example.chase.basiclists.control;

import android.app.Activity;
import android.content.res.Resources;

import com.example.chase.basiclists.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by chase on 6/4/2017.
 */

public class BasicListFile extends ListFile {

    public static final String BASIC_LIST = "BasicList\n";
    protected final String BULLET_SEQUENCE = "\u2022 ";

    public BasicListFile(Activity activity, String fileName, String folder) {
        super(activity, fileName, folder);
        setFilePath();
    }

    @Override
    public boolean saveFile(String fileText) {
        if (!getExternalStorageWritable())
            return false;

        File file = getNotesStorageDir(folderPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getPath() + File.separator + fileName);
            fileText = BASIC_LIST + fileText;
            byte[] buffer = fileText.getBytes();
            fos.write(buffer, 0, buffer.length);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            try{
                if (fos != null)
                    fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public String readFile() {
        if (!isExternalStorageReadable())
            return "UNREADABLE";
        File file = getNotesStorageDir(folderPath);
        FileInputStream fis = null;
        String fileText = null;
        try {
            fis = new FileInputStream(file.getPath() + File.separator + fileName);
            int length = (int) new File(file.getPath() + File.separator + fileName).length();
            byte[] buffer = new byte[length];
            fis.read(buffer, 0, length);
            fis.close();
            fileText = new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if (fis != null)
                    fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(fileText != null) {
            if(fileText.contains(BASIC_LIST))
                fileText = fileText.substring(fileText.lastIndexOf(BASIC_LIST) + BASIC_LIST.length());
            return fileText;
        }
        return "FILE NOT READ";
    }

    @Override
    public boolean deleteFile() {
        File filePath = getNotesStorageDir(folderPath);
        File file = new File(filePath.getPath() + File.separator + fileName);
        return file.delete();
    }

    @Override
    public ListFile getFileType() {
        String fileText = readFile();
        if (fileText.contains("BulletedList\n"))
            return new BulletedListFile(activity, fileName.substring(0, fileName.lastIndexOf('.')), folder);
        if (fileText.contains("NumberedList\n"))
            return new NumberedListFile(activity, fileName.substring(0, fileName.lastIndexOf('.')), folder);
        return this;
    }

    private void setFilePath()
    {
        if(folder != null)
            folderPath = APP_FOLDER + File.separator + folder;
        else
            folderPath = APP_FOLDER;

        if(fileName != null)
            fileName = fileName + FILE_EXTENSION;
        else
            fileName = DEFAULT_FILE + FILE_EXTENSION;
    }
}
