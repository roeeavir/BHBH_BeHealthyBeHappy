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
import com.example.bhbh_behealthybehappy.Models.ItemEntry;
import com.example.bhbh_behealthybehappy.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private List<ItemEntry> items;
    private List<ItemEntry> itemsCopy;
    private LayoutInflater mInflater;
    private MyItemClickListener mClickListener;

    public ItemAdapter(Context context, List<ItemEntry> _items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = _items;
        this.itemsCopy = new ArrayList<>(items);// Soft copy of the original list
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("pttt", "Position = " + position);
        ItemEntry m = items.get(position);
        holder.listItem_LBL_name.setText(m.getName());
        holder.listItem_LBL_info.setText(m.toString());

        if (m.getItemType() == Enums.ITEM_THEME.ACTIVITY)
            setActivity(holder, m);
        else {
            if (m.getScore() == 0)
                holder.listItem_LBL_free.setVisibility(View.VISIBLE);
            else if (m.getScoreType() == Enums.SCORE.RED_HEART) {
                holder.listItem_RTB_redHearts.setRating((float) m.getScore());
//                holder.listItem_RTB_redHearts.setNumStars((int) m.getScore());
                holder.listItem_RTB_redHearts.setVisibility(View.VISIBLE);

            } else {
                holder.listItem_RTB_blackHearts.setRating((float) m.getScore());
                holder.listItem_RTB_blackHearts.setVisibility(View.VISIBLE);
            }
            if (m.getItemType() == Enums.ITEM_THEME.DRINK) {
                setDrink(holder, m);
            } else
                setFood(holder, m);

        }

        holder.listItem_BTN_readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onReadMoreClicked(v, m);
                }
            }
        });

        holder.listItem_BTN_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onAddItemClicked(v, m);
                }
            }
        });
    }

    private void setFood(@NonNull MyViewHolder holder, ItemEntry m) {
        holder.listItem_LBL_foodWeight.setText(m.getItemType().toString() + " score by 100 grams");
        holder.listItem_LBL_foodWeight.setVisibility(View.VISIBLE);
        holder.listItem_RLT_background1.setBackgroundResource(R.drawable.item_list_background_red);
        holder.listItem_RLT_background2.setBackgroundResource(R.drawable.item_list_background_red);
        holder.listItem_RLT_background3.setBackgroundResource(R.drawable.item_list_background_red);

    }

    private void setDrink(@NonNull MyViewHolder holder, ItemEntry m) {
        holder.listItem_LBL_drinkAmount.setText(m.getItemType().toString() + " score by 100 milliliter");
        holder.listItem_LBL_drinkAmount.setVisibility(View.VISIBLE);
        holder.listItem_RLT_background1.setBackgroundResource(R.drawable.item_list_background_blue);
        holder.listItem_RLT_background2.setBackgroundResource(R.drawable.item_list_background_blue);
        holder.listItem_RLT_background3.setBackgroundResource(R.drawable.item_list_background_blue);
    }

    private void setActivity(@NonNull MyViewHolder holder, ItemEntry m) {
        holder.listItem_LBL_activityTime.setText(m.getItemType().toString() + " score by 15 minutes");
        holder.listItem_LBL_activityTime.setVisibility(View.VISIBLE);
        holder.listItem_RTB_greenStars.setRating((float) m.getScore());
        holder.listItem_RTB_greenStars.setVisibility(View.VISIBLE);
        holder.listItem_RLT_background1.setBackgroundResource(R.drawable.item_list_background_green);
        holder.listItem_RLT_background2.setBackgroundResource(R.drawable.item_list_background_green);
        holder.listItem_RLT_background3.setBackgroundResource(R.drawable.item_list_background_green);
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

    public void filter(String text) {// Filters items in list
        items.clear();
        if (text.isEmpty()) {
            items.addAll(itemsCopy);
        } else {
            text = text.toLowerCase();
            for (ItemEntry item : itemsCopy) {
                if (item.getName().toLowerCase().contains(text)) { // Shows items by name
                    items.add(item);
                } else if (text.equals("free")) { // Shows all free items
                    if (item.getScore() == 0) {
                        items.add(item);
                    }
                }
                try {
                    if (item.getScore() == Double.parseDouble(text)) { // Shows items by score
                        items.add(item);
                    }
                } catch (Exception e) {

                }
            }
        }
        notifyDataSetChanged();
    }

    // parent activity will implement this method to respond to click events
    public interface MyItemClickListener {
        void onItemClick(View view, int position);

        void onReadMoreClicked(View view, ItemEntry item);

        void onAddItemClicked(View view, ItemEntry item);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listItem_LBL_name;
        TextView listItem_LBL_drinkAmount;
        TextView listItem_LBL_info;
        TextView listItem_LBL_foodWeight;
        TextView listItem_LBL_activityTime;
        TextView listItem_LBL_free;
        MaterialButton listItem_BTN_readMore;
        MaterialButton listItem_BTN_addItem;
        AppCompatRatingBar listItem_RTB_redHearts;
        AppCompatRatingBar listItem_RTB_blackHearts;
        AppCompatRatingBar listItem_RTB_greenStars;
        RelativeLayout listItem_RLT_background1;
        RelativeLayout listItem_RLT_background2;
        RelativeLayout listItem_RLT_background3;


        public MyViewHolder(View itemView) {
            super(itemView);
            listItem_LBL_name = itemView.findViewById(R.id.listItem_LBL_name);
            listItem_LBL_foodWeight = itemView.findViewById(R.id.listItem_LBL_foodWeight);
            listItem_LBL_drinkAmount = itemView.findViewById(R.id.listItem_LBL_drinkAmount);
            listItem_LBL_info = itemView.findViewById(R.id.listItem_LBL_info);
            listItem_LBL_activityTime = itemView.findViewById(R.id.listItem_LBL_activityTime);
            listItem_LBL_free = itemView.findViewById(R.id.listItem_LBL_free);
            listItem_BTN_readMore = itemView.findViewById(R.id.listItem_BTN_readMore);
            listItem_BTN_addItem = itemView.findViewById(R.id.listItem_BTN_addItem);
            listItem_RTB_redHearts = itemView.findViewById(R.id.listItem_RTB_redHearts);
            listItem_RTB_blackHearts = itemView.findViewById(R.id.listItem_RTB_blackHearts);
            listItem_RTB_greenStars = itemView.findViewById(R.id.listItem_RTB_greenStars);
            listItem_RLT_background1 = itemView.findViewById(R.id.listItem_RLT_background1);
            listItem_RLT_background2 = itemView.findViewById(R.id.listItem_RLT_background2);
            listItem_RLT_background3 = itemView.findViewById(R.id.listItem_RLT_background3);

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
