package mhealth.login.fragments.Broadcast;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import mhealth.login.adapters.SentBroadcastsAdapter;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.BroadCast;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class BroadcastHistoryFragment extends Fragment {
    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;


    private boolean myShouldLoadMore = true;
    private String MY_NEXT_LINK = null;

    private SentBroadcastsAdapter mAdapter;
    private ArrayList<BroadCast> broadCastArrayList;


    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;


    @BindView(R.id.no_broadcasts)
    LinearLayout no_broadcasts;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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
        root =  inflater.inflate(R.layout.fragment_broadcast_history, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(BroadcastHistoryFragment.this).navigate(R.id.nav_complete_profile);
        }


        broadCastArrayList = new ArrayList<>();
        mAdapter = new SentBroadcastsAdapter(context, broadCastArrayList);


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

        mAdapter.setOnItemClickListener(new SentBroadcastsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                BroadCast broadCast = broadCastArrayList.get(position);
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("broadcast", broadCast);
//                NavHostFragment.findNavController(BroadcastHistoryFragment.this).navigate(R.id.nac_resource_details, bundle);
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
                Stash.getString(Constants.END_POINT)+ Constants.APPROVED_BROADCASTS, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

//                    Log.e("resoponse", response.toString());

                    broadCastArrayList.clear();

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

                            no_broadcasts.setVisibility(View.GONE);


                            for (int i = 0; i < myArray.length(); i++) {

                                JSONObject item = (JSONObject) myArray.get(i);


                                int  id = item.has("id") ? item.getInt("id") : 0;
                                int  facility_id = item.has("facility_id") ? item.getInt("facility_id") : 0;
                                int  cadre_id = item.has("cadre_id") ? item.getInt("cadre_id") : 0;
                                String created_by = item.has("created_by") ? item.getString("created_by") : "";
                                String approved_by = item.has("approved_by") ? item.getString("approved_by") : "";
                                String approved = item.has("approved") ? item.getString("approved") : "";
                                String SMSmessage = item.has("message") ? item.getString("message") : "";
                                int audience = item.has("audience") ? item.getInt("audience") : 0;
                                String created_at = item.has("created_at") ? item.getString("created_at") : "";
                                String updated_at = item.has("updated_at") ? item.getString("updated_at") : "";

                                JSONObject facility = item.has("facility") ? item.getJSONObject("facility") : null;
                                JSONObject cadre = item.has("cadre") ? item.getJSONObject("cadre") : null;

                                String facilityName =  "";
                                String cadreName =  "";

                                if (facility != null)
                                    facilityName = facility.has("name") ? facility.getString("name") : "";

                                if (cadre != null)
                                    cadreName = cadre.has("name") ? cadre.getString("name") : "";

                                BroadCast broadCast = new BroadCast(id,facility_id,cadre_id,created_by,approved_by,approved,SMSmessage,audience,created_at,updated_at,facilityName,cadreName);

                                broadCastArrayList.add(broadCast);
                                mAdapter.notifyDataSetChanged();

                            }

                        }else {
                            //not data found
                            no_broadcasts.setVisibility(View.VISIBLE);
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
                Snackbar.make(root.findViewById(R.id.frag_broadcasts), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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



                            for (int i = 0; i < myVehiclesArray.length(); i++) {

                                JSONObject item = (JSONObject) myVehiclesArray.get(i);

                                int  id = item.has("id") ? item.getInt("id") : 0;
                                int  facility_id = item.has("facility_id") ? item.getInt("facility_id") : 0;
                                int  cadre_id = item.has("cadre_id") ? item.getInt("cadre_id") : 0;
                                String created_by = item.has("created_by") ? item.getString("created_by") : "";
                                String approved_by = item.has("approved_by") ? item.getString("approved_by") : "";
                                String approved = item.has("approved") ? item.getString("approved") : "";
                                String SMSmessage = item.has("message") ? item.getString("message") : "";
                                int audience = item.has("audience") ? item.getInt("audience") : 0;
                                String created_at = item.has("created_at") ? item.getString("created_at") : "";
                                String updated_at = item.has("updated_at") ? item.getString("updated_at") : "";

                                JSONObject facility = item.has("facility") ? item.getJSONObject("facility") : null;
                                JSONObject cadre = item.has("cadre") ? item.getJSONObject("cadre") : null;

                                String facilityName =  "";
                                String cadreName =  "";

                                if (facility != null)
                                    facilityName = facility.has("name") ? facility.getString("name") : "";

                                if (cadre != null)
                                    cadreName = cadre.has("name") ? cadre.getString("name") : "";

                                BroadCast broadCast = new BroadCast(id,facility_id,cadre_id,created_by,approved_by,approved,SMSmessage,audience,created_at,updated_at,facilityName,cadreName);

                                broadCastArrayList.add(broadCast);
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
                Snackbar.make(root.findViewById(R.id.frag_broadcasts), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



}
