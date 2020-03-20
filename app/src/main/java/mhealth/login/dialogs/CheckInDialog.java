package mhealth.login.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fxn.stash.Stash;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.fragments.CheckinFragment;
import mhealth.login.fragments.CreateProfileFragment;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class CheckInDialog extends BottomSheetDialogFragment {


    private Context context;
    private Unbinder unbinder;
    private User loggedInUser;

    private FusedLocationProviderClient fusedLocationClient;



    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.title)
    TextView titleTextView;

    @BindView(R.id.checkin)
    Button checkIn;

    @BindView(R.id.cancel_checkin)
    Button cancelCheckin;

    Location location; // location
    double lat; // latitude
    double lng; // longitude



    public CheckInDialog() {
        // Required empty public constructor
    }



    public static CheckInDialog newInstance(String error, Context context) {
        CheckInDialog fragment = new CheckInDialog();
        fragment.context = context;
        return fragment;
    }

    public static CheckInDialog newInstance(Context context) {
        CheckInDialog fragment = new CheckInDialog();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.info_check_in, container, false);
        unbinder = ButterKnife.bind(this, view);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.e("Location: ", location.getLatitude()+" : "+location.getLongitude());

                            String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center="+ location.getLatitude()+","+location.getLongitude()+ "&zoom=16&size=600x300&sensor=true&markers=color:red%7label:C%7C|"+location.getLatitude()+","+location.getLongitude()+"&key=" + Constants.PLACES_API_KEY;

                            Log.e("map url: ", mapUrl);

                            Picasso.get()
                                    .load(mapUrl)
                                    .into(image);




//                            Glide.with(context)
//                                    .load(mapUrl)
//                                    .fitCenter()
////                                    .placeholder(R.drawable.img_wizard_1)
//                                    .into(image);




                        }else {
                            Log.e("Location: ", "location is null");
                        }
                    }
                });

        cancelCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(CheckInDialog.this).navigate(R.id.nav_check_in);


            }
        });

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Check In confirmed!", Toast.LENGTH_SHORT).show();
                sendCheckin();

            }
        });



        return view;
    }

    private void sendCheckin(){


        JSONObject payload  = new JSONObject();
        try {
            payload.put("lat", lat);
            payload.put("lng", lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Stash.getString(Constants.END_POINT)+Constants.CHECKIN, payload, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());



                try {
                    boolean  status = response.has("success") && response.getBoolean("success");
                    String  message = response.has("message") ? response.getString("message") : "" ;
                    String  errors = response.has("errors") ? response.getString("errors") : "" ;

                    if (status){
                        InfoMessage bottomSheetFragment = InfoMessage.newInstance("Success!",message, context);
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());


                        Stash.put(Constants.LOGGED_IN_USER, loggedInUser);
                        NavHostFragment.findNavController(CheckInDialog.this).navigate(R.id.nav_check_in);

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


                VolleyLog.d(TAG, "Error: " + error.getMessage());

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


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
