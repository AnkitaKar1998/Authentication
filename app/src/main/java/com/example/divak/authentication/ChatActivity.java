package com.example.divak.authentication;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class ChatActivity extends AppCompatActivity {

    Button sendButton;
    EditText message;
    LinearLayout teacherChatSection,secondLayout;
    String Gname,loginType;
    FirebaseUser user;
    private DatabaseReference mDatabase =FirebaseDatabase.getInstance().getReference();
    SharedPreferences sharedPreferences;
    String currentChatID;
    String title;
    Task<Uri> downloadUrl;
    File file;
    Uri fileUri;
    Context context;
    ImageView attach;
    FirebaseStorage storage;
    boolean upStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_chat);
        context=ChatActivity.this;

        sharedPreferences=getSharedPreferences("mydata",MODE_PRIVATE);
        attach=findViewById(R.id.attach_iv);
        storage=FirebaseStorage.getInstance();

        secondLayout=findViewById(R.id.ll_second_section);
        sendButton = findViewById(R.id.send_button);
        message = findViewById(R.id.et_message);
        teacherChatSection = findViewById(R.id.ll_teacher_chat_section);
        user= FirebaseAuth.getInstance().getCurrentUser();


        if (getIntent().hasExtra("Department")) {
            Intent intent = getIntent();
            Gname = intent.getStringExtra("Department");
        }

        Log.d("msg","type in chat:"+sharedPreferences.getString("type",""));
        if(sharedPreferences.getString("type","").equals("student")){
            Gname=sharedPreferences.getString("Department","");
            secondLayout.setVisibility(View.GONE);
            currentChatID =getIntent().getStringExtra("teacherId");
            Log.d("msg","Gname:"+Gname);
            Log.d("msg","CurrentChatId:"+currentChatID);

        }else {
            secondLayout.setVisibility(View.VISIBLE);
            currentChatID =user.getUid();
        }



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message.getText().toString();
                message.getText().clear();
                TextView textView = new TextView(ChatActivity.this);
                textView.setText(msg);
                setDesign(textView);
                teacherChatSection.addView(textView);
                String id=mDatabase.push().getKey();
                ModelForSingleMsg object=new ModelForSingleMsg();
                object.setMsg(msg);
                object.setSenderName(sharedPreferences.getString("name",""));
                object.setMsgType("text");
                mDatabase.child("groups").child(Gname).child(currentChatID).child("teacherId").setValue(currentChatID);
                mDatabase.child("groups").child(Gname).child(currentChatID).child("teacherName").setValue(sharedPreferences.getString("name",""));
                mDatabase.child("groups").child(Gname).child(currentChatID).child("messages").child(id).setValue(object);
                final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollView));
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             final MaterialDialog materialDialog= new MaterialDialog.Builder(context)
                        .customView(R.layout.bottom_dialog_layout,false)
                        .title("select")
                        .show();
                LinearLayout gallery= (LinearLayout) materialDialog.findViewById(R.id.gallery_layout);
                LinearLayout photo= (LinearLayout) materialDialog.findViewById(R.id.photo_layout);
                LinearLayout file= (LinearLayout) materialDialog.findViewById(R.id.file_layout);

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hasPermissionsgallery()){
                            accessImage();
                        }else {
                            requestPermissionGallery();
                        }
                        materialDialog.dismiss();
                    }
                });

                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hasPermissionscamera()){
                            takePhoto();
                        }else {
                            requestPermissionCamera();
                        }
                        materialDialog.dismiss();
                    }
                });

                file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hasPermissionsgallery()){
                            openAttachedDocuments();
                        }else {
                            requestPermissionGallery();
                        }
                        materialDialog.dismiss();
                    }

                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child("groups").child(Gname).child(currentChatID).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherChatSection.removeAllViews();
                upStatus=false;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    ModelForSingleMsg det=data.getValue(ModelForSingleMsg.class);
                    if (det.getMsgType().equals("text")){
                        TextView textView = new TextView(ChatActivity.this);
                        String msg=det.getSenderName()+":\n"+det.getMsg();
                        textView.setText(msg);
                        setDesign(textView);
                        teacherChatSection.addView(textView);
                        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollView));
                        scrollview.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    }else {
                        ImageView imageView=new ImageView(context);
                        TextView textView=new TextView(context);
                        LinearLayout layout=new LinearLayout(context);
                        ProgressBar progressBar=new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
                        String msg=det.getMsg();
                        textView.setText(msg);
                        mediaUploadDesign(layout,textView,imageView,progressBar);
                        teacherChatSection.addView(layout);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setDesign(TextView textView) {
        LinearLayout.LayoutParams txtlayParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtlayParams.gravity = Gravity.CENTER;
        txtlayParams.setMargins(50,20,50,20);
        textView.setLayoutParams(txtlayParams);
        textView.setPadding(20,20,20,20);
        textView.setTextSize(20);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setBackgroundColor(Color.parseColor("#FFCDDEFF"));
    }

    public void mediaUploadDesign(LinearLayout layout, final TextView textView, final ImageView imageView, final ProgressBar progressBar){
        LinearLayout.LayoutParams txtlayParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.8f);
        txtlayParams.gravity = Gravity.CENTER_VERTICAL;
        LinearLayout.LayoutParams imglayParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.2f);
        imglayParams.gravity=Gravity.CENTER;

        progressBar.setLayoutParams(imglayParams);

        textView.setLayoutParams(txtlayParams);
        textView.setPadding(5,5,5,5);
        txtlayParams.setMargins(2,2,2,2);

        imageView.setLayoutParams(imglayParams);
        imageView.setPadding(2,2,2,2);
        if(sharedPreferences.getString("type","").equals("student")){
            imageView.setImageResource(R.drawable.ic_file_download_black_24dp);
            progressBar.setVisibility(View.GONE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String fileName=textView.getText().toString().trim();
                    String extension=fileName.substring(fileName.lastIndexOf(".")+1);
//                    download(fileName,extension,progressBar,imageView);
                    downloadFile(fileName,progressBar,imageView);
                    imageView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }else {

            if (upStatus==true){
                imageView.setVisibility(View.GONE);
            }else {
                progressBar.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ic_check_blue_24dp);
            }
        }

        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(50,20,50,20);
        layout.setPadding(20,20,20,20);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundColor(Color.parseColor("#FFCDDEFF"));
        layout.addView(textView);
        layout.addView(imageView);
        layout.addView(progressBar);
    }

    private void downloadFile(String fileName, final ProgressBar progressBar, final ImageView imageView) {
        final StorageReference storageRef=storage.getReference(fileName);

        File rootPath = new File(Environment.getExternalStorageDirectory(), "Demo");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,fileName);

        storageRef .getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                Log.d("msg","progress = "+progress);
                if (progress==100){
                    imageView.setImageResource(R.drawable.ic_check_blue_24dp);
                    imageView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }


    public void uploadFile(final ImageView imageView, final ProgressBar progressBar){
        final StorageReference storageRef=storage.getReference(""+title);
        Log.d("msg"," in upload file");
        UploadTask uploadTask = storageRef.putFile(fileUri);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("msg","upload Unsuccessful");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("msg","upload success");
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                Log.d("msg","progress = "+progress);
                if (progress==100){
                    imageView.setImageResource(R.drawable.ic_check_blue_24dp);
                    imageView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
        downloadUrl = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                Log.d("msg","in then");
                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String id=mDatabase.push().getKey();
                    Log.d("msg","uri:"+downloadUri);
                    ModelForSingleMsg object=new ModelForSingleMsg();
                    object.setMsg(downloadUri.getLastPathSegment().toString());
                    object.setSenderName(sharedPreferences.getString("name",""));
                    object.setMsgType("media");
                    mDatabase.child("groups").child(Gname).child(currentChatID).child("messages").child(id).setValue(object);
                    Log.d("msg","task success");

                } else {
                    // Handle failures
                    // ...
                    Log.d("msg","task unsuccess");
                }
            }
        });
