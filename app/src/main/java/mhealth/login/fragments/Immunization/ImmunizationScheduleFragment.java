package mhealth.login.fragments.Immunization;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
//import com.fxn.stash.Stash;

import java.util.List;

import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.UserStorage;
import mhealth.login.models.Token;
import mhealth.login.models.User;


public class ImmunizationScheduleFragment extends Fragment {
    //private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;

//    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;

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
        root =  inflater.inflate(R.layout.fragment_immunization_schedule, container, false);
        //unbinder = ButterKnife.bind(this, root);

        shimmer_my_container=(ShimmerFrameLayout) root.findViewById(R.id.shimmer_my_container);


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



}
