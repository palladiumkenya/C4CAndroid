package mhealth.login.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.MainActivity;
import mhealth.login.R;
import mhealth.login.SignInActivity;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.Cadre;
import mhealth.login.models.Facility;
import mhealth.login.models.FacilityDepartment;
import mhealth.login.models.Hcw;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class UpdateProfileFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    ArrayList<String> facilitiesList;
    ArrayList<Facility> facilities;

    ArrayList<String> facilityDepartmentsList;
    ArrayList<FacilityDepartment> facilityDepartments;

    ArrayList<String> cadreList;
    ArrayList<Cadre> cadres;

    private User loggedInUser;
    private int facilityID = 0;
    private int facilityDepartmentID = 0;
    private int cadreID = 0;



    @BindView(R.id.card_name)
    TextView card_name;

    @BindView(R.id.card_phone)
    TextView card_phone;

    @BindView(R.id.first_name)
    EditText first_name;

    @BindView(R.id.sur_name)
    EditText sur_name;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.phone_number)
    EditText phone_number;

    @BindView(R.id.facilitySpinner)
    SearchableSpinner facilitySpinner;

    @BindView(R.id.facilityDepartment)
    SearchableSpinner facilityDepartmentSpinner;

    @BindView(R.id.cadre)
    SearchableSpinner cadreSpinner;

    @BindView(R.id.old_password)
    EditText old_password;

    @BindView(R.id.input_password)
    EditText input_password;

    @BindView(R.id.input_confirm_password)
    EditText input_confirm_password;

    @BindView(R.id.btn_update_profile)
    Button btn_update_profile;

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
        root =  inflater.inflate(R.layout.fragment_update_profile, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(UpdateProfileFragment.this).navigate(R.id.nav_complete_profile);
        }


        card_name.setText(loggedInUser.getFirst_name()+" "+loggedInUser.getSurname());
        card_phone.setText(loggedInUser.getMsisdn());

        first_name.setText(loggedInUser.getFirst_name());
        sur_name.setText(loggedInUser.getSurname());
        email.setText(loggedInUser.getEmail());
        phone_number.setText(loggedInUser.getMsisdn());


        facilitySpinner.setTitle("Select Facility");
        facilitySpinner.setPositiveButton("OK");

        facilityDepartmentSpinner.setTitle("Select Department");
        facilityDepartmentSpinner.setPositiveButton("OK");

        cadreSpinner.setTitle("Select Cadre");
        cadreSpinner.setPositiveButton("OK");

        getProfile();

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkNulls()){
                    lyt_progress.setVisibility(View.VISIBLE);
                    updateProfile(facilityID,facilityDepartmentID,cadreID,first_name.getText().toString(),sur_name.getText().toString(),
                            email.getText().toString(),phone_number.getText().toString());
                }
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getFacilities() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Stash.getString(Constants.END_POINT)+Constants.FACILITIES, null, new Response.Listener<JSONObject>() {

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

                        facilities = new ArrayList<Facility>();
                        facilitiesList = new ArrayList<String>();

                        facilities.clear();
                        facilitiesList.clear();

                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject facility = (JSONObject) jsonArray.get(i);

                            int id = facility.has("id") ? facility.getInt("id") : 0;
                            String mfl_code = facility.has("mfl_code") ? facility.getString("mfl_code") : "";
                            String name = facility.has("name") ? facility.getString("name") : "";
                            String county = facility.has("county") ? facility.getString("county") : "";
                            String sub_county = facility.has("sub_county") ? facility.getString("sub_county") : "";

                            Facility newFacility = new Facility(id,mfl_code,name,county,sub_county);

                            facilities.add(newFacility);
                            facilitiesList.add(newFacility.getName());
                        }

//                        facilities.add(new Facility(0,"--select facility--","--select facility--","--select--","--select--"));
//                        facilitiesList.add("--select facility--");


                        Hcw hcw = (Hcw) Stash.getObject(Constants.HCW, Hcw.class);

                        Log.e("HCW:", hcw.getDob()+" "+hcw.getId_number()+" "+hcw.getFacility_id());

                        int pos =facilities.indexOf(new Facility(hcw.getFacility_id(),"","","",""));
                        if (pos == -1)
                            pos=0;

                        Log.e("POS ", ""+pos);

