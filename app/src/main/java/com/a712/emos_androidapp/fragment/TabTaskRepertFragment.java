package com.a712.emos_androidapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.receiveModel.TaskInspectModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.a712.emos_androidapp.utils.RecyclerViewDivider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_ACCESS_MODIFY;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_TYPE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_TASK_INSPECT;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_UPDATE_TASK_INSPECT;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_TOTAL;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TabTaskRepertFragment extends Fragment {

    // Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Gson gson = new Gson();
    private View view;
    private static String ftId;
    private static String ftType;
    private static boolean accessModify;
    private RecyclerView recyclerView;
    private List<TaskInspectModel> modelListTemp;
    private TaskReportRecyclerViewAdapter mRecyclerViewAdapter;
    // 声明broadcastreceiver
    private IntentFilter mQueryTaskInspectIntentFilter;
    private QueryTaskInspectReceiver mQueryTaskInspectReceiver;
    private IntentFilter mUpdateTaskInspectIntentFilter;
    private UpdateTaskInspectReceiver mUpdateTaskInspectReceiver;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TabTaskRepertFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TabTaskRepertFragment newInstance(int columnCount) {
        TabTaskRepertFragment fragment = new TabTaskRepertFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != view){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        }else {
            view = inflater.inflate(R.layout.fragment_tab_task_report_list, container, false);
            Bundle bundle = this.getActivity().getIntent().getExtras();
            ftId = bundle.getString(BUNDLE_FT_ID);
            ftType = bundle.getString(BUNDLE_FT_TYPE);
            accessModify = bundle.getBoolean(BUNDLE_ACCESS_MODIFY);
            // Set the adapter
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
            }
            recyclerView.addItemDecoration(new RecyclerViewDivider(getContext(), LinearLayoutManager.HORIZONTAL));
            // Set the adapter
            if(modelListTemp == null){
                modelListTemp = new ArrayList<>();
            }else{
                modelListTemp.clear();
            }
            FloatingActionButton btnTaskReportSubmit = (FloatingActionButton)view.findViewById(R.id.btn_task_report_submit);
            btnTaskReportSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(modelListTemp != null && modelListTemp.size()>0){
                        //更新正在选中的数据
                        mRecyclerViewAdapter.updatePositonEditTextValue();
                        Snackbar.make(recyclerView,"已提交巡检数据更新，等待处理结果......",Snackbar.LENGTH_SHORT).show();
                        //提交更新
                        String stringInspect = gson.toJson(modelListTemp);
                        QueryDataIntentService.startUpdateTaskInspect(getContext(),stringInspect);
                    }else{
                        Snackbar.make(recyclerView,"无巡检报表数据！",Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
            if(accessModify){
                btnTaskReportSubmit.setVisibility(View.VISIBLE);
            }else{
                btnTaskReportSubmit.setVisibility(View.GONE);
            }
            mRecyclerViewAdapter = new TaskReportRecyclerViewAdapter(modelListTemp,mListener);
            recyclerView.setAdapter(mRecyclerViewAdapter);
            //初始broadcastreceiver
            mQueryTaskInspectIntentFilter = new IntentFilter(INTENT_QUERY_TASK_INSPECT);
            mQueryTaskInspectReceiver = new QueryTaskInspectReceiver();
            mUpdateTaskInspectIntentFilter = new IntentFilter(INTENT_UPDATE_TASK_INSPECT);
            mUpdateTaskInspectReceiver = new UpdateTaskInspectReceiver();
            //查询任务巡检项
            QueryDataIntentService.startQueryTaskInspect(getContext(),ftId);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mQueryTaskInspectReceiver, mQueryTaskInspectIntentFilter);
        getActivity().registerReceiver(mUpdateTaskInspectReceiver, mUpdateTaskInspectIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mQueryTaskInspectReceiver);
        getActivity().unregisterReceiver(mUpdateTaskInspectReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(TaskInspectModel item);
    }

    /**
     * receiver来接收数据
     */
    public class QueryTaskInspectReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            boolean result = intent.getBooleanExtra(RESULT_FLAG, false);
            String info = intent.getStringExtra(RESULT_INFO);
            int total = intent.getIntExtra(RESULT_TOTAL, 0);
            String data = bundle.getString(RESULT_EXTRA_DATA);
            //如果查到数据验证正确
            if (result) {
                if (total == 0) {
                    Snackbar.make(recyclerView, "无巡检报表数据!",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    modelListTemp = gson.fromJson(data, new TypeToken<List<TaskInspectModel>>() {
                    }.getType());
                    // Set the adapter
                    if(mRecyclerViewAdapter != null){
                        mRecyclerViewAdapter.setmValues(modelListTemp);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }else{
                        mRecyclerViewAdapter = new TaskReportRecyclerViewAdapter(modelListTemp,mListener);
                    }
                }
            } else {//如果失败
                Snackbar.make(recyclerView,info,Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * receiver来接收响应数据
     */
    public class UpdateTaskInspectReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);
            //如果查到数据验证正确
            if (result) {
                Toast.makeText(getContext(),info,Toast.LENGTH_LONG).show();
            } else {//如果失败
                Toast.makeText(getContext(),info,Toast.LENGTH_LONG).show();
            }
        }
    }

}
