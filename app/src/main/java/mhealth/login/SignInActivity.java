package mhealth.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void gotoHome(View view) {

        // Toast.makeText(this, "You have clicked this button", Toast.LENGTH_SHORT).show();

        Intent mint = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mint);
    }

    public void gotoSignUp(View view) {

        Intent mint = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(mint);

    }
}
