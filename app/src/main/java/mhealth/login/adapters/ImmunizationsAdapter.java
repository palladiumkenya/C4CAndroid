package mhealth.login.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mhealth.login.R;
import mhealth.login.dependencies.Tools;
import mhealth.login.dependencies.ViewAnimation;
import mhealth.login.models.BroadCast;
import mhealth.login.models.Immunization;

public class ImmunizationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Immunization> items = new ArrayList<>();

    private Context context;
    private ImmunizationsAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(ImmunizationsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ImmunizationsAdapter(Context context, List<Immunization> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView disease;
        public TextView immunizations;
        public TextView dose_1_date;
        public TextView dose_2_date;
        public TextView dose_3_date;
        public ImageButton bt_expand;
        public View layoutDose1;
        public View layoutDose2;
        public View layoutDose3;
        public View lyt_expand;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            disease = (TextView) v.findViewById(R.id.disease);
            immunizations = (TextView) v.findViewById(R.id.immunizations);
            dose_1_date = (TextView) v.findViewById(R.id.dose_1_date);
            dose_2_date = (TextView) v.findViewById(R.id.dose_2_date);
            dose_3_date = (TextView) v.findViewById(R.id.dose_3_date);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            layoutDose1 = (View) v.findViewById(R.id.layoutDose1);
            layoutDose2 = (View) v.findViewById(R.id.layoutDose2);
            layoutDose3 = (View) v.findViewById(R.id.layoutDose3);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_immunization, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Immunization obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.disease.setText(obj.getDisease());
            view.immunizations.setText("Doses: "+obj.getImmunizations().size());

            for (int i=0; i<obj.getImmunizations().size(); i++){
                switch (i) {
                    case 0:
                        view.dose_1_date.setText(obj.getImmunizations().get(i));
                        view.layoutDose1.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        view.dose_2_date.setText(obj.getImmunizations().get(i));
                        view.layoutDose2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        view.dose_3_date.setText(obj.getImmunizations().get(i));
                        view.layoutDose3.setVisibility(View.VISIBLE);
                        break;
                    default:
                        Log.e("More data:","more than one immunizations found");
                }
            }

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterposition =holder.getAdapterPosition();
                    boolean show = toggleLayoutExpand(!obj.expanded, v, view.lyt_expand);
                    items.get(adapterposition).expanded = show;
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

