package mhealth.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
//import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.fragments.auth.LoginFragment;
import mhealth.login.interfaces.NavigationHost;
import mhealth.login.models.Creds;
import mhealth.login.models.User;
import static mhealth.login.dependencies.AppController.TAG;

public class SignInActivity extends AppCompatActivity implements NavigationHost {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(Constants.API_VERSION);

    private  Toolbar toolbar;
    private EditText input_phone;
    private EditText input_password;
    private Button btn_login;
    private LinearLayout lyt_progress;
    private TextView forgot_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sign In");
        setSupportActionBar(toolbar);

//        Stash.put(Constants.END_POINT, "http://c4ctest.mhealthkenya.org/api/"); //temporary endpoint
        //live
       // Stash.put(Constants.END_POINT, "https://c4c-api.mhealthkenya.org/api/"); //temporary endpoint
        //test
       // Stash.put(Constants.END_POINT,"https://prod.kenyahmis.org:8003/api/");



        mAuth = FirebaseAuth.getInstance();

//        if (Stash.getObject(Constants.LOGGED_IN_USER, User.class) == null){
//            //user not logged in. show login or register
//            firebaseAuthAnonymous();
//        }else {
//            //user logged in. take to home
//            startActivity(new Intent(SignInActivity.this, MainActivity.class));
//            finish();
//        }


        getCreds();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuthAnonymous();
                }
            }
        };



        Fragment newFragment = LoginFragment.getInstance(SignInActivity.this);
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.frag_container, newFragment).commit();



    }

    private void firebaseAuthAnonymous() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FIREBASE AUTH", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            Toast.makeText(SignInActivity.this, user.getUid(), Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FIREBASE AUTH", "signInAnonymously:failure", task.getException());
                        }

                        // ...
                    }
                });

    }

    private void getCreds() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    updateDataSnapshot(dataSnapshot);

                } catch (NullPointerException ex) {
//                        Log.e("nullpointer detected", ex.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                    Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void updateDataSnapshot(DataSnapshot dataSnapshot) {
      //  Stash.put(Constants.END_POINT, Objects.requireNonNull(dataSnapshot.getValue(Creds.class)).getEnd_point());

//        Toast.makeText(SignInActivity.this, Objects.requireNonNull(dataSnapshot.getValue(Creds.class)).getEnd_point(), Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }


    /**
     * Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack, String title) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        toolbar.setTitle(title);

        transaction.commit();

    }

}
