package com.foodcityapp.esa.foodcityapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private String category = "";
    //private ShoppingList<Product> productsList;


    private DatabaseReference dbRef;
    private Query dbQueryCategory;
    private DatabaseReference dbRefUser;
    private MainActivity main;
    public FirebaseAuth auth;
    public FirebaseAuth.AuthStateListener authListener;
    private ImageButton addListbtn;
    private ArrayList<CategoryModel> AllProducts;
    private ArrayList<Product> products;
    private Button btn3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_products, container, false);

        RecyclerView product_Recycle = (RecyclerView) rootView.findViewById(R.id.productRecycleView);
       // btn3 = (Button) rootView.findViewById(R.id.refreshbtn);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Products");
        dbRef.keepSynced(true);

        dbRefUser = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRefUser.keepSynced(true);

        AllProducts = new ArrayList<CategoryModel>();
        products = new ArrayList<Product>();

        Activity activity = getActivity();
        if(activity!=null) {
            Thread tread = new Thread() {
                public void run() {
                    try {
                        sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        populate();
                    }
                }
            };
            tread.start();


            product_Recycle.setHasFixedSize(true);
            RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(AllProducts, getContext());
            product_Recycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            product_Recycle.setAdapter(adapter);
        }
       /* btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populate();
            }
        });*/

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        retriveData();
    }

    public void retriveData(){
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                String cat = product.getProdCat();
                addProd(product);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void addProd(Product product){
        products.add(product);
    }



    public void populate(){

        CategoryModel categoryModel;


        String [] prodCat = getResources().getStringArray(R.array.cat_array);
        for(int i = 0; i < prodCat.length; i++) {
            categoryModel = new CategoryModel();
            String title = prodCat[i];
            categoryModel.setHeaderTitle(" " + title);

            for(Product prod: products) {
                ArrayList<Product> displayProduct = new ArrayList<>();
                if (prod.getProdCat().equalsIgnoreCase(title)) {
                    displayProduct.add(prod);
                }
                categoryModel.setAllItemsInCategory(displayProduct);
            }
            AllProducts.add(categoryModel);
        }
    }
}

    //method should include for login




