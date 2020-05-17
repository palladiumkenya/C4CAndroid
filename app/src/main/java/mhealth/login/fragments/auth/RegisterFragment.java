package mhealth.login.fragments.auth;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import java.util.HashMap;
import java.util.Map;

import mhealth.login.R;
import mhealth.login.SignInActivity;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.interfaces.NavigationHost;

import static mhealth.login.dependencies.AppController.TAG;


public class RegisterFragment extends Fragment {

    //private Context context;
     NavigationHost navigationDelegate;
     Context context;
     private View root;
    private TextView sign_in;
    private EditText first_name;
    private EditText sur_name;
    private EditText email;
    private EditText phone_number;
    private Spinner gender;
    private EditText input_password;
    private EditText input_confirm_password;
    private Button btn_sign_up;
    private LinearLayout lyt_progress;
    private CheckBox consent_check_box;



     private FirebaseAuth mAuth;
     private FirebaseAuth.AuthStateListener mAuthStateListener;




    public static Fragment getInstance(SignInActivity context){

        RegisterFragment frag =     new RegisterFragment();
        frag.navigationDelegate = context;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.frag_register,container,false);


        mAuth = FirebaseAuth.getInstance();



        sign_in = (TextView) root.findViewById(R.id.sign_in);
        first_name = (EditText)root.findViewById(R.id.first_name);
        sur_name = (EditText)root.findViewById(R.id.sur_name);
        email = (EditText)root.findViewById(R.id.email);
        phone_number = (EditText)root.findViewById(R.id.phone_number);
        gender = (Spinner) root.findViewById(R.id.gender);
        input_password = (EditText)root.findViewById(R.id.input_password);
        input_confirm_password = (EditText)root.findViewById(R.id.input_confirm_password);
        btn_sign_up = (Button) root.findViewById(R.id.sign_up);
        lyt_progress = (LinearLayout)root.findViewById(R.id.lyt_progress);
        consent_check_box = (CheckBox) root.findViewById(R.id.consent_check_box);

        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && !charSequence.toString().equals("7")) {
                    phone_number.getText().clear();
                    Toast.makeText(context, "Please start with 7...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateTexts()){

                    lyt_progress.setVisibility(View.VISIBLE);
                    sendSignUpRequest(first_name.getText().toString(), sur_name.getText().toString(), email.getText().toString(),
                            phone_number.getText().toString(),gender.getSelectedItem().toString(),input_password.getText().toString(), input_confirm_password.getText().toString());
                }
            }
        });


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navigationDelegate!=null)
                    navigationDelegate.navigateTo(LoginFragment.getInstance((SignInActivity) getActivity()),true, "Sign in");
            }
        });

        return root;
    }



    private boolean validateTexts() {
        boolean valid = true;

        if(TextUtils.isEmpty(first_name.getText().toString()))
        {
            first_name.setError(getString(R.string.name_required));
            valid = false;
            return valid;
        }

        if(TextUtils.isEmpty(sur_name.getText().toString()))
        {
            sur_name.setError(getString(R.string.surname_required));
            valid = false;
            return valid;
        }

        if(TextUtils.isEmpty(email.getText().toString()))
        {
            email.setError(getString(R.string.email_required));
            valid = false;
            return valid;
        }

        if(gender.getSelectedItem().toString().equals("Please select your gender"))
        {
            Snackbar.make(root.findViewById(R.id.sign_up_layout), "Please select your gender", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }
        if(TextUtils.isEmpty(phone_number.getText().toString()))
        {
            phone_number.setError(getString(R.string.phone_number_required));
            valid = false;
            return valid;
        }

        if(TextUtils.isEmpty(input_password.getText().toString()))
        {
            input_password.setError(getString(R.string.password_required));
            valid = false;
            return valid;
        }


        if(!input_password.getText().toString().equals(input_confirm_password.getText().toString()))
        {
            input_confirm_password.setError(getString(R.string.must_match));
            valid = false;
            return valid;
        }

        return valid;
    }

    private void sendSignUpRequest(String first_name,String sur_name,String email,String phone_no,String gender,String input_password,String input_confirm_password) {

        JSONObject payload  = new JSONObject();
        try {
            payload.put("role_id", 3); //health care worker
            payload.put("first_name", first_name);
            payload.put("surname", sur_name);
            payload.put("gender", gender);
            payload.put("email", email);
            payload.put("consent", consent_check_box.isChecked() ? 1 : 0);
            payload.put("msisdn", "254"+phone_no);
            payload.put("password", input_password);
            payload.put("password_confirmation", input_confirm_password);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Stash.getString(Constants.END_POINT)+Constants.REGISTER, payload, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());


                lyt_progress.setVisibility(View.GONE);

                try {
                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;

                    if (status){
//                        InfoMessage bottomSheetFragment = InfoMessage.newInstance("Success!",message,context);
//                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());

                        Snackbar.make(root.findViewById(R.id.sign_up_layout), message, Snackbar.LENGTH_LONG).show();

                        if(navigationDelegate!=null)
                            navigationDelegate.navigateTo(LoginFragment.getInstance((SignInActivity) getActivity()),false);

                    }else {
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors,context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                lyt_progress.setVisibility(View.GONE);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Snackbar.make(root.findViewById(R.id.sign_up_layout), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

            }
        }){

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Authorization", loggei);
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


    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        this.context = ctx;
    }
}
