package mhealth.login.fragments.Reasources;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.SpellCheckerSubtype;

//import com.fxn.stash.Stash;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.UserStorage;
import mhealth.login.models.Hcw;
import mhealth.login.models.Profile;
import mhealth.login.models.Token;
import mhealth.login.models.User;


public class ReasourcesFragments extends Fragment {
   // private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;
    private Hcw hcw;


    TabLayout tab_layout;
    ViewPager view_pager;

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
        root =  inflater.inflate(R.layout.fragment_reasources_fragments, container, false);
      //  unbinder = ButterKnife.bind(this, root);

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

         tab_layout= (TabLayout) root.findViewById(R.id.tab_layout);
        view_pager= (ViewPager) root.findViewById(R.id.view_pager);

       // hcw = (Hcw) Stash.getObject(Constants.HCW, Hcw.class);

        try{
            List<Profile> _url =Profile.findWithQuery(Profile.class, "SELECT *from Profile ORDER BY id DESC LIMIT 1");
            if (_url.size()==1){
                for (int x=0; x<_url.size(); x++){
                    hcw=   _url.get(x).getProfile();
                }
            }

        } catch(Exception e){

        }

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(ReasourcesFragments.this).navigate(R.id.nav_complete_profile);
        }


        setupViewPager(view_pager);
        tab_layout.setupWithViewPager(view_pager);


        return root;
    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new SpecialResourcesTabFragment(), "Covid-19");
        adapter.addFragment(new CMESTabFragment(), "General");
        adapter.addFragment(new ProtocolsTabFragment(), hcw == null ? "Facility" : hcw.getFacility_name());
        adapter.addFragment(new InfoCenterTabFragment(), "Info Center");
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
