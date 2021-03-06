package ah.production.dileverybot.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ah.production.dileverybot.R;
import ah.production.dileverybot.adapter.CartAdapter;
import ah.production.dileverybot.adapter.VegAdapter;
import ah.production.dileverybot.model.CartItemsData;
import ah.production.dileverybot.model.ItemsData;
import ah.production.dileverybot.util.SwipeController;

public class VegiteblesActivity extends AppCompatActivity implements VegAdapter.OnItemClickListner, View.OnClickListener {

    ArrayList<ItemsData> itemsData = new ArrayList<>();
    ArrayList<CartItemsData> cartItems = new ArrayList<>();
    ArrayList<ItemsData> itemsDataFruits = new ArrayList<>();

    private VegAdapter vegAdapter;
    private CartAdapter cartAdapter;
    private RecyclerView rv_vegetables;
    private ImageView iv_cart;
    private ImageView iv_home;
    private GestureDetector gesture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegitebles);

        SwipeController.MyStatusFragment = "VegiteblesActivity";

        rv_vegetables = findViewById(R.id.rv_scaler);
        iv_cart = findViewById(R.id.iv_clear_cart);
        iv_home = findViewById(R.id.iv_home);

        iv_cart.setOnClickListener(this);
        iv_home.setOnClickListener(this);


        cartItems = (ArrayList<CartItemsData>) getIntent().getSerializableExtra("cart_key");
        itemsData = (ArrayList<ItemsData>) getIntent().getSerializableExtra("veg_key");
        itemsDataFruits = (ArrayList<ItemsData>) getIntent().getSerializableExtra("fruit_key");

        if (cartItems == null) {

            cartItems = new ArrayList<>();

        }

        String[] vegetableList = getResources().getStringArray(R.array.vegetables);

        if (itemsData == null || itemsData.size() == 0) {

            itemsData = new ArrayList<>();

            for (int x = 0; x < vegetableList.length; x++) {
                ItemsData vegItem = new ItemsData(vegetableList[x], false, -1, "");
                itemsData.add(vegItem);
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_vegetables.setLayoutManager(layoutManager);
        vegAdapter = new VegAdapter(getApplicationContext(), itemsData, VegiteblesActivity.this::onItemClick);
        rv_vegetables.setAdapter(vegAdapter);

        Log.d("TAG", "onCreate: " + itemsData);
    }


    @Override
    public void onItemClick(int position) {
        Log.d("TAG", "onItemClick: " + position);

        if (itemsData.get(position).isIs_check()) {
            itemsData.get(position).setIs_check(false);

            for (int y = 0; y < cartItems.size(); y++) {
                if (cartItems.get(y).getItem_name().equals(itemsData.get(position).getItem_name())) {
                    cartItems.remove(y);
                }
            }

        } else {
            itemsData.get(position).setIs_check(true);
            CartItemsData cartItemsData = new CartItemsData(itemsData.get(position).getItem_name(), itemsData.get(position).isIs_check(), itemsData.get(position).getPosition(), itemsData.get(position).getItem_quanti());

            if (cartItems == null){
                cartItems = new ArrayList<>();
            }
            cartItems.add(cartItemsData);
        }

        Log.d("TAG", "onItemClick: " + cartItems);
        vegAdapter = new VegAdapter(getApplicationContext(), itemsData, VegiteblesActivity.this::onItemClick);
        rv_vegetables.setAdapter(vegAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.iv_clear_cart:
                showCart(v, cartItems);
                break;

                case R.id.iv_home:

                    finish();
                    Intent intenHome = new Intent(getApplicationContext(), MainActivity.class);
                    intenHome.putExtra("cart_key", cartItems);
                    intenHome.putExtra("veg_key", itemsData);
                    intenHome.putExtra("fruit_key", itemsDataFruits);
                    startActivity(intenHome);

                break;

        }
    }

    private void showCart(View v, ArrayList<CartItemsData> cartItems_) {

        Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.custom_dialog_cart);

        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        ListView rv_cart = dialog.findViewById(R.id.rv_cart);
        Button btn_weight = dialog.findViewById(R.id.btn_weight);

        cartAdapter = new CartAdapter(VegiteblesActivity.this, cartItems_);
        rv_cart.setAdapter(cartAdapter);

        DisplayMetrics metrics = v.getContext().getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((3 * width) / 4, (3 * height) / 4);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intentWeight = new Intent(getApplicationContext(), WeightActivity.class);
                intentWeight.putExtra("cart_key", cartItems_);
                intentWeight.putExtra("veg_key", itemsData);
                intentWeight.putExtra("fruit_key", itemsDataFruits);
                startActivity(intentWeight);
            }
        });

        rv_cart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "onItemLongClick: "+ position);

                androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(VegiteblesActivity.this);

                LayoutInflater inflater = getWindow().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog_alert, null);
                builder1.setView(dialogView);

                TextView head = dialogView.findViewById(R.id.tv_head);
                Button btn_no = dialogView.findViewById(R.id.btn_no);
                Button btn_remove = dialogView.findViewById(R.id.btn_yes);

                head.setText("Are you sure you want to remove " + cartItems_.get(position).getItem_name() + " from cart?");
                androidx.appcompat.app.AlertDialog alert11 = builder1.create();

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert11.dismiss();
                    }
                });

                btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("TAG", "onClick: pos " + position);

                        if (itemsData != null) {
                            for (int y = 0; y < itemsData.size(); y++) {
                                if (cartItems_.get(position).getItem_name().equals(itemsData.get(y).getItem_name())) {
                                    itemsData.get(y).setIs_check(false);
                                    itemsData.get(y).setItem_quanti("");
                                }
                            }
                        }

                        if (itemsDataFruits != null) {
                            for (int y = 0; y < itemsDataFruits.size(); y++) {
                                if (cartItems_.get(position).getItem_name().equals(itemsDataFruits.get(y).getItem_name())) {
                                    itemsDataFruits.get(y).setIs_check(false);
                                    itemsDataFruits.get(y).setItem_quanti("");
                                }
                            }
                        }

/*
                        for (int y = 0; y < itemsData.size(); y++) {
                            if (cartItems_.get(position).getItem_name().equals(itemsData.get(y).getItem_name())) {
                                itemsData.get(y).setIs_check(false);
                            }
                        }
*/

                        cartItems_.remove(position);
                        cartAdapter = new CartAdapter(VegiteblesActivity.this, cartItems_);
                        rv_cart.setAdapter(cartAdapter);

                        vegAdapter = new VegAdapter(getApplicationContext(), itemsData, VegiteblesActivity.this::onItemClick);
                        rv_vegetables.setAdapter(vegAdapter);

                        alert11.dismiss();
                    }
                });
                alert11.show();

                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(VegiteblesActivity.this);

        LayoutInflater inflater = getWindow().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_alert, null);
        builder1.setView(dialogView);

        TextView head = dialogView.findViewById(R.id.tv_head);
        Button btn_no = dialogView.findViewById(R.id.btn_no);
        Button btn_remove = dialogView.findViewById(R.id.btn_yes);

        btn_remove.setText("Yes");

        head.setText("This operation will navigate to Home page");
        androidx.appcompat.app.AlertDialog alert11 = builder1.create();

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
            }
        });

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
                finish();
                Intent intenHome = new Intent(getApplicationContext(), MainActivity.class);
                intenHome.putExtra("cart_key", cartItems);
                intenHome.putExtra("veg_key", itemsData);
                intenHome.putExtra("fruit_key", itemsDataFruits);
                startActivity(intenHome);
            }
        });
        alert11.show();
    }
}