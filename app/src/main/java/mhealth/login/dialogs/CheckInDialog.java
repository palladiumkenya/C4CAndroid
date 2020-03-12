package mhealth.login.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;


public class CheckIn extends BottomSheetDialogFragment {


    private String error;
    private Context context;
    private Unbinder unbinder;
    private String title = null;


    @BindView(R.id.error_message)
    TextView errorMessage;

    @BindView(R.id.title)
    TextView titleTextView;

    public CheckIn() {
        // Required empty public constructor
    }



    public static CheckIn newInstance(String error, Context context) {
        CheckIn fragment = new CheckIn();
        fragment.error = error;
        fragment.context = context;
        return fragment;
    }

    public static CheckIn newInstance(String title, String error, Context context) {
        CheckIn fragment = new CheckIn();
        fragment.error = error;
        fragment.title = title;
        fragment.context = context;
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
        View view = inflater.inflate(R.layout.info_check_in, container, false);
        unbinder = ButterKnife.bind(this, view);

        errorMessage.setText(error);
        if (title != null)
            titleTextView.setText(title);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
