package mhealth.login.fragments.Exposures;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.MultiSelectSpinner;
import mhealth.login.dependencies.MultiSelectSpinnerPpe;
import mhealth.login.dependencies.MultiSelectSpinnerSymptoms;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.County;
import mhealth.login.models.SubCounty;
import mhealth.login.models.User;
import mhealth.login.models.Ward;
import mhealth.login.models.counties;
import mhealth.login.models.scounties;
import mhealth.login.models.wards;

import static mhealth.login.dependencies.AppController.TAG;


public class ReportCovidExposureFragment extends Fragment {

    private RequestQueue rq;

    ArrayList<String> countiesList;
    ArrayList<counties> countiess;

    ArrayList<String> scountyList;
    ArrayList<scounties> scountiess;

    ArrayList<String> wardsList;
    ArrayList<wards> wardss;

    private int countyID = 2620;
    private int scountyID = 2620;

    private int wardID = 2620;



    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

    private String contactWithStr = "";
    private String ppeWornStr = "";
    private String ppes = "";
    private String ipcTrainingStr = "";
    private String covidSymptomsStr = "";
    private String symptoms = "";
    private String pcrTestStr = "";
    private String isolationMethodStr = "";
    private String DATE_OF_EXPOSURE = "";
    private String DATE_OF_START_ISOLATION = "";


    private int ppeWorn = 0;
    private int ipcTraining = 0;

   // private int countyID = 2620;
   // private int subCountyID = 2620;
   // private int wardID = 2620;


    private ArrayAdapter<String> arrayContactWith,arrayPpeWorn, arrayIpcTraining, arrayCovidSymptoms, arrayPcrTest, arrayIsolationMethod;

    @BindView(R.id.til_id_number)
    TextInputLayout til_id_number;

    @BindView(R.id.etxt_id_no)
    TextInputEditText etxt_id_no;

    @BindView(R.id.til_date_of_covid_exposure)
    TextInputLayout til_date_of_covid_exposure;

    @BindView(R.id.etxt_date_of_covid_exposure)
    TextInputEditText etxt_date_of_covid_exposure;

    @BindView(R.id.contact_with)
    MaterialBetterSpinner contact_with;

    @BindView(R.id.ppe_worn)
    MaterialBetterSpinner ppe_worn;

    @BindView(R.id.lyt_ppe)
    LinearLayout lyt_ppe;

    @BindView(R.id.ppe_spinner)
    MultiSelectSpinnerPpe ppe_spinner;

    @BindView(R.id.ipc_training)
    MaterialBetterSpinner ipc_training;

    @BindView(R.id.covid_symptoms)
    MaterialBetterSpinner covid_symptoms;

    @BindView(R.id.lyt_symptoms)
    LinearLayout lyt_symptoms;

    @BindView(R.id.symptoms_spinner)
    MultiSelectSpinnerSymptoms symptoms_spinner;

    @BindView(R.id.pcr_test)
    MaterialBetterSpinner pcr_test;

    @BindView(R.id.isolation_method)
    MaterialBetterSpinner isolation_method;

    @BindView(R.id.til_date_isolation_start)
    TextInputLayout til_date_isolation_start;

    @BindView(R.id.etxt_date_isolation_start)
    TextInputEditText etxt_date_isolation_start;

    @BindView(R.id.til_place_of_diagnosis)
    TextInputLayout til_place_of_diagnosis;

    @BindView(R.id.etxt_place_of_diagnosis)
    TextInputEditText etxt_place_of_diagnosis;

    @BindView(R.id.countySpinner)
    SearchableSpinner countySpinner;

    @BindView(R.id.subCountySpinner)
    SearchableSpinner subCountySpinner;

    @BindView(R.id.wardSpinner)
    SearchableSpinner wardSpinner;

    @BindView(R.id.lyt_progress)
    LinearLayout lyt_progress;

    @BindView(R.id.btn_report_covid_exposure)
    MaterialButton btn_report_covid_exposure;

  //  ArrayList<String> countiesList;
    ArrayList<County> counties;

    ArrayList<String> subCountiesList;
    ArrayList<SubCounty> subCounties;

