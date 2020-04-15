package mhealth.login.fragments.Exposures;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fxn.stash.Stash;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.adapters.CheckinAdapter;
import mhealth.login.adapters.ExposuresAdapter;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.CheckIn;
import mhealth.login.models.Exposure;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class ExposuresFragment extends Fragment {
    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

    private boolean myShouldLoadMore = true;
    private String MY_NEXT_LINK = null;

    private ExposuresAdapter mAdapter;
    private ArrayList<Exposure> exposureArrayList;


    private boolean fabExpanded = false;



    @BindView(R.id.fabReport)
    ExtendedFloatingActionButton fabReport;

    @BindView(R.id.layoutFabC19)
    LinearLayout layoutFabC19;

    @BindView(R.id.layoutFabOther)
    LinearLayout layoutFabOther;

    @BindView(R.id.fab_other_exposure)
    FloatingActionButton fab_other_exposure;

    @BindView(R.id.fab_cov_exposure)
    FloatingActionButton fab_cov_exposure;

//    @BindView(R.id.fab_add_exposure)
//    FloatingActionButton fab_add_exposure;

    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.no_exposures)
    LinearLayout no_exposures;


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
        root =  inflater.inflate(R.layout.fragment_exposures, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(ExposuresFragment.this).navigate(R.id.nav_complete_profile);
        }


        fabReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });

        //Only main FAB is visible in the beginning
        closeSubMenusFab();




        fab_other_exposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_add_exposure);
            }
        });

        fab_cov_exposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogContact();
            }
        });

        exposureArrayList = new ArrayList<>();
        mAdapter = new ExposuresAdapter(context,exposureArrayList);


        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        recyclerView.setAdapter(mAdapter);

        firstLoad();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollHorizontally(1)) {
                    if (myShouldLoadMore && !MY_NEXT_LINK.equals("null")) {
                        loadMore();
                    }
                }
            }
        });

        mAdapter.setOnItemClickListener(new ExposuresAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Exposure clickedItem = exposureArrayList.get(position);
//                Intent i = new Intent(context, ClickedActivity.class);
//                i.putExtra("vehicle", (Serializable) clickedItem);
//                startActivity(i);

            }
        });


        return root;
    }


    //closes FAB submenus
    private void closeSubMenusFab(){
        layoutFabC19.setVisibility(View.INVISIBLE);
        layoutFabOther.setVisibility(View.INVISIBLE);
        fabReport.setIcon(getResources().getDrawable(R.drawable.ic_exposure));
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        layoutFabC19.setVisibility(View.VISIBLE);
        layoutFabOther.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fabReport.setIcon(getResources().getDrawable(R.drawable.ic_close));
        fabExpanded = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_my_container.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_my_container.stopShimmerAnimation();
        super.onPause();
    }


    private void firstLoad() {

        myShouldLoadMore =false;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Stash.getString(Constants.END_POINT)+ Constants.GET_EXPOSURES, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

//                    Log.e("resoponse", response.toString());

                    exposureArrayList.clear();

                    myShouldLoadMore = true;
                    if (recyclerView != null)
                        recyclerView.setVisibility(View.VISIBLE);

                    if (shimmer_my_container!=null){
                        shimmer_my_container.stopShimmerAnimation();
                        shimmer_my_container.setVisibility(View.GONE);
                    }


                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;


                    if (status){
                        JSONArray myArray = response.getJSONArray("data");
                        JSONObject links = response.getJSONObject("links");
                        MY_NEXT_LINK = links.getString("next");

                        if (myArray.length() > 0){

                            no_exposures.setVisibility(View.GONE);


                            for (int i = 0; i < myArray.length(); i++) {

                                JSONObject item = (JSONObject) myArray.get(i);


                                int  id = item.has("id") ? item.getInt("id") : 0;
                                String exposure_date = item.has("exposure_date") ? item.getString("exposure_date") : "";
                                String pep_date = item.has("pep_date") ? item.getString("pep_date") : "";
                                String exposure_location = item.has("exposure_location") ? item.getString("exposure_location") : "";
                                String exposure_type = item.has("exposure_type") ? item.getString("exposure_type") : "";
                                String device_used = item.has("device_used") ? item.getString("device_used") : "";
                                String result_of = item.has("result_of") ? item.getString("result_of") : "";
                                String device_purpose = item.has("device_purpose") ? item.getString("device_purpose") : "";
                                String exposure_when = item.has("exposure_when") ? item.getString("exposure_when") : "";
                                String exposure_description = item.has("exposure_description") ? item.getString("exposure_description") : "";
                                int previous_exposures = item.has("previous_exposures") ? item.getInt("previous_exposures") : 0;
                                String patient_hiv_status = item.has("patient_hiv_status") ? item.getString("patient_hiv_status") : "";
                                String patient_hbv_status = item.has("patient_hbv_status") ? item.getString("patient_hbv_status") : "";
                                String previous_pep_initiated = item.has("previous_pep_initiated") ? item.getString("previous_pep_initiated") : "";

                                Exposure exposure = new Exposure(id,exposure_date,pep_date,exposure_location,exposure_type,device_used,result_of,device_purpose,
                                        exposure_when,exposure_description,patient_hiv_status,patient_hbv_status,previous_exposures,previous_pep_initiated);
                                exposureArrayList.add(exposure);
                                mAdapter.notifyDataSetChanged();

                            }

                        }else {
                            //not data found
                            if (no_exposures!=null)
                                no_exposures.setVisibility(View.VISIBLE);
                        }
                    }else {
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors,context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                myShouldLoadMore =true;

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (root!=null)
                    Snackbar.make(root.findViewById(R.id.fragment_exposures), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

            }
        }){
            /*
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

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void loadMore() {

        myShouldLoadMore =false;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                MY_NEXT_LINK, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    myShouldLoadMore = true;

                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;


                    if (status){
                        JSONArray myVehiclesArray = response.getJSONArray("data");
                        JSONObject links = response.getJSONObject("links");
                        MY_NEXT_LINK = links.getString("next");

                        if (myVehiclesArray.length() > 0){

                            no_exposures.setVisibility(View.GONE);


                            for (int i = 0; i < myVehiclesArray.length(); i++) {

                                JSONObject item = (JSONObject) myVehiclesArray.get(i);


                                int  id = item.has("id") ? item.getInt("id") : 0;
                                String exposure_date = item.has("exposure_date") ? item.getString("exposure_date") : "";
                                String pep_date = item.has("pep_date") ? item.getString("pep_date") : "";
                                String exposure_location = item.has("exposure_location") ? item.getString("exposure_location") : "";
                                String exposure_type = item.has("exposure_type") ? item.getString("exposure_type") : "";
                                String device_used = item.has("device_used") ? item.getString("device_used") : "";
                                String result_of = item.has("result_of") ? item.getString("result_of") : "";
                                String device_purpose = item.has("device_purpose") ? item.getString("device_purpose") : "";
                                String exposure_when = item.has("exposure_when") ? item.getString("exposure_when") : "";
                                String exposure_description = item.has("exposure_description") ? item.getString("exposure_description") : "";
                                int previous_exposures = item.has("previous_exposures") ? item.getInt("previous_exposures") : 0;
                                String patient_hiv_status = item.has("patient_hiv_status") ? item.getString("patient_hiv_status") : "";
                                String patient_hbv_status = item.has("patient_hbv_status") ? item.getString("patient_hbv_status") : "";
                                String previous_pep_initiated = item.has("previous_pep_initiated") ? item.getString("previous_pep_initiated") : "";

                                Exposure exposure = new Exposure(id,exposure_date,pep_date,exposure_location,exposure_type,device_used,result_of,device_purpose,
                                        exposure_when,exposure_description,patient_hiv_status,patient_hbv_status,previous_exposures,previous_pep_initiated);

                                exposureArrayList.add(exposure);
                                mAdapter.notifyDataSetChanged();
                            }

                        }else {
                            //not data found
                        }
                    }else {
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance(message,errors,context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                myShouldLoadMore =true;

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Snackbar.make(root.findViewById(R.id.fragment_exposures), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showDialogContact() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_contact);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        (dialog.findViewById(R.id.btn_report)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted()){
                    call_action(Constants.NASCOP_CONTACT);
                }
            }
        });

        dialog.show();
    }


    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {       //call request code

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call_action(Constants.NASCOP_CONTACT);
                } else {
                    Toast.makeText(context, "Unable to make call without call permissions", Toast.LENGTH_SHORT).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void call_action(String phoneNo){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNo));
        startActivity(callIntent);
    }


}