//                        Stash.put(Constants.DISTRICTS_ARRAYLIST, facilities);
//                        Stash.put(Constants.DISTRICTS_LIST, facilitiesList);

                        ArrayAdapter<String> aa=new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_dropdown_item,
                                facilitiesList){
                            @Override
                            public int getCount() {
                                return super.getCount(); // you dont display last item. It is used as hint.
                            }
                        };

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        facilitySpinner.setAdapter(aa);
                        facilitySpinner.setSelection(pos);
//                        facilitySpinner.setSelection(aa.getCount()-1);

                        facilityID = facilities.get(pos).getId();
//                        facilityID = facilities.get(aa.getCount()-1).getId();

                        facilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                                facilityDepartmentSpinner.setAdapter(null);

                                facilityID = facilities.get(position).getId();

//                                Toast.makeText(context,facilityID+"", Toast.LENGTH_LONG).show();

                                if (facilityID !=0)
                                    getDepartments(facilityID);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    } else {
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors, context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(root.findViewById(R.id.fragment_create_profile), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Snackbar snackbar = Snackbar.make(root.findViewById(R.id.fragment_create_profile), VolleyErrors.getVolleyErrorMessages(error, getContext()), Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        getFacilities();
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

    private void getDepartments(int id) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Stash.getString(Constants.END_POINT)+Constants.FACILITY_DEPARTMENTS+id, null, new Response.Listener<JSONObject>() {

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

                        facilityDepartments = new ArrayList<FacilityDepartment>();
                        facilityDepartmentsList = new ArrayList<String>();

                        facilityDepartments.clear();
                        facilityDepartmentsList.clear();

                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject facilityDepartment = (JSONObject) jsonArray.get(i);

                            int id = facilityDepartment.has("id") ? facilityDepartment.getInt("id") : 0;
                            int facility_id = facilityDepartment.has("facility_id") ? facilityDepartment.getInt("facility_id") : 0;
                            String department_name = facilityDepartment.has("department_name") ? facilityDepartment.getString("department_name") : "";


                            FacilityDepartment newFacilityDept = new FacilityDepartment(id,facility_id,department_name);

                            facilityDepartments.add(newFacilityDept);
                            facilityDepartmentsList.add(newFacilityDept.getDepartment_name());
                        }

//                        facilityDepartments.add(new FacilityDepartment(0,0,"--select department--"));
//                        facilityDepartmentsList.add("--select department--");

                        Hcw hcw = (Hcw) Stash.getObject(Constants.HCW, Hcw.class);

                        Log.e("HCW:", hcw.getDob()+" "+hcw.getId_number()+" "+hcw.getFacility_id());

                        int pos =facilityDepartments.indexOf(new FacilityDepartment(hcw.getFacility_department_id(),0,""));
                        if (pos == -1)
                            pos=0;


//                        Stash.put(Constants.DISTRICTS_ARRAYLIST, facilities);
//                        Stash.put(Constants.DISTRICTS_LIST, facilitiesList);

                        ArrayAdapter<String> aa=new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_dropdown_item,
                                facilityDepartmentsList){
                            @Override
                            public int getCount() {
                                return super.getCount(); // you dont display last item. It is used as hint.
                            }
                        };

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        facilityDepartmentSpinner.setAdapter(aa);
                        facilityDepartmentSpinner.setSelection(pos);
//                        facilityDepartmentSpinner.setSelection(aa.getCount()-1);

                        facilityDepartmentID = facilityDepartments.get(pos).getId();
                        Log.e("facilityDepartmentID: ", facilityDepartmentID+"");
//                        facilityDepartmentID = facilityDepartments.get(aa.getCount()-1).getId();

                        facilityDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                                facilityDepartmentID = facilityDepartments.get(position).getId();

//                                Toast.makeText(context,facilityDepartments.get(position).getDepartment_name(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    } else {
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors, context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(root.findViewById(R.id.fragment_create_profile), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Snackbar snackbar = Snackbar.make(root.findViewById(R.id.fragment_create_profile), VolleyErrors.getVolleyErrorMessages(error, getContext()), Snackbar.LENGTH_LONG);
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

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject cadre = (JSONObject) jsonArray.get(i);

                            int id = cadre.has("id") ? cadre.getInt("id") : 0;
                            String name = cadre.has("name") ? cadre.getString("name") : "";


                            Cadre cadre1 = new Cadre(id,name);

                            cadres.add(cadre1);
                            cadreList.add(cadre1.getName());
                        }

//                        cadres.add(new Cadre(0,"--select cadre--"));
//                        cadreList.add("--select cadre--");
                        Hcw hcw = (Hcw) Stash.getObject(Constants.HCW, Hcw.class);


                        int pos =cadres.indexOf(new Cadre(hcw.getCadre_id(),""));
                        if (pos == -1)
                            pos=0;


                        ArrayAdapter<String> aa=new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_dropdown_item,
                                cadreList){
                            @Override
                            public int getCount() {
                                return super.getCount(); // you dont display last item. It is used as hint.
                            }
                        };

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        cadreSpinner.setAdapter(aa);
                        cadreSpinner.setSelection(pos);
//                        cadreSpinner.setSelection(aa.getCount()-1);

                        cadreID = cadres.get(pos).getId();
//                        cadreID = cadres.get(aa.getCount()-1).getId();

                        cadreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                                cadreID = cadres.get(position).getId();

//                                Toast.makeText(context,facilityDepartments.get(position).getDepartment_name(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    } else {
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors, context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(root.findViewById(R.id.fragment_create_profile), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Snackbar snackbar = Snackbar.make(root.findViewById(R.id.fragment_create_profile), VolleyErrors.getVolleyErrorMessages(error, getContext()), Snackbar.LENGTH_LONG);
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

    private void getProfile(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Stash.getString(Constants.END_POINT)+ Constants.GET_PROFILE, null, new Response.Listener<JSONObject>() {

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
                        String dob = hcw.has("dob") ? hcw.getString("dob") : "";
                        String id_number = hcw.has("id_number") ? hcw.getString("id_number") : "";

                        Hcw hcw1 = new Hcw(id,facility_id,facility_department_id,cadre_id,dob,id_number);

                        Stash.put(Constants.HCW, hcw1);

                        facilityID = facility_id;

                        getFacilities();
                        getCadres();

                    }else {
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors, context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(root.findViewById(R.id.update_profile), Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Snackbar.make(root.findViewById(R.id.update_profile), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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

    private boolean checkNulls() {

        boolean valid = true;


        if(TextUtils.isEmpty(first_name.getText().toString()))
        {
            first_name.setError("First name is required");
            valid = false;
            return valid;
        }

        if(TextUtils.isEmpty(sur_name.getText().toString()))
        {
            sur_name.setError("Surname is required");
            valid = false;
            return valid;
        }

        if(TextUtils.isEmpty(phone_number.getText().toString()))
        {
            phone_number.setError("Phone number is required");
            valid = false;
            return valid;
        }

        if(facilityID == 0)
        {
            Snackbar.make(root.findViewById(R.id.update_profile), "Please select a facility", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(facilityDepartmentID == 0)
        {
            Snackbar.make(root.findViewById(R.id.update_profile), "Please select a department", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(cadreID == 0)
        {
            Snackbar.make(root.findViewById(R.id.update_profile), "Please select a cadre", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        return valid;
    }

    private void updateProfile(int facilityID, int facilityDepartmentID, int cadreID, String first_name,
                                 String surname, String email, String phone_no) {

        JSONObject payload  = new JSONObject();
        try {
            payload.put("facility_id", facilityID);
            payload.put("facility_department_id", facilityDepartmentID);
            payload.put("cadre_id", cadreID);
            payload.put("first_name", first_name);
            payload.put("surname", surname);
            payload.put("email", email);
            payload.put("msisdn", phone_no);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Stash.getString(Constants.END_POINT)+Constants.UPDATE_PROFILE, payload, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());


                lyt_progress.setVisibility(View.GONE);

                try {
                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;

                    if (status){

                        //update HCW stash object
                        Hcw hcw = (Hcw) Stash.getObject(Constants.HCW, Hcw.class);
                        hcw.setFacility_id(facilityID);
                        hcw.setFacility_department_id(facilityDepartmentID);
                        hcw.setCadre_id(cadreID);
                        Stash.put(Constants.HCW, hcw);


                        //updatge lofgged in user
                        loggedInUser.setFirst_name(first_name);
                        loggedInUser.setSurname(surname);
                        loggedInUser.setEmail(email);
                        loggedInUser.setMsisdn(phone_no);
                        Stash.put(Constants.LOGGED_IN_USER, loggedInUser);

                        NavHostFragment.findNavController(UpdateProfileFragment.this).navigate(R.id.nav_home);

                        Toast.makeText(context,message, Toast.LENGTH_LONG).show();

//                        InfoMessage bottomSheetFragment = InfoMessage.newInstance("Success!",message, context);
//                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());


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
                Snackbar.make(root.findViewById(R.id.update_profile), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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
