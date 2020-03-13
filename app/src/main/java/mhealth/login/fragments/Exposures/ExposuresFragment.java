package mhealth.login.fragments.Exposures;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


    @BindView(R.id.fab_add_exposure)
    FloatingActionButton fab_add_exposure;

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

        fab_add_exposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_add_exposure);
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
                                int device_id = item.has("device_id") ? item.getInt("device_id") : 0;
                                String device_name = item.has("device") ? item.getString("device") : "";
                                String date = item.has("date") ? item.getString("date") : "";
                                String type = item.has("type") ? item.getString("type") : "";
                                String location = item.has("location") ? item.getString("location") : "";
                                String description = item.has("description") ? item.getString("description") : "";
                                int previous_exposures = item.has("previous_exposures") ? item.getInt("previous_exposures") : 0;
                                String patient_hiv_status = item.has("patient_hiv_status") ? item.getString("patient_hiv_status") : "";
                                String patient_hbv_status = item.has("patient_hbv_status") ? item.getString("patient_hbv_status") : "";
                                int pep_initiated = item.has("pep_initiated") ? item.getInt("pep_initiated") : 0;
                                String device_purpose = item.has("device_purpose") ? item.getString("device_purpose") : "";

                                Exposure exposure = new Exposure(id,device_id,device_name,date,type,location,description,previous_exposures,
                                        patient_hiv_status,patient_hbv_status,pep_initiated,device_purpose);

                                exposureArrayList.add(exposure);
                                mAdapter.notifyDataSetChanged();

                            }

                        }else {
                            //not data found
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
                                int device_id = item.has("device_id") ? item.getInt("device_id") : 0;
                                String device_name = item.has("device") ? item.getString("device") : "";
                                String date = item.has("date") ? item.getString("date") : "";
                                String type = item.has("type") ? item.getString("type") : "";
                                String location = item.has("location") ? item.getString("location") : "";
                                String description = item.has("description") ? item.getString("description") : "";
                                int previous_exposures = item.has("previous_exposures") ? item.getInt("location") : 0;
                                String patient_hiv_status = item.has("patient_hiv_status") ? item.getString("patient_hiv_status") : "";
                                String patient_hbv_status = item.has("patient_hbv_status") ? item.getString("patient_hbv_status") : "";
                                int pep_initiated = item.has("pep_initiated") ? item.getInt("pep_initiated") : 0;
                                String device_purpose = item.has("device_purpose") ? item.getString("device_purpose") : "";

                                Exposure exposure = new Exposure(id,device_id,device_name,date,type,location,description,previous_exposures,
                                        patient_hiv_status,patient_hbv_status,pep_initiated,device_purpose);

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

}
