package com.example.bhbh_behealthybehappy.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;
import com.example.bhbh_behealthybehappy.Models.ItemEntry;
import com.example.bhbh_behealthybehappy.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private List<ItemEntry> items;
    private LayoutInflater mInflater;
    private MyItemClickListener mClickListener;

    public ItemAdapter(Context context, List<ItemEntry> _items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = _items;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    //    TextView listItem_LBL_amount;
//    TextView listItem_LBL_info;
//    MaterialButton main_BTN_readMore;
//    ShapeableImageView listItem_RTB_redHearts;
//    ShapeableImageView listItem_RTB_blackHearts;
//    ShapeableImageView listItem_RTB_greenStars;
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("pttt", "Position = " + position);
        ItemEntry m = items.get(position);
        holder.listItem_LBL_name.setText(m.getItemType().toString() + "\n" + m.getName());
        if (m.getItemType() == Enums.ITEM_THEME.DRINK)
            holder.listItem_LBL_amount.setText("Weight: " + m.getAmount());
        else{
            holder.listItem_LBL_weight.setText("Weight: " + m.getWeight());
            if(m.getScoreType() == Enums.SCORE.RED_HEART)
                holder.listItem_RTB_redHearts.setRating((float )m.getScore());
        }

//        holder.listItem_LBL_amount.setText("" + m.get);


//        holder.main_BTN_readMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mClickListener != null) {
//                    mClickListener.onReadMoreClicked(v, m);
//                }
//            }
//        });
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

        TextView listItem_LBL_name;
        TextView listItem_LBL_amount;
        TextView listItem_LBL_info;
        TextView listItem_LBL_weight;
        MaterialButton main_BTN_readMore;
        AppCompatRatingBar listItem_RTB_redHearts;
        AppCompatRatingBar listItem_RTB_blackHearts;
        AppCompatRatingBar listItem_RTB_greenStars;


        public MyViewHolder(View itemView) {
            super(itemView);
            listItem_LBL_name = itemView.findViewById(R.id.listItem_LBL_name);
            listItem_LBL_weight = itemView.findViewById(R.id.listItem_LBL_weight);
            listItem_LBL_amount = itemView.findViewById(R.id.listItem_LBL_amount);
            listItem_LBL_info = itemView.findViewById(R.id.listItem_LBL_info);
            main_BTN_readMore = itemView.findViewById(R.id.main_BTN_readMore);
            listItem_RTB_redHearts = itemView.findViewById(R.id.listItem_RTB_redHearts);
            listItem_RTB_blackHearts = itemView.findViewById(R.id.listItem_RTB_blackHearts);
            listItem_RTB_greenStars = itemView.findViewById(R.id.listItem_RTB_greenStars);

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
