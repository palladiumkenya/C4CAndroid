package mhealth.login.fragments.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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


public class SendOtpFragment extends Fragment {

    NavigationHost navigationDelegate;
    private MaterialButton cancel;
    private MaterialButton submit;
    private EditText phone_number;
    private LinearLayout lyt_progress;


    private Context context;
    private View root;



    public SendOtpFragment() {
        // Required empty public constructor
    }


    public static Fragment getInstance(SignInActivity context){

        SendOtpFragment frag =     new SendOtpFragment();
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
        root = inflater.inflate(R.layout.fragment_send_otp, container, false);

        cancel = root.findViewById(R.id.cancel);
        submit = root.findViewById(R.id.submit);
        phone_number = root.findViewById(R.id.phone_number);
        lyt_progress = root.findViewById(R.id.lyt_progress);


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


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navigationDelegate!=null)
                    navigationDelegate.navigateTo(LoginFragment.getInstance((SignInActivity) getActivity()),true, "Sign in");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTexts()){
                    lyt_progress.setVisibility(View.VISIBLE);
                    requestOtp(phone_number.getText().toString());
                }
            }
        });

        return root;

    }


    private boolean validateTexts() {
        boolean valid = true;

        if(TextUtils.isEmpty(phone_number.getText().toString()))
        {
            phone_number.setError(getString(R.string.phone_number_required));
            valid = false;
            return valid;
        }

        return valid;
    }

    private void requestOtp(String phone_no) {

        JSONObject payload  = new JSONObject();
        try {
            payload.put("msisdn", "254"+phone_no);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.END_POINT+Constants.SEND_OTP, payload, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());


                lyt_progress.setVisibility(View.GONE);

                try {
                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;

                    if (status){

                        if(navigationDelegate!=null)
                            navigationDelegate.navigateTo(ResetPasswordFragment.getInstance((SignInActivity) getActivity(), "254"+phone_no),true,"Change Password");

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
                Snackbar.make(root.findViewById(R.id.frag_send_otp), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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
