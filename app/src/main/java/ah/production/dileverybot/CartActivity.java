package ah.production.dileverybot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import ah.production.dileverybot.model.CartItemsData;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";

    private ArrayList<CartItemsData> cartItemsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        cartItemsData = getIntent().getParcelableArrayListExtra("cart_key");
        Log.d(TAG, "onCreate: " + cartItemsData);

    }
}