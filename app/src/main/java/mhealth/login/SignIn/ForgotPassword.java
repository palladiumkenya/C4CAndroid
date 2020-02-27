package mhealth.login.SignIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import mhealth.login.R;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    public void gotoSignIn(View view) {

        Intent mint = new Intent(getApplicationContext(), SignIn.class);
        startActivity(mint);

    }
}
