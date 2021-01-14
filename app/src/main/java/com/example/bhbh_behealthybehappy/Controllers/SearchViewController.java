package com.example.bhbh_behealthybehappy.Controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bhbh_behealthybehappy.Activities.CustomeItemActivity;
import com.example.bhbh_behealthybehappy.Activities.SearchActivity;
import com.example.bhbh_behealthybehappy.Models.ItemEntry;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;
import com.example.bhbh_behealthybehappy.Utils.FirebaseHelper;
import com.example.bhbh_behealthybehappy.Utils.ItemAdapter;
import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.DATES_REF;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.ITEMS_REF;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.USERS_REF;

public class SearchViewController {// Search Activity Controller Class


    // Variables
    private Context context;

    private SearchView search_SRC_searchView;

    private ImageButton search_IMB_back;
    private ImageButton search_IBT_addWater;
    private ImageView search_IMG_background;
    private Button search_BTN_customItem;


    private Enums.ITEM_THEME theme = Enums.ITEM_THEME.FOOD;

    private ArrayList<ItemEntry> items;
    private HashMap<String, ItemEntry> itemsMap = new HashMap<>();

    private RecyclerView search_LST_list;

    private ItemAdapter item_adapter;

    private String date;

    public SearchViewController(Context context) {
        this.context = context;

        findViews();
        initViews();


        generateItems();


    }

    private void findViews() {
        search_SRC_searchView = ((SearchActivity) context).findViewById(R.id.search_SRC_searchView);
        search_IMB_back = ((SearchActivity) context).findViewById(R.id.search_IMB_back);
        search_IMG_background = ((SearchActivity) context).findViewById(R.id.search_IMG_background);
        search_BTN_customItem = ((SearchActivity) context).findViewById(R.id.search_BTN_customItem);
        search_IBT_addWater = ((SearchActivity) context).findViewById(R.id.search_IBT_addWater);
        search_LST_list = ((SearchActivity) context).findViewById(R.id.search_LST_list);
    }

    private void setItemsInList() {

        items = new ArrayList<>(itemsMap.values());

        item_adapter = new ItemAdapter(((SearchActivity) context), items);

        item_adapter.setClickListener(new ItemAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyHelper.getInstance().toast(items.get(position).getName());
            }

            @Override
            public void onReadMoreClicked(View view, ItemEntry item) {
                openInfo(item);
            }

            @Override
            public void onAddItemClicked(View view, ItemEntry item) {
                view.setEnabled(false);
                addItem(item);
            }
        });

        search_LST_list.setLayoutManager(new LinearLayoutManager(((SearchActivity) context)));
        search_LST_list.setAdapter(item_adapter);
    }

    private void addItem(ItemEntry item) {// Adds item to user database
        FirebaseUser user = FirebaseHelper.getInstance().getUser();
        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);

        myRef.child(user.getUid()).child(DATES_REF).child(date).child(item.getName()).setValue(item.getName());

    }

    private void openInfo(ItemEntry item) {// Opens a dialog window containing item notes
        new AlertDialog.Builder(((SearchActivity) context))
                .setTitle(item.getName())
                .setMessage(item.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private void generateItems() {
        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(ITEMS_REF);
//        myRef.setValue(null);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot postSnapshot : dataSnapshot.child(theme.toString()).getChildren()) {
                    ItemEntry temp = postSnapshot.getValue(ItemEntry.class);
                    itemsMap.put(temp.getName(), temp);
                    Log.d("pttt", "Value is: " + temp.getName());
                }
                if (itemsMap != null)
                    setItemsInList();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });

//        items.add(new ItemEntry("ma", Enums.SCORE.RED_HEART, "mi"));


    }

    private void initViews() {
        search_IMB_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchActivity) context).finish();
            }
        });
        search_BTN_customItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomActivity();
//                ((SearchActivity) context).recreate();
            }
        });

        search_SRC_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                item_adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                item_adapter.filter(newText);
                return true;
            }
        });


    }

    private void openCustomActivity() {
        search_BTN_customItem.setEnabled(false);
        Intent myIntent = new Intent(context, CustomeItemActivity.class);
        myIntent.putExtra(CustomeItemActivity.ADD_CUSTOM_ITEM, theme);
        context.startActivity(myIntent);// Opens winner activity
        search_BTN_customItem.setEnabled(true);
    }


    public void updateTheme(int id, Enums.ITEM_THEME th) {
        theme = th;
        updateSearch_IMG_background(id);
        updateSearch_BTN_customItem();
        updateHintSearch_SRC_searchView();
    }

    private void updateSearch_IMG_background(int id) {
        Glide.with(context).load(id).into(search_IMG_background);
    }

    private void updateSearch_BTN_customItem() {
        String s = search_BTN_customItem.getText().toString() + " " + theme.toString();
        search_BTN_customItem.setText(s);
    }

    private void updateHintSearch_SRC_searchView() {
        String s = search_SRC_searchView.getQueryHint() + " " + theme.toString();
        search_SRC_searchView.setQueryHint(s);
    }

    public void setAddWaterButtonToVisible() {
        search_IBT_addWater.setVisibility(View.VISIBLE);
    }

    public void setDate(String s) {
        this.date = s;
    }
}