//        storageRef.child(title).putFile(fileUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                downloadUrl=taskSnapshot.getStorage().getDownloadUrl();
//                String id=mDatabase.push().getKey();
//                mDatabase.child("groups").child(Gname).child(currentChatID).child(id).setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
////                            imageView.setImageResource(R.drawable.ic_check_black_24dp);
//                            Toast.makeText(context,"Uploaded",Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(context,"Not Uploaded Try Again",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context,"Not Uploaded Try Again",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//
//            }
//        });
    }


    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(this.getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        fileUri = FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, 1);

    }

    private void openAttachedDocuments () {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select file"),
                    3);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog

        }
    }

    private void accessImage() {
        Intent photoLibraryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoLibraryIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("msg","onActivityResult called");
        final ImageView imageView=new ImageView(context);
        final TextView textView=new TextView(context);
        final LinearLayout layout=new LinearLayout(context);
        final ProgressBar progressBar=new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
        if (resultCode==RESULT_OK){
            if (requestCode==1){
                Log.d("msg","Camera if");
                title=getTitleOfFile(file.toString());

                textView.setText(title);
                setDesign(textView);
                Log.d("msg","title: "+title);
                Log.d("msg","title: "+fileUri);
                teacherChatSection.addView(textView);
                Log.d("msg","view added");
                mediaUploadDesign(layout,textView,imageView,progressBar);
                final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollView));
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
            if (requestCode==2){
                Log.d("msg","gallery if");
                fileUri=data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                String picturePath = getPath(filePathColumn);
                title=getTitleOfFile(picturePath);

                MaterialDialog materialDialog=new MaterialDialog.Builder(context).
                        customView(R.layout.media_upload_design,false)
                        .title("Are You sure?")
                        .positiveText("Send")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                upStatus=true;
                                textView.setText(title);
                                mediaUploadDesign(layout,textView,imageView,progressBar);
                                teacherChatSection.addView(layout);
                                uploadFile(imageView,progressBar);
                                final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollView));
                                scrollview.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();

                TextView textView1= (TextView) materialDialog.findViewById(R.id.media_name_tv);
                textView1.setText(title);

                Log.d("msg","view added");
