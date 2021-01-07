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
import com.a712.emos_androidapp.receiveModel.FaultModel;
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
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_DEAL;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_REASON;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_HANDLE_TIME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_IS_SOLVE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_OUTLET_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_RECORD_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_RECORD_TIME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_WORKER_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_WORKER_LIST;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_UPDATE_FAULT_BASE;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_TOTAL;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_FAULT_KEY;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_REMARK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabFaultBaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabFaultBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFaultBaseFragment extends Fragment {
    //  parameter arguments, choose names that match
    private View view;
    private static String faultId;
    private static boolean accessModify;
    // 声明broadcastreceiver
    private IntentFilter mHandleFaultIntentFilter;
    private HandleFaultReceiver mHandleFaultReceiver;
    private IntentFilter mFaultDetailIntentFilter;
    private FaultDetailReceiver mFaultDetailReceiver;
    // 声明broadcastreceiver
    private IntentFilter mUpdateFaultIntentFilter;
    private UpdateFaultReceiver mUpdateFaultReceiver;
    //存储用户列表
    private ArrayList<UserModel> workerList;
    private RadioButton radioFinished;
    private RadioButton radioUnfinished;
    private Spinner spinnerWorker;
    private Spinner spinnerRecord;
    private Button btnFaultHandleDate;
    private TextView tvFaultHandleDate;
    private Button btnFaultHandleTime;
    private TextView tvFaultHandleTime;
    private Button btnFaultRecordDate;
    private TextView tvFaultRecordDate;
    private Button btnFaultRecordTime;
    private TextView tvFaultRecordTime;
    private AppCompatEditText editFaultReason;
    private AppCompatEditText editFaultDeal;
    private AppCompatEditText editFaultDescription;
    private AppCompatEditText editFaultRemark;
    //存储要提交的数据
    private FaultModel faultModel;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TabFaultBaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFaultBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFaultBaseFragment newInstance(String param1, String param2) {
        TabFaultBaseFragment fragment = new TabFaultBaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_tab_fault_base, container, false);
            Bundle bundle = getActivity().getIntent().getExtras();
            faultModel = new FaultModel();
            faultModel.setOutletDeviceId(bundle.getInt(BUNDLE_DEVICE_ID));
            faultModel.setOutletCode(bundle.getString(BUNDLE_OUTLET_CODE));
            faultId = bundle.getString(BUNDLE_FT_ID);
            accessModify = bundle.getBoolean(BUNDLE_ACCESS_MODIFY);
            initView(view);
            //broadcaster接收过滤器
            mHandleFaultIntentFilter = new IntentFilter(INTENT_QUERY_WORKER_LIST);
            mHandleFaultReceiver = new HandleFaultReceiver();
            mFaultDetailIntentFilter = new IntentFilter(INTENT_QUERY_FAULT_KEY);
            mFaultDetailReceiver = new FaultDetailReceiver();
            mUpdateFaultIntentFilter = new IntentFilter(INTENT_UPDATE_FAULT_BASE);
            mUpdateFaultReceiver = new UpdateFaultReceiver();
            //查询运维人员
            QueryDataIntentService.startQueryWorkList(getContext(), faultModel.getOutletCode(), 0);
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initView(View view) {
        radioFinished = (RadioButton) view.findViewById(R.id.radio_finished);
        radioUnfinished = (RadioButton) view.findViewById(R.id.radio_unfinished);
        spinnerWorker = (Spinner) view.findViewById(R.id.spinner_handle_fault_worker);
        spinnerRecord = (Spinner) view.findViewById(R.id.spinner_fault_record);
        btnFaultHandleDate = (Button) view.findViewById(R.id.btn_fault_handle_date);
        tvFaultHandleDate = (TextView) view.findViewById(R.id.tv_fault_handle_date);
        btnFaultHandleTime = (Button) view.findViewById(R.id.btn_fault_handle_time);
        tvFaultHandleTime = (TextView) view.findViewById(R.id.tv_fault_handle_time);
        btnFaultRecordDate = (Button) view.findViewById(R.id.btn_fault_record_date);
        tvFaultRecordDate = (TextView) view.findViewById(R.id.tv_fault_record_date);
        btnFaultRecordTime = (Button) view.findViewById(R.id.btn_fault_record_time);
        tvFaultRecordTime = (TextView) view.findViewById(R.id.tv_fault_record_time);
        editFaultReason = (AppCompatEditText) view.findViewById(R.id.edit_fault_reason);
        editFaultDeal = (AppCompatEditText) view.findViewById(R.id.edit_fault_deal);
        editFaultDescription = (AppCompatEditText) view.findViewById(R.id.edit_fault_description);
        editFaultRemark = (AppCompatEditText) view.findViewById(R.id.edit_fault_remark);
        FloatingActionButton btnFaultBaseSubmit = (FloatingActionButton) view.findViewById(R.id.btn_fault_base_submit);

        btnFaultHandleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(tvFaultHandleDate);
            }
        });
        btnFaultHandleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(tvFaultHandleTime);
            }
        });
        btnFaultRecordDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(tvFaultRecordDate);
            }
        });
        btnFaultRecordTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(tvFaultRecordTime);
            }
        });

        TextView tvFaultHandleClear = (TextView) view.findViewById(R.id.tv_fault_handle_clear);
        TextView tvFaultRecordClear = (TextView) view.findViewById(R.id.tv_fault_record_clear);
        tvFaultHandleClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFaultHandleDate.setText("");
                tvFaultHandleTime.setText("");
            }
        });
        tvFaultRecordClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFaultRecordDate.setText("");
                tvFaultRecordTime.setText("");
            }
        });
        btnFaultBaseSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getView(), "已提交基本信息更新，等待处理结果......", Snackbar.LENGTH_SHORT).show();
                setFaultBaseSubmit();
            }
        });
        if(accessModify){
            btnFaultBaseSubmit.setVisibility(View.VISIBLE);
        }else{
            btnFaultBaseSubmit.setVisibility(View.GONE);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mHandleFaultReceiver, mHandleFaultIntentFilter);
        getActivity().registerReceiver(mFaultDetailReceiver, mFaultDetailIntentFilter);
        getActivity().registerReceiver(mUpdateFaultReceiver, mUpdateFaultIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mHandleFaultReceiver);
        getActivity().unregisterReceiver(mFaultDetailReceiver);
        getActivity().unregisterReceiver(mUpdateFaultReceiver);
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
     * 提交故障基本数据
     */
    private void setFaultBaseSubmit() {

        if (radioFinished.isChecked()) {
            faultModel.setIsSolve("1");
        } else {
            faultModel.setIsSolve("0");
        }
        String workerCode = workerList.get(spinnerWorker.getSelectedItemPosition()).getUserCode();
        faultModel.setWorkCode(workerCode);
        String handleFaultDateTime = "";
        String handleFaultDate = tvFaultHandleDate.getText().toString();
        String handleFaultTime = tvFaultHandleTime.getText().toString();
        if (handleFaultDate != null && !handleFaultDate.isEmpty() && handleFaultTime != null && !handleFaultTime.isEmpty()) {
            handleFaultDateTime = handleFaultDate + " " + handleFaultTime;
        } else if ((handleFaultDate == null || handleFaultDate.isEmpty()) && (handleFaultTime == null || handleFaultTime.isEmpty())) {
        } else {
            Toast.makeText(getActivity(), "请选择处理日期和时间！", Toast.LENGTH_LONG).show();
            return;
        }
        faultModel.setFaultHandleTime(handleFaultDateTime);
        if (handleFaultDateTime.isEmpty() && radioFinished.isChecked()) {
            Toast.makeText(getActivity(), "已完成故障必须填写处理日期和时间！", Toast.LENGTH_LONG).show();
            return;
        }
        int recordPosition = spinnerRecord.getSelectedItemPosition();
        faultModel.setRecordFlag(String.valueOf(recordPosition));
        String recordFaultDateTime = "";
        String recordFaultDate = tvFaultRecordDate.getText().toString();
        String recordFaultTime = tvFaultRecordTime.getText().toString();
        if (recordFaultDate != null && !recordFaultDate.isEmpty() && recordFaultTime != null && !recordFaultTime.isEmpty()) {
            recordFaultDateTime = recordFaultDate + " " + recordFaultTime;
        } else if ((recordFaultDate == null || recordFaultDate.isEmpty()) && (recordFaultTime == null || recordFaultTime.isEmpty())) {
        } else {
            Toast.makeText(getActivity(), "请选择报备日期和时间！", Toast.LENGTH_LONG).show();
            return;
        }
        faultModel.setRecordTime(recordFaultDateTime);
        if (recordFaultDateTime.isEmpty() && radioFinished.isChecked()) {
            Toast.makeText(getActivity(), "已完成故障必须填写处理日期和时间！", Toast.LENGTH_LONG).show();
            return;
        }
        String faultReason = editFaultReason.getText().toString();
        String faultDeal = editFaultDeal.getText().toString();
        String faultDescription = editFaultDescription.getText().toString();
        String faultRemark = editFaultRemark.getText().toString();
        //在存入bundle时要设置utf-8编码
        try {
            faultReason = URLEncoder.encode(faultReason, "utf-8");
            faultDeal = URLEncoder.encode(faultDeal, "utf-8");
            faultDescription = URLEncoder.encode(faultDescription, "utf-8");
            faultRemark = URLEncoder.encode(faultRemark, "utf-8");
            faultModel.setFaultReason(faultReason);
            faultModel.setFaultDeal(faultDeal);
            faultModel.setFaultDescription(faultDescription);
            faultModel.setFaultRemark(faultRemark);
        } catch (UnsupportedEncodingException e) {
            Log.e("故障基本信息转义错误：", e.getMessage());
        }
        // Log.d("提交信息：", outletDeviceId + "-"+ faultTypeCode + "-"+ workerCode+ "-"+ managerCode + "-"+ faultStartTime+ "-"+ faultEndTime+ "-"+ faultDescription);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_FT_ID, faultId);
        bundle.putInt(BUNDLE_DEVICE_ID, faultModel.getOutletDeviceId());
        bundle.putString(BUNDLE_IS_SOLVE, faultModel.getIsSolve());
        bundle.putString(BUNDLE_WORKER_CODE, faultModel.getWorkCode());
        bundle.putString(BUNDLE_HANDLE_TIME, faultModel.getFaultHandleTime());
        bundle.putString(BUNDLE_RECORD_FLAG, faultModel.getRecordFlag());
        bundle.putString(BUNDLE_RECORD_TIME, faultModel.getRecordTime());
        bundle.putString(BUNDLE_FT_REASON, faultModel.getFaultReason());
        bundle.putString(BUNDLE_FT_DEAL, faultModel.getFaultDeal());
        bundle.putString(BUNDLE_DESCRIPTION, faultDescription);
        bundle.putString(BUNDLE_FT_REMARK, faultRemark);

        QueryDataIntentService.startUpdateFault(getContext(), bundle);
    }

    /**
     * receiver来接收数据-运维人员
     */
    public class HandleFaultReceiver extends BroadcastReceiver {
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
                QueryDataIntentService.startQueryFaultKey(getContext(), faultId);
            } else {
                //失败
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * receiver来接收数据-故障明细
     */
    public class FaultDetailReceiver extends BroadcastReceiver {
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

                    ArrayList<FaultModel> modelListTemp = gson.fromJson(data, new TypeToken<List<FaultModel>>() {
                    }.getType());
                    if (modelListTemp != null && modelListTemp.size() > 0) {//返回的是运维负责人
                        //复制画面控件值
                        FaultModel faultModel = modelListTemp.get(0);
                        if (faultModel.getIsSolve().equals("1")) {
                            radioFinished.setChecked(true);
                            radioUnfinished.setChecked(false);
                        } else {
                            radioFinished.setChecked(false);
                            radioUnfinished.setChecked(true);
                        }
                        for (int i = 0; i < spinnerWorker.getCount(); i++) {
                            if (spinnerWorker.getItemAtPosition(i).toString().equals(faultModel.getWorkName())) {
                                spinnerWorker.setSelection(i);
                                break;
                            } else {
                                continue;
                            }
                        }
                        spinnerRecord.setSelection(Integer.valueOf(faultModel.getRecordFlag()));
                        String faultHandleTime = faultModel.getFaultHandleTime();
                        if (faultHandleTime != null && !faultHandleTime.isEmpty() && faultHandleTime.indexOf(" ") > 0) {
                            tvFaultHandleDate.setText(faultHandleTime.substring(0, faultHandleTime.indexOf(" ")));
                            tvFaultHandleTime.setText(faultHandleTime.substring(faultHandleTime.indexOf(" "), faultHandleTime.length()));
                        }
                        String faultRecordTime = faultModel.getRecordTime();
                        if (faultRecordTime != null && !faultRecordTime.isEmpty() && faultRecordTime.indexOf(" ") > 0) {
                            tvFaultRecordDate.setText(faultRecordTime.substring(0, faultRecordTime.indexOf(" ")));
                            tvFaultRecordTime.setText(faultRecordTime.substring(faultRecordTime.indexOf(" "), faultRecordTime.length()));
                        }
                        editFaultReason.setText(faultModel.getFaultReason());
                        editFaultDeal.setText(faultModel.getFaultDeal());
                        editFaultDescription.setText(faultModel.getFaultDescription());
                        editFaultRemark.setText(faultModel.getFaultRemark());
                    }
                }
            } else {
                //失败
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * receiver来接收数据--更新故障基本信息
     */
    public class UpdateFaultReceiver extends BroadcastReceiver {
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
