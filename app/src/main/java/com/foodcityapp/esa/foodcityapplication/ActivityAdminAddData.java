package com.foodcityapp.esa.foodcityapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ActivityAdminAddData extends AppCompatActivity {

    private Button addProdBtn;
    private EditText prodName, prodPrice, prodQty, prodDesc;
    private Spinner prodCat;
    private ImageButton prodImg;

    private Uri imguri = null;
    private static final int GALLERY_REQUEST = 1;

    private DatabaseReference dbRef;
    private StorageReference storageRef;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_data);
        //Firebase.setAndroidContext(this);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Products");
        storageRef = FirebaseStorage.getInstance().getReference();


        prodName = (EditText) findViewById(R.id.prodName);
        prodPrice = (EditText) findViewById(R.id.prodPrice);
        prodQty = (EditText) findViewById(R.id.prodQty);
        prodDesc = (EditText) findViewById(R.id.prodDesc);
        prodCat = (Spinner) findViewById(R.id.prodCat);

        progress = new ProgressDialog(this);

        prodImg = (ImageButton) findViewById(R.id.prodImg);
        prodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        addProdBtn = (Button) findViewById(R.id.addProductsBtn);
        addProdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addProduct();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imguri = data.getData();
            prodImg.setImageURI(imguri);
        }
    }
    public void addProduct(){


        progress.show();


        final String prodNamestr = prodName.getText().toString().trim();
        final String prodPricestr = prodPrice.getText().toString().trim();
        final String prodQtystr = prodQty.getText().toString().trim();
        final String prodDescstr = prodDesc.getText().toString().trim();
        final String prodCatstr = String.valueOf(prodCat.getSelectedItem());

        if(!TextUtils.isEmpty(prodNamestr) && !TextUtils.isEmpty(prodPricestr) && !TextUtils.isEmpty(prodQtystr) && !TextUtils.isEmpty(prodDescstr) && !TextUtils.isEmpty(prodCatstr)){

            progress.setMessage("Loading....");
            StorageReference filePath = storageRef.child("Product_images").child(imguri.getLastPathSegment());
            filePath.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    progress.dismiss();
                    DatabaseReference newProduct = dbRef.push();
                    newProduct.child("prodId").setValue(newProduct.getKey());
                    newProduct.child("prodName").setValue(prodNamestr);
                    newProduct.child("prodPrice").setValue(prodPricestr);
                    newProduct.child("prodQty").setValue(prodQtystr);
                    newProduct.child("prodDesc").setValue(prodDescstr);
                    newProduct.child("prodCat").setValue(prodCatstr);
                    newProduct.child("prodImg").setValue(downloadUri.toString());

                    clearFields();
                }
            });

        }else{
            Toast.makeText(getApplicationContext(), "Non of the fields can't be empty!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void clearFields(){

        prodName.setText(null);
        prodPrice.setText(null);
        prodDesc.setText(null);
        prodQty.setText(null);
        prodImg.setImageURI(null);
    }
}
