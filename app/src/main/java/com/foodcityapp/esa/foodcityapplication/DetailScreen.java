package com.foodcityapp.esa.foodcityapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import static java.security.AccessController.getContext;

public class DetailScreen extends AppCompatActivity {

    private String producKey;
    private DatabaseReference dbRef;
    private int qtyNum = 1;
    double price = 0.0;


    TextView prodNametv;
    TextView prodDesctv;
    TextView prodPricetv;
    TextView prodQtytv;
    TextView prodTypetv;
    EditText prodAmountet;
    ImageView prodImgiv;
    ImageButton upBtn;
    ImageButton downBtn;
    Button addToListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Products");
        dbRef.keepSynced(true);
        producKey = getIntent().getStringExtra("product");
        prodNametv = (TextView) findViewById(R.id.prodNameTxtD);
        prodDesctv = (TextView) findViewById(R.id.prodDescTxtD);
        prodPricetv = (TextView) findViewById(R.id.prodPriceTxtD);
        prodQtytv = (TextView) findViewById(R.id.prodQtyTxtD);
        prodTypetv = (TextView) findViewById(R.id.prodTypeTxtD);
        prodImgiv = (ImageView) findViewById(R.id.prodImgViewD);
        prodAmountet = (EditText) findViewById(R.id.amountTxtD);
        upBtn = (ImageButton)findViewById(R.id.upBtnD);
        downBtn = (ImageButton)findViewById(R.id.downBtnD);
        addToListBtn = (Button) findViewById(R.id.addToListBtnD);


        dbRef.child(producKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String prodName = (String) dataSnapshot.child("prodName").getValue();
                final String prodDesc = (String) dataSnapshot.child("prodDesc").getValue();
                final String prodPrice = (String) dataSnapshot.child("prodPrice").getValue();
                final String prodType = (String) dataSnapshot.child("prodCat").getValue();
                final String prodImg = (String) dataSnapshot.child("prodImg").getValue();
                final String prodQty = (String) dataSnapshot.child("prodQty").getValue();
               if(!prodAmountet.getText().toString().equals("")) {
                   prodAmountet.setText(Integer.toString(qtyNum));
                   String amount = prodAmountet.getText().toString().trim();
               }

               // int amnt = Integer.parseInt(amount);

                price = Double.parseDouble(prodPrice);
                qtyBtn();
                String msg="Stock Available";
                try {
                   int qty = Integer.parseInt(prodQty);
                   if(qty<20){
                        msg = "Stock running low";
                    }
                    if(qty<5){
                        msg = "Stock not available";
                    }
                }
                catch (TypeNotPresentException e){
                    Toast.makeText(DetailScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(DetailScreen.this, MainActivity.class);
                    startActivity(i);
                }



                prodNametv.setText(prodName);
                prodDesctv.setText(prodDesc);
                prodPricetv.setText("Rs. "+price);
                prodQtytv.setText(msg);
                prodTypetv.setText(prodType);
                Picasso.with(DetailScreen.this).load(prodImg).networkPolicy(NetworkPolicy.OFFLINE).into(prodImgiv, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {

                        Picasso.with(DetailScreen.this).load(prodImg).into(prodImgiv);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if(prodAmountet.getText() != null) {

            prodAmountet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(!prodAmountet.getText().toString().equals("")) {
                        if (Integer.parseInt(prodAmountet.getText().toString()) > 0) {
                            qtyNum = Integer.parseInt(s.toString());
                            prodPricetv.setText("Rs. " + Double.toString(calcTotal(qtyNum, price)));


                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }

    private double calcTotal(int amnt, double priceeach) {
        double total = 0;

        total = priceeach*amnt;

        return total;
    }

    public void qtyBtn(){

        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qtyNum++;
                prodAmountet.setText(Integer.toString(qtyNum));
                prodPricetv.setText("Rs. "+Double.toString(calcTotal(qtyNum, price)));
            }
        });
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(qtyNum>1) {
                    qtyNum--;
                    prodAmountet.setText(Integer.toString(qtyNum));
                    prodPricetv.setText("Rs. "+Double.toString(calcTotal(qtyNum, price)));

                }
                }
        });
    }
}
