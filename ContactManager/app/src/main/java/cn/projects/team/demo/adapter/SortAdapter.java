package cn.projects.team.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.droidlover.xdroidmvp.demo.R;
import cn.projects.team.demo.model.Contact;
//import cn.projects.team.demo.ui.AddContactsActivity;
//
public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Contact> mData;
    private Context mContext;

    public SortAdapter(Context context, List<Contact> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.mContext = context;
    }

    @Override
    public SortAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_name, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
        viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SortAdapter.ViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }

        holder.tvName.setText(this.mData.get(position).getName());
        Glide.with(mContext)
                .load(this.mData.get(position).getIcon())
                .into( holder.icon);
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mData.get(position).getName(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //**********************itemClick************************
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //**************************************************************

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView icon ;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * Update the list after the activity
     * @param list the contact items that need to be updated
     */
    public void updateList(List<Contact> list){
        this.mData = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * get the value of char ascii based on the current position of the ListView
     * @param position the position of the ListView
     */
    public int getSectionForPosition(int position) {
        return mData.get(position).getLetters().charAt(0);
    }

    /**
     * get the position of the first character based on the value of char ascii
     * @param section that the first character located
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