//                uploadFile(imageView);

            }
            if (requestCode==3){
                Log.d("msg","file if");
                fileUri=data.getData();
                char[] titleDoc = data.getData().toString().toCharArray();
                String finalTitle = "";
                for (int i = titleDoc.length - 1; i >= 0; i--) {
                    if (titleDoc[i] == '/')
                        break;
                    else
                        finalTitle = finalTitle + titleDoc[i];
                }
                title=new StringBuilder(finalTitle).reverse().toString();
            }
        }

    }

    public boolean hasPermissionscamera() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public boolean hasPermissionsgallery() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void requestPermissionGallery() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 2);
        }
    }

    public void requestPermissionCamera() {

        String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode) {

            case 1:
                for (int res : grantResults) {
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;

            default:
                allowed = false;
                break;
        }

        if (allowed) {
            //Permission granted do what you want to do
        } else {
            Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                }else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                }else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public String getPath(String[] filePathColumn){


        Cursor cursor = getContentResolver().query(fileUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filepath = cursor.getString(columnIndex);
        cursor.close();
        return filepath;
    }

    public String getTitleOfFile(String filePath){

        char[] title=filePath.toCharArray();
        String finalTitle="";
        for(int count=title.length-1;count>=0;count--){
            if(title[count]=='/')
                break;
            finalTitle=finalTitle+title[count];
        }
        return new StringBuilder(finalTitle).reverse().toString();
    }
}
