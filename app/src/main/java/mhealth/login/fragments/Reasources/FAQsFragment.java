package mhealth.login.fragments.Reasources;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
//import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mhealth.login.R;
import mhealth.login.adapters.FAQsAdapter;
import mhealth.login.adapters.ImmunizationsAdapter;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.UserStorage;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.FAQ;
import mhealth.login.models.Immunization;
import mhealth.login.models.Token;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;


public class FAQsFragment extends Fragment {
    //private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;


   // @BindView(R.id.recyclerView)
    RecyclerView recyclerView;



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
        root =  inflater.inflate(R.layout.fragment_faqs, container, false);
        //unbinder = ButterKnife.bind(this, root);

        recyclerView= (RecyclerView) root.findViewById(R.id.recyclerView);

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



        faqArrayList = new ArrayList<>();
        mAdapter = new FAQsAdapter(context, faqArrayList);


        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        recyclerView.setAdapter(mAdapter);

        loadFAQs();


        mAdapter.setOnItemClickListener(new FAQsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                FAQ clickedItem = faqArrayList.get(position);
//                Intent i = new Intent(context, ClickedActivity.class);
//                i.putExtra("vehicle", (Serializable) clickedItem);
//                startActivity(i);

            }
        });



        return root;
    }



    private void loadFAQs() {

        faqArrayList.add(new FAQ("Security and Confidentiality?","All data shared within this application is safe and secure. "));

        faqArrayList.add(new FAQ("What constitutes an occupational exposure ?",Html.fromHtml("an exposure can be defined as a percutaneous injury (e.g., needle stick or " +
                "cut with a sharp object) or contact of mucous membrane or non-intact skin (e.g., exposed skin that is chapped, abraded, or with dermatitis) " +
                "with blood, saliva, tissue, or other body fluids that are potentially infectious. Exposure incidents might place dental health care personnel at " +
                "risk for hepatitis B virus (HBV), hepatitis C virus (HCV), or human immunodeficiency virus (HIV) infection, and therefore should be evaluated " +
                "immediately following treatment of the exposure site by a qualified health care professional ").toString()));

        faqArrayList.add(new FAQ("What body fluids are potentially infectious during an occupational exposure ?",Html.fromHtml("when evaluating occupational exposures to fluids" +
                " that might contain hepatitis B virus (HBV), hepatitis C virus (HCV), or human immunodeficiency virus (HIV), health care workers should consider " +
                "that all blood, body fluids, secretions, and excretions except sweat, may contain transmissible infectious agents. Blood contains the greatest " +
                "proportion of infectious blood-borne virus particle titers of all body fluids and is the most critical transmission vehicle in the health-care " +
                "setting. During dental procedures it is predictable that saliva will become contaminated with blood. If blood is not visible, it is still likely" +
                " that very small quantities of blood are present, but the risk for transmitting HBV, HCV, or HIV is extremely small. Despite this small transmission " +
                "risk, a qualified health care professional1 should evaluate any occupational exposure2 to saliva in dental settings, regardless of visible blood ?").toString()));

        faqArrayList.add(new FAQ("What is the risk of infection after an occupational exposure; Hepatitis B Virus (HBV)?",Html.fromHtml("Health care workers who have received hepatitis B vaccine and have" +
                " developed immunity to the virus are at virtually no risk for infection. For an unvaccinated person, the risk from a single needlestick or a cut " +
                "exposure to HBV-infected blood ranges from 6%–30% and depends on the hepatitis B e antigen (HBeAg) status of the source individual. Individuals who " +
                "are both hepatitis B surface antigen (HBsAg) positive and HBeAg positive have more viruses in their blood and are more likely to transmit" +
                " HBV.<br/><br/>\\n<b>Hepatitis C Virus (HCV) </b>: Based on limited studies, the estimated risk for infection after a needle-stick or cut exposure to " +
                "HCV-infected blood is approximately 1.8%. The risk following a blood splash is unknown but is believed to be very small; however, HCV infection from " +
                "such an exposure has been reported.<br/><br/>\\n<b>Human Immunodeficiency Virus (HIV)</b>: The average risk for HIV infection after a needlestick or cut" +
                " exposure to HlV-infected blood is 0.3% (about 1 in 300). Stated another way, 99.7% of needlestick/cut exposures to HIV-contaminated blood do not lead to " +
                "infection. The risk after exposure of the eye, nose, or mouth to HIV-infected blood is estimated to be, on average, 0.1% (1 in 1,000).The risk after " +
                "exposure of the skin to HlV-infected blood is estimated to be less than 0.1%. A small amount of blood on intact skin probably poses no risk at all. There" +
                " have been no documented cases of HIV transmission due to an exposure involving a small amount of blood on intact skin (a few drops of blood on skin for" +
                " a short period of time). The risk may be higher if the skin is damaged (for example, by a recent cut), if the contact involves a large area of skin, or if" +
                " the contact is prolonged.").toString()));

        faqArrayList.add(new FAQ("What should be done following an occupational exposure ?",Html.fromHtml("Wounds and skin sites that have been in contact with blood or body fluids should be washed with soap and water; mucous membranes" +
                " should be flushed with water. Immediate evaluation must be performed by a qualified health care professional.1 Health care providers who evaluate " +
                "exposed dental health care professionals should be").toString()));

        faqArrayList.add(new FAQ("What information should I provide in case of an exposure at work?",Html.fromHtml("Date and time of exposure; <br/><br/>\\n<b>Details of the procedure being performed;</b>: ncluding where and how the" +
                " exposure occurred, whether the exposure involved a sharp device, the type of device, whether there was visible blood on the device, and how " +
                "and when during its handling the exposure occurred.<br/><br/>\\n<b>Details of the exposure;</b>: including the type and amount of fluid or material " +
                "and the severity of the exposure to the site. For a percutaneous injury, details would include the depth of the wound, the gauge of the needle, and" +
                " whether fluid was injected; for a skin or mucous membrane exposure they would include the estimated volume of material, the duration of contact, and" +
                " the condition of the skin (e.g., chapped, abraded, or intact).<br/><br/>\\n<b>Details about the exposure source; </b>: whether the patient was" +
                " infected with hepatitis B virus (HBV) and his or her hepatitis B e antigen (HBeAg) status; hepatitis C virus (HCV); or human immunodeficiency" +
                " virus (HIV); and, if the source was infected with HIV, the stage of disease, history of antiretroviral therapy, and viral load, if known. If this" +
                " information is not known from the medical record, then the source patient should be asked to obtain serologic testing for HBV, HCV, and " +
                "HIV<br/><br/>\\n<b>Details about the exposed person;</b>: e.g. hepatitis B vaccination and vaccine-response status.").toString()));

        faqArrayList.add(new FAQ("What information should I provide in case of an exposure at work?", Html.fromHtml("Date and time of exposure;" +
                " <br/><br/>\n<b>Details of the procedure being performed;</b>: ncluding where and how the exposure occurred, whether the exposure involved a" +
                " sharp device, the type of device, whether there was visible blood on the device, and how and when during its handling the exposure occurred." +
                "<br/><br/>\n<b>Details of the exposure;</b>: including the type and amount of fluid or material and the severity of the exposure to the site. For" +
                " a percutaneous injury, details would include the depth of the wound, the gauge of the needle, and whether fluid was injected; for a skin or " +
                "mucous membrane exposure they would include the estimated volume of material, the duration of contact, and the condition of the skin (e.g., " +
                "chapped, abraded, or intact).<br/><br/>\n<b>Details about the exposure source; </b>: whether the patient was infected with hepatitis B virus " +
                "(HBV) and his or her hepatitis B e antigen (HBeAg) status; hepatitis C virus (HCV); or human immunodeficiency virus (HIV); and, if the source " +
                "was infected with HIV, the stage of disease, history of antiretroviral therapy, and viral load, if known. If this information is not known from " +
                "the medical record, then the source patient should be asked to obtain serologic testing for HBV, HCV, and HIV<br/><br/>\n<b>Details about the " +
                "exposed person;</b>: e.g. hepatitis B vaccination and vaccine-response status.").toString()));

        faqArrayList.add(new FAQ("What factors must qualified health care professionals consider when assessing the need for follow-up of occupational" +
                " exposures ?",Html.fromHtml("The evaluation must include the following factors to determine the need for further follow-up:<br/><br/>Type " +
                "of exposure, percutaneous injury (e.g., depth, extent), Mucous membrane exposure, non-intact skin exposure, Bites resulting in blood exposure to" +
                " either person involved, Type and amount of fluid/tissue, Blood, Fluids containing blood, Infectious status of source, Presence of hepatitis B " +
                "surface antigen (HBsAg) and hepatitis B e antigen (HBeAg), Presence of hepatitis C virus (HCV) antibody, Presence of human immunodeficiency virus" +
                " (HIV) antibody, Susceptibility of exposed person, Hepatitis B vaccine and vaccine response status, HBV, HCV, or HIV immune status<br/><br/>After" +
                " conducting this initial evaluation of the occupational exposure, a qualified health care professional must decide whether to conduct further " +
                "follow-up on an individual basis using all of the information obtained").toString()));

        faqArrayList.add(new FAQ("What are some measures to reduce the risk of blood contact ?",Html.fromHtml("Avoiding occupational exposures to " +
                "blood is the primary way to prevent transmission of HBV, HCV, and HIV in health care settings. Methods used to reduce such exposures in include" +
                " engineering and work practice controls and the use of personal protective equipment (PPE).<br/><br/>Engineering controls isolate or remove the" +
                " blood borne pathogens hazard from the workplace. These controls are frequently technology-based and often incorporate safer designs of " +
                "instruments and devices. Examples include sharps disposal containers, rubber dams, and self-sheathing needles. Whenever possible, engineering " +
                "controls should be used as the primary method to reduce exposures to blood borne pathogens following skin penetration with sharp instruments " +
                "or needles.\n<br/><br/>Work practice controls are behavior-based and are intended to reduce the risk of blood exposure by changing the manner " +
                "in which a task is performed. Personal protective equipment consists of specialized clothing or equipment worn to protect against hazards. " +
                "Examples include gloves, masks, protective eyewear with side shields, and gowns to prevent skin and mucous membrane exposures.").toString()));




        mAdapter.notifyDataSetChanged();

    }


}
