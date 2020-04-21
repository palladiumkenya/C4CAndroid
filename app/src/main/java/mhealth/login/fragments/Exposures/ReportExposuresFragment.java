package mhealth.login.fragments.Exposures;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
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
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import mhealth.login.fragments.auth.LoginFragment;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class ReportExposuresFragment extends Fragment {
    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

    private String DATE_OF_EXPOSURE = "";
    private String DATE_OF_PEP = "";



    private String exposureTypeStr = "";
    private String deviceUsedStr = "";
    private String devicePurposeStr = "";
    private String exposureHowStr = "";
    private String describeExposureStr = "";
    private String pepInitiatedStr = "";
    private String pepPreviousInitiatedStr = "";
    private String exposureLocationStr = "";
    private String patientHIVStr = "";
    private String patientHBVStr = "";
    private String exposureCauseStr = "";
    private boolean isSharp = false;

    private ArrayAdapter<String> arrayExposureType, arrayPepInitiated, arrayPepPreviousInitiated,
            arrayExposureLocation, arrayHIVStatus,arrayHBVStatus,arrayDeviceUsed, arrayDevicePurpose, arrayHow,arrayDescribe,arrayExposureCause;


    @BindView(R.id.exposure_date)
    EditText exposure_date;



    @BindView(R.id.pep_initiated)
    MaterialBetterSpinner pep_initiated;

    @BindView(R.id.pep_date)
    EditText pep_date;


    @BindView(R.id.exposure_type)
    MaterialBetterSpinner exposure_type;

    @BindView(R.id.device_used)
    MaterialBetterSpinner device_used;


    @BindView(R.id.otherDeviceET)
    EditText otherDeviceET;

    @BindView(R.id.describe_exposure)
    MaterialBetterSpinner describe_exposure;

    @BindView(R.id.device_purpose)
    MaterialBetterSpinner device_purpose;

    @BindView(R.id.otherPurposeET)
    EditText otherPurposeET;

    @BindView(R.id.exposure_location)
    MaterialBetterSpinner exposure_location;

    @BindView(R.id.otherLocationET)
    EditText otherLocationET;

    @BindView(R.id.exposure_how)
    MaterialBetterSpinner exposure_how;

    @BindView(R.id.otherHowET)
    EditText otherHowET;

    @BindView(R.id.exposure_cause)
    MaterialBetterSpinner exposure_cause;

    @BindView(R.id.otherExposureCauseET)
    EditText otherExposureCauseET;

    @BindView(R.id.patient_hiv)
    MaterialBetterSpinner patient_hiv;

    @BindView(R.id.patient_hbv)
    MaterialBetterSpinner patient_hbv;

    @BindView(R.id.previous_exposures)
    EditText previous_exposures;

    @BindView(R.id.pepPreviousSpinner)
    MaterialBetterSpinner pepPreviousSpinner;

    @BindView(R.id.layoutSharp)
    LinearLayout layoutSharp;

    @BindView(R.id.layoutNotSharp)
    LinearLayout layoutNotSharp;

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
        root =  inflater.inflate(R.layout.fragment_report_exposures, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(ReportExposuresFragment.this).navigate(R.id.nav_complete_profile);
        }



        exposure_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExposureDate();
            }
        });



        arrayPepInitiated = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.pep));
        pep_initiated.setAdapter(arrayPepInitiated);
        pep_initiated.setText("");
        pep_initiated.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pepInitiatedStr=pep_initiated.getText().toString();

                if (pepInitiatedStr.equals("Yes")){
                    pep_date.setVisibility(View.VISIBLE);

                }else {
                    pep_date.setVisibility(View.GONE);
                    DATE_OF_PEP = "";
                }


            }
        });
        pep_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPepDate();
            }
        });


        arrayExposureLocation = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.exposure_occurrence));
        exposure_location.setAdapter(arrayExposureLocation);
        exposure_location.setText("");
        exposure_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                exposureLocationStr=exposure_location.getText().toString();

                if (exposureLocationStr.equals("Other")){
                    otherLocationET.setVisibility(View.VISIBLE);

                }else {
                    otherLocationET.setVisibility(View.GONE);
                    otherLocationET.setText("");
                }


            }
        });


        arrayExposureType = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.exposure_type));
        exposure_type.setAdapter(arrayExposureType);
        exposure_type.setText("");
        exposure_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                exposureTypeStr=exposure_type.getText().toString();

                if (exposureTypeStr.equals("Needle Stick") || exposureTypeStr.equals("Cuts")){
                    isSharp = true;
                    layoutSharp.setVisibility(View.VISIBLE);
                    layoutNotSharp.setVisibility(View.GONE);
                }else {
                    isSharp = false;
                    layoutSharp.setVisibility(View.GONE);
                    layoutNotSharp.setVisibility(View.VISIBLE);
                }

            }
        });



        /*
         *
         * beginning of sharp
         */

        arrayDeviceUsed = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.device_used));
        device_used.setAdapter(arrayDeviceUsed);
        device_used.setText("");
        device_used.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                deviceUsedStr=device_used.getText().toString();

                if (deviceUsedStr.equals("Other")){
                    otherDeviceET.setVisibility(View.VISIBLE);
                }else {
                    otherDeviceET.setVisibility(View.GONE);
                }

            }
        });


        arrayDevicePurpose = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.device_purpose));
        device_purpose.setAdapter(arrayDevicePurpose);
        device_purpose.setText("");
        device_purpose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                devicePurposeStr=device_purpose.getText().toString();

                if (devicePurposeStr.equals("Other")){
                    otherPurposeET.setVisibility(View.VISIBLE);
                }else {
                    otherPurposeET.setVisibility(View.GONE);
                }

            }
        });


        arrayHow = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.exposure_how));
        exposure_how.setAdapter(arrayHow);
        exposure_how.setText("");
        exposure_how.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                exposureHowStr=exposure_how.getText().toString();

                if (exposureHowStr.equals("Other")){
                    otherHowET.setVisibility(View.VISIBLE);
                }else {
                    otherHowET.setVisibility(View.GONE);
                }

            }
        });


        arrayDescribe = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.exposure_describe));
        describe_exposure.setAdapter(arrayDescribe);
        describe_exposure.setText("");
        describe_exposure.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (describe_exposure.getText().toString().equals("Little or no bleeding")){
                    describeExposureStr = "Superficial";
                }else {
                    describeExposureStr = "Deep";
                }

            }
        });


        /*
        *
        * end of sharp
        */




        /*
         *
         * beginning of NOT sharp
         */

        arrayExposureCause = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.exposure_cause));
        exposure_cause.setAdapter(arrayExposureCause);
        exposure_cause.setText("");
        exposure_cause.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                exposureCauseStr=exposure_cause.getText().toString();

                if (exposureCauseStr.equals("Other")){
                    otherExposureCauseET.setVisibility(View.VISIBLE);
                }else {
                    otherExposureCauseET.setVisibility(View.GONE);
                }

            }
        });


        /*
         *
         * end of NOT sharp
         */




        arrayHIVStatus = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.patient_hiv));
        patient_hiv.setAdapter(arrayHIVStatus);
        patient_hiv.setText("");
        patient_hiv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                patientHIVStr=patient_hiv.getText().toString();
            }
        });


        arrayHBVStatus = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.patient_hbv));
        patient_hbv.setAdapter(arrayHBVStatus);
        patient_hbv.setText("");
        patient_hbv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                patientHBVStr=patient_hbv.getText().toString();
            }
        });


        previous_exposures.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    if (Integer.parseInt(previous_exposures.getText().toString()) > 0){
                        pepPreviousSpinner.setVisibility(View.VISIBLE);
                    }else {
                        pepPreviousSpinner.setVisibility(View.GONE);
                    }
                } catch (NumberFormatException e){
                    pepPreviousSpinner.setVisibility(View.GONE);
                }


            }
        });


        arrayPepPreviousInitiated = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.pep));
        pepPreviousSpinner.setAdapter(arrayPepPreviousInitiated);
        pepPreviousSpinner.setText("");
        pepPreviousSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pepPreviousInitiatedStr= pepPreviousSpinner.getText().toString();
            }
        });






        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNulls()){
                    lyt_progress.setVisibility(View.GONE);
                    doReport();
                }
            }
        });


        return root;
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
                        getExposureTime();
                    }
                }, cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getExposureTime() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                DATE_OF_EXPOSURE = DATE_OF_EXPOSURE + " " + selectedHour + ":" + selectedMinute;

                exposure_date.setText(DATE_OF_EXPOSURE);
