package com.foodcityapp.esa.foodcityapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AccountSetupActivity extends AppCompatActivity {

    private ImageButton setupImgBtn;
    private TextView setupName;
    private Button setupSubmitBtn;

    private static final int GALLERY_REQUEST = 0;
    private static final String TAG = "AccountSetupActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        setupImgBtn = (ImageButton) findViewById(R.id.setupImgBtnS);
        setupName = (TextView) findViewById(R.id.setupNameS);
        setupSubmitBtn = (Button) findViewById(R.id.setupSubmitBtnS);

        setupImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                /*galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image*//*");*/
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    setupImgBtn.setImageURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Log.v(TAG,error.toString());
                }


        }


    }
}
