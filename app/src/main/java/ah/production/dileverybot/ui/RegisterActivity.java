package ah.production.dileverybot.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ah.production.dileverybot.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_login;
    private EditText et_username;
    private EditText et_email;
    private EditText et_password;
    private EditText et_mobile;
    private EditText et_location;
    private Button btn_register;
    private ConstraintLayout cv_main;

    private String userName="";
    private String email="";
    private String password="";
    private String mobile="";
    private String location="";

    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tv_login = findViewById(R.id.tv_login);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_mobile = findViewById(R.id.et_mobile);
        et_location = findViewById(R.id.et_location);
        btn_register = findViewById(R.id.btn_login);
        cv_main = findViewById(R.id.cv_main);

        tv_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.tv_login:

                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
                finish();
                break;

            case R.id.btn_login:

                userName = et_username.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                mobile = et_mobile.getText().toString();
                location = et_location.getText().toString();

                if (userName.isEmpty() || email.isEmpty() || password.isEmpty()
                        || mobile.isEmpty() || location.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Fill the required fields", Toast.LENGTH_LONG).show();
                } else {
                    userRegister(userName, email, password, mobile, location);
                }

                break;
        }
    }

    /**
     * Register New User
     *
     * @param userName
     * @param email
     * @param password
     * @param mobile
     * @param location
     */
    private void userRegister(String userName,
                              String email,
                              String password,
                              String mobile,
                              String location) {

        showProgressDialog("Registering...");

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Map<String, Object> user = new HashMap<>();
                user.put("userId", firebaseAuth.getCurrentUser().getUid());
                user.put("userName", userName);
                user.put("email", email);
                user.put(" mobile", mobile);
                user.put("location", location);

                DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        hideProgressDialog();
                        showSnackMSG(cv_main, "User Account created...");
                        Intent intentDashboard = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentDashboard);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                        Log.d("TAG", "onFailure: " + e.getMessage());
                        showSnackMSG(cv_main, "User Account is not created...");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                showSnackMSG(cv_main, e.getMessage());
            }
        });
    }

    private void showSnackMSG(ConstraintLayout cv_main, String msg) {

        Snackbar snackbar = Snackbar.make(cv_main, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    // Show Progress Bar
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
}