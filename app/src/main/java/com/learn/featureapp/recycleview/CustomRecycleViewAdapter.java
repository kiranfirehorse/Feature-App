package com.learn.featureapp.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.learn.featureapp.R;

import java.util.List;

/**
 * Created by neeraj on 8/3/16.
 */
public class CustomRecycleViewAdapter extends RecyclerView.Adapter<CustomRecycleViewAdapter.ViewHolder> {
    private List itemList;
    private Context context;

    public CustomRecycleViewAdapter(Context context) {
        this.context = context;
    }

    public List getItemList() {
        return itemList;
    }

    public void setItemList(List itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the cell layout for the recycle view here
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_list_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //load the elements in view holder with appropriate values from listItem.get(position)
    }

    @Override
    public int getItemCount() {
        /**
         uncomment the following code
         if (itemList == null) {
         return 0;
         } else {
         return itemList.size();
         }
         */
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.cell_image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
    }
}
