package mhealth.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void gotoSignIn(View view) {

        // Toast.makeText(this, "You have clicked this button", Toast.LENGTH_SHORT).show();

        Intent mint = new Intent(getApplicationContext(), SignIn.class);
        startActivity(mint);
    }
}
