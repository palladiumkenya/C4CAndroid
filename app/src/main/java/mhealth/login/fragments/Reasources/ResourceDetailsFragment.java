package mhealth.login.fragments.Reasources;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mhealth.login.R;
import mhealth.login.adapters.ResourcesAdapter;
import mhealth.login.adapters.ResourcesFilesAdapter;
import mhealth.login.dependencies.AppController;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.VolleyErrors;
import mhealth.login.dialogs.InfoMessage;
import mhealth.login.models.Resource;
import mhealth.login.models.ResourceFile;
import mhealth.login.models.User;

import static mhealth.login.dependencies.AppController.TAG;

public class ResourceDetailsFragment extends Fragment {
    private Unbinder unbinder;
    private View root;
    private Context context;

    private User loggedInUser;
    private Resource resource;

    private ResourcesFilesAdapter mAdapter;



    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.additional_files)
    TextView additional_files;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;



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
        root =  inflater.inflate(R.layout.fragment_resource_details, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (User) Stash.getObject(Constants.LOGGED_IN_USER, User.class);

        if (loggedInUser.getProfile_complete() == 0){
            NavHostFragment.findNavController(ResourceDetailsFragment.this).navigate(R.id.nav_complete_profile);
        }

        assert getArguments() != null;
        resource = (Resource) getArguments().getSerializable("resource");

        if (resource != null){
            title.setText(resource.getTitle());
            date.setText("Posted on: "+resource.getCreated_at());
            //text.setText(resource.getBody());

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                text.setText(Html.fromHtml(resource.getBody(),Html.FROM_HTML_MODE_LEGACY));
            } else {
                text.setText(Html.fromHtml(resource.getBody()));
            }

            // text has links specified by putting <a> tags in the string
            // resource.  By default these links will appear but not
            // respond to user input.  To make them active, you need to
            // call setMovementMethod() on the TextView object.
            text.setMovementMethod(LinkMovementMethod.getInstance());


            Glide.with(context)
                    .load(resource.getFile())
                    .fitCenter()
                    .placeholder(R.drawable.image_7)
                    .into(image);


            if (resource.getResourceFiles().size() == 0){
                additional_files.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

            }else {
                additional_files.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            mAdapter = new ResourcesFilesAdapter(context, resource.getResourceFiles());
            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);

            //set data and list adapter
            recyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new ResourcesFilesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    ResourceFile resourceFile = resource.getResourceFiles().get(position);

                    Uri uri = Uri.parse(resourceFile.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });


        }



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
