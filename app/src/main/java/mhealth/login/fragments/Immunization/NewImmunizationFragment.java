package mhealth.login.fragments.Immunization;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.fragments.Exposures.ReportExposuresFragment;
import mhealth.login.models.Device;
import mhealth.login.models.Disease;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;

public class NewImmunizationFragment extends Fragment {


    private Context context;
    private View root;
    private Unbinder unbinder;

    private User loggedInUser;

    private int DISEASE_ID = 0;

    private String DATE_OF_IMMUNIZAION1 = null;
    private String DATE_OF_IMMUNIZAION2 = null;
    private String DATE_OF_IMMUNIZAION3 = null;


    ArrayList<String> diseasesList;
    ArrayList<Disease> diseases;


    @BindView(R.id.disease)
    SearchableSpinner diseaseSpinner ;

    @BindView(R.id.immunization_date1)
    TextView immunization_date1 ;

    @BindView(R.id.immunization_date2)
    TextView immunization_date2 ;

    @BindView(R.id.immunization_date3)
    TextView immunization_date3 ;

    @BindView(R.id.submit_immunization)
    Button btn_submit_immunization;

    @BindView(R.id.secondDoseRadioGroup)
    RadioGroup secondDoseRadioGroup;

    @BindView(R.id.thirdDoseRadioGroup)
    RadioGroup thirdDoseRadioGroup;

    @BindView(R.id.lyt_progress)
    LinearLayout lyt_progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(ctx);
        this.context = ctx;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_new_immunization, container, false);
        unbinder = ButterKnife.bind(this, root);


        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(NewImmunizationFragment.this).navigate(R.id.nav_complete_profile);
        }

        secondDoseRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                if (checkedRadioButton.getText().equals("Yes")){
                    immunization_date2.setVisibility(View.VISIBLE);
                }else if (checkedRadioButton.getText().equals("No")){
                    immunization_date2.setVisibility(View.GONE);
                    DATE_OF_IMMUNIZAION2 = null;
                    immunization_date2.setText(getString(R.string.date_of_second_dose));

                }
            }
        });

        thirdDoseRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                if (checkedRadioButton.getText().equals("Yes")){
                    immunization_date3.setVisibility(View.VISIBLE);
                }else if (checkedRadioButton.getText().equals("No")){
                    immunization_date3.setVisibility(View.GONE);
                    DATE_OF_IMMUNIZAION3 = null;
                    immunization_date3.setText(getString(R.string.date_of_third_dose));

                }
            }
        });



        immunization_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImmunizationDate1();
            }
        });

        immunization_date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImmunizationDate2();
            }
        });

        immunization_date3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImmunizationDate3();
            }
        });

        diseaseSpinner.setTitle("Select disease immunized");
        diseaseSpinner.setPositiveButton("OK");

        getDiseases();

        btn_submit_immunization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DISEASE_ID == 0){
                    Snackbar.make(view, "Please select a disease", Snackbar.LENGTH_LONG).show();
                }
               /* else if (DATE_OF_IMMUNIZAION1 <= DATE_OF_IMMUNIZAION2){

                }*/

                else {
                    if (DATE_OF_IMMUNIZAION1 == null ){
                        Snackbar.make(view, "Please set date of first immunization", Snackbar.LENGTH_LONG).show();
                    }

                    else {
                        lyt_progress.setVisibility(View.GONE);
                        doReport();
                    }
                }
            }
        });

        return root;
    }

    private void getImmunizationDate1(){
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

                        DATE_OF_IMMUNIZAION1 = newFormat.format(new Date(date_ship_millis));
                        immunization_date1.setText("Date of first immunization: "+newFormat.format(new Date(date_ship_millis)));


                    }
                }, cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getImmunizationDate2(){
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

                        DATE_OF_IMMUNIZAION2 = newFormat.format(new Date(date_ship_millis));
                        immunization_date2.setText("Date of second immunization: "+newFormat.format(new Date(date_ship_millis)));

                    }
                }, cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getImmunizationDate3(){
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

                        DATE_OF_IMMUNIZAION3 = newFormat.format(new Date(date_ship_millis));
                        immunization_date3.setText("Date of third immunization: "+newFormat.format(new Date(date_ship_millis)));

                    }
                }, cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getDiseases() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.END_POINT+Constants.DISEASES, null, new Response.Listener<JSONObject>() {

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

                        diseases = new ArrayList<Disease>();
                        diseasesList = new ArrayList<String>();

                        diseases.clear();
                        diseasesList.clear();

                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                            int id = jsonObject.has("id") ? jsonObject.getInt("id") : 0;
                            String name = jsonObject.has("name") ? jsonObject.getString("name") : "";
                            int doses = jsonObject.has("doses") ? jsonObject.getInt("doses") : 0;
                            int intervals = jsonObject.has("intervals") ? jsonObject.getInt("intervals") : 0;

                            Disease newDisease = new Disease(id,name,doses,intervals);

                            diseases.add(newDisease);
                            diseasesList.add(newDisease.getName());
                        }

                        diseases.add(new Disease(0,"--select disease--",0,0));
                        diseasesList.add("--select disease--");


//                        Stash.put(Constants.DISTRICTS_ARRAYLIST, facilities);
//                        Stash.put(Constants.DISTRICTS_LIST, facilitiesList);

                        ArrayAdapter<String> aa=new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_dropdown_item,
                                diseasesList){
                            @Override
                            public int getCount() {
                                return super.getCount(); // you dont display last item. It is used as hint.
                            }
                        };

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        diseaseSpinner.setAdapter(aa);
                        diseaseSpinner.setSelection(aa.getCount()-1);

                        DISEASE_ID = diseases.get(aa.getCount()-1).getId();

                        diseaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                DISEASE_ID = diseases.get(position).getId();
//                                Toast.makeText(context,DISEASE_ID+"", Toast.LENGTH_LONG).show();
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

                    Snackbar.make(root.findViewById(R.id.frag_new_immunization), Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Snackbar snackbar = Snackbar.make(root.findViewById(R.id.frag_new_immunization), VolleyErrors.getVolleyErrorMessages(error, getContext()), Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        getDiseases();
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

    private void doReport() {
        JSONObject payload  = new JSONObject();
        try {
            payload.put("disease_id", DISEASE_ID);
            payload.put("date", DATE_OF_IMMUNIZAION1);
            payload.put("second_dose", DATE_OF_IMMUNIZAION2);
            payload.put("third_dose", DATE_OF_IMMUNIZAION3);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.END_POINT+Constants.NEW_IMMUNIZATIONS, payload, new Response.Listener<JSONObject>() {

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
                Snackbar.make(root.findViewById(R.id.frag_new_immunization), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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
