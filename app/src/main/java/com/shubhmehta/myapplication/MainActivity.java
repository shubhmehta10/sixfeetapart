package com.shubhmehta.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String videoPath = "";
    public String param = "";
   // private static final String SERVER_PATH = "http://192.168.0.104:8000/";
    // private static final String SERVER_PATH1 = "";
    VideoView result_video;
    VideoView videoView;
   // String encodedString = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Button click = (Button) findViewById(R.id.button);
        videoView = (VideoView) findViewById(R.id.videoView);  //casting to VideoView is not Strictly required above API level 26
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoView.start(); //need to make transition seamless.
            }
        });
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.logo1); //set the path of the video that we need to use in our VideoView
        videoView.start();  //start() method of the VideoView class will start the video to play
        if (checkPermission()) {
            //main logic or main code

            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    //code for opening camera
    public void capturePhoto(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
        //  Intent intent = new Intent(this,Outputmodel.class);

    }


    //trying to run python file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoPath = getRealPathFromURIPath(videoUri, MainActivity.this);
            //param = getFileName(videoPath);
            uploadVideoToServer(videoPath);
            //   downloadFile();
            Intent intent = new Intent(this, OutputModel.class);
            startActivity(intent);
        }

    }

//            String command = "python /c start python C:\\Users\\LENOVO\\AndroidStudioProjects\\MyApplication\\app\\src\\main\\java\\com\\shubhmehta\\myapplication\\SocialDistancingDetector.py";
//            try {
//                Process p = Runtime.getRuntime().exec("C:\\Users\\LENOVO\\AndroidStudioProjects\\MyApplication\\app\\src\\main\\java\\com\\shubhmehta\\myapplication\\SocialDistancingDetector.py" + param );
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

    // MyClass myclass = new MyClass();
    // myclass.printvariableToFile("inputfile.txt",param);


    //method to get url of recorded video
//    public String getUrlFromUri(Uri contentUri){
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(contentUri,proj,null,null,null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//
//    }

    public String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    //method to get name of recorded video
    public String getFileName(String path) {
        String[] pathArray = path.split("/");
        return pathArray[pathArray.length - 1];
    }

    //Uploading the video to server
    private void uploadVideoToServer(String pathToVideoFile) {
        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(videoFile,MediaType.parse("video/*"));
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(logging)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(" https://ef1ec303e0fe.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VideoInterface vInterface = retrofit.create(VideoInterface.class);
        Call<ResultObject> serverCom = vInterface.uploadVideoToServer(vFile);
        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();
                if (!TextUtils.isEmpty(result.getMessage())) {
                    Toast.makeText(MainActivity.this, "Result " + result.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Result " + result.getMedia_url());
                    savingFile(result.getResp());
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Log.d(TAG, "Error message" + t.getMessage());
            }
        });
    }


    //    private void downloadFile() {
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(SERVER_PATH)
//                .build();
//
//        InputInterface handlerService = retrofit.create(InputInterface.class);
//
//        Call<ResponseBody> call = handlerService.downloadFileByUrl(SERVER_PATH);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    if (writeResponseBodyToDisk(response.body())) {
//                       // listener.onFileLoaded(file);
//                    }
//                } else {
//                    //listener.onDownloadFailed("Resource not Found");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                //listener.onDownloadFailed("Download Failed");
//                t.printStackTrace();
//            }
//        });
//    }
    private void savingFile(String base64) {
        //File file = new File(getExternalFilesDir(null) + File.separator + "Output.mp4");

        Log.d("String", base64);

        //Decode String To Video With mig Base64.
        byte[] decodedBytes = Base64.decode(base64.getBytes(),Base64.DEFAULT);

        try {

            FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Output.mp4");
            out.write(decodedBytes);
            out.close();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Error", e.toString());

        }
    }
}

