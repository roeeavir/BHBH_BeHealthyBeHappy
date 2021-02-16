package com.example.bhbh_behealthybehappy.Utils;

import android.content.Context;
import android.text.Spanned;
import android.text.style.ClickableSpan;
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
        String total_score = "";
        String total_quantity = "";
        holder.listUserItem_LBL_name.setText(m.getItemEntry().getName());

        // Checks the item type and score type, then acts accordingly (setting data and visibility)
        if (m.getItemEntry().getItemType() == Enums.ITEM_THEME.ACTIVITY) {
            setActivity(holder, m);
            total_score = m.getScore_by_quantity() + " green star(s)";
            total_quantity = " minutes";
        } else {
            if (m.getScore_by_quantity() == 0)
                holder.listUserItem_LBL_free.setVisibility(View.VISIBLE);
            else if (m.getItemEntry().getScoreType() == Enums.SCORE.RED_HEART) {
                holder.listUserItem_RTB_redHearts.setRating((float) m.getScore_by_quantity());
                holder.listUserItem_RTB_redHearts.setVisibility(View.VISIBLE);
                total_score = m.getScore_by_quantity() + " red heart(s)";

            } else {
                holder.listUserItem_RTB_blackHearts.setRating((float) m.getScore_by_quantity());
                holder.listUserItem_RTB_blackHearts.setVisibility(View.VISIBLE);
                total_score = m.getScore_by_quantity() + " black heart(s)";

            }
            if (m.getItemEntry().getItemType() == Enums.ITEM_THEME.DRINK) {
                setDrink(holder);
                total_quantity = " milliliters";
            } else {
                setFood(holder);
                total_quantity = " grams";
            }
        }

        // Checks if score is higher than the maximum that can be shown
        if (m.getScore_by_quantity() >= 5) {
            total_score = "Total score:\n" + total_score;
            holder.listUserItem_LBL_score.setText(total_score);
            holder.listUserItem_LBL_score.setVisibility(View.VISIBLE);
        }
        total_quantity = holder.itemView.getResources().getString(R.string.quantity) + "\n" +
                m.getQuantity() + total_quantity;
        holder.listUserItem_LBL_quantity.setText(total_quantity);

        holder.listUserItem_BTN_setQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onSetQuantityClicked(m);
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

    // Setting food views
    private void setFood(@NonNull MyViewHolder holder) {
        holder.listUserItemRLT_background1.setBackgroundResource(R.drawable.item_list_background_red);
        holder.listUserItem_RLT_background2.setBackgroundResource(R.drawable.item_list_background_red);
        holder.listUserItem_RLT_background3.setBackgroundResource(R.drawable.item_list_background_red);
    }

    // Setting Drink views
    private void setDrink(@NonNull MyViewHolder holder) {
        holder.listUserItemRLT_background1.setBackgroundResource(R.drawable.item_list_background_blue);
        holder.listUserItem_RLT_background2.setBackgroundResource(R.drawable.item_list_background_blue);
        holder.listUserItem_RLT_background3.setBackgroundResource(R.drawable.item_list_background_blue);
    }

    // Setting Activity views
    private void setActivity(@NonNull MyViewHolder holder, UserItemEntry m) {
        holder.listUserItem_RTB_greenStars.setRating((float) m.getScore_by_quantity());
        holder.listUserItem_RTB_greenStars.setVisibility(View.VISIBLE);
        holder.listUserItemRLT_background1.setBackgroundResource(R.drawable.item_list_background_green);
        holder.listUserItem_RLT_background2.setBackgroundResource(R.drawable.item_list_background_green);
        holder.listUserItem_RLT_background3.setBackgroundResource(R.drawable.item_list_background_green);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    // allows clicks events to be caught
    public void setClickListener(MyItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
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

        void onSetQuantityClicked(UserItemEntry item);

        void onRemoveItemClicked(View view, UserItemEntry item);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listUserItem_LBL_name;
        TextView listUserItem_LBL_free;
        TextView listUserItem_LBL_quantity;
        TextView listUserItem_LBL_score;
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
            listUserItem_LBL_free = itemView.findViewById(R.id.listUserItem_LBL_free);
            listUserItem_LBL_score = itemView.findViewById(R.id.listUserItem_LBL_score);
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
