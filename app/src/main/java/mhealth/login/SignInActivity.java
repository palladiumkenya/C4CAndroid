package mhealth.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.fxn.stash.Stash;
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
import mhealth.login.models.Creds;
import mhealth.login.models.User;
import static mhealth.login.dependencies.AppController.TAG;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(Constants.API_VERSION);

    private EditText input_phone;
    private EditText input_password;
    private Button btn_login;
    private LinearLayout lyt_progress;
    private TextView forgot_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sign In");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        if (Stash.getObject(Constants.LOGGED_IN_USER, User.class) == null){
            //user not logged in. show login or register
            firebaseAuthAnonymous();
        }else {
            //user logged in. take to home
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }


        getCreds();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuthAnonymous();
                }
            }
        };

        input_phone = (EditText)findViewById(R.id.input_phone);
        input_password = (EditText)findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        lyt_progress = (LinearLayout)findViewById(R.id.lyt_progress);
        forgot_password = (TextView) findViewById(R.id.forgot_password);

        input_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && !charSequence.toString().equals("7")) {
                    input_phone.getText().clear();
                    Toast.makeText(SignInActivity.this, "Please start with 7...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(input_phone.getText().toString())){
                    Snackbar.make(findViewById(R.id.login_layout), "Please enter your phone number", Snackbar.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(input_password.getText().toString())){
                    Snackbar.make(findViewById(R.id.login_layout), "Please enter your password", Snackbar.LENGTH_LONG).show();
                }else {
                    lyt_progress.setVisibility(View.VISIBLE);
                    sendLoginRequest(input_phone.getText().toString(), input_password.getText().toString());
                }

            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });



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
        Stash.put(Constants.END_POINT, Objects.requireNonNull(dataSnapshot.getValue(Creds.class)).getEnd_point());

//        Toast.makeText(SignInActivity.this, Objects.requireNonNull(dataSnapshot.getValue(Creds.class)).getEnd_point(), Toast.LENGTH_LONG).show();

    }


    public void gotoSignUp(View view) {

        Intent mint = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(mint);
    }

    private void sendLoginRequest(String phone_no, String password) {

//        StringBuilder builder = new StringBuilder(phone_no);
//        builder.deleteCharAt(0);
//
//        String phoneWithoutPlus = builder.toString();

        JSONObject payload  = new JSONObject();
        try {
            payload.put("msisdn", "254"+phone_no);
            payload.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("SENDING PAYLOAD", payload.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Stash.getString(Constants.END_POINT)+ Constants.LOGIN, payload, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());

                lyt_progress.setVisibility(View.GONE);

                try {
                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;

                    if (status){
                        String access_token = response.has("access_token") ? response.getString("access_token") : "";
                        String token_type = response.has("token_type") ? response.getString("token_type") : "";
                        String expires_at = response.has("expires_at") ? response.getString("expires_at") : "";

                        JSONObject user = response.getJSONObject("user");

                        int id = user.has("id") ? user.getInt("id") : 0;
                        int role_id = user.has("role_id") ? user.getInt("role_id") : 0;
                        int profile_complete = user.has("profile_complete") ? user.getInt("profile_complete") : 0;
                        String first_name = user.has("first_name") ? user.getString("first_name") : "";
                        String surname = user.has("surname") ? user.getString("surname") : "";
                        String gender = user.has("gender") ? user.getString("gender") : "";
                        String email = user.has("email") ? user.getString("email") : "";
                        String msisdn = user.has("msisdn") ? user.getString("msisdn") : "";
                        String created_at = user.has("created_at") ? user.getString("created_at") : "";

                        User newUser = new User(access_token,token_type,expires_at,first_name,surname,gender,email,msisdn,created_at,id,role_id,profile_complete);

//                    Stash.put(Constants.AUTHENTICATED, true);
                        Stash.put(Constants.LOGGED_IN_USER, newUser);



                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }else {
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors,SignInActivity.this);
                        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(R.id.login_layout), Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {



                lyt_progress.setVisibility(View.GONE);
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Snackbar.make(findViewById(R.id.login_layout), VolleyErrors.getVolleyErrorMessages(error, SignInActivity.this), Snackbar.LENGTH_LONG).show();

                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    String body;
                    //get status code here
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    //get response body and parse with appropriate encoding
                    if(error.networkResponse.data!=null) {
                        try {
                            body = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                            JSONObject json = new JSONObject(body);
                            Log.e("error response : ", json.toString());

                            String message = json.has("message") ? json.getString("message") : "";
                            String errors = json.has("errors") ? json.getString("errors") : "";

                            InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors,SignInActivity.this);
                            assert getFragmentManager() != null;
                            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        })
        {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Authorization", loggei);
                headers.put("Content-Type", "application/json");
               // headers.put("Accept", "application/json");
                return headers;
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq);
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
}
