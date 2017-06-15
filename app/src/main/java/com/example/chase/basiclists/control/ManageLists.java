package com.example.chase.basiclists.control;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * Created by chase on 6/4/2017.
 */

public class ManageLists {
    private static final String LOG_TAG = "CHASE'S APP";

    private ArrayList<ListFile> listFiles = new ArrayList<ListFile>();
    private Activity activity;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public ManageLists(Activity activity) {
        this.activity = activity;
        readExistingFiles(null);
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    private static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void readExistingFiles(String startFolder)
    {
        listFiles.clear();
        try {
            ListFile tempFile;
            if(startFolder == null)
                tempFile = new PublicListFile(activity, null, null);
            else
                tempFile = new PublicListFile(activity, null, startFolder);
            File publicSavesFolder = tempFile.getNotesStorageDir(tempFile.getFolderPath());
            File[] listOfFiles = publicSavesFolder.listFiles();
            for (File temp : listOfFiles) {
                if (temp.isFile()) {
                    listFiles.add(new PublicListFile(activity, temp.getName().substring(0, temp.getName().lastIndexOf('.')), tempFile.getFolder()));
                }
                else if (temp.isDirectory()){
                    readExistingFiles(temp.getName());
                }
            }

        }
        catch (Exception e)
        {
            System.out.println("ReadExistFiles: " + e);
        }
    }


    public void addNewFile(ListFile newFile)
    {
        listFiles.add(newFile);
    }

    public ListFile getFile(String fileName, String folder)
    {
        for(ListFile file : listFiles)
        {
            if(file.getFileName() == fileName)
                return file;
        }
        return null;
    }

    public ListFile getFile(int index)
    {
        return listFiles.get(index).getFileType();
    }

    public boolean deleteFiles(ArrayList<Integer> positions)
    {
        for(Integer i : positions)
        {
            if(!listFiles.get(i).deleteFile())
            {
                Toast.makeText(activity, "Files Not Deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> getFileNames()
    {
        ArrayList<String> fileNames = new ArrayList<String>();
        for (ListFile file : listFiles)
        {
            fileNames.add(file.getFileName());
        }

        return fileNames;
    }



}
