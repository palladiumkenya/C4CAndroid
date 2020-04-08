package mhealth.login.fragments.Immunization;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.adapters.ExposuresAdapter;
import mhealth.login.adapters.ImmunizationsAdapter;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.Exposure;
import mhealth.login.models.Immunization;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class ImmunizationProfileFragment extends Fragment {
    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.no_immunizations)
    LinearLayout no_immunizations;


    private ImmunizationsAdapter mAdapter;
    private ArrayList<Immunization> immunizationArrayList;

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
        root =  inflater.inflate(R.layout.fragment_immunization_profile, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);


        immunizationArrayList = new ArrayList<>();
        mAdapter = new ImmunizationsAdapter(context, immunizationArrayList);


        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        recyclerView.setAdapter(mAdapter);

        loadImmunizations();


        mAdapter.setOnItemClickListener(new ImmunizationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Immunization clickedItem = immunizationArrayList.get(position);
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
        shimmer_my_container.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_my_container.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void loadImmunizations() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Stash.getString(Constants.END_POINT)+ Constants.IMMUNIZATIONS, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

//                    Log.e("resoponse", response.toString());

                    immunizationArrayList.clear();

                    if (recyclerView!=null)
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

                        if (myArray.length() > 0){

                            no_immunizations.setVisibility(View.GONE);


                            for (int i = 0; i < myArray.length(); i++) {

                                JSONObject item = (JSONObject) myArray.get(i);


                                int  disease_id = item.has("disease_id") ? item.getInt("disease_id") : 0;
                                String disease = item.has("disease") ? item.getString("disease") : "";
                                JSONArray immunizations = item.has("immunizations") ? item.getJSONArray("immunizations") : null;

                                List<String> immunizationDates = new ArrayList<>();

                                if (immunizations != null){
                                    for (int im = 0; im < immunizations.length(); im++) {
                                        immunizationDates.add((String) immunizations.get(im));
                                    }
                                }

                               Immunization newImmunization = new Immunization(disease_id, disease,immunizationDates);

                                immunizationArrayList.add(newImmunization);
                                mAdapter.notifyDataSetChanged();

                            }

                        }else {
                            //not data found
                            no_immunizations.setVisibility(View.VISIBLE);
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

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (root!=null)
                    Snackbar.make(root.findViewById(R.id.fragment_immunization_profile), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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


}
