package ah.production.dileverybot.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ah.production.dileverybot.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_register;
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;

    private String userEmail ="";
    private String password="";

    private ConstraintLayout cv_main;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        tv_register = findViewById(R.id.tv_register);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.tv_register:

                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                finish();
                break;

            case R.id.btn_login:

                userEmail = et_username.getText().toString();
                password = et_password.getText().toString();

                if (userEmail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Fill the required fields", Toast.LENGTH_LONG).show();
                }
                else {
                    userLogin(userEmail, password);
                }

                break;
        }
    }

    /**
     * User Login
     * @param userEmail
     * @param password
     */
    private void userLogin(String userEmail, String password) {

        showProgressDialog("Login...");
        firebaseAuth.signInWithEmailAndPassword(userEmail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                hideProgressDialog();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
            }
        });

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