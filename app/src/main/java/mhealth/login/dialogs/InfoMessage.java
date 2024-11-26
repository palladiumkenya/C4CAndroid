package mhealth.login.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import mhealth.login.R;


public class InfoMessage extends BottomSheetDialogFragment {


    private String error;
    private Context context;
    //private Unbinder unbinder;
    private String title = null;


   // @BindView(R.id.error_message)
    TextView errorMessage;

   // @BindView(R.id.title)
    TextView titleTextView;

    public InfoMessage() {
        // Required empty public constructor
    }



    public static InfoMessage newInstance(String error, Context context) {
        InfoMessage fragment = new InfoMessage();
        fragment.error = error;
        fragment.context = context;
        return fragment;
    }

    public static InfoMessage newInstance(String title, String error, Context context) {
        InfoMessage fragment = new InfoMessage();
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
        View view = inflater.inflate(R.layout.info_bottom_sheet, container, false);
        //unbinder = ButterKnife.bind(this, view);


         errorMessage= view.findViewById(R.id.error_message);
         titleTextView=view.findViewById(R.id.title);


        errorMessage.setText(error);
        if (title != null)
            titleTextView.setText(title);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
}
