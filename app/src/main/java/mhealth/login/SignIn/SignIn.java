package mhealth.login.SignIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import mhealth.login.MainActivity;
import mhealth.login.R;
import mhealth.login.SignUp.SignUp;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void gotoHome(View view) {


        Intent mint = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mint);
    }

    public void gotoSignUp(View view) {

        Intent mint = new Intent(getApplicationContext(), SignUp.class);
        startActivity(mint);

    }

    public void gotoForgotPassword(View view) {

        Intent mint = new Intent(getApplicationContext(), ForgotPassword.class);
        startActivity(mint);

    }
}