   // ArrayList<String> wardsList;
    ArrayList<Ward> wards;


    String[] ppeArray = {"None", "Single gloves", "N95 mask (or equivalent)", "Face shield or goggles/protective glasses", "Disposable gown", "Waterproof apron"};
    String[] symptomsArray = {"None", "Fever", "Cough", "Difficult in breathing", "Fatigue", "Sneezing", "Sore throat"};


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
        rq = Volley.newRequestQueue(context);
        root = inflater.inflate(R.layout.fragment_report_covid_exposure, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(ReportCovidExposureFragment.this).navigate(R.id.nav_complete_profile);
        }

        initialise();
       // getCounties();
        getFacilities();
        checkNulls();

        btn_report_covid_exposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkNulls()){
                    reportCovidExposure();
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

    private void  initialise(){

        etxt_date_of_covid_exposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExposureDate();
            }
        });

        arrayContactWith = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.contact_with));
        contact_with.setAdapter(arrayContactWith);
        contact_with.setText("");
        contact_with.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                contactWithStr=contact_with.getText().toString();

            }
        });

        arrayPpeWorn = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.ppe_worn));
        ppe_worn.setAdapter(arrayPpeWorn);
        ppe_worn.setText("");
        ppe_worn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ppeWornStr=ppe_worn.getText().toString();

                switch (ppeWornStr) {
                        case "Yes":
                        ppeWorn = 1;
                        break;
                    case "No":
                        ppeWorn = 0;
                        break;

                }

                if (ppeWornStr.equals("Yes")){
                    lyt_ppe.setVisibility(View.VISIBLE);

                }else {
                    lyt_ppe.setVisibility(View.GONE);
                }


            }
        });

        ppe_spinner.setItems(ppeArray);
        ppe_spinner.hasNoneOption(true);
        ppe_spinner.setSelection(new ArrayList<>());
        ppe_spinner.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {
                //Toast.makeText(context,"Selected indices: " + indices,Toast.LENGTH_LONG).show();




            }

            @Override
            public void selectedStrings(List<String> ppeStrings) {
                Toast.makeText(context,"Selected items: " + ppe_spinner.getSelectedItemsAsString(),Toast.LENGTH_LONG).show();
                ppes = ppe_spinner.getSelectedItemsAsString();


            }
        });

        arrayIpcTraining = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.ipc_training));
        ipc_training.setAdapter(arrayIpcTraining);
        ipc_training.setText("");
        ipc_training.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ipcTrainingStr=ipc_training.getText().toString();

                switch (ipcTrainingStr) {
                    case "Yes":
                        ipcTraining = 1;
                        break;
                    case "No":
                        ipcTraining = 0;
                        break;

                }

            }
        });

        arrayCovidSymptoms = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.covid_symptoms));
        covid_symptoms.setAdapter(arrayCovidSymptoms);
        covid_symptoms.setText("");
        covid_symptoms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                covidSymptomsStr=covid_symptoms.getText().toString();

                if (covidSymptomsStr.equals("Yes")){
                    lyt_symptoms.setVisibility(View.VISIBLE);

                }else {
                    lyt_symptoms.setVisibility(View.GONE);
                }


            }
        });

        symptoms_spinner.setItems(symptomsArray);
        symptoms_spinner.hasNoneOption(true);
        symptoms_spinner.setSelection(new ArrayList<>());
        symptoms_spinner.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {
                //Toast.makeText(context,"Selected indices: " + indices,Toast.LENGTH_LONG).show();



            }

            @Override
            public void selectedStrings(List<String> symptomsStrings) {
                Toast.makeText(context,"Selected items: " + symptoms_spinner.getSelectedItemsAsString(),Toast.LENGTH_LONG).show();
                symptoms = symptoms_spinner.getSelectedItemsAsString();

            }
        });

        arrayPcrTest = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.pcr_test));
        pcr_test.setAdapter(arrayPcrTest);
        pcr_test.setText("");
        pcr_test.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pcrTestStr=pcr_test.getText().toString();

            }
        });

        arrayIsolationMethod = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.isolation_method));
        isolation_method.setAdapter(arrayIsolationMethod);
        isolation_method.setText("");
        isolation_method.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isolationMethodStr=isolation_method.getText().toString();

            }
        });

        etxt_date_isolation_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIsolationStartDate();
            }
        });


    }

    private boolean checkNulls(){

        boolean valid = true;

       if (etxt_id_no.equals("")){

           Snackbar.make(root.findViewById(R.id.fragment_report_covid), "Please enter your ID Number.", Snackbar.LENGTH_LONG).show();
           valid =false;
           return valid;

       }

        if (etxt_date_of_covid_exposure.equals("")){

            Snackbar.make(root.findViewById(R.id.fragment_report_covid), "Please select your date of exposure.", Snackbar.LENGTH_LONG).show();
            valid =false;
            return valid;

        }

        if (etxt_date_isolation_start.equals("")){

            Snackbar.make(root.findViewById(R.id.fragment_report_covid), "Please select your start date for isolation.", Snackbar.LENGTH_LONG).show();
            valid =false;
            return valid;

        }

        if (etxt_place_of_diagnosis.equals("")){

            Snackbar.make(root.findViewById(R.id.fragment_report_covid), "Please enter where you were diagonised.", Snackbar.LENGTH_LONG).show();
            valid =false;
            return valid;

        }

        if(countyID == 0)
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_covid), "Please select your county.", Snackbar.LENGTH_LONG).show();
            valid =false;
            return valid;
        }


        if(scountyID == 0)
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_covid), "Please select your sub county", Snackbar.LENGTH_LONG).show();
            valid =false;
            return valid;
        }

        if(wardID == 0)
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_covid), "Please select your ward", Snackbar.LENGTH_LONG).show();
            valid =false;
            return valid;
        }

        return valid;

    }

    private void getExposureDate(){
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

                        DATE_OF_EXPOSURE = newFormat.format(new Date(date_ship_millis));

                        etxt_date_of_covid_exposure.setText(DATE_OF_EXPOSURE);
                    }
                }, cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getIsolationStartDate(){
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

                        DATE_OF_START_ISOLATION = newFormat.format(new Date(date_ship_millis));

                        etxt_date_isolation_start.setText(DATE_OF_START_ISOLATION);

                    }
                }, cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getCounties() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                Constants.COUNTIES, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
