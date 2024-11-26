package mhealth.login.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.dependencies.Tools;
import mhealth.login.dependencies.ViewAnimation;
import mhealth.login.models.CheckIn;
import mhealth.login.models.Exposure;

public class ExposuresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Exposure> items = new ArrayList<>();

    private Context context;
    private ExposuresAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(ExposuresAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ExposuresAdapter(Context context, List<Exposure> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView type;
        public TextView location;
        public TextView date;
        public TextView previous_exposure;
        public TextView pep_initiated;
        public TextView patient_hiv;
        public TextView patient_hbv;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            type = (TextView) v.findViewById(R.id.type);
            location = (TextView) v.findViewById(R.id.location);
            date = (TextView) v.findViewById(R.id.date);
            previous_exposure = (TextView) v.findViewById(R.id.previous_exposure);
            pep_initiated = (TextView) v.findViewById(R.id.pep_initiated);
            patient_hiv = (TextView) v.findViewById(R.id.patient_hiv);
            patient_hbv = (TextView) v.findViewById(R.id.patient_hbv);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exposure, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Exposure obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.type.setText(obj.getExposure_type());
            view.location.setText("Location: "+obj.getExposure_location());
            view.date.setText(obj.getExposure_date());
            view.previous_exposure.setText("Previous Exposures: "+obj.getPrevious_exposures());
            view.pep_initiated.setText("Previous Pep Initiated: "+obj.getPrevious_pep_initiated());
            view.patient_hbv.setText("Patient HBV: "+obj.getPatient_hbv_status());
            view.patient_hiv.setText("Patient HIV: "+obj.getPatient_hiv_status());


            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition =holder.getAbsoluteAdapterPosition();
                    boolean show = toggleLayoutExpand(!obj.expanded, v, view.lyt_expand);
                    items.get(adapterPosition).expanded = show;
                }
            });


            // void recycling view
            if(obj.expanded){
                view.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                view.lyt_expand.setVisibility(View.GONE);
            }
            Tools.toggleArrow(obj.expanded, view.bt_expand, false);
        }
    }

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);
        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}

