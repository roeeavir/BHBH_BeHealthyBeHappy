package com.example.bhbh_behealthybehappy.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;
import com.example.bhbh_behealthybehappy.Models.UserItemEntry;
import com.example.bhbh_behealthybehappy.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.MyViewHolder> {

    private List<UserItemEntry> items;
    private List<UserItemEntry> itemsCopy;
    private LayoutInflater mInflater;
    private MyItemClickListener mClickListener;

    public UserItemAdapter(Context context, List<UserItemEntry> _user_items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = _user_items;
        this.itemsCopy = new ArrayList<>(items);// Soft copy of the original list
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_user_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("pttt", "Position = " + position);
        UserItemEntry m = items.get(position);
        holder.listUserItem_LBL_name.setText(m.getItemEntry().getName());
        holder.listUserItem_LBL_time.setText(m.getItemEntry().toString());
        holder.listUserItem_LBL_quantity.setText(holder.itemView.getResources().getString(R.string.quantity) + " " + m.getQuantity());
        if (m.getItemEntry().getItemType() == Enums.ITEM_THEME.ACTIVITY)
            setActivity(holder, m);
        else {
            if (m.getScore_by_quantity() == 0)
                holder.listUserItem_LBL_free.setVisibility(View.VISIBLE);
            else if (m.getItemEntry().getScoreType() == Enums.SCORE.RED_HEART) {
                holder.listUserItem_RTB_redHearts.setRating((float) m.getScore_by_quantity());
                holder.listUserItem_RTB_redHearts.setVisibility(View.VISIBLE);

            } else {
                holder.listUserItem_RTB_blackHearts.setRating((float) m.getScore_by_quantity());
                holder.listUserItem_RTB_blackHearts.setVisibility(View.VISIBLE);
            }
            if (m.getItemEntry().getItemType() == Enums.ITEM_THEME.DRINK) {
                setDrink(holder, m);
            } else
                setFood(holder, m);


        }

        holder.listUserItem_BTN_setQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onSetQuantityClicked(v, m);
                }
            }
        });

        holder.listUserItem_BTN_removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onRemoveItemClicked(v, m);
                }
            }
        });


    }

    private void setFood(@NonNull MyViewHolder holder, UserItemEntry m) {
        holder.listUserItemRLT_background1.setBackgroundResource(R.drawable.item_list_background_red);
        holder.listUserItem_RLT_background2.setBackgroundResource(R.drawable.item_list_background_red);
        holder.listUserItem_RLT_background3.setBackgroundResource(R.drawable.item_list_background_red);
        holder.listUserItem_LBL_quantity.setText(holder.listUserItem_LBL_quantity.getText() + " grams");
    }

    private void setDrink(@NonNull MyViewHolder holder, UserItemEntry m) {
        holder.listUserItemRLT_background1.setBackgroundResource(R.drawable.item_list_background_blue);
        holder.listUserItem_RLT_background2.setBackgroundResource(R.drawable.item_list_background_blue);
        holder.listUserItem_RLT_background3.setBackgroundResource(R.drawable.item_list_background_blue);
        holder.listUserItem_LBL_quantity.setText(holder.listUserItem_LBL_quantity.getText() + " milliliters");
    }

    private void setActivity(@NonNull MyViewHolder holder, UserItemEntry m) {
        holder.listUserItem_RTB_greenStars.setRating((float) m.getScore_by_quantity());
        holder.listUserItem_RTB_greenStars.setVisibility(View.VISIBLE);
        holder.listUserItemRLT_background1.setBackgroundResource(R.drawable.item_list_background_green);
        holder.listUserItem_RLT_background2.setBackgroundResource(R.drawable.item_list_background_green);
        holder.listUserItem_RLT_background3.setBackgroundResource(R.drawable.item_list_background_green);
        holder.listUserItem_LBL_quantity.setText(holder.listUserItem_LBL_quantity.getText() + " minutes");
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    // convenience method for getting data at click position
    UserItemEntry getItem(int id) {
        return items.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(MyItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void filter(String text) {// Filters items in list
        items.clear();
        if (text.isEmpty()) {
            items.addAll(itemsCopy);
        } else {
            text = text.toLowerCase();
            for (UserItemEntry item : itemsCopy) {
                if (item.getItemEntry().getName().toLowerCase().contains(text)) { // Shows items by name
                    items.add(item);
                } else if (text.equals("free")) { // Shows all free items
                    if (item.getScore_by_quantity() == 0) {
                        items.add(item);
                    }
                }
                try {
                    if (item.getScore_by_quantity() == Double.parseDouble(text)) { // Shows items by score
                        items.add(item);
                    }
                } catch (Exception e) {

                }
            }
        }
        notifyDataSetChanged();
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void removeItem(UserItemEntry itemEntry) {
        items.remove(itemEntry);

        notifyDataSetChanged();
    }


    // parent activity will implement this method to respond to click events
    public interface MyItemClickListener {
        void onItemClick(View view, int position);

        void onSetQuantityClicked(View view, UserItemEntry item);

        void onRemoveItemClicked(View view, UserItemEntry item);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listUserItem_LBL_name;
        TextView listUserItem_LBL_free;
        TextView listUserItem_LBL_time;
        TextView listUserItem_LBL_quantity;
        MaterialButton listUserItem_BTN_setQuantity;
        MaterialButton listUserItem_BTN_removeItem;
        AppCompatRatingBar listUserItem_RTB_redHearts;
        AppCompatRatingBar listUserItem_RTB_blackHearts;
        AppCompatRatingBar listUserItem_RTB_greenStars;
        RelativeLayout listUserItemRLT_background1;
        RelativeLayout listUserItem_RLT_background2;
        RelativeLayout listUserItem_RLT_background3;


        public MyViewHolder(View itemView) {
            super(itemView);
            listUserItem_LBL_name = itemView.findViewById(R.id.listUserItem_LBL_name);
            listUserItem_LBL_time = itemView.findViewById(R.id.listUserItem_LBL_time);
            listUserItem_LBL_free = itemView.findViewById(R.id.listUserItem_LBL_free);
            listUserItem_LBL_quantity = itemView.findViewById(R.id.listUserItem_LBL_quantity);
            listUserItem_BTN_setQuantity = itemView.findViewById(R.id.listUserItem_BTN_setQuantity);
            listUserItem_BTN_removeItem = itemView.findViewById(R.id.listUserItem_BTN_removeItem);
            listUserItem_RTB_redHearts = itemView.findViewById(R.id.listUserItem_RTB_redHearts);
            listUserItem_RTB_blackHearts = itemView.findViewById(R.id.listUserItem_RTB_blackHearts);
            listUserItem_RTB_greenStars = itemView.findViewById(R.id.listUserItem_RTB_greenStars);
            listUserItemRLT_background1 = itemView.findViewById(R.id.listUserItem_RLT_background1);
            listUserItem_RLT_background2 = itemView.findViewById(R.id.listUserItem_RLT_background2);
            listUserItem_RLT_background3 = itemView.findViewById(R.id.listUserItem_RLT_background3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }
    }

}
