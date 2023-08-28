package mhealth.login.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class FeedbackFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

    @BindView(R.id.type_feedback)
    Spinner type_feedback;

    @BindView(R.id.category)
    Spinner category;

    @BindView(R.id.et_post)
    EditText et_post;

    @BindView(R.id.anonymous)
    CheckBox anonymous;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.lyt_progress)
    LinearLayout lyt_progress;

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        this.context = ctx;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_feedback, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(FeedbackFragment.this).navigate(R.id.nav_complete_profile);
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNulls()){
                    lyt_progress.setVisibility(View.VISIBLE);
                    sendFeedback();
                }
            }
        });

        return root;
    }

    private void sendFeedback() {
        JSONObject payload  = new JSONObject();
        try {
            payload.put("category", category.getSelectedItem().toString());
            payload.put("type", type_feedback.getSelectedItem().toString());
            payload.put("feedback", et_post.getText().toString());
            payload.put("anonymous", anonymous.isChecked() ? 1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.END_POINT+Constants.FEEDBACK, payload, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());


                lyt_progress.setVisibility(View.GONE);

                try {
                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;

                    if (status){

                        category.setSelection(0);
                        type_feedback.setSelection(0);
                        et_post.getText().clear();

                        InfoMessage bottomSheetFragment = InfoMessage.newInstance("Success!",message, context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());


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
                Snackbar.make(root.findViewById(R.id.fragment_feedback), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

            }
        }){

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean checkNulls() {

        boolean valid = true;


        if(TextUtils.isEmpty(et_post.getText().toString()))
        {
            Snackbar.make(root.findViewById(R.id.fragment_feedback), "Please write something", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(type_feedback.getSelectedItem().toString().equals("What type of feedback is it?"))
        {
            Snackbar.make(root.findViewById(R.id.fragment_feedback), "Please select feedback type", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(category.getSelectedItem().toString().equals("What category is it in?"))
        {
            Snackbar.make(root.findViewById(R.id.fragment_feedback), "Please select category of feedback", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        return valid;
    }


}
