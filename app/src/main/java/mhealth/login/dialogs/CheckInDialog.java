package mhealth.login.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.dependencies.Constants;


public class CheckInDialog extends BottomSheetDialogFragment {


    private Context context;
    private Unbinder unbinder;

    private FusedLocationProviderClient fusedLocationClient;



    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.title)
    TextView titleTextView;

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

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.e("Location: ", location.getLatitude()+" : "+location.getLongitude());
                            String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center="
//                            String mapUrl = "http://maps.google.com/maps/api/staticmap?center="
                                    + location.getLatitude()+","+location.getLongitude()
                                    + "&zoom=15&size=600x300&key=" + Constants.PLACES_API_KEY;


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



        return view;
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
