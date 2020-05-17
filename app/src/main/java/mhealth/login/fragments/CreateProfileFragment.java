package mhealth.login.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import mhealth.login.models.Cadre;
import mhealth.login.models.Facility;
import mhealth.login.models.FacilityDepartment;
import mhealth.login.models.User;
import static mhealth.login.dependencies.AppController.TAG;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;



public class CreateProfileFragment extends Fragment {
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
    private String first_dose = "";
    private String second_dose = "";
    private String third_dose = "";
    private String DOB = "";



    @BindView(R.id.facilitySpinner)
    SearchableSpinner facilitySpinner;

    @BindView(R.id.facilityDepartment)
    SearchableSpinner facilityDepartmentSpinner;

    @BindView(R.id.cadre)
    SearchableSpinner cadreSpinner;

    @BindView(R.id.id_no)
    EditText id_no;

    @BindView(R.id.tv_dob)
    TextView tv_dob;

    @BindView(R.id.dose1_date)
    TextView dose1_date;

    @BindView(R.id.dose2_date)
    TextView dose2_date;

    @BindView(R.id.dose3_date)
    TextView dose3_date;

    @BindView(R.id.doseOneRadioGroup)
    RadioGroup doseOneRadioGroup;

    @BindView(R.id.doseTwoRadioGroup)
    RadioGroup doseTwoRadioGroup;

    @BindView(R.id.doseThreeRadioGroup)
    RadioGroup doseThreeRadioGroup;

    @BindView(R.id.lyt_progress)
    LinearLayout lyt_progress;

    @BindView(R.id.btn_submit)
    Button btn_submit;




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
        root =  inflater.inflate(R.layout.fragment_create_profile, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        facilitySpinner.setTitle("Select Facility");
        facilitySpinner.setPositiveButton("OK");

        facilityDepartmentSpinner.setTitle("Select Department");
        facilityDepartmentSpinner.setPositiveButton("OK");

        cadreSpinner.setTitle("Select Cadre");
        cadreSpinner.setPositiveButton("OK");

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDoseDate(0);
            }
        });

        dose1_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDoseDate(1);
            }
        });

        dose2_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDoseDate(2);
            }
        });

        dose3_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDoseDate(3);
            }
        });

        doseOneRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    if (checkedRadioButton.getText().equals("No")){
                        dose1_date.setText(getString(R.string.date_of_first_dose));
                        first_dose="";
                    }
                }
            }
        });

        doseTwoRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    if (checkedRadioButton.getText().equals("No")){
                        dose2_date.setText(getString(R.string.date_of_second_dose));
                        second_dose="";
                    }
                }
            }
        });

        doseThreeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    if (checkedRadioButton.getText().equals("No")) {
                        dose3_date.setText(getString(R.string.date_of_third_dose));
                        third_dose = "";
                    }
                }
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNulls()){
                    lyt_progress.setVisibility(View.VISIBLE);
                    completeProfile(facilityID,facilityDepartmentID,cadreID,DOB,id_no.getText().toString(),first_dose,second_dose,third_dose);
                }
            }
        });

        getFacilities();
        getCadres();

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

                        facilities.add(new Facility(0,"--select facility--","--select facility--","--select--","--select--"));
                        facilitiesList.add("--select facility--");


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

                        if (facilitySpinner != null){
                            facilitySpinner.setAdapter(aa);
                            facilitySpinner.setSelection(aa.getCount()-1);

                            facilityID = facilities.get(aa.getCount()-1).getId();

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

                        }


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

                        facilityDepartments.add(new FacilityDepartment(0,0,"--select department--"));
                        facilityDepartmentsList.add("--select department--");


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
                        facilityDepartmentSpinner.setSelection(aa.getCount()-1);

                        facilityDepartmentID = facilityDepartments.get(aa.getCount()-1).getId();

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

                        cadres.add(new Cadre(0,"--select cadre--"));
                        cadreList.add("--select cadre--");


//                        Stash.put(Constants.DISTRICTS_ARRAYLIST, facilities);
//                        Stash.put(Constants.DISTRICTS_LIST, facilitiesList);

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
                        cadreSpinner.setSelection(aa.getCount()-1);

                        cadreID = cadres.get(aa.getCount()-1).getId();

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

    private void getDoseDate(final  int doseNumber) {
        Calendar cur_calender = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        long date_ship_millis = calendar.getTimeInMillis();
                        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

                        if (doseNumber == 0){
                            Calendar userAge = new GregorianCalendar(year,month,day);
                            Calendar minAdultAge = new GregorianCalendar();
                            minAdultAge.add(Calendar.YEAR, -18);
                            if (minAdultAge.before(userAge)) {
                                tv_dob.setText("Age can not be below 18");
                                tv_dob.setTextColor(Color.RED);
                            }else {
                                DOB = newFormat.format(date_ship_millis);
                                tv_dob.setText(newFormat.format(date_ship_millis));
                            }

                        }else if (doseNumber == 1){
                            first_dose = newFormat.format(date_ship_millis);
                            dose1_date.setText(newFormat.format(date_ship_millis));
                        }else if (doseNumber == 2){
                            second_dose = newFormat.format(date_ship_millis);
                            dose2_date.setText(newFormat.format(date_ship_millis));
                        }else if (doseNumber == 3){
                            third_dose = newFormat.format(date_ship_millis);
                            dose3_date.setText(newFormat.format(date_ship_millis));
                        }
                    }
                }, cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private boolean checkNulls() {

        boolean valid = true;


        if(DOB.equals(""))
        {
            tv_dob.setText("Date of birth is required. Click to set");
            tv_dob.setTextColor(Color.RED);
            valid = false;
            return valid;
        }

        if(facilityID == 0)
        {
            Snackbar.make(root.findViewById(R.id.fragment_create_profile), "Please select a facility", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(facilityDepartmentID == 0)
        {
            Snackbar.make(root.findViewById(R.id.fragment_create_profile), "Please select a department", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(cadreID == 0)
        {
            Snackbar.make(root.findViewById(R.id.fragment_create_profile), "Please select a cadre", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        return valid;
    }

    private void completeProfile(int facilityID, int facilityDepartmentID, int cadreID, String dob, String idNo, String first_dose,
                                 String second_dose, String third_dose) {

        JSONObject payload  = new JSONObject();
        try {
            payload.put("facility_id", facilityID);
            payload.put("facility_department_id", facilityDepartmentID);
            payload.put("cadre_id", cadreID);
            payload.put("dob", dob);
            payload.put("id_no", idNo);
            payload.put("hepatitis_1", first_dose);
            payload.put("hepatitis_2", second_dose);
            payload.put("hepatitis_3", third_dose);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Stash.getString(Constants.END_POINT)+Constants.COMPLETE_PROFILE, payload, new Response.Listener<JSONObject>() {

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

                        loggedInUser.setProfile_complete(1);

                        Stash.put(Constants.LOGGED_IN_USER, loggedInUser);
                        NavHostFragment.findNavController(CreateProfileFragment.this).navigate(R.id.nav_home);

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
                Snackbar.make(root.findViewById(R.id.fragment_create_profile), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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
