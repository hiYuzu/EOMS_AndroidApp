package com.a712.emos_androidapp.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.receiveModel.DeviceCheckedModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.a712.emos_androidapp.utils.FunctionUtil;
import com.a712.emos_androidapp.utils.RecyclerViewDivider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_ACCESS_MODIFY;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_TYPE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_FAULT_DEVICE_CHECKED;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_TASK_DEVICE_CHECKED;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_UPDATE_FAULT_DEVICE_CHECKED;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_UPDATE_TASK_DEVICE_CHECKED;
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
public class TabPartDeviceFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private View view;
    private static String ftId;
    private static String ftType;
    private static boolean accessModify;
    private RecyclerView recyclerView;
    private List<DeviceCheckedModel> modelListTemp;
    private DeviceItemRecyclerViewAdapter mRecyclerViewAdapter;
    // 声明broadcastreceiver
    private IntentFilter mDeviceCheckedIntentFilter;
    private DeviceCheckedReceiver mDeviceCheckedReceiver;
    private IntentFilter mUpdateDeviceIntentFilter;
    private UpdateDeviceReceiver mUpdateDeviceReceiver;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TabPartDeviceFragment() {
    }

    // Customize parameter initialization
    @SuppressWarnings("unused")
    public static TabPartDeviceFragment newInstance(int columnCount) {
        TabPartDeviceFragment fragment = new TabPartDeviceFragment();
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
            view = inflater.inflate(R.layout.fragment_tab_part_device_list, container, false);
            Bundle bundle = this.getActivity().getIntent().getExtras();
            ftId = bundle.getString(BUNDLE_FT_ID);
            ftType = bundle.getString(BUNDLE_FT_TYPE);
            accessModify = bundle.getBoolean(BUNDLE_ACCESS_MODIFY);
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
            mRecyclerViewAdapter = new DeviceItemRecyclerViewAdapter(modelListTemp,mListener);
            recyclerView.setAdapter(mRecyclerViewAdapter);

            FloatingActionButton btnFaultDeviceSubmit = (FloatingActionButton)view.findViewById(R.id.btn_fault_device_submit);
            btnFaultDeviceSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //提交更新时间确认
                    showSelectDeviceTimeDialog();
                }
            });
            if(accessModify){
                btnFaultDeviceSubmit.setVisibility(View.VISIBLE);
            }else{
                btnFaultDeviceSubmit.setVisibility(View.GONE);
            }
            //broadcaster接收过滤器
            String intentDeviceCheckedFileter = "";
            String intentUpdateDeviceFileter = "";
            if(FunctionUtil.isFault(ftType)) {
                intentDeviceCheckedFileter = INTENT_QUERY_FAULT_DEVICE_CHECKED;
                intentUpdateDeviceFileter = INTENT_UPDATE_FAULT_DEVICE_CHECKED;
            }else{
                intentDeviceCheckedFileter = INTENT_QUERY_TASK_DEVICE_CHECKED;
                intentUpdateDeviceFileter = INTENT_UPDATE_TASK_DEVICE_CHECKED;
            }
            mDeviceCheckedIntentFilter = new IntentFilter(intentDeviceCheckedFileter);
            mDeviceCheckedReceiver = new DeviceCheckedReceiver();
            mUpdateDeviceIntentFilter = new IntentFilter(intentUpdateDeviceFileter);
            mUpdateDeviceReceiver = new UpdateDeviceReceiver();
            //查询更换部件
            if(FunctionUtil.isFault(ftType)) {
                QueryDataIntentService.startQueryFaultDeviceChecked(getContext(), ftId);
            }else{
                QueryDataIntentService.startQueryTaskDeviceChecked(getContext(), ftId);
            }
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mDeviceCheckedReceiver, mDeviceCheckedIntentFilter);
        getActivity().registerReceiver(mUpdateDeviceReceiver, mUpdateDeviceIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mDeviceCheckedReceiver);
        getActivity().unregisterReceiver(mUpdateDeviceReceiver);
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
        void onListFragmentInteraction(DeviceCheckedModel itemList);
    }

    /**
     * 弹出查询设置弹出框
     */
    private void showSelectDeviceTimeDialog() {
        LinearLayout linearLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_select_device_time, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_device_time);
        //list of items
        builder.setView(linearLayout);
        final Button btnSelectDeviceDate = (Button) linearLayout.findViewById(R.id.btn_select_device_date);
        final Button btnSelectDeviceTime = (Button) linearLayout.findViewById(R.id.btn_select_device_time);
        final TextView tvSelectDeviceDate = (TextView) linearLayout.findViewById(R.id.tv_select_device_date);
        final TextView tvSelectDeviceTime = (TextView) linearLayout.findViewById(R.id.tv_select_device_time);
        //设置默认时间
        SimpleDateFormat dDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dDateFormat.format(new java.util.Date());
        SimpleDateFormat tDateFormat = new SimpleDateFormat("HH:mm:00");
        String time = tDateFormat.format(new java.util.Date());
        tvSelectDeviceDate.setText(date);
        tvSelectDeviceTime.setText(time);
        btnSelectDeviceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(tvSelectDeviceDate);
            }
        });
        btnSelectDeviceTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(tvSelectDeviceTime);
            }
        });
        //确定按钮
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //提交更新
                        List<String> listId = mRecyclerViewAdapter.getSelectId();
                        String[] list = listId.toArray(new String[listId.size()]);

                        String replaceTime = "";
                        String selectDeviceDate = tvSelectDeviceDate.getText().toString();
                        String selectDeviceTime = tvSelectDeviceTime.getText().toString();
                        if(selectDeviceDate != null && !selectDeviceDate.isEmpty()
                                && selectDeviceTime != null && !selectDeviceTime.isEmpty()){
                            replaceTime = selectDeviceDate+" "+selectDeviceTime;
                            if(FunctionUtil.isFault(ftType)){
                                QueryDataIntentService.startUpdateFaultDeviceChecked(getContext(),ftId,list,replaceTime);
                            }else{
                                QueryDataIntentService.startUpdateTaskDeviceChecked(getContext(),ftId,list,replaceTime);
                            }
                            Snackbar.make(recyclerView,"已提交部件更新，等待处理结果......",Snackbar.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"请选择处理日期和时间！",Toast.LENGTH_LONG).show();
                        }
                    }
                });
        //取消按钮
        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    /**
     * 显示日期选择器
     */
    private void showDate(final TextView textView) {
        //时间按钮
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            //两个要对应
                            String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            Date date = formatter.parse(dateInString);
                            textView.setText(formatter.format(date).toString());
                        } catch (Exception ex) {

                        }

                    }
                }, year, month, day);
        dd.show();
    }

    /**
     * 显示时间选择器
     */
    private void showTime(final TextView textView) {
        //时间按钮
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        TimePickerDialog td = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        try {
                            String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            java.sql.Time timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                            String timeInString = String.valueOf(timeValue);
                            textView.setText(timeInString);
                        } catch (Exception ex) {

                        }
                    }
                }, hour, min, true);
        td.show();
    }

    /**
     * receiver来接收数据
     */
    public class DeviceCheckedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = intent.getBooleanExtra(RESULT_FLAG, false);
            String info = intent.getStringExtra(RESULT_INFO);
            int total = intent.getIntExtra(RESULT_TOTAL, 0);
            String data = bundle.getString(RESULT_EXTRA_DATA);
            //如果查到数据验证正确
            if (result) {
                if (total == 0) {
                    Snackbar.make(recyclerView, "无更换部件数据!",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    modelListTemp = gson.fromJson(data, new TypeToken<List<DeviceCheckedModel>>() {
                    }.getType());
                    // Set the adapter
                    if(mRecyclerViewAdapter != null){
                        mRecyclerViewAdapter.setmValues(modelListTemp);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }else{
                        mRecyclerViewAdapter = new DeviceItemRecyclerViewAdapter(modelListTemp,mListener);
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
    public class UpdateDeviceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);
            //如果查到数据验证正确
            if (result) {
                //查询更换部件
                if(FunctionUtil.isFault(ftType)) {
                    QueryDataIntentService.startQueryFaultDeviceChecked(getContext(), ftId);
                }else{
                    QueryDataIntentService.startQueryTaskDeviceChecked(getContext(), ftId);
                }
                Toast.makeText(getContext(),info,Toast.LENGTH_LONG).show();
            } else {//如果失败
                Toast.makeText(getContext(),info,Toast.LENGTH_LONG).show();
            }
        }
    }

}
