package com.foodcityapp.esa.foodcityapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ListsFragment extends Fragment {

    private ListView ListslistV;
    private ShoppingList shoppingList;
    private DatabaseReference dbRef;
    private DatabaseReference dbRefUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lists,container,false);
        ListslistV = (ListView) rootView.findViewById(R.id.listsListView);
        shoppingList = new ShoppingList();


        dbRef = FirebaseDatabase.getInstance().getReference().child("Lists");
        dbRef.keepSynced(true);

        FirebaseListAdapter<ShoppingList> firebaseListAdapter = new FirebaseListAdapter<ShoppingList>(getActivity(), ShoppingList.class, R.layout.list_title_item, dbRef) {
            @Override
            protected void populateView(View v, ShoppingList model, int position) {

                shoppingList = getItem(position);

                final TextView listTitle = (TextView) v.findViewById(R.id.listTitle);
                final TextView listContent = (TextView) v.findViewById(R.id.listContent);
               // final TextView listTitle = (TextView) v.findViewById(R.id.listTitle);

                listTitle.setText(shoppingList.getListTitle());
                //listContent.setText();

                ListslistV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String listKey = getRef(position).getKey();
                        shoppingList = getItem(position);

                        Toast.makeText(getContext(), shoppingList.getListTitle() + listKey, Toast.LENGTH_LONG).show();
                    }
                });

            }


        };


        ListslistV.setAdapter(firebaseListAdapter);


        return rootView;
    }
}
