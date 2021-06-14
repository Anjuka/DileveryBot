package ah.production.dileverybot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

import ah.production.dileverybot.adapter.ScalerAdapter;
import ah.production.dileverybot.model.CartItemsData;
import ah.production.dileverybot.model.ItemsData;

public class ConfrimActivity extends AppCompatActivity implements ScalerAdapter.OnItemClickListner {

    ArrayList<ItemsData> itemsData = new ArrayList<>();
    ArrayList<CartItemsData> cartItemsData = new ArrayList<>();
    ArrayList<ItemsData> itemsDataFruits = new ArrayList<>();

    private RecyclerView rv_scaler;
    private ScalerAdapter scalerAdapter;
    private Button btn_confrim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrim);

        cartItemsData = (ArrayList<CartItemsData>) getIntent().getSerializableExtra("cart_key");
        itemsData = (ArrayList<ItemsData>) getIntent().getSerializableExtra("veg_key");
        itemsDataFruits = (ArrayList<ItemsData>) getIntent().getSerializableExtra("fruit_key");

        Log.d("TAG", "onCreate: " + cartItemsData);

        rv_scaler = findViewById(R.id.rv_scaler);
        btn_confrim = findViewById(R.id.btn_confrim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_scaler.setLayoutManager(layoutManager);
        scalerAdapter = new ScalerAdapter(getApplicationContext(), cartItemsData, ConfrimActivity.this::onItemClick);
        rv_scaler.setAdapter(scalerAdapter);

        btn_confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfrimMSG(v);
            }
        });
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

        final int orderNumber = new Random().nextInt(100000) + 000000;


    }

    @Override
    public void onItemClick(int position) {
        Log.d("ConfrimActivity", "onItemClick: " + position);
    }
}