package mhealth.login.fragments.Immunization;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.fxn.stash.Stash;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private String DATE_OF_IMMUNIZAION1 = "";
    private String DATE_OF_IMMUNIZAION2 = "";



    @BindView(R.id.immunization_date1)
    TextView immunization_date1 ;

    @BindView(R.id.immunization_date2)
    TextView immunization_date2 ;

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

        immunization_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImmunizationDate1();
            }
        });

        immunization_date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImmunizationDate2();
            }
        });



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

    private void getImmunizationDate1(){
        Calendar cur_calender = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        long date_ship_millis = calendar.getTimeInMillis();
                        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

                        DATE_OF_IMMUNIZAION1 = newFormat.format(new Date(date_ship_millis));
                        immunization_date1.setText("Date of first immunization: "+newFormat.format(new Date(date_ship_millis)));


                    }
                }, cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getImmunizationDate2(){
        Calendar cur_calender = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        long date_ship_millis = calendar.getTimeInMillis();
                        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

                        DATE_OF_IMMUNIZAION2 = newFormat.format(new Date(date_ship_millis));
                        immunization_date2.setText("Date of second immunization: "+newFormat.format(new Date(date_ship_millis)));

                    }
                }, cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
