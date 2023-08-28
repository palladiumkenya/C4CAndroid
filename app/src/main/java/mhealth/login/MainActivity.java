package mhealth.login;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.Creds;
import mhealth.login.models.Hcw;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private User loggedInUser;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(Constants.API_VERSION);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser == null){
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        }


        mAuth = FirebaseAuth.getInstance();
        getCreds();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuthAnonymous();
                }
            }
        };



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerLayout = navigationView.getHeaderView(0); // 0-index header
        TextView drawer_name = (TextView) headerLayout.findViewById(R.id.drawer_name);
        TextView drawer_phone = (TextView) headerLayout.findViewById(R.id.drawer_phone);
        ImageView drawer_image = (ImageView) headerLayout.findViewById(R.id.drawer_image);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_exposures, R.id.nav_immunizations,
                 R.id.nav_resources, R.id.nav_check_in,
                R.id.nav_tools, R.id.nav_update_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        if (loggedInUser != null){
            drawer_name.setText(loggedInUser.getFirst_name()+" "+loggedInUser.getSurname());
            drawer_phone.setText("+"+loggedInUser.getMsisdn());

        }

        getProfile();

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                signout();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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

    public void signout(){
        // Firebase sign out
        mAuth.signOut();
        //shared prefs sign out

       // String endPoint = Stash.getString(Constants.END_POINT);
        //Stash.clearAll();

       // Stash.put(Constants.END_POINT, endPoint);

        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void getProfile(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.END_POINT+ Constants.GET_PROFILE, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.e(TAG, response.toString());


                try {
                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;

                    if (status){

                        JSONObject data = response.getJSONObject("data");

                        JSONObject hcw = data.getJSONObject("hcw");

                        int id = hcw.has("id") ? hcw.getInt("id") : 0;
                        int facility_id = hcw.has("facility_id") ? hcw.getInt("facility_id") : 0;
                        int facility_department_id = hcw.has("facility_department_id") ? hcw.getInt("facility_department_id") : 0;
                        int cadre_id = hcw.has("cadre_id") ? hcw.getInt("cadre_id") : 0;
                        String facility_name = hcw.has("facility_name") ? hcw.getString("facility_name") : "";
                        String dob = hcw.has("dob") ? hcw.getString("dob") : "";
                        String id_number = hcw.has("id_number") ? hcw.getString("id_number") : "";

                        Hcw hcw1 = new Hcw(id,facility_id,facility_department_id,cadre_id,facility_name,dob,id_number);

                        Stash.put(Constants.HCW, hcw1);

                    }else {
                        Log.e("errors: ",message+"==>"+errors);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e(TAG, "Error: " + error.getMessage());
            }
        })
        {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", loggedInUser.getToken_type()+" "+loggedInUser.getAccess_token());
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



}
