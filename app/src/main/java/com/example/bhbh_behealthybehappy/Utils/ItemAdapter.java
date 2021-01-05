package com.example.bhbh_behealthybehappy.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhbh_behealthybehappy.Models.ItemEntry;
import com.example.bhbh_behealthybehappy.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>{

    private List<ItemEntry> items;
    private LayoutInflater mInflater;
    private MyItemClickListener mClickListener;

    public ItemAdapter(Context context, List<ItemEntry> items) {
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    // convenience method for getting data at click position
    ItemEntry getItem(int id) {
        return items.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(MyItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
        void onReadMoreClicked(View view, ItemEntry item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item_LBL_name;
        TextView item_LBL_info;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
