package mhealth.login.fragments.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

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


public class ResetPasswordFragment extends Fragment {

    NavigationHost navigationDelegate;
    String phoneNumber = "";

    private EditText phone_number;
    private EditText otp;
    private EditText new_pass;
    private EditText new_repass;
    private MaterialButton btn_reset_password;
    private LinearLayout lyt_progress;

    private Context context;
    private View root;


    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    public static Fragment getInstance(SignInActivity context, String phoneNumber){

        ResetPasswordFragment frag =     new ResetPasswordFragment();
        frag.phoneNumber = phoneNumber;
        frag.navigationDelegate = context;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_reset_password, container, false);

        phone_number = root.findViewById(R.id.phone_number);
        otp = root.findViewById(R.id.otp);
        new_pass = root.findViewById(R.id.new_pass);
        new_repass = root.findViewById(R.id.new_repass);
        btn_reset_password = root.findViewById(R.id.btn_reset_password);
        lyt_progress = root.findViewById(R.id.lyt_progress);

        phone_number.setText(phoneNumber);

        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTexts()){
                    lyt_progress.setVisibility(View.VISIBLE);
                    resetPassword(phoneNumber,otp.getText().toString(),new_pass.getText().toString());
                }
            }
        });


        return root;
    }


    private boolean validateTexts() {
        boolean valid = true;

        if(TextUtils.isEmpty(otp.getText().toString()))
        {
            otp.setError(getString(R.string.otp_required));
            valid = false;
            return valid;
        }

        if(TextUtils.isEmpty(new_pass.getText().toString()))
        {
            new_pass.setError(getString(R.string.new_pass_re));
            valid = false;
            return valid;
        }

        if(!new_pass.getText().toString().equals(new_repass.getText().toString()))
        {
            new_repass.setError(getString(R.string.bath_pass));
            valid = false;
            return valid;
        }

        return valid;
    }

    private void resetPassword(String phone_no, String otp, String pass) {

        JSONObject payload  = new JSONObject();
        try {
            payload.put("msisdn", phone_no);
            payload.put("otp", otp);
            payload.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.END_POINT+Constants.RESET_PASSWORD, payload, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());


                lyt_progress.setVisibility(View.GONE);

                try {
                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;

                    if (status){

                        Snackbar.make(root.findViewById(R.id.frag_reset_pass), message, Snackbar.LENGTH_LONG).show();

                        if(navigationDelegate!=null)
                            navigationDelegate.navigateTo(LoginFragment.getInstance((SignInActivity) getActivity()),true,"Sign In");

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
                Snackbar.make(root.findViewById(R.id.frag_reset_pass), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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
