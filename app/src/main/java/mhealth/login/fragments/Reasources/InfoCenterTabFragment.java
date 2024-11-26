package mhealth.login.fragments.Reasources;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
//import com.fxn.stash.Stash;


import java.util.List;

import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.UserStorage;
import mhealth.login.models.Token;
import mhealth.login.models.User;


public class InfoCenterTabFragment extends Fragment {
   // private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

    LinearLayout faqs;
    LinearLayout art_guidelines;
    LinearLayout nascop;
    LinearLayout nascop_hiv;
    LinearLayout resources_links;

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
        root =  inflater.inflate(R.layout.fragment_info_center_tab, container, false);
        //unbinder = ButterKnife.bind(this, root);

        //loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);
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


        faqs= (LinearLayout) root.findViewById(R.id.faqs);

        art_guidelines= (LinearLayout) root.findViewById(R.id.art_guidelines);
        nascop= (LinearLayout) root.findViewById(R.id.nascop);
        nascop_hiv= (LinearLayout) root.findViewById(R.id.nascop_hiv);
        resources_links= (LinearLayout) root.findViewById(R.id.resources_links);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(InfoCenterTabFragment.this).navigate(R.id.nav_complete_profile);
        }

        faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(InfoCenterTabFragment.this).navigate(R.id.nav_faqs);
            }
        });

        art_guidelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(Constants.ART_GUIDELINES);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        nascop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Constants.NASCOP_WEBSITE);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        nascop_hiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(InfoCenterTabFragment.this).navigate(R.id.nascop_training);

            }
        });

        resources_links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(InfoCenterTabFragment.this).navigate(R.id.nav_resource_links);

            }
        });


        return root;
    }


}
