package mhealth.login.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
//import com.fxn.stash.Stash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.UserStorage;
import mhealth.login.models.Token;
import mhealth.login.models.User;

public class HomeFragment extends Fragment {


   // private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;




    CardView exposuresLayout;
    CardView immunizationLayout;
    CardView broadcastsLayout;
    CardView resourceCenterLayout;
    CardView checkinLayout;
    CardView feedbackLayout;
    CardView jitengeLayout;




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
       // unbinder = ButterKnife.bind(this, root);

       // loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);
//        try{
//            List<Token> _url =Token.findWithQuery(Token.class, "SELECT *from Token ORDER BY id DESC LIMIT 1");
//            if (_url.size()==1){
//                for (int x=0; x<_url.size(); x++){
//                    loggedInUser=   _url.get(x).getToken();
//            }
//        }
//
//        } catch(Exception e){
//
//        }
        loggedInUser = UserStorage.getUser(context);




         exposuresLayout= (CardView) root.findViewById(R.id.exposuresLayout);
         immunizationLayout= (CardView) root.findViewById(R.id.immunizationLayout);
         broadcastsLayout= (CardView) root.findViewById(R.id.broadcastsLayout);
        resourceCenterLayout= (CardView) root.findViewById(R.id.resourceCenterLayout);
        checkinLayout= (CardView) root.findViewById(R.id.checkinLayout);
        feedbackLayout= (CardView) root.findViewById(R.id.feedbackLayout);
        jitengeLayout= (CardView) root.findViewById(R.id.jitengeLayout);



        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_complete_profile);
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


        jitengeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_feedback);
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.mhealthkenya.dm.mohkenya"));
//                startActivity(intent);

                Intent i;
                PackageManager manager = context.getPackageManager();
                try {
                    i = manager.getLaunchIntentForPackage("com.mhealthkenya.dm.mohkenya");
                    if (i == null)
                        throw new PackageManager.NameNotFoundException();
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {

                    //if not found in device then will come here
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.mhealthkenya.dm.mohkenya"));
                    startActivity(intent);
                }


            }
        });




        return root;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}