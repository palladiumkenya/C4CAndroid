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
import mhealth.login.models.ResourceFile;

public class ResourcesFilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ResourceFile> items = new ArrayList<>();

    private Context context;
    private ResourcesFilesAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(ResourcesFilesAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ResourcesFilesAdapter(Context context, List<ResourceFile> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView file_name;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            file_name = (TextView) v.findViewById(R.id.file_name);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resource_file, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ResourceFile obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.file_name.setText(obj.getFile_name());

//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                view.snippet.setText(Html.fromHtml(obj.getBody().substring(0, Math.min(obj.getBody().length(), 100)),Html.FROM_HTML_MODE_LEGACY));
//            } else {
//                view.snippet.setText(Html.fromHtml(obj.getBody().substring(0, Math.min(obj.getBody().length(), 100))));
//            }


            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterposition =holder.getAdapterPosition();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(adapterposition);
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

