package mhealth.login.fragments.Exposures;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import mhealth.login.fragments.CreateProfileFragment;
import mhealth.login.models.Device;
import mhealth.login.models.Facility;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class ReportExposuresFragment extends Fragment {
    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

    private int deviceID = 0;

    private int pepInitiated = 0;
    private int previousExposures = 0;
    private String patientHIV = "Not Specified";
    private String patientHBV = "Not Specified";
    private String DATE_OF_EXPOSURE = "";


    ArrayList<String> devicesList;
    ArrayList<Device> devices;




    @BindView(R.id.exposure_device)
    SearchableSpinner exposureDeviceSpinner;

    @BindView(R.id.previous_exposure)
    Spinner previousExposureSpinner;

    @BindView(R.id.pepSpinner)
    Spinner pepSpinner;

    @BindView(R.id.patient_hiv)
    Spinner patientHIVSpinner;

    @BindView(R.id.patient_hbv)
    Spinner patientHBVSpinner;

    @BindView(R.id.exposure_location)
    Spinner exposure_location;

    @BindView(R.id.typeET)
    EditText typeET;

    @BindView(R.id.previous_exposures)
    EditText previous_exposureTV;

    @BindView(R.id.device_purposeET)
    EditText device_purposeET;

    @BindView(R.id.info_exposure)
    EditText info_exposure;

    @BindView(R.id.date_et)
    TextView date_et;

    @BindView(R.id.linearPEP)
    LinearLayout linearPEP;

    @BindView(R.id.linearHIV)
    LinearLayout linearHIV;

    @BindView(R.id.linearHBV)
    LinearLayout linearHBV;

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

        exposureDeviceSpinner.setTitle("Select Device");
        exposureDeviceSpinner.setPositiveButton("OK");


        previousExposureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String previousExposed = previousExposureSpinner.getSelectedItem().toString();

                if (previousExposed.equals("Yes")){
                    previous_exposureTV.setVisibility(View.VISIBLE);
                    linearPEP.setVisibility(View.VISIBLE);
                    linearHIV.setVisibility(View.VISIBLE);
                    linearHBV.setVisibility(View.VISIBLE);

                }else if (previousExposed.equals("No")){
                    previous_exposureTV.setVisibility(View.GONE);
                    previous_exposureTV.getText().clear();
                    linearPEP.setVisibility(View.GONE);
                    linearHIV.setVisibility(View.GONE);
                    linearHBV.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        pepSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String previousExposed = pepSpinner.getSelectedItem().toString();

                if (previousExposed.equals("Yes")){
                    pepInitiated = 1;

                }else if (previousExposed.equals("No")){
                   pepInitiated = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        patientHIVSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                patientHIV = patientHIVSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        patientHBVSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                patientHBV = patientHBVSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        date_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getExposureDate();
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

        getDevices();

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
                        date_et.setText("Date of exposure: "+newFormat.format(new Date(date_ship_millis)));
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

    private boolean checkNulls() {

        boolean valid = true;


        if(TextUtils.isEmpty(typeET.getText().toString()))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please provide type of exposure", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(DATE_OF_EXPOSURE.equals(""))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select date of exposure", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(deviceID == 0)
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please select the device used", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(TextUtils.isEmpty(info_exposure.getText().toString()))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please provide some more information on the exposure", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        if(TextUtils.isEmpty(device_purposeET.getText().toString()))
        {
            Snackbar.make(root.findViewById(R.id.fragment_report_exposure), "Please provide the purpose of the device used", Snackbar.LENGTH_LONG).show();
            valid = false;
            return valid;
        }

        return valid;
    }


    private void getDevices() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Stash.getString(Constants.END_POINT)+Constants.DEVICES, null, new Response.Listener<JSONObject>() {

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

                        devices = new ArrayList<Device>();
                        devicesList = new ArrayList<String>();

                        devices.clear();
                        devicesList.clear();

                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                            int id = jsonObject.has("id") ? jsonObject.getInt("id") : 0;
                            int facility_id = jsonObject.has("facility_id") ? jsonObject.getInt("facility_id") : 0;
                            String name = jsonObject.has("name") ? jsonObject.getString("name") : "";
                            int safety_designed = jsonObject.has("safety_designed") ? jsonObject.getInt("safety_designed") : 0;
                            String created_at = jsonObject.has("created_at") ? jsonObject.getString("created_at") : "";

                            Device newDevice = new Device(id,facility_id,name,safety_designed, created_at);

                            devices.add(newDevice);
                            devicesList.add(newDevice.getName());
                        }

                        devices.add(new Device(0,0,"--select device--",0,"--select--"));
                        devicesList.add("--select device--");


//                        Stash.put(Constants.DISTRICTS_ARRAYLIST, facilities);
//                        Stash.put(Constants.DISTRICTS_LIST, facilitiesList);

                        ArrayAdapter<String> aa=new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_dropdown_item,
                                devicesList){
                            @Override
                            public int getCount() {
                                return super.getCount(); // you dont display last item. It is used as hint.
                            }
                        };

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        exposureDeviceSpinner.setAdapter(aa);
                        exposureDeviceSpinner.setSelection(aa.getCount()-1);

                        deviceID = devices.get(aa.getCount()-1).getId();

                        exposureDeviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                deviceID = devices.get(position).getId();
//                                Toast.makeText(context,facilityID+"", Toast.LENGTH_LONG).show();
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

                    Snackbar.make(root.findViewById(R.id.fragment_report_exposure), Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Snackbar snackbar = Snackbar.make(root.findViewById(R.id.fragment_report_exposure), VolleyErrors.getVolleyErrorMessages(error, getContext()), Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        getDevices();
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
            payload.put("device_id", deviceID);
            payload.put("type", typeET.getText().toString());
            payload.put("location", exposure_location.getSelectedItem().toString());
            payload.put("date", DATE_OF_EXPOSURE);
            payload.put("description", info_exposure.getText().toString());
            payload.put("previous_exposures", previousExposureSpinner.getSelectedItem().toString().equals("No") ? previousExposures : previous_exposureTV.getText().toString());
            payload.put("patient_hiv_status", patientHIV);
            payload.put("patient_hbv_status", patientHBV);
            payload.put("device_purpose", device_purposeET.getText().toString());
            payload.put("pep_initiated", pepInitiated);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Stash.getString(Constants.END_POINT)+Constants.REPORT_EXPOSURE, payload, new Response.Listener<JSONObject>() {

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
