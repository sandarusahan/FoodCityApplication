package com.foodcityapp.esa.foodcityapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText nameTxt;
    private TextInputEditText emailTxt;
    private TextInputEditText mobileTxt;
    private TextInputEditText passTxt;
    private Button regBtn;
    private ImageButton imgBtnReg;

    FirebaseAuth auth;

    private ProgressDialog progress;
    private DatabaseReference dbRef;
    private StorageReference storageRef;
    private Uri imguri = null;
    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        storageRef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        imgBtnReg = (ImageButton) findViewById(R.id.imageBtnReg);
        imgBtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        regBtn = (Button) findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addUser();
            }
        });

        nameTxt = (TextInputEditText) findViewById(R.id.nameTxtReg);
        emailTxt = (TextInputEditText) findViewById(R.id.emailTxtReg);
        mobileTxt = (TextInputEditText) findViewById(R.id.mobileTxtReg);
        passTxt = (TextInputEditText) findViewById(R.id.passTxtReg);



        progress = new ProgressDialog(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imguri = data.getData();
            imgBtnReg.setImageURI(imguri);
        }
    }

    public void addUser(){



        final String username = nameTxt.getText().toString().trim();
        final String useremail = emailTxt.getText().toString().trim();
        final String usermobile = mobileTxt.getText().toString().trim();
        final String userpass = passTxt.getText().toString().trim();


        if(!TextUtils.isEmpty(useremail) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(usermobile) && !TextUtils.isEmpty(userpass)){

            progress.setMessage("Signing up...");
            progress.show();
            auth.createUserWithEmailAndPassword(useremail, userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){



                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String userId = currentUser.getUid();

                        DatabaseReference currentUserDb = dbRef.child(userId);
                        HashMap<String, String> userMap = new HashMap<String, String>();
                        userMap.put("name", username);
                        userMap.put("status", "I love shopping at FoodCity :D");
                        userMap.put("image", "default");
                        userMap.put("thumb_img", "thumb_default");

                        currentUserDb.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progress.dismiss();
                                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Couldn't upload your data to the database!", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
                                }
                            }
                        });


                    }
                }
            });


        }else{
            Toast.makeText(getApplicationContext(), "Non of the fields can't be empty!!", Toast.LENGTH_SHORT).show();
        }
    }
}
