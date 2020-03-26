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
import mhealth.login.models.BroadCast;

public class PendingBroadcastsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BroadCast> items = new ArrayList<>();

    private Context context;
    private PendingBroadcastsAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(PendingBroadcastsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PendingBroadcastsAdapter(Context context, List<BroadCast> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView audience;
        public TextView cadre;
        public TextView created_date;
        public TextView approved_date;
        public TextView created_by;
        public TextView approved_by;
        public TextView message;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            audience = (TextView) v.findViewById(R.id.audience);
            created_date = (TextView) v.findViewById(R.id.created_date);
            approved_date = (TextView) v.findViewById(R.id.approved_date);
            created_by = (TextView) v.findViewById(R.id.created_by);
            approved_by = (TextView) v.findViewById(R.id.approved_by);
            message = (TextView) v.findViewById(R.id.message);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_broadcast, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BroadCast obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.audience.setText("Recipients: "+obj.getAudience());
            view.created_date.setText("On "+obj.getCreated_at());
            view.approved_date.setText("Approved on: "+obj.getUpdated_at());
            view.created_by.setText("Created by: "+obj.getCreated_by());
            view.approved_by.setText("Approved by: "+obj.getApproved_by());
            view.message.setText(obj.getMessage());

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

