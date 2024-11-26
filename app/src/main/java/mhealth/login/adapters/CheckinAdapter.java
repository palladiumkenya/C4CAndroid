package mhealth.login.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import mhealth.login.R;
import mhealth.login.dependencies.Constants;
import mhealth.login.models.CheckIn;

public class CheckinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CheckIn> items = new ArrayList<>();

    private Context context;
    private CheckinAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(CheckinAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CheckinAdapter(Context context, List<CheckIn> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView date;
        public TextView approved;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            date = (TextView) v.findViewById(R.id.date);
            approved = (TextView) v.findViewById(R.id.approved);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkin, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CheckIn obj = items.get(position);
        OriginalViewHolder view = (OriginalViewHolder) holder;
        view.date.setText(obj.getCreated_at());
        view.approved.setText(obj.getApproved() == 1 ? "Approved" : "Not Approved");

        String mapUrl = "http://maps.google.com/maps/api/staticmap?center="
                + obj.getLat() + "," + obj.getLng()
                + "&markers=color:red%7Clabel:C%7C|" + obj.getLat() + "," + obj.getLng()
                + "&zoom=16&size=600x300&key=" + Constants.PLACES_API_KEY;

        Glide.with(context)
                .load(mapUrl)
                .fitCenter()
                .placeholder(R.drawable.img_wizard_1)
                .into(view.image);

        // Use holder.getAdapterPosition() instead of the method parameter 'position'
        view.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}

