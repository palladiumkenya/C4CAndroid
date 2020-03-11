package mhealth.login.fragments.Immunization;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.fxn.stash.Stash;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.fragments.HomeFragment;
import mhealth.login.models.User;

public class NewImmunizationFragment extends Fragment {


    private Context context;
    private View root;
    private Unbinder unbinder;

    private User loggedInUser;



    @BindView(R.id.submit_immunization)
    Button btn_submit_immunization;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(ctx);
        this.context = ctx;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_new_immunization, container, false);
        unbinder = ButterKnife.bind(this, root);


        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(NewImmunizationFragment.this).navigate(R.id.nav_complete_profile);
        }

        btn_submit_immunization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(NewImmunizationFragment.this).navigate(R.id.nav_home);
                Toast.makeText(context, "Immunization details updated", Toast.LENGTH_SHORT).show();

//                Snackbar.make(view, "Immunization details updated", Snackbar.LENGTH_LONG).show();
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
