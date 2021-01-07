package com.a712.emos_androidapp.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.fragment.TabPartDeviceFragment.OnListFragmentInteractionListener;
import com.a712.emos_androidapp.receiveModel.DeviceCheckedModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DeviceCheckedModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DeviceItemRecyclerViewAdapter extends RecyclerView.Adapter<DeviceItemRecyclerViewAdapter.ViewHolder> {

    private List<DeviceCheckedModel> mValues;
    private OnListFragmentInteractionListener mListener;
    public List<String> mListId = new ArrayList<>();

    public DeviceItemRecyclerViewAdapter(List<DeviceCheckedModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tab_part_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getDeviceId());
        holder.mIdView.setVisibility(View.INVISIBLE);
        holder.mCheckedView.setChecked(mValues.get(position).getChecked());
        holder.mContentView.setText(mValues.get(position).getDeviceName());
        holder.mModelView.setText(mValues.get(position).getDeviceModel());
        holder.mRepalceTimeView.setText(mValues.get(position).getReplaceTime());
        //设置初始化选中列表
        if(mValues.get(position).getChecked()){
            if(holder.mCheckedView.isChecked()){
                if(!mListId.contains(holder.mIdView.getText().toString())){
                    mListId.add(holder.mIdView.getText().toString());
                }
            }else{
                if(mListId.contains(holder.mIdView.getText().toString())){
                    mListId.remove(holder.mIdView.getText().toString());
                }
            }
        }
        //设置点击
        holder.mCheckedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValues.get(position).setChecked(holder.mCheckedView.isChecked());
                if(holder.mCheckedView.isChecked()){
                    if(!mListId.contains(holder.mIdView.getText().toString())){
                        mListId.add(holder.mIdView.getText().toString());
                    }
                }else{
                    if(mListId.contains(holder.mIdView.getText().toString())){
                        mListId.remove(holder.mIdView.getText().toString());
                    }
                }
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<String> getSelectId(){
        return mListId;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final CheckBox mCheckedView;
        public final TextView mContentView;
        public final TextView mModelView;
        public final TextView mRepalceTimeView;
        public DeviceCheckedModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mCheckedView = (CheckBox) view.findViewById(R.id.ck_device);
            mContentView = (TextView) view.findViewById(R.id.tv_content);
            mModelView = (TextView) view.findViewById(R.id.tv_model);
            mRepalceTimeView = (TextView) view.findViewById(R.id.tv_replace_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public List<DeviceCheckedModel> getmValues() {
        return mValues;
    }

    public void setmValues(List<DeviceCheckedModel> mValues) {
        this.mValues = mValues;
    }

    public OnListFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnListFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

}
