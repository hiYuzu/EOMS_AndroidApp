package com.a712.emos_androidapp.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.receiveModel.FileModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Lenovo on 2017/7/4.
 */

public class FileGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private List<FileModel> datas;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);

        void onItemLongClick(View view);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public FileGridAdapter(Context context, List<FileModel> datas) {
        mContext = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext
            ).inflate(R.layout.grid_file_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            return holder;

        } else {
            MyViewHolder2 holder2 = new MyViewHolder2(LayoutInflater.from(mContext).inflate(R.layout.page_item, parent,
                    false));
            return holder2;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            Picasso.with(mContext).load(datas.get(position).getFileRelativePath()).into(((MyViewHolder) holder).iv);
        } else if (holder instanceof MyViewHolder2) {
//            ((MyViewHolder2) holder).tv.setText(datas.get(position).getPage() + "页");
            ((MyViewHolder2) holder).tv.setText("无更多图片");
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        //判断item是图还是显示页数（图片有URL）
        if (!TextUtils.isEmpty(datas.get(position).getFileRelativePath())) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemLongClick(v);
        }
        return false;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageButton iv;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageButton) view.findViewById(R.id.iv);
        }
    }

    class MyViewHolder2 extends RecyclerView.ViewHolder {
        private TextView tv;

        public MyViewHolder2(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
        }
    }

}
