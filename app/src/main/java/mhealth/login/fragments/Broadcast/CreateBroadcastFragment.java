package mhealth.login.fragments.Broadcast;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.MultiSelectSpinner;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.fragments.Exposures.ReportExposuresFragment;
import mhealth.login.models.Cadre;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class CreateBroadcastFragment extends Fragment {
    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;


    @BindView(R.id.cadre_spinner)
    MultiSelectSpinner cadre_spinner;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.et_message)
    EditText et_message;

    @BindView(R.id.lyt_progress)
    LinearLayout lyt_progress;

    ArrayList<String> cadreList;
    ArrayList<Cadre> cadres;

    List<Integer> cadreIDs;

    //final List<String> list = Arrays.asList(getResources().getStringArray(R.array.target_group));
    //String[] array = {"None", "Apple", "Google", "Facebook", "Tesla", "IBM", "Twitter"};



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
        root =  inflater.inflate(R.layout.fragment_create_broadcast_message, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(CreateBroadcastFragment.this).navigate(R.id.nav_complete_profile);
        }

        cadreIDs = new ArrayList<>();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_message.getText().toString())){
                    Snackbar snackbar = Snackbar.make(root.findViewById(R.id.frag_create_broadcast), "Please type a message", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    if (cadreIDs.size() == 0){
                        Snackbar snackbar = Snackbar.make(root.findViewById(R.id.frag_create_broadcast), "Please select a cadre", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }else if (cadreIDs.size() == 1 && cadreIDs.get(0)==0){
                        Snackbar snackbar = Snackbar.make(root.findViewById(R.id.frag_create_broadcast), "Please select a cadre", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }else {
                        createBroadcast();
                    }
                }
            }
        });


        getCadres();

        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getCadres() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Stash.getString(Constants.END_POINT)+Constants.CADRES, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());

                try {

                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;


                    if (status)
                    {
//                        Stash.clear(Constants.DISTRICTS_ARRAYLIST);
//                        Stash.clear(Constants.DISTRICTS_LIST);

                        cadres = new ArrayList<Cadre>();
                        cadreList = new ArrayList<String>();

                        cadreList.clear();
                        cadres.clear();

                        JSONArray jsonArray = response.getJSONArray("data");

                        cadres.add(new Cadre(0,"None"));
                        cadreList.add("None");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject cadre = (JSONObject) jsonArray.get(i);

                            int id = cadre.has("id") ? cadre.getInt("id") : 0;
                            String name = cadre.has("name") ? cadre.getString("name") : "";

                            Cadre cadre1 = new Cadre(id,name);

                            cadres.add(cadre1);
                            cadreList.add(cadre1.getName());
                        }


                        ArrayAdapter<String> aa=new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_dropdown_item,
                                cadreList){
                            @Override
                            public int getCount() {
                                return super.getCount(); // you dont display last item. It is used as hint.
                            }
                        };

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                        cadre_spinner.setItems(cadreList);
                        cadre_spinner.hasNoneOption(true);
                        cadre_spinner.setSelection(new int[]{0});
                        cadre_spinner.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                            @Override
                            public void selectedIndices(List<Integer> indices) {
                                //Toast.makeText(context,"Selected indices: " + indices,Toast.LENGTH_LONG).show();

                                cadreIDs.clear();
                                for (int i = 0; i < indices.size(); i++) {
                                    cadreIDs.add(cadres.get(indices.get(i)).getId());
                                    //Toast.makeText(context,"Selected item: " + cadres.get(indices.get(i)).getName(),Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void selectedStrings(List<String> strings) {
                              // Toast.makeText(context,"Selected items: " + strings,Toast.LENGTH_LONG).show();

                            }
                        });


                    } else {
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors, context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(root.findViewById(R.id.frag_create_broadcast), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Snackbar snackbar = Snackbar.make(root.findViewById(R.id.frag_create_broadcast), VolleyErrors.getVolleyErrorMessages(error, getContext()), Snackbar.LENGTH_LONG);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();


            }
        })
        {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() {
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

    private void createBroadcast() {
        JSONObject payload  = new JSONObject();
        try {
            payload.put("cadres", new JSONArray(cadreIDs));
            payload.put("message", et_message.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("Payload:",payload.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Stash.getString(Constants.END_POINT)+Constants.CREATE_BROADCAST, payload, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());


                lyt_progress.setVisibility(View.GONE);

                try {
                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;

                    if (status){
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance("Success!",message, context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());

                        et_message.getText().clear();

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
                Snackbar.make(root.findViewById(R.id.frag_create_broadcast), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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

}
