package com.a712.emos_androidapp.fragment;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.fragment.TabTaskRepertFragment.OnListFragmentInteractionListener;
import com.a712.emos_androidapp.fragment.dummy.DummyContent.DummyItem;
import com.a712.emos_androidapp.receiveModel.TaskInspectModel;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskReportRecyclerViewAdapter extends RecyclerView.Adapter<TaskReportRecyclerViewAdapter.ViewHolder> {

    private List<TaskInspectModel> mValues;
    private OnListFragmentInteractionListener mListener;
    private int EditTextSelectPosition;
    private String SelectName;
    private EditText EditTextSelect;

    public TaskReportRecyclerViewAdapter(List<TaskInspectModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tab_task_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTaskInspectId());
        holder.mIdView.setVisibility(View.INVISIBLE);
        holder.mTypeView.setText(mValues.get(position).getItemTypeName());
        holder.mNameView.setText(mValues.get(position).getItemName());
        holder.mContentView.setText(mValues.get(position).getItemContent());
        for (int i = 0; i <= holder.mResultView.getCount(); i++) {
            if (holder.mResultView.getItemAtPosition(i).toString().equals(mValues.get(position).getItemResultName())) {
                holder.mResultView.setSelection(i);
                break;
            } else {
                continue;
            }
        }
        holder.mRemarkView.setText(mValues.get(position).getItemRemark());

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

        holder.mResultView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mValues.get(position).setItemResult(String.valueOf(i));
                mValues.get(position).setItemResultName(holder.mResultView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        holder.mContentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    //失去焦点
                    mValues.get(position).setItemContent(holder.mContentView.getText().toString());
                    SelectName = null;
                } else {
                    EditTextSelectPosition = position;
                    SelectName = "mContentView";
                    EditTextSelect = holder.mContentView;
                }
            }
        });

        holder.mRemarkView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    //失去焦点
                    mValues.get(position).setItemRemark(holder.mRemarkView.getText().toString());
                    SelectName = null;
                } else {
                    EditTextSelectPosition = position;
                    SelectName = "mRemarkView";
                    EditTextSelect = holder.mRemarkView;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mTypeView;
        public final TextView mNameView;
        public final EditText mContentView;
        public final Spinner mResultView;
        public final EditText mRemarkView;
        public TaskInspectModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.tv_id);
            mTypeView = (TextView) view.findViewById(R.id.tv_type);
            mNameView = (TextView) view.findViewById(R.id.tv_name);
            mContentView = (EditText) view.findViewById(R.id.et_content);
            mResultView = (Spinner) view.findViewById(R.id.sp_result);
            mRemarkView = (EditText) view.findViewById(R.id.et_remark);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTypeView.getText() + "，" + mNameView.getText() + "'";
        }
    }

    /**
     * 更新未失去焦点的EditText
     */
    protected void updatePositonEditTextValue() {
        if(SelectName != null){
            if(SelectName.equals("mContentView")){
                mValues.get(EditTextSelectPosition).setItemContent(EditTextSelect.getText().toString());
            }else if(SelectName.equals("mRemarkView")){
                mValues.get(EditTextSelectPosition).setItemRemark(EditTextSelect.getText().toString());
            }else{
                return;
            }
        }else
        {
            return;
        }

    }

    public List<TaskInspectModel> getmValues() {
        return mValues;
    }

    public void setmValues(List<TaskInspectModel> mValues) {
        this.mValues = mValues;
    }

    public OnListFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnListFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    public int getEditTextSelectPosition() {
        return EditTextSelectPosition;
    }

    public void setEditTextSelectPosition(int editTextSelectPosition) {
        EditTextSelectPosition = editTextSelectPosition;
    }

    public String getSelectName() {
        return SelectName;
    }

    public void setSelectName(String selectName) {
        SelectName = selectName;
    }

    public EditText getEditTextSelect() {
        return EditTextSelect;
    }

    public void setEditTextSelect(EditText editTextSelect) {
        EditTextSelect = editTextSelect;
    }

}
