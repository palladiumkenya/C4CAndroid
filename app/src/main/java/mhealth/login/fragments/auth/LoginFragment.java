package mhealth.login.fragments.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;


import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mhealth.login.MainActivity;
import mhealth.login.R;
import mhealth.login.SignInActivity;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.interfaces.NavigationHost;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class LoginFragment extends Fragment {

    NavigationHost navigationDelegate;

    //private Context context;
     private EditText  input_password,input_phone;
     private TextView sign_up,text_feedback,forgot_password;
     private Button btn_login;
     private LinearLayout lyt_progress;

     private FirebaseAuth mAuth;
     private FirebaseAuth.AuthStateListener mAuthStateListener;

     private Context context;
     private View root;




    public static Fragment getInstance(SignInActivity context){

        LoginFragment frag = new LoginFragment();
        frag.navigationDelegate = context;

        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       root = inflater.inflate(R.layout.frag_login,container,false);
       // Stash.put(Constants.END_POINT, "https://c4c_api.mhealthkenya.org/api/");


        mAuth = FirebaseAuth.getInstance();



        input_phone = (EditText)root.findViewById(R.id.input_phone);
        input_password = (EditText)root.findViewById(R.id.input_password);
        btn_login = (Button)root.findViewById(R.id.btn_login);
        lyt_progress = (LinearLayout)root.findViewById(R.id.lyt_progress);
        forgot_password = (TextView)root.findViewById(R.id.forgot_password);
        sign_up = (TextView)root.findViewById(R.id.sign_up);

        input_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && !charSequence.toString().equals("7")) {
                    input_phone.getText().clear();
                    Toast.makeText(context, "Please start with 7...", Toast.LENGTH_LONG).show();
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
                    Snackbar.make(root.findViewById(R.id.login_layout), "Please enter your phone number", Snackbar.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(input_password.getText().toString())){
                    Snackbar.make(root.findViewById(R.id.login_layout), "Please enter your password", Snackbar.LENGTH_LONG).show();
                }else {
                    lyt_progress.setVisibility(View.VISIBLE);
                    sendLoginRequest(input_phone.getText().toString(), input_password.getText().toString());
                }

            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(navigationDelegate!=null)
                    navigationDelegate.navigateTo(SendOtpFragment.getInstance((SignInActivity) getActivity()),true, "Reset Password");
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(navigationDelegate!=null)
                    navigationDelegate.navigateTo(RegisterFragment.getInstance((SignInActivity) getActivity()),true, "Sign up");
            }
        });



        return root;
    }


//    private void showPassResetDialog() {
//        final Dialog dialog = new Dialog(getContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//        dialog.setContentView(R.layout.dialog_reset);
//        dialog.setCancelable(true);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        ((TextView) dialog.findViewById(R.id.title)).setText("Reset Password");
//        ((TextView) dialog.findViewById(R.id.content)).setText("We will send you password reset instructions to "+mAuth.getCurrentUser().getEmail());
//
//        Glide.with(context).load(mAuth.getCurrentUser().getPhotoUrl())
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into((CircularImageView) dialog.findViewById(R.id.image));
//
//
//        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        ((AppCompatButton) dialog.findViewById(R.id.bt_follow)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(getApplicationContext(), "Follow Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        dialog.show();
//        dialog.getWindow().setAttributes(lp);
//    }



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
        Log.e("SENDING TO", Constants.END_POINT+ Constants.LOGIN);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.END_POINT+ Constants.LOGIN, payload, new Response.Listener<JSONObject>() {

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



                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Objects.requireNonNull(getActivity()).finish();

                    }else {
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors,context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(root.findViewById(R.id.login_layout), Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {



                lyt_progress.setVisibility(View.GONE);
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Snackbar.make(root.findViewById(R.id.login_layout), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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

                            InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors,context);
                            bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());



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
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        this.context = ctx;
    }


}
