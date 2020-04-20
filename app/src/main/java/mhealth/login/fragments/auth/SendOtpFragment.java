package mhealth.login.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mhealth.login.R;


public class SendOtpFragment extends Fragment {


    public SendOtpFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SendOtpFragment newInstance() {
        SendOtpFragment fragment = new SendOtpFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_otp, container, false);
    }
}