//                Log.d(TAG, response.toString());

                try {
                    counties = new ArrayList<County>();
                    countiesList = new ArrayList<String>();

                    counties.clear();
                    countiesList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = (JSONObject) response.get(i);

                        int id = jsonObject.has("id") ? jsonObject.getInt("id") : 0;
                        String name = jsonObject.has("name") ? jsonObject.getString("name") : "";
                        String organisationunitid = jsonObject.has("organisationunitid") ? jsonObject.getString("organisationunitid") : "";

                        County county = new County(id,name,organisationunitid);

                        counties.add(county);
                        countiesList.add(county.getName());
                    }

                    counties.add(new County(0,"--select your county--", "2620"));
                    countiesList.add("--select your county--");


//                        Stash.put(Constants.DISTRICTS_ARRAYLIST, facilities);
//                        Stash.put(Constants.DISTRICTS_LIST, facilitiesList);

                    ArrayAdapter<String> aa=new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            countiesList){
                        @Override
                        public int getCount() {
                            return super.getCount(); // you dont display last item. It is used as hint.
                        }
                    };

                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    countySpinner.setAdapter(aa);
                    countySpinner.setSelection(aa.getCount()-1);

                    countyID = counties.get(aa.getCount()-1).getId();

                    countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            countyID = counties.get(position).getId();
                            getSubCounties(counties.get(position).getOrganisationunitid());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();

