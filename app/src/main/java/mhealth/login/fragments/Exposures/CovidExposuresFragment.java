package mhealth.login.fragments.Exposures;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
//import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mhealth.login.R;
import mhealth.login.adapters.CovidExposuresAdapter;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.UserStorage;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.CovidExposure;
import mhealth.login.models.Token;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class CovidExposuresFragment extends Fragment {
   // private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

    private boolean myShouldLoadMore = true;
    private String MY_NEXT_LINK = null;

    private CovidExposuresAdapter mAdapter;
    private List<CovidExposure> covidExposureArrayList;




//    @BindView(R.id.fab_add_exposure)
//    FloatingActionButton fab_add_exposure;

//    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;
//
//    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
//
//    @BindView(R.id.no_exposures)
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
        root =  inflater.inflate(R.layout.fragment_covid_exposures, container, false);
        //unbinder = ButterKnife.bind(this, root);




 shimmer_my_container= (ShimmerFrameLayout) root.findViewById(R.id.shimmer_my_container);
 recyclerView = ( RecyclerView) root.findViewById(R.id.recyclerView);
 no_exposures= (LinearLayout) root.findViewById(R.id.no_exposures);

       // loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);
//        try{
//            List<Token> _url =Token.findWithQuery(Token.class, "SELECT *from Token ORDER BY id DESC LIMIT 1");
//            if (_url.size()==1){
//                for (int x=0; x<_url.size(); x++){
//                    loggedInUser=   _url.get(x).getToken();
//                }
//            }
//
//        } catch(Exception e){
//
//        }
        loggedInUser = UserStorage.getUser(context);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(CovidExposuresFragment.this).navigate(R.id.nav_complete_profile);
        }


        covidExposureArrayList = new ArrayList<>();
        mAdapter = new CovidExposuresAdapter(context, covidExposureArrayList);


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

        mAdapter.setOnItemClickListener(new CovidExposuresAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CovidExposure clickedItem = covidExposureArrayList.get(position);
//                Intent i = new Intent(context, ClickedActivity.class);
//                i.putExtra("vehicle", (Serializable) clickedItem);
//                startActivity(i);

            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
//        shimmer_my_container.startShimmerAnimation();
        shimmer_my_container.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
//        shimmer_my_container.stopShimmerAnimation();
        shimmer_my_container.setVisibility(View.GONE);
        super.onPause();
    }


    private void firstLoad() {

        myShouldLoadMore =false;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.END_POINT+ Constants.MY_COVID_EXPOSURES, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

//                    Log.e("resoponse", response.toString());

                    covidExposureArrayList.clear();

                    myShouldLoadMore = true;
                    if (recyclerView != null)
                        recyclerView.setVisibility(View.VISIBLE);

                    if (shimmer_my_container!=null){
//                        shimmer_my_container.stopShimmerAnimation();
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

                            if (no_exposures != null)
                                no_exposures.setVisibility(View.GONE);


                            for (int i = 0; i < myArray.length(); i++) {

                                JSONObject item = (JSONObject) myArray.get(i);


                                int  id = item.has("id") ? item.getInt("id") : 0;
                                int id_no = item.has("id_no") ? item.getInt("id_no") : 0;
                                String date_of_contact = item.has("date_of_contact") ? item.getString("date_of_contact") : "";
                                String ppe_worn = item.has("ppe_worn") ? item.getString("ppe_worn") : "";
                                String ppes = item.has("ppes") ? item.getString("ppes") : "";
                                String ipc_training = item.has("ipc_training") ? item.getString("ipc_training") : "";
                                String symptoms = item.has("symptoms") ? item.getString("symptoms") : "";
                                String pcr_test = item.has("pcr_test") ? item.getString("pcr_test") : "";
                                String management = item.has("management") ? item.getString("management") : "";
                                String isolation_start_date = item.has("isolation_start_date") ? item.getString("isolation_start_date") : "";
                                String contact_with = item.has("contact_with") ? item.getString("contact_with") : "";
                                String place_of_diagnosis = item.has("place_of_diagnosis") ? item.getString("place_of_diagnosis") : "";

                                CovidExposure covid_exposure = new CovidExposure(id,id_no,date_of_contact,ppe_worn,ppes,ipc_training,symptoms, pcr_test,management,
                                        isolation_start_date, contact_with,place_of_diagnosis);
                                covidExposureArrayList.add(covid_exposure);
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
                //if (root!=null)
                  //  Snackbar.make(root.findViewById(R.id.fragment_exposures), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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
                                int id_no = item.has("id_no") ? item.getInt("id_no") : 0;
                                String date_of_contact = item.has("date_of_contact") ? item.getString("date_of_contact") : "";
                                String ppe_worn = item.has("ppe_worn") ? item.getString("ppe_worn") : "";
                                String ppes = item.has("ppes") ? item.getString("ppes") : "";
                                String ipc_training = item.has("ipc_training") ? item.getString("ipc_training") : "";
                                String symptoms = item.has("symptoms") ? item.getString("symptoms") : "";
                                String pcr_test = item.has("pcr_test") ? item.getString("pcr_test") : "";
                                String management = item.has("management") ? item.getString("management") : "";
                                String isolation_start_date = item.has("isolation_start_date") ? item.getString("isolation_start_date") : "";
                                String contact_with = item.has("contact_with") ? item.getString("contact_with") : "";
                                String place_of_diagnosis = item.has("place_of_diagnosis") ? item.getString("place_of_diagnosis") : "";

                                CovidExposure covid_exposure = new CovidExposure(id,id_no,date_of_contact,ppe_worn,ppes,ipc_training,symptoms, pcr_test,management,
                                        isolation_start_date, contact_with,place_of_diagnosis);

                                covidExposureArrayList.add(covid_exposure);
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
