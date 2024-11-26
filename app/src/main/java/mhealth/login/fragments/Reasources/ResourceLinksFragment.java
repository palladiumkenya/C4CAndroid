package mhealth.login.fragments.Reasources;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.fxn.stash.Stash;

import java.util.ArrayList;
import java.util.List;

//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.adapters.FAQsAdapter;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.UserStorage;
import mhealth.login.models.FAQ;
import mhealth.login.models.Token;
import mhealth.login.models.User;


public class ResourceLinksFragment extends Fragment {
   // private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;



    TextView lk1;
    TextView lk2;
    TextView lk3;
    TextView lk4;
    TextView lk5;
    TextView lk6;
    TextView lk7;
    TextView lk8;
    TextView lk9;
    TextView lk10;


    private FAQsAdapter mAdapter;
    private ArrayList<FAQ> faqArrayList;

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
        root =  inflater.inflate(R.layout.fragment_resource_links, container, false);
       // unbinder = ButterKnife.bind(this, root);

        lk1= (TextView) root.findViewById(R.id.lk1);
        lk2= (TextView) root.findViewById(R.id.lk2);
        lk3= (TextView) root.findViewById(R.id.lk3);
        lk4= (TextView) root.findViewById(R.id.lk4);
        lk5= (TextView) root.findViewById(R.id.lk5);
        lk6= (TextView) root.findViewById(R.id.lk6);
        lk7= (TextView) root.findViewById(R.id.lk7);
        lk8= (TextView) root.findViewById(R.id.lk8);
        lk9= (TextView) root.findViewById(R.id.lk9);
        lk10= (TextView) root.findViewById(R.id.lk10);

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



        setLinks();



        return root;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }


    private void setLinks(){

        try{
            String link1="https://c4c.mhealthkenya.co.ke/assets/IPC/national.pdf";
            String link2="https://c4c.mhealthkenya.co.ke/assets/IPC/Cardiovascular-guidelines-2018.pdf";
            String link3="https://c4c.mhealthkenya.co.ke/assets/IPC/clinical guidelines diagnosis and treatment.pdf";
            String link4="https://c4c.mhealthkenya.co.ke/assets/IPC/HealthActNo.21of2017.pdf";
            String link5="https://c4c.mhealthkenya.co.ke/assets/IPC/kenya-strategy-ncds-2015-2020.pdf";
            String link6="https://c4c.mhealthkenya.co.ke/assets/IPC/kenya_health_policy_2014_to_2030.pdf";
            String link7="https://c4c.mhealthkenya.co.ke/assets/IPC/National-Cancer-Screening-Guidelines-2018.pdf";
            String link8="https://c4c.mhealthkenya.co.ke/assets/IPC/National-Cancer-Treatment-Guidelines.pdf";
            String link9="https://c4c.mhealthkenya.co.ke/assets/IPC/NATIONAL-GUIDELINES-FOR-HEALTHY-DIETS-AND-PHYSICAL-ACTIVITY-2017-NEW-EDIT.pdf";
            String link10="https://c4c.mhealthkenya.co.ke/assets/IPC/National-Palliative-Care-Guidelines.pdf";

            setLinkToText(lk1.getText().toString(),lk1.getText().toString().length(),lk1,link1);
            setLinkToText(lk2.getText().toString(),lk2.getText().toString().length(),lk2,link2);
            setLinkToText(lk3.getText().toString(),lk3.getText().toString().length(),lk3,link3);
            setLinkToText(lk4.getText().toString(),lk4.getText().toString().length(),lk4,link4);
            setLinkToText(lk5.getText().toString(),lk5.getText().toString().length(),lk5,link5);
            setLinkToText(lk6.getText().toString(),lk6.getText().toString().length(),lk6,link6);
            setLinkToText(lk7.getText().toString(),lk7.getText().toString().length(),lk7,link7);
            setLinkToText(lk8.getText().toString(),lk8.getText().toString().length(),lk8,link8);
            setLinkToText(lk9.getText().toString(),lk9.getText().toString().length(),lk9,link9);
            setLinkToText(lk10.getText().toString(),lk10.getText().toString().length(),lk10,link10);

        }
        catch(Exception e){

            e.printStackTrace();

        }
    }

    private void setLinkToText(String message, int meslength, TextView tv, final String link){

        try{

            SpannableString spannableString = new SpannableString(message);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                }
            };
            spannableString.setSpan(clickableSpan, spannableString.length() - meslength,
                    spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(spannableString, TextView.BufferType.SPANNABLE);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
