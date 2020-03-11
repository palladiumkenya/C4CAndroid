package mhealth.login.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fxn.stash.Stash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.models.User;

public class HomeFragment extends Fragment {


    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;



    @BindView(R.id.exposuresLayout)
    CardView exposuresLayout;

    @BindView(R.id.immunizationLayout)
    CardView immunizationLayout;

    @BindView(R.id.broadcastsLayout)
    CardView broadcastsLayout;

    @BindView(R.id.resourceCenterLayout)
    CardView resourceCenterLayout;

    @BindView(R.id.checkinLayout)
    CardView checkinLayout;

    @BindView(R.id.feedbackLayout)
    CardView feedbackLayout;




    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        this.context = ctx;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);


        if (loggedInUser.getProfile_complete() == 0){
            //NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_complete_profile);
        }

        exposuresLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_exposures);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_exposures);

            }
        });

        immunizationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Navigation.findNavController(v).navigate(R.id.nav_immunizations);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_immunizations);

            }
        });

        broadcastsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_broadcasts);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_broadcasts);

            }
        });

        resourceCenterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_resources);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_resources);

            }
        });

        checkinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_check_in);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_check_in);

            }
        });

        feedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_feedback);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_feedback);

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
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}