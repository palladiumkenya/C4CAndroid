package mhealth.login.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fxn.stash.Stash;
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
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.CheckInDialog;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.CheckIn;
import mhealth.login.models.User;
import static mhealth.login.dependencies.AppController.TAG;


public class CheckinFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;


    private boolean myShouldLoadMore = true;
    private String MY_NEXT_LINK = null;

    private CheckinAdapter mAdapter;
    private ArrayList<CheckIn> myVehiclesArrayList;

    private static final int REQUEST_LOCATION = 100;


    @BindView(R.id.fab_checkin)
    FloatingActionButton fab_checkin;

    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.no_checkins)
    LinearLayout no_checkins;



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
        root =  inflater.inflate(R.layout.fragment_checkin, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(CheckinFragment.this).navigate(R.id.nav_complete_profile);
        }
        fab_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);
                } else {
                    CheckInDialog bottomSheetFragment = CheckInDialog.newInstance(context);
                    bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                }
            }
        });



        myVehiclesArrayList = new ArrayList<>();
        mAdapter = new CheckinAdapter(context,myVehiclesArrayList);


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

        mAdapter.setOnItemClickListener(new CheckinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CheckIn clickedItem = myVehiclesArrayList.get(position);
//                Intent i = new Intent(context, ClickedActivity.class);
//                i.putExtra("vehicle", (Serializable) clickedItem);
//                startActivity(i);

            }
        });

        return root;
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
                Constants.END_POINT+ Constants.CHECKIN_HISTORY, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

//                    Log.e("resoponse", response.toString());

                    myVehiclesArrayList.clear();

                    myShouldLoadMore = true;
                    recyclerView.setVisibility(View.VISIBLE);

                    if (shimmer_my_container!=null){
                        shimmer_my_container.stopShimmerAnimation();
                        shimmer_my_container.setVisibility(View.GONE);
                    }


                        boolean  status = response.has("success") && response.getBoolean("success");
                        String  message = response.has("message") ? response.getString("message") : "" ;
                        String  errors = response.has("errors") ? response.getString("errors") : "" ;


                        if (status){
                            JSONArray myVehiclesArray = response.getJSONArray("data");
                            JSONObject links = response.getJSONObject("links");
                            MY_NEXT_LINK = links.getString("next");

                            if (myVehiclesArray.length() > 0){

                                no_checkins.setVisibility(View.GONE);


                                for (int i = 0; i < myVehiclesArray.length(); i++) {

                                    JSONObject item = (JSONObject) myVehiclesArray.get(i);


                                    int  id = item.has("id") ? item.getInt("id") : 0;
                                    int user_id = item.has("user_id") ? item.getInt("user_id") : 0;
                                    String lat = item.has("lat") ? item.getString("lat") : "";
                                    String lng = item.has("lng") ? item.getString("lng") : "";
                                    int approved = item.has("approved") ? item.getInt("approved") : 0;
                                    String created_at = item.has("created_at") ? item.getString("created_at") : "";
                                    String updated_at = item.has("updated_at") ? item.getString("updated_at") : "";

                                    CheckIn checkIn = new CheckIn(id,user_id,lat,lng,approved,created_at,updated_at);

                                    myVehiclesArrayList.add(checkIn);
                                    mAdapter.notifyDataSetChanged();

                                }

                            }else {
                                //not data found
                                no_checkins.setVisibility(View.VISIBLE);
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
                Snackbar.make(root.findViewById(R.id.frag_checkin), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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

                            no_checkins.setVisibility(View.GONE);


                            for (int i = 0; i < myVehiclesArray.length(); i++) {

                                JSONObject item = (JSONObject) myVehiclesArray.get(i);


                                int  id = item.has("id") ? item.getInt("id") : 0;
                                int user_id = item.has("user_id") ? item.getInt("user_id") : 0;
                                String lat = item.has("lat") ? item.getString("lat") : "";
                                String lng = item.has("lng") ? item.getString("lng") : "";
                                int approved = item.has("approved") ? item.getInt("approved") : 0;
                                String created_at = item.has("created_at") ? item.getString("created_at") : "";
                                String updated_at = item.has("updated_at") ? item.getString("updated_at") : "";

                                CheckIn checkIn = new CheckIn(id,user_id,lat,lng,approved,created_at,updated_at);

                                myVehiclesArrayList.add(checkIn);
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
                Snackbar.make(root.findViewById(R.id.frag_checkin), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do de ting

                    CheckInDialog bottomSheetFragment = CheckInDialog.newInstance(context);
                    bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());

                } else {
                    InfoMessage bottomSheetFragment = InfoMessage.newInstance("Permission Denied",
                            "Unfortunately, you can not check in if you don\'t allow the device to access your location",context);
                    bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
