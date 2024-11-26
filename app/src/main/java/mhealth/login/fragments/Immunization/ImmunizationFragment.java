package mhealth.login.fragments.Immunization;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//import com.fxn.stash.Stash;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.UserStorage;
import mhealth.login.fragments.HomeFragment;
import mhealth.login.models.Token;
import mhealth.login.models.User;


public class ImmunizationFragment extends Fragment {
   // private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;


//    @BindView(R.id.tab_layout)
   TabLayout tab_layout;
//
//    @BindView(R.id.view_pager)
    ViewPager view_pager;
//
//    @BindView(R.id.fab_new_immunization)
    FloatingActionButton fab_new_immunization;

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
        root =  inflater.inflate(R.layout.fragment_immunization, container, false);
       // unbinder = ButterKnife.bind(this, root);

        tab_layout=(TabLayout) root.findViewById(R.id.tab_layout);
        view_pager =(ViewPager) root.findViewById(R.id.view_pager);

        fab_new_immunization =(FloatingActionButton) root.findViewById(R.id.fab_new_immunization);







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
            NavHostFragment.findNavController(ImmunizationFragment.this).navigate(R.id.nav_complete_profile);
        }

        setupViewPager(view_pager);
        tab_layout.setupWithViewPager(view_pager);

        fab_new_immunization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ImmunizationFragment.this).navigate(R.id.nav_new_immunization);
            }
        });


        return root;
    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ImmunizationProfileFragment(), "Immunization Profile");
//        adapter.addFragment(new ImmunizationScheduleFragment(), "Immunization Schedule");
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





}
