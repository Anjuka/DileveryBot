package ah.production.dileverybot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_menu;
    private ImageView iv_cam;
    private Button btn_veg;
    private Button btn_fruits;
    private Button btn_grocery;
    private Button btn_medicine;
    private Button btn_customlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.iv_menu:
                break;

            case R.id.btn_veg:
                Intent intentVeg = new Intent(getApplicationContext(), VegiteblesActivity.class);
                startActivity(intentVeg);
                break;

            case R.id.btn_fruits:
                Intent intentVFru = new Intent(getApplicationContext(), FruitsActivity.class);
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
}