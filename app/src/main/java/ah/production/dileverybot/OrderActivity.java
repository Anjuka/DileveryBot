package ah.production.dileverybot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ah.production.dileverybot.adapter.OrderListAdapter;
import ah.production.dileverybot.adapter.ScalerAdapter;
import ah.production.dileverybot.model.CartItemsData;
import ah.production.dileverybot.model.ItemsData;
import ah.production.dileverybot.model.OrderData;
import ah.production.dileverybot.ui.MainActivity;
import ah.production.dileverybot.ui.WeightActivity;

public class OrderActivity extends AppCompatActivity implements OrderListAdapter.OnItemClickListner, View.OnClickListener {

    private ImageView iv_info;
    private ImageView iv_home;
    private RecyclerView rv_orders;
    OrderListAdapter orderListAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String user_id = "";

    private ProgressDialog progressDialog;

    private ArrayList<Map<String, String>> serviceRecordsLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        progressDialog = new ProgressDialog(this);

        iv_home = findViewById(R.id.iv_home);
        iv_info = findViewById(R.id.iv_info);
        rv_orders = findViewById(R.id.rv_orders);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_id = firebaseAuth.getUid();

        iv_home.setOnClickListener(this);
        iv_info.setOnClickListener(this);


        showProgressDialog("Loading...");
        CollectionReference documentReference = firebaseFirestore.collection("orderRecords/" + user_id + "/" + "order");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("TAG", "onSuccess: " + queryDocumentSnapshots);

                if (queryDocumentSnapshots.isEmpty()) {
                    hideProgressDialog();
                    Log.d("TAG", "onSuccess: LIST EMPTY");
                    Toast.makeText(getApplicationContext(), "Currently you don't have any orders", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    ArrayList<OrderData> orderDataArrayList = (ArrayList<OrderData>) queryDocumentSnapshots.toObjects(OrderData.class);

                    Log.d("TAG", "onSuccess: " + orderDataArrayList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    rv_orders.setLayoutManager(layoutManager);
                    orderListAdapter = new OrderListAdapter(getApplicationContext(), orderDataArrayList, OrderActivity.this::onItemClick);
                    rv_orders.setAdapter(orderListAdapter);
                    hideProgressDialog();
                }
            }
        });
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
    public void onItemClick(int position) {
        Log.d("TAG", "onItemClick: " + position);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.iv_home:
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_info:
                showMSG(v);
                break;
        }
    }

    private void showMSG(View v) {

        Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.custom_dialog_order);

        ImageView iv_close = dialog.findViewById(R.id.iv_close);

        DisplayMetrics metrics = v.getContext().getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((3 * width) / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}