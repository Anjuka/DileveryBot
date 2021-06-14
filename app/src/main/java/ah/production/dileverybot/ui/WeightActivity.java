package ah.production.dileverybot.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ah.production.dileverybot.R;
import ah.production.dileverybot.adapter.Cart_Scaler_Adapter;
import ah.production.dileverybot.adapter.ScalerAdapter;
import ah.production.dileverybot.model.CartItemsData;
import ah.production.dileverybot.model.ItemsData;

public class WeightActivity extends AppCompatActivity implements ScalerAdapter.OnItemClickListner, View.OnClickListener {

    ArrayList<ItemsData> itemsData = new ArrayList<>();
    ArrayList<CartItemsData> cartItemsData = new ArrayList<>();
    ArrayList<ItemsData> itemsDataFruits = new ArrayList<>();

    private RecyclerView rv_scaler;
    private ScalerAdapter scalerAdapter;
    private Cart_Scaler_Adapter cartAdapter;
    private ImageView iv_cart;
    private ImageView iv_home;
    private boolean isEmpty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        cartItemsData = (ArrayList<CartItemsData>) getIntent().getSerializableExtra("cart_key");
        itemsData = (ArrayList<ItemsData>) getIntent().getSerializableExtra("veg_key");
        itemsDataFruits = (ArrayList<ItemsData>) getIntent().getSerializableExtra("fruit_key");

        Log.d("TAG", "onCreate: " + cartItemsData);

        rv_scaler = findViewById(R.id.rv_scaler);
        iv_cart = findViewById(R.id.iv_clear_cart);
        iv_home = findViewById(R.id.iv_home);

        iv_cart.setOnClickListener(this);
        iv_home.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_scaler.setLayoutManager(layoutManager);
        scalerAdapter = new ScalerAdapter(getApplicationContext(), cartItemsData, WeightActivity.this::onItemClick);
        rv_scaler.setAdapter(scalerAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Log.d("TAG", "onItemClick: " + position);

        Dialog dialog = new Dialog(WeightActivity.this);
        dialog.setContentView(R.layout.custom_dialog_scaler);

        Button update = dialog.findViewById(R.id.btn_add_scale);
        EditText et_weight = dialog.findViewById(R.id.et_weight);
        ConstraintLayout cv_main = dialog.findViewById(R.id.cv_main);

        DisplayMetrics metrics = WeightActivity.this.getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((3 * width) / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemsData.get(position).setItem_quanti(et_weight.getText().toString());

                if (itemsData != null) {

                    for (int x = 0; x < itemsData.size(); x++) {
                        if (itemsData.get(x).getItem_name().equals(cartItemsData.get(position))) {
                            itemsData.get(x).setItem_quanti(et_weight.getText().toString());
                        }
                    }
                }

                if (itemsDataFruits != null) {
                    for (int z = 0; z < itemsDataFruits.size(); z++) {
                        if (itemsDataFruits.get(z).getItem_name().equals(cartItemsData.get(position))) {
                            itemsDataFruits.get(z).setItem_quanti(et_weight.getText().toString());
                        }
                    }
                }

                scalerAdapter = new ScalerAdapter(getApplicationContext(), cartItemsData, WeightActivity.this::onItemClick);
                rv_scaler.setAdapter(scalerAdapter);
                hideKeyboard(cv_main);
                dialog.dismiss();
            }
        });

    }

    //Hide Keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.iv_clear_cart:
                showCart(v, cartItemsData);
                break;

            case R.id.iv_home:

                finish();
                Intent intenHome = new Intent(getApplicationContext(), MainActivity.class);
                intenHome.putExtra("cart_key", cartItemsData);
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

        btn_weight.setText("Checkout >>");

        cartAdapter = new Cart_Scaler_Adapter(WeightActivity.this, cartItems_);
        rv_cart.setAdapter(cartAdapter);

        dialog.setCancelable(false);

        DisplayMetrics metrics = v.getContext().getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((3 * width) / 4, (3 * height) / 4);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEmpty = false;
                dialog.dismiss();
            }
        });

        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int x =0; x < cartItems_.size(); x++){
                    if (cartItems_.get(x).getItem_quanti().equals("")){
                        isEmpty = true;
                    }
                }

                if (isEmpty){
                    Toast.makeText(getApplicationContext(), "Please set quantity for all cart items", Toast.LENGTH_LONG).show();
                }
                else {
                    dialog.dismiss();
                    Intent intentWeight = new Intent(getApplicationContext(), ConfrimActivity.class);
                    intentWeight.putExtra("cart_key", cartItems_);
                    intentWeight.putExtra("veg_key", itemsData);
                    intentWeight.putExtra("fruit_key", itemsDataFruits);
                    startActivity(intentWeight);
                    isEmpty = false;
                }
            }
        });

        rv_cart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "onItemLongClick: " + position);

                androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(WeightActivity.this);

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

                        cartItemsData.remove(position);
                        cartAdapter = new Cart_Scaler_Adapter(WeightActivity.this, cartItemsData);
                        rv_cart.setAdapter(cartAdapter);

                        scalerAdapter = new ScalerAdapter(getApplicationContext(), cartItemsData, WeightActivity.this::onItemClick);
                        rv_scaler.setAdapter(scalerAdapter);

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
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(WeightActivity.this);

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
                intenHome.putExtra("cart_key", cartItemsData);
                intenHome.putExtra("veg_key", itemsData);
                intenHome.putExtra("fruit_key", itemsDataFruits);
                startActivity(intenHome);
            }
        });
        alert11.show();
    }

}