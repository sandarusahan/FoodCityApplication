package com.foodcityapp.esa.foodcityapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by sanda on 06/11/2017.
 */

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder>{

    private ArrayList<Product> productsList;
    private Product product;
    private Context context;




    public SectionListDataAdapter(ArrayList<Product> productsList,Context context) {
        this.productsList = productsList;
        this.context = context;


    }

    @Override
    public SectionListDataAdapter.SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SectionListDataAdapter.SingleItemRowHolder holder, int position) {

        product = productsList.get(position);

        holder.tvTitle.setText(product.getProdName());
        holder.tvPrice.setText("Rs. " + product.getProdPrice());
        Picasso.with(context).load(product.getProdImg()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.itemImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

                Picasso.with(context).load(product.getProdImg()).into(holder.itemImage);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != productsList ? productsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder{

        protected TextView tvTitle;

        protected TextView tvPrice;

        protected ImageView itemImage;

        public SingleItemRowHolder(View itemView) {
            super(itemView);

            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            this.tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            this.itemImage = (ImageView) itemView.findViewById(R.id.itemImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String productKey = product.getProdId();
                    Intent i = new Intent(v.getContext(), DetailScreen.class);
                    i.putExtra("product", productKey);
                    v.getContext().startActivity(i);
                    Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
