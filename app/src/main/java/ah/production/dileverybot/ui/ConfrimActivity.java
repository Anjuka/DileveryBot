package ah.production.dileverybot.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import ah.production.dileverybot.R;
import ah.production.dileverybot.adapter.ConfrimAdapter;
import ah.production.dileverybot.model.CartItemsData;
import ah.production.dileverybot.model.ItemsData;
import ah.production.dileverybot.model.OrderData;

public class ConfrimActivity extends AppCompatActivity implements ConfrimAdapter.OnItemClickListner, View.OnClickListener {

    ArrayList<ItemsData> itemsData = new ArrayList<>();
    ArrayList<CartItemsData> cartItemsData = new ArrayList<>();
    ArrayList<ItemsData> itemsDataFruits = new ArrayList<>();

    private RecyclerView rv_scaler;
    private ConfrimAdapter scalerAdapter;
    private Button btn_confrim;
    private ImageView iv_home;
    private ImageView iv_clear_cart;

    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrim);

        progressDialog = new ProgressDialog(this);

        cartItemsData = (ArrayList<CartItemsData>) getIntent().getSerializableExtra("cart_key");
        itemsData = (ArrayList<ItemsData>) getIntent().getSerializableExtra("veg_key");
        itemsDataFruits = (ArrayList<ItemsData>) getIntent().getSerializableExtra("fruit_key");

        Log.d("TAG", "onCreate: " + cartItemsData);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_id = firebaseAuth.getUid();

        iv_home = findViewById(R.id.iv_home);
        rv_scaler = findViewById(R.id.rv_scaler);
        btn_confrim = findViewById(R.id.btn_confrim);
        iv_clear_cart = findViewById(R.id.iv_clear_cart);

        iv_home.setOnClickListener(this);
        iv_clear_cart.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_scaler.setLayoutManager(layoutManager);
        setRV();

        btn_confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfrimMSG(v);
            }
        });
    }

    private void setRV() {
        scalerAdapter = new ConfrimAdapter(getApplicationContext(), cartItemsData, this::onItemClick);
        rv_scaler.setAdapter(scalerAdapter);
    }

    private void showConfrimMSG(View v) {

        Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.custom_dialog_confrim);

        Button btn_cancel = dialog.findViewById(R.id.btn_no);
        Button btn_confirm = dialog.findViewById(R.id.btn_yes);

        DisplayMetrics metrics = v.getContext().getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((3 * width) / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadOrder(cartItemsData);
            }
        });

    }

    private void uploadOrder(ArrayList<CartItemsData> cartItemsData) {

        showProgressDialog("Ordering...");

        final int orderNumber = new Random().nextInt(100000) + 000000;
        Log.d("TAG", "uploadOrder: orderNumber " + orderNumber);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        OrderData orderData = new OrderData(currentTime, currentDate, orderNumber, cartItemsData);
        DocumentReference documentServiceRecords = firebaseFirestore
                .collection("orderRecords").document(user_id)
                .collection(String.valueOf(orderNumber)).document("order");


        documentServiceRecords.set(orderData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgressDialog();
                Toast.makeText(ConfrimActivity.this, "Order Number " + orderNumber + " successfully granted...", Toast.LENGTH_SHORT).show();
                Intent intenHome = new Intent(getApplicationContext(), MainActivity.class);

                cartItemsData.clear();

                itemsData = new ArrayList<>();
                itemsDataFruits = new ArrayList<>();
                itemsData.clear();
                itemsDataFruits.clear();

                intenHome.putExtra("cart_key", cartItemsData);
                intenHome.putExtra("veg_key", itemsData);
                intenHome.putExtra("fruit_key", itemsDataFruits);
                startActivity(intenHome);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Toast.makeText(ConfrimActivity.this, "Ordering Process Failed...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onItemClick(int position) {
        Log.d("ConfrimActivity", "onItemClick Long Pressed: " + position);

        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(ConfrimActivity.this);

        LayoutInflater inflater = getWindow().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_alert, null);
        builder1.setView(dialogView);

        TextView head = dialogView.findViewById(R.id.tv_head);
        Button btn_no = dialogView.findViewById(R.id.btn_no);
        Button btn_remove = dialogView.findViewById(R.id.btn_yes);

        head.setText("Are you sure you want to remove " + cartItemsData.get(position).getItem_name() + " from cart?");
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
                        if (cartItemsData.get(position).getItem_name().equals(itemsData.get(y).getItem_name())) {
                            itemsData.get(y).setIs_check(false);
                            itemsData.get(y).setItem_quanti("");
                        }
                    }
                }

                if (itemsDataFruits != null) {
                    for (int y = 0; y < itemsDataFruits.size(); y++) {
                        if (cartItemsData.get(position).getItem_name().equals(itemsDataFruits.get(y).getItem_name())) {
                            itemsDataFruits.get(y).setIs_check(false);
                            itemsDataFruits.get(y).setItem_quanti("");
                        }
                    }
                }

                cartItemsData.remove(position);

                setRV();
                alert11.dismiss();
            }
        });
        alert11.show();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.iv_clear_cart:

                androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(ConfrimActivity.this);

                LayoutInflater inflater = getWindow().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog_alert, null);
                builder1.setView(dialogView);

                TextView head = dialogView.findViewById(R.id.tv_head);
                Button btn_no = dialogView.findViewById(R.id.btn_no);
                Button btn_remove = dialogView.findViewById(R.id.btn_yes);

                head.setText("Are you sure you want to clear the cart, If you want to remove item wise please press and hold on item");
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

                        if (itemsData != null) {
                            for (int y = 0; y < itemsData.size(); y++) {
                                if (itemsData.get(y).isIs_check()) {
                                    itemsData.get(y).setIs_check(false);
                                    itemsData.get(y).setItem_quanti("");
                                }
                            }
                        }

                        if (itemsDataFruits != null) {
                            for (int y = 0; y < itemsDataFruits.size(); y++) {
                                if (itemsDataFruits.get(y).isIs_check()) {
                                    itemsDataFruits.get(y).setIs_check(false);
                                    itemsDataFruits.get(y).setItem_quanti("");
                                }
                            }
                        }

                        cartItemsData.clear();

                        setRV();
                        alert11.dismiss();
                    }
                });
                alert11.show();


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


    //Progress Bar
    public void showProgressDialog(String message) {

        if (null != progressDialog) {

            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    //Hide Progress Bar
    public void hideProgressDialog() {

        if (null != progressDialog)
            progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(ConfrimActivity.this);

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