package cn.projects.team.demo.adapter;

import android.content.Context;
import android.content.Intent;
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
import cn.projects.team.demo.model.ContactGroup;
//import cn.projects.team.demo.ui.GroupContactActivity;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ContactGroup> mData;
    private Context mContext;

    public GroupAdapter(Context context, List<ContactGroup> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.mContext = context;
    }



    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_group, parent,false);
        GroupAdapter.ViewHolder viewHolder = new GroupAdapter.ViewHolder(view);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GroupAdapter.ViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }

        holder.tvName.setText(this.mData.get(position).getGroupName());

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, mData.get(position).getName(),Toast.LENGTH_SHORT).show();

                //edit it later
//                Intent intent = new Intent(mContext,GroupContactActivity.class);
//                intent.putExtra("groupId",mData.get(position).getId());
//                mContext.startActivity(intent);
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

    private SortAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(SortAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //**************************************************************

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
