package ah.production.dileverybot.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import ah.production.dileverybot.FCMNotification;
import ah.production.dileverybot.OrderActivity;
import ah.production.dileverybot.R;
import ah.production.dileverybot.model.CartItemsData;
import ah.production.dileverybot.model.ItemsData;
import ah.production.dileverybot.model.OrderData;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private ImageView iv_menu;
    private ImageView iv_cam;
    private Button btn_veg;
    private Button btn_fruits;
    private Button btn_grocery;
    private Button btn_medicine;
    private Button btn_customlist;
    private DrawerLayout drawer_layout;
    private NavigationView nav_view;
    private Toolbar toolBar;

    private ArrayList<CartItemsData> cartItemsData = new ArrayList<>();
    ArrayList<ItemsData> itemsData = new ArrayList<>();
    ArrayList<ItemsData> itemsDataFruits = new ArrayList<>();

    FCMNotification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nav_view = findViewById(R.id.nav_view);
        drawer_layout = findViewById(R.id.drawer_layout);
        iv_menu = findViewById(R.id.iv_menu);
        btn_veg = findViewById(R.id.btn_veg);
        btn_fruits = findViewById(R.id.btn_fruits);
        btn_grocery = findViewById(R.id.btn_grocery);
        btn_medicine = findViewById(R.id.btn_medicine);
        btn_customlist = findViewById(R.id.btn_customlist);
        iv_cam = findViewById(R.id.iv_cam);

        iv_menu.setOnClickListener(this);
        btn_veg.setOnClickListener(this);
        btn_fruits.setOnClickListener(this);
        btn_grocery.setOnClickListener(this);
        btn_medicine.setOnClickListener(this);
        btn_customlist.setOnClickListener(this);
        iv_cam.setOnClickListener(this);

        Intent intentBack = new Intent(this, FCMNotification.class);
        startService(intentBack);

        cartItemsData = (ArrayList<CartItemsData>) getIntent().getSerializableExtra("cart_key");
        itemsData = (ArrayList<ItemsData>) getIntent().getSerializableExtra("veg_key");
        itemsDataFruits = (ArrayList<ItemsData>) getIntent().getSerializableExtra("fruit_key");

        setSupportActionBar(toolBar);

        nav_view.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);

        nav_view.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.iv_menu:
                drawer_layout.openDrawer(GravityCompat.START);
                break;

            case R.id.btn_veg:
                Intent intentVeg = new Intent(getApplicationContext(), VegiteblesActivity.class);
                intentVeg.putExtra("cart_key", cartItemsData);
                intentVeg.putExtra("veg_key", itemsData);
                intentVeg.putExtra("fruit_key", itemsDataFruits);
                startActivity(intentVeg);
                break;

            case R.id.btn_fruits:
                Intent intentVFru = new Intent(getApplicationContext(), FruitsActivity.class);
                intentVFru.putExtra("cart_key", cartItemsData);
                intentVFru.putExtra("veg_key", itemsData);
                intentVFru.putExtra("fruit_key", itemsDataFruits);
                startActivity(intentVFru);
                break;

            case R.id.btn_grocery:
                showCustomDialog(v);
                break;

            case R.id.btn_medicine:
                showCustomDialog(v);
                break;

            case R.id.btn_customlist:
                showCustomDialog(v);
                break;

                case R.id.iv_cam:
                    Intent intentCam = new Intent(getApplicationContext(), CamActivity.class);
                    startActivity(intentCam);
                break;
        }

    }

    private void showCustomDialog(View v) {

        Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.custom_dialog);

        DisplayMetrics metrics = v.getContext().getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((4 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getWindow().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_alert, null);
        builder1.setView(dialogView);

        TextView head = dialogView.findViewById(R.id.tv_head);
        Button btn_no = dialogView.findViewById(R.id.btn_no);
        Button btn_remove = dialogView.findViewById(R.id.btn_yes);

        btn_remove.setText("Yes");

        head.setText("This operation will close the app");
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
            }
        });
        alert11.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_orders:
                finish();
                Intent intentOrder = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intentOrder);
                break;

            case R.id.nav_cart:
                break;

            case R.id.nav_user_about_us:
                break;

            case R.id.nav_guid:
                break;

            case R.id.nav_logout:
                //showLogoutConrim();
                break;
        }

        return true;
    }
}