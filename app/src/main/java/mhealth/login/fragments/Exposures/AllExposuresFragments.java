package mhealth.login.fragments.Exposures;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

//import com.fxn.stash.Stash;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


import mhealth.login.MainActivity;
import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.UserStorage;
import mhealth.login.fragments.Reasources.CMESTabFragment;
import mhealth.login.fragments.Reasources.InfoCenterTabFragment;
import mhealth.login.fragments.Reasources.ProtocolsTabFragment;
import mhealth.login.fragments.Reasources.SpecialResourcesTabFragment;
import mhealth.login.models.Hcw;
import mhealth.login.models.Profile;
import mhealth.login.models.Token;
import mhealth.login.models.User;


public class AllExposuresFragments extends Fragment {
   // private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;
    private Hcw hcw;

//    @BindView(R.id.tab_layout)
   TabLayout tab_layout;
//
//    @BindView(R.id.view_pager)
    ViewPager view_pager;
//
//    @BindView(R.id.fabReport)
    ExtendedFloatingActionButton fabReport;
//
//    @BindView(R.id.layoutFabC19)
    LinearLayout layoutFabC19;
//
//    @BindView(R.id.layoutFabOther)
   LinearLayout layoutFabOther;
//
//    @BindView(R.id.fab_other_exposure)
    FloatingActionButton fab_other_exposure;
//
//    @BindView(R.id.fab_cov_exposure)
    FloatingActionButton fab_cov_exposure;

    private boolean fabExpanded = false;


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
        root =  inflater.inflate(R.layout.fragment_exposures_fragments, container, false);
        //unbinder = ButterKnife.bind(this, root);

        tab_layout = (TabLayout) root.findViewById(R.id.tab_layout);
        view_pager= (ViewPager) root.findViewById(R.id.view_pager);
        fabReport= (ExtendedFloatingActionButton) root.findViewById(R.id.fabReport);

        layoutFabC19= ( LinearLayout) root.findViewById(R.id.layoutFabC19);

       layoutFabOther= (LinearLayout) root.findViewById(R.id.layoutFabOther);

        fab_other_exposure= (FloatingActionButton) root.findViewById(R.id.fab_other_exposure);

         fab_cov_exposure= (FloatingActionButton) root.findViewById(R.id.fab_cov_exposure);






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
        try{
            List<Profile> _prof =Profile.findWithQuery(Profile.class, "SELECT *from Profile ORDER BY id DESC LIMIT 1");
            if (_prof.size()==1){
                for (int x=0; x<_prof.size(); x++){
                    hcw=   _prof.get(x).getProfile();
                }
            }

        } catch(Exception e){

        }






       // hcw = (Hcw) Stash.getObject(Constants.HCW, Hcw.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(AllExposuresFragments.this).navigate(R.id.nav_complete_profile);
        }


        setupViewPager(view_pager);
        tab_layout.setupWithViewPager(view_pager);



        fabReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });

        //Only main FAB is visible in the beginning
        closeSubMenusFab();

        fab_other_exposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_add_exposure);
            }
        });

        fab_cov_exposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_report_covid_exposure);
            }
        });


        return root;
    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new HIVExposuresFragment(), "HIV EXPOSURES");
        adapter.addFragment(new CovidExposuresFragment(), "COVID EXPOSURES");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    //closes FAB submenus
    private void closeSubMenusFab(){
        layoutFabC19.setVisibility(View.INVISIBLE);
        layoutFabOther.setVisibility(View.INVISIBLE);
        fabReport.setIcon(getResources().getDrawable(R.drawable.ic_exposure));
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        layoutFabC19.setVisibility(View.VISIBLE);
        layoutFabOther.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fabReport.setIcon(getResources().getDrawable(R.drawable.ic_close));
        fabExpanded = true;
    }

}
