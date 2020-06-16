package mhealth.login.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mhealth.login.R;
import mhealth.login.dependencies.Tools;
import mhealth.login.dependencies.ViewAnimation;
import mhealth.login.models.CovidExposure;

public class CovidExposuresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CovidExposure> items = new ArrayList<>();

    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(CovidExposuresAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CovidExposuresAdapter(Context context, List<CovidExposure> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView date_of_contact;
        public TextView place_of_diagnosis;
        public TextView ppe;
        public TextView ppe_list;
        public TextView ipc_train;
        public TextView pcr_test_result;
        public TextView symptoms;
        public TextView contact;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            date_of_contact = (TextView) v.findViewById(R.id.date_of_contact);
            place_of_diagnosis = (TextView) v.findViewById(R.id.place_of_diagnosis);
            ppe = (TextView) v.findViewById(R.id.ppe);
            ppe_list = (TextView) v.findViewById(R.id.ppe_list);
            ipc_train = (TextView) v.findViewById(R.id.ipc_train);
            pcr_test_result = (TextView) v.findViewById(R.id.pcr_test_result);
            symptoms = (TextView) v.findViewById(R.id.tv_symptoms);
            contact = (TextView) v.findViewById(R.id.tv_contact);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_covid_exposure, parent, false);
        vh = new CovidExposuresAdapter.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CovidExposure obj = items.get(position);
        if (holder instanceof CovidExposuresAdapter.OriginalViewHolder) {
            CovidExposuresAdapter.OriginalViewHolder view = (CovidExposuresAdapter.OriginalViewHolder) holder;

            view.date_of_contact.setText(obj.getDate_of_contact());
            view.place_of_diagnosis.setText("Diagnosis: "+obj.getPlace_of_diagnosis());
            view.symptoms.setText("Symptoms: "+obj.getSymptoms());
            view.contact.setText("Contact: "+obj.getContact_with());
            view.ppe_list.setText("PPE List: "+obj.getPpes());
            view.pcr_test_result.setText("PCR Test: "+obj.getPcr_test());

            if (obj.getPpe_worn().equals("1")){
                view.ppe.setText("PPE Worn: Yes");
            }
            if (obj.getPpe_worn().equals("0")){
                view.ppe.setText("PPE Worn: No");
            }

            if (obj.getIpc_training().equals("1")){
                view.ipc_train.setText("IPC Training: Yes");
            }
            if (obj.getIpc_training().equals("0")){
                view.ipc_train.setText("IPC Training: No");
            }

//            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(view, items.get(position), position);
//                    }
//                }
//            });

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!obj.expanded, v, view.lyt_expand);
                    items.get(position).expanded = show;
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