//                date_et.setText("Date of exposure: "+DATE_OF_EXPOSURE);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    private void getPepDate(){
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

                        DATE_OF_PEP = newFormat.format(new Date(date_ship_millis));
                        getPepTime();
                    }
                }, cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getPepTime() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                DATE_OF_PEP = DATE_OF_PEP + " " + selectedHour + ":" + selectedMinute;

                pep_date.setText(DATE_OF_PEP);
//                date_et.setText("Date of exposure: "+DATE_OF_EXPOSURE);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean checkNulls() {

        boolean valid = true;
        

        if(DATE_OF_EXPOSURE.equals(""))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select date and time of exposure", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(pepInitiatedStr.equals(""))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select if pep was initiated", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(pepInitiatedStr.equals("Yes") && DATE_OF_PEP.equals(""))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please enter date and time of PEP initiation", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(exposureLocationStr.equals(""))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select where the exposure occurred", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(exposureLocationStr.equals("Other") && otherLocationET.getText().toString().equals(""))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please specify where exposure occurred", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }


        //////////////////////////////////////////////////////////////////////////////
        // type of exposure validation here
        // //////////////////////////////////////////////////////////////////////////

        if(exposureTypeStr.equals(""))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select the type of exposure", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }


        if (isSharp){
            if(deviceUsedStr.equals(""))
            {
                Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select the device that caused injury/exposure", Snackbar.LENGTH_LONG).show();
                valid = false;
                return valid;
            }

            if(deviceUsedStr.equals("Other") && otherDeviceET.getText().toString().equals(""))
            {
                Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please specify the device that caused injury/exposure", Snackbar.LENGTH_LONG).show();
                valid = false;
                return valid;
            }


            if(devicePurposeStr.equals(""))
            {
                Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select the purpose of the device used", Snackbar.LENGTH_LONG).show();
                valid = false;
                return valid;
            }

            if(devicePurposeStr.equals("Other") && otherPurposeET.getText().toString().equals(""))
            {
                Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please specify the purpose of the device used", Snackbar.LENGTH_LONG).show();
                valid = false;
                return valid;
            }


            if(exposureHowStr.equals(""))
            {
                Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select how the injury/exposure occurred", Snackbar.LENGTH_LONG).show();
                valid = false;
                return valid;
            }

            if(exposureHowStr.equals("Other") && otherHowET.getText().toString().equals(""))
            {
                Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please specify how the injury/exposure occurred", Snackbar.LENGTH_LONG).show();
                valid = false;
                return valid;
            }

            if(describeExposureStr.equals(""))
            {
                Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select a description of the injury/exposure", Snackbar.LENGTH_LONG).show();
                valid = false;
                return valid;
            }

        }else {

            if(exposureCauseStr.equals(""))
            {
                Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select what the exposure was a result of", Snackbar.LENGTH_LONG).show();
                valid = false;
                return valid;
            }

            if(exposureCauseStr.equals("Other") && otherExposureCauseET.getText().toString().equals(""))
            {
                Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please specify what the exposure was a result of", Snackbar.LENGTH_LONG).show();
                valid = false;
                return valid;
            }
        }


        //////////////////////////////////////////////////////////////////////////////
        // end of type of exposure validation
        // //////////////////////////////////////////////////////////////////////////

        if(patientHIVStr.equals(""))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select HIV status of source patient/specimen", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(patientHBVStr.equals(""))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select HBV status of source patient/specimen", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(TextUtils.isEmpty(previous_exposures.getText().toString()))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please enter number of exposures in last 1 year", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        try {
            if (Integer.parseInt(previous_exposures.getText().toString()) > 0){
                if(pepPreviousInitiatedStr.equals(""))
                {
                    Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please answer if PEP was initiated in the previous exposure", Snackbar.LENGTH_LONG).show();
                    valid = false;
                    return valid;
                }
            }
        } catch (NumberFormatException e){
           e.printStackTrace();
        }

        return valid;
    }




    private void doReport() {
        JSONObject payload  = new JSONObject();
        try {
            payload.put("exposure_date", DATE_OF_EXPOSURE);
            payload.put("pep_date",DATE_OF_PEP);
            payload.put("exposure_location", exposureLocationStr.equals("Other") ? otherLocationET.getText().toString() : exposureLocationStr);
            payload.put("exposure_type", exposureTypeStr);
            payload.put("device_used", deviceUsedStr.equals("Other") ? otherDeviceET.getText().toString() : deviceUsedStr);
            payload.put("result_of", exposureCauseStr.equals("Other") ? otherExposureCauseET.getText().toString() : exposureCauseStr);
            payload.put("device_purpose", devicePurposeStr.equals("Other") ? otherPurposeET.getText().toString() : devicePurposeStr);
            payload.put("exposure_when", exposureHowStr.equals("Other") ? otherHowET.getText().toString() : exposureHowStr);
            payload.put("exposure_description", describeExposureStr);
            payload.put("patient_hiv_status", patientHIVStr);
            payload.put("patient_hbv_status", patientHBVStr);
            payload.put("previous_exposures", previous_exposures.getText().toString());
            payload.put("previous_pep_initiated", pepPreviousInitiatedStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("Payload", payload.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Stash.getString(Constants.END_POINT)+Constants.REPORT_EXPOSURE, payload, new Response.Listener<JSONObject>() {

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

                        Snackbar.make(root.findViewById(R.id.fragment_report_exposure), message, Snackbar.LENGTH_LONG).show();


                        NavHostFragment.findNavController(ReportExposuresFragment.this).navigate(R.id.nav_exposures);

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
                Snackbar.make(root.findViewById(R.id.fragment_report_exposure), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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
