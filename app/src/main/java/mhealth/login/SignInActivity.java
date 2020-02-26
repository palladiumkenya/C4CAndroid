package mhealth.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.login.dependencies.Constants;

public class SignInActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(Constants.API_VERSION);


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