//                    Snackbar.make(findViewById(R.id.activity_sign_up), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Volley===> ", "Error: " + error.getMessage());
                final Snackbar snackbar = Snackbar.make(root.findViewById(R.id.fragment_report_covid), VolleyErrors.getVolleyErrorMessages(error, getContext()), Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        getCounties();
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
//                headers.put("Authorization", loggedInUser.getToken_type()+" "+loggedInUser.getAccess_token());
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

    private void getSubCounties(final String OrgUnitID) {


        Log.e("get sub counties for ",OrgUnitID);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                Constants.SUB_COUNTIES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.e("Get sub counties", response.toString());

                try {
                    JSONArray jsonArrayResp = new JSONArray(response);

                    subCounties = new ArrayList<SubCounty>();
                    subCountiesList = new ArrayList<String>();

                    subCounties.clear();
                    subCountiesList.clear();

                    for (int i = 0; i < jsonArrayResp.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArrayResp.get(i);

                        int id = jsonObject.has("id") ? jsonObject.getInt("id") : 0;
                        String name = jsonObject.has("name") ? jsonObject.getString("name") : "";
                        String organisationunitid = jsonObject.has("organisationunitid") ? jsonObject.getString("organisationunitid") : "";

                        SubCounty subCounty = new SubCounty(id,name,organisationunitid);

                        subCounties.add(subCounty);
                        subCountiesList.add(subCounty.getName());
                    }

                    subCounties.add(new SubCounty(0,"--select your sub county--", "2620"));
                    subCountiesList.add("--select your sub county--");


//                        Stash.put(Constants.DISTRICTS_ARRAYLIST, facilities);
//                        Stash.put(Constants.DISTRICTS_LIST, facilitiesList);

                    ArrayAdapter<String> aa=new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            subCountiesList){
                        @Override
                        public int getCount() {
                            return super.getCount(); // you dont display last item. It is used as hint.
                        }
                    };

                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    subCountySpinner.setAdapter(aa);
                    subCountySpinner.setSelection(aa.getCount()-1);

                    scountyID = subCounties.get(aa.getCount()-1).getId();

                    subCountySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            scountyID = subCounties.get(position).getId();
                            getWards(subCounties.get(position).getOrganisationunitid());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(root.findViewById(R.id.fragment_report_covid), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Volley===> ", "Error: " + error.getMessage());
                final Snackbar snackbar = Snackbar.make(root.findViewById(R.id.fragment_report_covid), VolleyErrors.getVolleyErrorMessages(error, getContext()), Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        getSubCounties(OrgUnitID);
                    }
                }).show();


            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("org_unit_id", OrgUnitID);
                return params;
            }
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
////                headers.put("Authorization", loggedInUser.getToken_type()+" "+loggedInUser.getAccess_token());
//                headers.put("Content-Type", "application/json");
//                headers.put("Accept", "application/json");
//                return headers;
//            }

        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void getWards(final String OrgUnitID) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                Constants.WARDS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());

                try {
                    wards = new ArrayList<Ward>();
                    wardsList = new ArrayList<String>();

                    wards.clear();
                    wardsList.clear();

                    JSONArray jsonArrayResp = new JSONArray(response);


                    for (int i = 0; i < jsonArrayResp.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArrayResp.get(i);

                        int id = jsonObject.has("id") ? jsonObject.getInt("id") : 0;
                        String name = jsonObject.has("name") ? jsonObject.getString("name") : "";
                        String organisationunitid = jsonObject.has("organisationunitid") ? jsonObject.getString("organisationunitid") : "";

                        Ward ward = new Ward(id,name,organisationunitid);

                        wards.add(ward);
                        wardsList.add(ward.getName());
                    }

                    wards.add(new Ward(0,"--select your ward--", "2620"));
                    wardsList.add("--select your ward--");


//                        Stash.put(Constants.DISTRICTS_ARRAYLIST, facilities);
//                        Stash.put(Constants.DISTRICTS_LIST, facilitiesList);

                    ArrayAdapter<String> aa=new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            wardsList){
                        @Override
                        public int getCount() {
                            return super.getCount(); // you dont display last item. It is used as hint.
                        }
                    };

                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    wardSpinner.setAdapter(aa);
                    wardSpinner.setSelection(aa.getCount()-1);

                    wardID = wards.get(aa.getCount()-1).getId();

                    wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            wardID = wards.get(position).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(root.findViewById(R.id.fragment_report_covid), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Volley===> ", "Error: " + error.getMessage());
                final Snackbar snackbar = Snackbar.make(root.findViewById(R.id.fragment_report_covid), VolleyErrors.getVolleyErrorMessages(error, getContext()), Snackbar.LENGTH_LONG);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();


            }
        })
        {

//            /**
//             * Passing some request headers
//             */
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
////                headers.put("Authorization", loggedInUser.getToken_type()+" "+loggedInUser.getAccess_token());
//                headers.put("Content-Type", "application/json");
//                headers.put("Accept", "application/json");
//                return headers;
//            }

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("org_unit_id", OrgUnitID);
                return params;
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void reportCovidExposure() {
        JSONObject payload  = new JSONObject();
        try {
            payload.put("contact_with",contactWithStr);
            payload.put("id_no",etxt_id_no.getText().toString());
            payload.put("date_of_contact", DATE_OF_EXPOSURE);
            payload.put("ppe_worn", ppeWorn);
            payload.put("ppes",ppes);
            payload.put("ipc_training", ipcTraining);
            payload.put("place_of_diagnosis", etxt_place_of_diagnosis.getText().toString());
            payload.put("symptoms", symptoms);
            payload.put("pcr_test", pcrTestStr);
            payload.put("management", isolationMethodStr);
            payload.put("isolation_start_date", etxt_date_isolation_start.getText().toString());
            payload.put("county_id", countyID);
            payload.put("subcounty_id", scountyID);
            payload.put("ward_id", wardID);



        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("Payload", payload.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.END_POINT+Constants.REPORT_COVID_EXPOSURE, payload, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("Respose:", response.toString());


                lyt_progress.setVisibility(View.GONE);

                try {
                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;

                    if (status){
//                        InfoMessage bottomSheetFragment = InfoMessage.newInstance("Success!",message, context);
//                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());

                        Snackbar.make(root.findViewById(R.id.fragment_report_covid), message, Snackbar.LENGTH_LONG).show();


                        NavHostFragment.findNavController(ReportCovidExposureFragment.this).navigate(R.id.nav_exposures);

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
                Snackbar.make(root.findViewById(R.id.fragment_report_covid), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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
    //new county list
    public void getFacilities() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                Constants.COUNTIES, null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {


                try {


                    countiess = new ArrayList<counties>();
                    countiesList = new ArrayList<String>();

                    countiess.clear();
                    countiesList.clear();


                    for (int i = 0; i < response.length(); i++) {
                        JSONObject service = (JSONObject) response.get(i);


                        int id = service.has("id") ? service.getInt("id") : 0;
                        String name = service.has("name") ? service.getString("name") : "";
                        int code = service.has("code") ? service.getInt("code") : 0;


                        counties newCounty = new counties(id, name, code);

                        countiess.add(newCounty);
                        countiesList.add(newCounty.getName());
                    }
                    countiess.add(new counties(0, " ", 0));
                    countiesList.add(" ");




                    ArrayAdapter<String> aa = new ArrayAdapter<String>(context,
                            android.R.layout.simple_spinner_dropdown_item,
                            countiesList) {
                        @Override
                        public int getCount() {
                            return super.getCount(); // you dont display last item. It is used as hint.
                        }
                    };

                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // if (ServiceSpinner != null){
                    countySpinner.setAdapter(aa);
                    countySpinner.setSelection(aa.getCount() - 1);

                    countyID = countiess.get(aa.getCount() - 1).getId();

                    countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


                            // serviceUnitSpinner.setAdapter(null);

                            countyID = countiess.get(position).getId();
                            //getDepartments(services.get(position).getService_id());

//
                                   /* if (serviceID !=0)
                                        Toast.makeText(Registration.this, "getting units", Toast.LENGTH_LONG).show();*/
                            try {
                                getDepartments(countyID);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //getDepartments(serviceID);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {


                        }
                    });




                    //}

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(Registration.this, " cant get services", Toast.LENGTH_LONG).show();
                error.printStackTrace();
                getFacilities();
            }
        }
        ) {

            /**
             * Passing some request headers
             */
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Authorization", loggedInUser.getToken_type()+" "+loggedInUser.getAccess_token());
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //AppController.getInstance().addToRequestQueue(jsonObjReq);
        //rq.add(jsonArrayRequest);
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);



    }

    //SubcountyList

    public void getDepartments(int ID) {



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Constants.SUB_COUNTIES+ID,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //Toast.makeText(Registration.this, "response", Toast.LENGTH_LONG).show();

                try {

                    scountiess = new ArrayList<scounties>();
                    scountyList = new ArrayList<String>();

                    scountiess.clear();
                    scountyList.clear();


                    //JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject serviceUnit = (JSONObject) response.get(i);

                        int id = serviceUnit.has("id") ? serviceUnit.getInt("id") : 0;
                        String name = serviceUnit.has("name") ? serviceUnit.getString("name") : "";


                        scounties newServiceUnit = new scounties(id, name);

                        scountiess.add(newServiceUnit);
                        scountyList.add(newServiceUnit.getName());
                    }

                    scountiess.add(new scounties(0, ""));
                    scountyList.add("");

                    ArrayAdapter<String> aa = new ArrayAdapter<String>(context,
                            android.R.layout.simple_spinner_dropdown_item,
                            scountyList) {
                        @Override
                        public int getCount() {
                            return super.getCount(); // you dont display last item. It is used as hint.
                        }
                    };

                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    subCountySpinner.setAdapter(aa);
                    subCountySpinner.setSelection(aa.getCount() - 1);

                    scountyID = scountiess.get(aa.getCount() - 1).getId();

                    subCountySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        //@Overide
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                            // Toast.makeText(Registration.this, "null selected", Toast.LENGTH_LONG).show();
                            scountyID = scountiess.get(position).getId();
                            getWards(scountyID);
                            //call wards here


//                                Toast.makeText(context,facilityDepartments.get(position).getDepartment_name(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Registration.this, "cant get", Toast.LENGTH_LONG).show();


                if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
                error.printStackTrace();
                getDepartments(countyID);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Authorization", loggedInUser.getToken_type()+" "+loggedInUser.getAccess_token());

                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //AppController.getInstance().addToRequestQueue(jsonObjReq);
        //rq.add(jsonArrayRequest);
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


    }
//call wards

    public void getWards(int ID) {



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,   Constants.WARDS+ID,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //Toast.makeText(Registration.this, "response", Toast.LENGTH_LONG).show();

                try {

                    wardss = new ArrayList<wards>();
                    wardsList = new ArrayList<String>();

                    wardss.clear();
                    wardsList.clear();


                    //JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject serviceUnit = (JSONObject) response.get(i);

                        int id = serviceUnit.has("id") ? serviceUnit.getInt("id") : 0;
                        String name = serviceUnit.has("name") ? serviceUnit.getString("name") : "";
                        int scounty_id = serviceUnit.has("scounty_id") ? serviceUnit.getInt("scounty_id") : 0;


                        wards newServiceUnit = new wards(id, name, scounty_id);

                        wardss.add(newServiceUnit);
                        wardsList.add(newServiceUnit.getName());
                    }

                    wardss.add(new wards(0, "", 0));
                    wardsList.add("");

                    ArrayAdapter<String> aa = new ArrayAdapter<String>(context,
                            android.R.layout.simple_spinner_dropdown_item,
                            wardsList) {
                        @Override
                        public int getCount() {
                            return super.getCount(); // you dont display last item. It is used as hint.
                        }
                    };

                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    wardSpinner.setAdapter(aa);
                    wardSpinner.setSelection(aa.getCount() - 1);

                    //wardID = wardss.get(aa.getCount() - 1).getId();
                    wardID = wardss.get(aa.getCount() -1).getId();
                    wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        //@Overide
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                            // Toast.makeText(Registration.this, "null selected", Toast.LENGTH_LONG).show();
                            wardID = wardss.get(position).getId();

                            //call wards here


//                                Toast.makeText(context,facilityDepartments.get(position).getDepartment_name(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Registration.this, "cant get", Toast.LENGTH_LONG).show();


                if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
                error.printStackTrace();
                getWards(scountyID);
                //getDepartments(countyID);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Authorization", loggedInUser.getToken_type()+" "+loggedInUser.getAccess_token());

                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //AppController.getInstance().addToRequestQueue(jsonObjReq);
        //rq.add(jsonArrayRequest);
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


    }




}
