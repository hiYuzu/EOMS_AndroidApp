package com.a712.emos_androidapp.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.receiveModel.TaskModel;
import com.a712.emos_androidapp.receiveModel.UserModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_ACCESS_MODIFY;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DESCRIPTION;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DEVICE_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_REMARK;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_HANDLE_TIME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_IS_SOLVE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_OUTLET_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_WORKER_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_TASK_KEY;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_WORKER_LIST;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_UPDATE_TASK_BASE;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_TOTAL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabTaskBaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabTaskBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabTaskBaseFragment extends Fragment {
    //  Rename parameter arguments, choose names that match
    private View view;
    private static String taskId;
    private static boolean accessModify;
    // 声明broadcastreceiver
    private IntentFilter mHandleTaskIntentFilter;
    private HandleTaskReceiver mHandleTaskReceiver;
    private IntentFilter mTaskDetailIntentFilter;
    private TaskDetailReceiver mTaskDetailReceiver;
    // 声明broadcastreceiver
    private IntentFilter mUpdateTaskIntentFilter;
    private UpdateTaskReceiver mUpdateTaskReceiver;
    //存储用户列表
    private ArrayList<UserModel> workerList;
    private RadioButton radioFinished;
    private RadioButton radioUnfinished;
    private Spinner spinnerWorker;
    private Button btnTaskHandleDate;
    private Button btnTaskHandleTime;
    private TextView tvTaskHandleDate;
    private TextView tvTaskHandleTime;
    private AppCompatEditText editTaskDescription;
    private AppCompatEditText editTaskRemark;
    //存储要提交的数据
    private TaskModel taskModel;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TabTaskBaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabTaskBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabTaskBaseFragment newInstance(String param1, String param2) {
        TabTaskBaseFragment fragment = new TabTaskBaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_tab_task_base, container, false);
            Bundle bundle = getActivity().getIntent().getExtras();
            taskModel = new TaskModel();
            taskModel.setOutletDeviceId(bundle.getInt(BUNDLE_DEVICE_ID));
            taskModel.setOutletCode(bundle.getString(BUNDLE_OUTLET_CODE));
            taskId = bundle.getString(BUNDLE_FT_ID);
            accessModify = bundle.getBoolean(BUNDLE_ACCESS_MODIFY);
            initView(view);
            //broadcaster接收过滤器
            mHandleTaskIntentFilter = new IntentFilter(INTENT_QUERY_WORKER_LIST);
            mHandleTaskReceiver = new HandleTaskReceiver();
            mTaskDetailIntentFilter = new IntentFilter(INTENT_QUERY_TASK_KEY);
            mTaskDetailReceiver = new TaskDetailReceiver();
            mUpdateTaskIntentFilter = new IntentFilter(INTENT_UPDATE_TASK_BASE);
            mUpdateTaskReceiver = new UpdateTaskReceiver();
            //查询运维人员
            QueryDataIntentService.startQueryWorkList(getContext(), taskModel.getOutletCode(), 0);
        }
        return view;
    }

    private void initView(View view) {
        radioFinished = (RadioButton) view.findViewById(R.id.radio_finished);
        radioUnfinished = (RadioButton) view.findViewById(R.id.radio_unfinished);
        spinnerWorker = (Spinner) view.findViewById(R.id.spinner_handle_task_worker);
        btnTaskHandleDate = (Button) view.findViewById(R.id.btn_task_handle_date);
        btnTaskHandleTime = (Button) view.findViewById(R.id.btn_task_handle_time);
        tvTaskHandleDate = (TextView) view.findViewById(R.id.tv_task_handle_date);
        tvTaskHandleTime = (TextView) view.findViewById(R.id.tv_task_handle_time);
        editTaskDescription = (AppCompatEditText) view.findViewById(R.id.edit_task_description);
        editTaskRemark = (AppCompatEditText) view.findViewById(R.id.edit_task_remark);
        FloatingActionButton btnTaskBaseSubmit = (FloatingActionButton) view.findViewById(R.id.btn_task_base_submit);

        btnTaskHandleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(tvTaskHandleDate);
            }
        });
        btnTaskHandleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(tvTaskHandleTime);
            }
        });

        TextView tvTaskHandleClear = (TextView) view.findViewById(R.id.tv_task_handle_clear);
        tvTaskHandleClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTaskHandleDate.setText("");
                tvTaskHandleTime.setText("");
            }
        });
        btnTaskBaseSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getView(), "已提交基本信息更新，等待处理结果......", Snackbar.LENGTH_SHORT).show();
                setTaskBaseSubmit();
            }
        });
        if(accessModify){
            btnTaskBaseSubmit.setVisibility(View.VISIBLE);
        }else{
            btnTaskBaseSubmit.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mHandleTaskReceiver, mHandleTaskIntentFilter);
        getActivity().registerReceiver(mTaskDetailReceiver, mTaskDetailIntentFilter);
        getActivity().registerReceiver(mUpdateTaskReceiver, mUpdateTaskIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mHandleTaskReceiver);
        getActivity().unregisterReceiver(mTaskDetailReceiver);
        getActivity().unregisterReceiver(mUpdateTaskReceiver);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
     * 提交任务基本数据
     */
    private void setTaskBaseSubmit() {

        if (radioFinished.isChecked()) {
            taskModel.setIsSolve("1");
        } else {
            taskModel.setIsSolve("0");
        }
        String workerCode = workerList.get(spinnerWorker.getSelectedItemPosition()).getUserCode();
        taskModel.setWorkCode(workerCode);
        String handleTaskDateTime = "";
        String handleTaskDate = tvTaskHandleDate.getText().toString();
        String handleTaskTime = tvTaskHandleTime.getText().toString();
        if (handleTaskDate != null && !handleTaskDate.isEmpty() && handleTaskTime != null && !handleTaskTime.isEmpty()) {
            handleTaskDateTime = handleTaskDate + " " + handleTaskTime;
        } else if ((handleTaskDate == null || handleTaskDate.isEmpty()) && (handleTaskTime == null || handleTaskTime.isEmpty())) {
        } else {
            Toast.makeText(getActivity(), "请选择处理日期和时间！", Toast.LENGTH_LONG);
            return;
        }
        taskModel.setTaskHandleTime(handleTaskDateTime);
        if (handleTaskDateTime.isEmpty() && radioFinished.isChecked()) {
            Toast.makeText(getActivity(), "已完成任务必须填写处理日期和时间！", Toast.LENGTH_LONG).show();
            return;
        }
        String taskDescription = editTaskDescription.getText().toString();
        String taskRemark = editTaskRemark.getText().toString();
        //在存入bundle时要设置utf-8编码
        try {
            taskDescription = URLEncoder.encode(taskDescription, "utf-8");
            taskRemark = URLEncoder.encode(taskRemark, "utf-8");
            taskModel.setTaskDescription(taskDescription);
            taskModel.setTaskRemark(taskRemark);
        } catch (UnsupportedEncodingException e) {
            Log.e("任务基本信息转义错误：", e.getMessage());
        }
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_FT_ID, taskId);
        bundle.putInt(BUNDLE_DEVICE_ID, taskModel.getOutletDeviceId());
        bundle.putString(BUNDLE_IS_SOLVE, taskModel.getIsSolve());
        bundle.putString(BUNDLE_WORKER_CODE, taskModel.getWorkCode());
        bundle.putString(BUNDLE_HANDLE_TIME, taskModel.getTaskHandleTime());
        bundle.putString(BUNDLE_DESCRIPTION, taskDescription);
        bundle.putString(BUNDLE_FT_REMARK, taskRemark);

        QueryDataIntentService.startUpdateTask(getContext(), bundle);
    }

    /**
     * receiver来接收数据-运维人员
     */
    public class HandleTaskReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);
            int total = bundle.getInt(RESULT_TOTAL, 0);
            String data = bundle.getString(RESULT_EXTRA_DATA);
            //如果查到数据验证正确
            if (result) {
                if (total > 0) {
                    ArrayList<UserModel> modelListTemp = gson.fromJson(data, new TypeToken<List<UserModel>>() {
                    }.getType());
                    if (modelListTemp.get(0).getUserFlag() == 0) {//返回的是运维负责人
                        workerList = modelListTemp;
                        ArrayAdapter<UserModel> adapterManager = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, workerList);
                        spinnerWorker.setAdapter(adapterManager);
                    }
                }
                //查询故障信息
                QueryDataIntentService.startQueryTaskKey(getContext(), taskId);
            } else {
                //失败
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * receiver来接收数据-任务明细
     */
    public class TaskDetailReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);
            int total = bundle.getInt(RESULT_TOTAL, 0);
            String data = bundle.getString(RESULT_EXTRA_DATA);
            //如果查到数据验证正确
            if (result) {
                if (total > 0) {

                    ArrayList<TaskModel> modelListTemp = gson.fromJson(data, new TypeToken<List<TaskModel>>() {
                    }.getType());
                    if (modelListTemp != null && modelListTemp.size() > 0) {//返回的是运维负责人
                        //复制画面控件值
                        TaskModel taskModel = modelListTemp.get(0);
                        if (taskModel.getIsSolve().equals("1")) {
                            radioFinished.setChecked(true);
                            radioUnfinished.setChecked(false);
                        } else {
                            radioFinished.setChecked(false);
                            radioUnfinished.setChecked(true);
                        }
                        for (int i = 0; i < spinnerWorker.getCount(); i++) {
                            if (spinnerWorker.getItemAtPosition(i).toString().equals(taskModel.getWorkName())) {
                                spinnerWorker.setSelection(i);
                                break;
                            } else {
                                continue;
                            }
                        }
                        String taskHandleTime = taskModel.getTaskHandleTime();
                        if (taskHandleTime != null && !taskHandleTime.isEmpty() && taskHandleTime.indexOf(" ") > 0) {
                            tvTaskHandleDate.setText(taskHandleTime.substring(0, taskHandleTime.indexOf(" ")));
                            tvTaskHandleTime.setText(taskHandleTime.substring(taskHandleTime.indexOf(" "), taskHandleTime.length()));
                        }
                        editTaskDescription.setText(taskModel.getTaskDescription());
                        editTaskRemark.setText(taskModel.getTaskRemark());
                    }
                }
            } else {
                //失败
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * receiver来接收数据--更新任务基本信息
     */
    public class UpdateTaskReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);
            //如果查到数据验证正确
            if (result) {
                Toast.makeText(getActivity(), info, Toast.LENGTH_LONG).show();
            } else {//如果失败
                Toast.makeText(getActivity(), info, Toast.LENGTH_LONG).show();
            }
        }
    }

}
