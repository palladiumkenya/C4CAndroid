package mhealth.login.fragments.Reasources;

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
import mhealth.login.adapters.ResourcesAdapter;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.UserStorage;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.fragments.Immunization.NewImmunizationFragment;
import mhealth.login.models.Resource;
import mhealth.login.models.ResourceFile;
import mhealth.login.models.Token;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class ProtocolsTabFragment extends Fragment {
  //  private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

    private boolean myShouldLoadMore = true;
    private String MY_NEXT_LINK = null;

    private ResourcesAdapter mAdapter;
    private ArrayList<Resource> resourceArrayList;


    ShimmerFrameLayout shimmer_my_container;
    LinearLayout no_protocols;
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
        root = inflater.inflate(R.layout.fragment_protocols_tab, container, false);
        //unbinder = ButterKnife.bind(this, root);

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


        shimmer_my_container= (ShimmerFrameLayout) root.findViewById(R.id.shimmer_my_container);
         no_protocols= ( LinearLayout) root.findViewById(R.id.no_protocols);
         recyclerView= (RecyclerView) root.findViewById(R.id.recyclerView);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(ProtocolsTabFragment.this).navigate(R.id.nav_complete_profile);
        }


        resourceArrayList = new ArrayList<>();
        mAdapter = new ResourcesAdapter(context, resourceArrayList);


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

        mAdapter.setOnItemClickListener(new ResourcesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Resource resource = resourceArrayList.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("resource", resource);
                NavHostFragment.findNavController(ProtocolsTabFragment.this).navigate(R.id.nac_resource_details, bundle);
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
                Constants.END_POINT+ Constants.HCW_PROTOCOLS, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

//                    Log.e("resoponse", response.toString());

                    resourceArrayList.clear();

                    myShouldLoadMore = true;

                    if (recyclerView !=null)
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

                            no_protocols.setVisibility(View.GONE);


                            for (int i = 0; i < myArray.length(); i++) {

                                JSONObject item = (JSONObject) myArray.get(i);


                                int  id = item.has("id") ? item.getInt("id") : 0;
                                String title = item.has("title") ? item.getString("title") : "";
                                String body = item.has("body") ? item.getString("body") : "";
                                String file = item.has("file") ? item.getString("file") : "";
                                String created_at = item.has("created_at") ? item.getString("created_at") : "";

                                JSONArray jsonArray = (JSONArray) item.getJSONArray("files");

                                ArrayList<ResourceFile> resourceFiles = new ArrayList<>();

                                for (int j = 0; j < jsonArray.length(); j++) {

                                    JSONObject files = (JSONObject) jsonArray.get(j);

                                    String link = files.has("link") ? files.getString("link") : "";
                                    String file_name = files.has("file_name") ? files.getString("file_name") : "";

                                    ResourceFile resourceFile = new ResourceFile(file_name,link);

                                    resourceFiles.add(resourceFile);
                                }


                                Resource resource = new Resource(id,title,body,file,created_at,resourceFiles);

                                resourceArrayList.add(resource);
                                mAdapter.notifyDataSetChanged();

                            }

                        }else {
                            //not data found
                            no_protocols.setVisibility(View.VISIBLE);
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
                Snackbar.make(root.findViewById(R.id.frag_protocols), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

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
                                String title = item.has("title") ? item.getString("title") : "";
                                String body = item.has("body") ? item.getString("body") : "";
                                String file = item.has("file") ? item.getString("file") : "";
                                String created_at = item.has("created_at") ? item.getString("created_at") : "";

                                JSONArray jsonArray = (JSONArray) item.getJSONArray("files");

                                ArrayList<ResourceFile> resourceFiles = new ArrayList<>();

                                for (int j = 0; j < jsonArray.length(); j++) {

                                    JSONObject files = (JSONObject) jsonArray.get(j);

                                    String link = files.has("link") ? files.getString("link") : "";
                                    String file_name = files.has("file_name") ? files.getString("file_name") : "";

                                    ResourceFile resourceFile = new ResourceFile(file_name,link);

                                    resourceFiles.add(resourceFile);
                                }


                                Resource resource = new Resource(id,title,body,file,created_at,resourceFiles);

                                resourceArrayList.add(resource);
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
                Snackbar.make(root.findViewById(R.id.frag_protocols), VolleyErrors.getVolleyErrorMessages(error, context), Snackbar.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


}
