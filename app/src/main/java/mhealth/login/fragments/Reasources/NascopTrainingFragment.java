package mhealth.login.fragments.Reasources;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.adapters.FAQsAdapter;
import mhealth.login.dependencies.Constants;
import mhealth.login.models.FAQ;
import mhealth.login.models.User;


public class NascopTrainingFragment extends Fragment {
    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.portallink)
    TextView portallink;

    @BindView(R.id.registrationlink)
    TextView registrationlink;



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
        root =  inflater.inflate(R.layout.fragment_nascop_training, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);


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


        portallink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Constants.PORTAL_LINK);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);

            }
        });

        registrationlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Constants.REGISTRATION_LINK);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void loadFAQs() {

        faqArrayList.add(new FAQ("Introduction",Html.fromHtml("The Ministry of Health (MOH), National AIDS and STI Control Programme (NASCOP) revised the HIV Monitoring & Evaluation tools " +
                "in 2016 with support from Implementing Partners.\n" +
                "\n" +
                "NASCOP led the development and implementation of a five-day course to update and orient health care providers on the revised national HIV M&E tools. \n" +
                "\n" +
                "NASCOP in partnership with the University of California San Francisco (UCSF), with funding from CDC, converted the HIV M&E tools training course " +
                "into eLearning format to facilitate reach to a wider audience. The online course is hosted at the University of Nairobi UHIV fellowship online" +
                " learning portal in the link below").toString()));

        faqArrayList.add(new FAQ("Purpose of the course",Html.fromHtml("The purpose of the eLearning M&E tools training course is to provide the front- line Health" +
                " Care Workers with the updates on the revised HIV M&E tools and to equip them with skills to correctly complete the tools and for appropriate" +
                " reporting. This course is accredited by regulatory bodies and carries Continuous Professional Development (CPD) points.").toString()));

        faqArrayList.add(new FAQ("How to register for National HIV Programme M&E Tools training course ",Html.fromHtml("Fill in the registration form available on the link below to enroll for the online National HIV Programmes M&E tools training course.").toString()));


        mAdapter.notifyDataSetChanged();

    }


}
