package mhealth.login.adapters;

import android.content.Context;
import android.text.Html;
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
import mhealth.login.models.Resource;

public class ResourcesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Resource> items = new ArrayList<>();

    private Context context;
    private ResourcesAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(ResourcesAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ResourcesAdapter(Context context, List<Resource> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView snippet;
        public TextView date;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            snippet = (TextView) v.findViewById(R.id.snippet);
            date = (TextView) v.findViewById(R.id.date);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cme, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Resource obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.title.setText(obj.getTitle());
            view.date.setText(obj.getCreated_at());

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                view.snippet.setText(Html.fromHtml(obj.getBody().substring(0, Math.min(obj.getBody().length(), 100)),Html.FROM_HTML_MODE_LEGACY));
            } else {
                view.snippet.setText(Html.fromHtml(obj.getBody().substring(0, Math.min(obj.getBody().length(), 100))));
            }


            if (obj.getFile() == null){
                view.image.setVisibility(View.GONE);
            }else {
                Glide.with(context)
                        .load(obj.getFile())
                        .fitCenter()
                        .placeholder(R.drawable.image_7)
                        .into(view.image);
            }

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}

