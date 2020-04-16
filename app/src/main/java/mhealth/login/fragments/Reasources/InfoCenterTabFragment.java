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
import com.fxn.stash.Stash;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.models.User;


public class InfoCenterTabFragment extends Fragment {
    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

    @BindView(R.id.faqs)
    LinearLayout faqs;

    @BindView(R.id.art_guidelines)
    LinearLayout art_guidelines;

    @BindView(R.id.nascop)
    LinearLayout nascop;

    @BindView(R.id.nascop_hiv)
    LinearLayout nascop_hiv;

    @BindView(R.id.resources_links)
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
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
