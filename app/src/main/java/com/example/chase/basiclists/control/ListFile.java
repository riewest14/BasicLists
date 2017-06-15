package com.example.chase.basiclists.control;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;

/**
 * Created by chase on 6/4/2017.
 */

public abstract class ListFile {
    static final String FILE_EXTENSION = ".txt";
    static final String APP_FOLDER = "BasicLists";
    static final String DEFAULT_FILE = "Basic_List";
    static final String DELIM = ", ";
    private static final String LOG_TAG = "CHASE'S APP";

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    protected String fileName, folder, folderPath;
    protected Activity activity;

    public ListFile(Activity activity,String fileName, String folder)
    {
        verifyStoragePermissions(activity);
        this.fileName = fileName;
        this.folder = folder;
        this.activity = activity;
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

    protected boolean getExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    protected boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getNotesStorageDir(String folderName) {

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), folderName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }



    public String getFileName() {
        return fileName;
    }

    public static String getDELIM() {
        return DELIM;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public static String getAppFolder() {
        return APP_FOLDER;
    }

    public String getFolder() {
        return folder;
    }

    abstract public boolean saveFile(String fileText);

    abstract public String readFile();

    abstract public boolean deleteFile();

    abstract public ListFile getFileType();



}
