package com.example.tec.uploadimagetoazure;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;


import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;

public class UploadImage extends Activity {

    byte[] byteArray;


    private MobileServiceClient mClient;

    /**
     * Mobile Service Table used to access data
     */
    private MobileServiceTable<User> mUser;


    private ImageView image;

    private ProgressBar mProgressBar;

    private Bitmap bitmap;
    String encodedImage;
    String ss;

    
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar7);


        image = (ImageView) findViewById(R.id.imageView);



        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "AZURE MOBILE SERVICE URL",
                    "MOBILE SERVICE KEY",
                    this);/*.withFilter(new ProgressFilter());*/

            // Get the Mobile Service Table instance to use
            mUser = mClient.getTable(User.class);

        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }






    }

    public  void selectPicture(View view){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Log.i("Uri", selectedImage.getPath());
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null,
                    null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.i("picturePath", picturePath);
            cursor.close();

            decodeFile(picturePath);

        }
    }
    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }


        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        // image.setImageBitmap(bitmap);


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();

        Log.d("try", "" + byteArray);

        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Log.d("encoded",encodedImage);

        ss= encodedImage.toString();

        byte[] data = Base64.decode(ss, Base64.DEFAULT);

        Bitmap bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);
        image.setImageBitmap(bitmap1);


    }

    public void addItem(View view) {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);


        Log.d("PIC","INSIDE ADD ITEM");


        if (mClient == null) {
            return;
        }

        // Create a new item
        final User item = new User();




        item.setimagestring(ss);







        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mUser.insert(item).get();



                    if (!item.isComplete()) {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                SharedPreferences acc = getSharedPreferences("myaccount", 0);
                                SharedPreferences.Editor editor=acc.edit();
                                editor.putString("si", ss);
                                editor.commit();
                                Intent MainActivity = new Intent(getApplicationContext(), UploadImage.class);
                                startActivity(MainActivity);
                                Toast toast=  Toast.makeText(getApplicationContext(),"LOGO UPDATED",Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();

                            }
                        });
                    }


                } catch (Exception exception) {
                    createAndShowDialog(exception, "Error");
                }
                return null;
            }
        }.execute();


    }







    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        //	createAndShowDialog(ex.getMessage(), title);
    }

}


