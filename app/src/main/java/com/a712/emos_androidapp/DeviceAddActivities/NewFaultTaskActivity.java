package com.a712.emos_androidapp.DeviceAddActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.a712.emos_androidapp.MyEomsActivity;
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

import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_COMPANY_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_COMPANY_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DESCRIPTION;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DEVICE_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DEVICE_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_END_TIME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_REMARK;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_TYPE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_TYPENAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_MANAGE_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_OUTLET_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_OUTLET_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_RECORD_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_RECORD_TIME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_START_TIME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_WORKER_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_ADD_FAULT;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_WORKER_LIST;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_TOTAL;

/**
 * Created by Administrator on 2017/4/10.
 */

public class NewFaultTaskActivity extends MyEomsActivity {

    private String userCode;
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    //声明控件
    private Spinner spinnerType;
    private Spinner spinnerManager;
    private Spinner spinnerWorker;
    private Spinner spinnerRecord;
    private Button buttonFaultBeginDate;
    private Button buttonFaultBeginTime;
    private Button buttonFaultEndDate;
    private Button buttonFaultEndTime;
    private Button buttonSubmit;
    private TextView textViewFaultBeginDate;
    private TextView textViewFaultBeginTime;
    private TextView textViewFaultEndDate;
    private TextView textViewFaultEndTime;
    private Button btnFaultRecordDate;
    private TextView tvFaultRecordDate;
    private Button btnFaultRecordTime;
    private TextView tvFaultRecordTime;
    private AppCompatEditText editTextDescription;
    private AppCompatEditText editFaultRemark;
    //获取提交的数据
    private String[] strings = new String[6];
    //存储用户列表
    private ArrayList<UserModel> workerList;
    private ArrayList<UserModel> managerList;

    //时间计时器
    Calendar c;
    int year, month, day, hour, min;
    java.sql.Time timeValue;
    SimpleDateFormat format;

    // 声明broadcastreceiver
    private IntentFilter mAddTaskIntentFilter;
    private AddFaultReceiver mAddFaultReceiver;

    private IntentFilter mTaskBackIntentFilter;
    private AddFaultGetInfoReceiver mFaultBackReceiver;
    //存储要提交的数据
    private FaultModel faultModel;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fault);

        //获取登录用户
        Resources resources = getResources();
        SharedPreferences pref = getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
        userCode = pref.getString(resources.getString(R.string.preference_username), null);

        faultModel = new FaultModel();
        Bundle bundle = this.getIntent().getExtras();
        faultModel.setOutletDeviceId(bundle.getInt(BUNDLE_DEVICE_ID));
        faultModel.setDeviceName(bundle.getString(BUNDLE_DEVICE_NAME));
        faultModel.setCompanyCode(bundle.getString(BUNDLE_COMPANY_CODE));
        faultModel.setCompanyName(bundle.getString(BUNDLE_COMPANY_NAME));
        faultModel.setOutletCode(bundle.getString(BUNDLE_OUTLET_CODE));
        faultModel.setOutletName(bundle.getString(BUNDLE_OUTLET_NAME));

        //Toast.makeText(this,Integer.toString(outletDeviceId),Toast.LENGTH_SHORT).show();
        //tool bar设置
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_fault);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上一个界面并关闭此界面
                onBackPressed();
            }
        });

        initView();
        //broadcaster接收过滤器
        mAddTaskIntentFilter = new IntentFilter(INTENT_QUERY_WORKER_LIST);
        mAddFaultReceiver = new AddFaultReceiver();
        mTaskBackIntentFilter = new IntentFilter(INTENT_ADD_FAULT);
        mFaultBackReceiver = new AddFaultGetInfoReceiver();
        //给spinner赋值
        String[] mItems = getResources().getStringArray(R.array.fault_type);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mItems);
        //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        //初始化时间
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        //时间按钮点击事件
        buttonFaultBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(buttonFaultBeginDate);
            }
        });
        buttonFaultEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(buttonFaultEndDate);
            }
        });
        btnFaultRecordDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(btnFaultRecordDate);
            }
        });
        buttonFaultBeginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(buttonFaultBeginTime);
            }
        });
        buttonFaultEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(buttonFaultEndTime);
            }
        });
        btnFaultRecordTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(btnFaultRecordTime);
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    setButtonSubmit();
                    Snackbar.make(v, "已提交新增故障，请等待处理结果......", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        //初始化list
        workerList = new ArrayList<>();
        managerList = new ArrayList<>();
        //查询人员和负责人下拉框
        QueryDataIntentService.startQueryWorkList(NewFaultTaskActivity.this, faultModel.getOutletCode(), 0);
        QueryDataIntentService.startQueryWorkList(NewFaultTaskActivity.this, faultModel.getOutletCode(), 1);

    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(mAddFaultReceiver, mAddTaskIntentFilter);
        registerReceiver(mFaultBackReceiver, mTaskBackIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(mAddFaultReceiver);
        unregisterReceiver(mFaultBackReceiver);
    }

    /**
     * 提交数据
     */
    private void setButtonSubmit() {

        if (spinnerType.getSelectedItemPosition() == 0) {
            faultModel.setFaultType("501");
        } else {
            faultModel.setFaultType("502");
        }
        faultModel.setFaultTypeName(spinnerType.getSelectedItem().toString());
        String workerCode = workerList.get(spinnerWorker.getSelectedItemPosition()).getUserCode();
        faultModel.setWorkCode(workerCode);
        String managerCode = managerList.get(spinnerManager.getSelectedItemPosition()).getUserCode();
        faultModel.setManageCode(managerCode);
        String faultStartTime = textViewFaultBeginDate.getText() + " " + textViewFaultBeginTime.getText();
        faultModel.setFaultBeginTime(faultStartTime);
        String faultEndTime = textViewFaultEndDate.getText() + " " + textViewFaultEndTime.getText();
        faultModel.setFaultEndTime(faultEndTime);
        int recordPosition = spinnerRecord.getSelectedItemPosition();
        faultModel.setRecordFlag(String.valueOf(recordPosition));
        String recordFaultDateTime = "";
        String recordFaultDate = tvFaultRecordDate.getText().toString();
        String recordFaultTime = tvFaultRecordTime.getText().toString();
        if (recordFaultDate != null && !recordFaultDate.isEmpty() && recordFaultTime != null && !recordFaultTime.isEmpty()) {
            recordFaultDateTime = recordFaultDate + " " + recordFaultTime;
        } else if ((recordFaultDate == null || recordFaultDate.isEmpty()) && (recordFaultTime == null || recordFaultTime.isEmpty())) {
        } else {
            Toast.makeText(this, "请选择报备日期和时间！", Toast.LENGTH_LONG).show();
            return;
        }
        faultModel.setRecordTime(recordFaultDateTime);
//        String faultDescription = editTextDescription.getText() + "";
        String deviceName = faultModel.getDeviceName();
        String companyName = faultModel.getCompanyName();
        String outletName = faultModel.getOutletName();
        String faultTypeName = faultModel.getFaultTypeName();
        String faultDescription = editTextDescription.getText().toString();
        String faultRemark = editFaultRemark.getText().toString();
        //在存入bundle时要设置utf-8编码
        try {
            deviceName = URLEncoder.encode(deviceName, "utf-8");
            companyName = URLEncoder.encode(companyName, "utf-8");
            outletName = URLEncoder.encode(outletName, "utf-8");
            faultTypeName = URLEncoder.encode(faultTypeName, "utf-8");
            faultDescription = URLEncoder.encode(faultDescription, "utf-8");
            faultRemark = URLEncoder.encode(faultRemark, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("新增故障转义错误：", e.getMessage());
        }
        // Log.d("提交信息：", outletDeviceId + "-"+ faultTypeCode + "-"+ workerCode+ "-"+ managerCode + "-"+ faultStartTime+ "-"+ faultEndTime+ "-"+ faultDescription);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_DEVICE_ID, faultModel.getOutletDeviceId());
        bundle.putString(BUNDLE_DEVICE_NAME, deviceName);
        bundle.putString(BUNDLE_COMPANY_CODE, faultModel.getCompanyCode());
        bundle.putString(BUNDLE_COMPANY_NAME, companyName);
        bundle.putString(BUNDLE_OUTLET_CODE, faultModel.getOutletCode());
        bundle.putString(BUNDLE_OUTLET_NAME, outletName);
        bundle.putString(BUNDLE_FT_TYPE, faultModel.getFaultType());
        bundle.putString(BUNDLE_FT_TYPENAME, faultTypeName);
        bundle.putString(BUNDLE_WORKER_CODE, faultModel.getWorkCode());
        bundle.putString(BUNDLE_MANAGE_CODE, faultModel.getManageCode());
        bundle.putString(BUNDLE_START_TIME, faultModel.getFaultBeginTime());
        bundle.putString(BUNDLE_END_TIME, faultModel.getFaultEndTime());
        bundle.putString(BUNDLE_RECORD_FLAG, faultModel.getRecordFlag());
        bundle.putString(BUNDLE_RECORD_TIME, faultModel.getRecordTime());
        bundle.putString(BUNDLE_DESCRIPTION, faultDescription);
        bundle.putString(BUNDLE_FT_REMARK, faultRemark);

        QueryDataIntentService.startInsertFault(NewFaultTaskActivity.this, bundle);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        spinnerType = (Spinner) findViewById(R.id.spinner_fault_type);
        spinnerManager = (Spinner) findViewById(R.id.spinner_fault_manager);
        spinnerWorker = (Spinner) findViewById(R.id.spinner_fault_worker);
        spinnerRecord = (Spinner) findViewById(R.id.spinner_fault_record);
        buttonFaultBeginDate = (Button) findViewById(R.id.btn_fault_begin_date);
        buttonFaultBeginTime = (Button) findViewById(R.id.btn_fault_begin_time);
        buttonFaultEndDate = (Button) findViewById(R.id.btn_fault_end_date);
        buttonFaultEndTime = (Button) findViewById(R.id.btn_fault_end_time);
        textViewFaultBeginDate = (TextView) findViewById(R.id.tv_fault_begin_date);
        textViewFaultBeginTime = (TextView) findViewById(R.id.tv_fault_begin_time);
        textViewFaultEndDate = (TextView) findViewById(R.id.tv_fault_end_date);
        textViewFaultEndTime = (TextView) findViewById(R.id.tv_fault_end_time);
        btnFaultRecordDate = (Button) findViewById(R.id.btn_fault_record_date);
        tvFaultRecordDate = (TextView) findViewById(R.id.tv_fault_record_date);
        btnFaultRecordTime = (Button) findViewById(R.id.btn_fault_record_time);
        tvFaultRecordTime = (TextView) findViewById(R.id.tv_fault_record_time);
        editTextDescription = (AppCompatEditText) findViewById(R.id.edit_fault_description);
        editFaultRemark = (AppCompatEditText) findViewById(R.id.edit_fault_remark);
        buttonSubmit = (Button) findViewById(R.id.btn_fault_submit);
        //设置默认时间
        SimpleDateFormat dDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dDateFormat.format(new java.util.Date());
        SimpleDateFormat tDateFormat = new SimpleDateFormat("HH:mm:00");
        String time = tDateFormat.format(new java.util.Date());
        textViewFaultBeginDate.setText(date);
        textViewFaultBeginTime.setText(time);
        textViewFaultEndDate.setText(date);
        textViewFaultEndTime.setText(time);
        TextView tvFaultRecordClear = (TextView) findViewById(R.id.tv_fault_record_clear);
        tvFaultRecordClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFaultRecordDate.setText("");
                tvFaultRecordTime.setText("");
            }
        });
    }

    /**
     * 显示日期选择器
     */
    private void showDate(final Button button) {
        DatePickerDialog dd = new DatePickerDialog(NewFaultTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            //两个要对应
                            String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            Date date = formatter.parse(dateInString);
                            if (button == buttonFaultBeginDate) {
                                strings[0] = formatter.format(date).toString();
                                textViewFaultBeginDate.setText(strings[0]);
                            } else if (button == buttonFaultEndDate) {
                                strings[1] = formatter.format(date).toString();
                                textViewFaultEndDate.setText(strings[1]);
                            } else if (button == btnFaultRecordDate) {
                                strings[2] = formatter.format(date).toString();
                                tvFaultRecordDate.setText(strings[2]);
                            }

                           /* formatter = new SimpleDateFormat("dd.MMM.yyyy");
                            textViewFaultBeginDate.setText(textViewFaultBeginDate.getText().toString()+"\n"+formatter.format(date).toString());

                            formatter = new SimpleDateFormat("dd/MMM/yyyy");
                            textViewFaultBeginDate.setText(textViewFaultBeginDate.getText().toString()+"\n"+formatter.format(date).toString());

                            formatter = new SimpleDateFormat("dd-MM-yyyy");
                            textViewFaultBeginDate.setText(textViewFaultBeginDate.getText().toString()+"\n"+formatter.format(date).toString());*/
                        } catch (Exception ex) {

                        }

                    }
                }, year, month, day);
        dd.show();
    }

    /**
     * 时间选择器
     *
     * @param button
     */
    private void showTime(final Button button) {
        TimePickerDialog td = new TimePickerDialog(NewFaultTaskActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        try {
                            String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                            format = new SimpleDateFormat("HH:mm");

                            timeValue = new java.sql.Time(format.parse(dtStart).getTime());

                            if (button == buttonFaultBeginTime) {
                                strings[3] = String.valueOf(timeValue);
                                textViewFaultBeginTime.setText(strings[3]);
                            } else if (button == buttonFaultEndTime) {
                                strings[4] = String.valueOf(timeValue);
                                textViewFaultEndTime.setText(strings[4]);
                            } else if (button == btnFaultRecordTime) {
                                strings[5] = String.valueOf(timeValue);
                                tvFaultRecordTime.setText(strings[5]);
                            }

                            /*String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");
                            textViewFaultBeginTime.setText(amPm + "\n" + String.valueOf(timeValue));*/
                        } catch (Exception ex) {
                            //textViewFaultBeginTime.setText(ex.getMessage().toString());
                        }
                    }
                },
                hour, min,
                true//true是24小时制，false是12小时制

        );
        td.show();
    }

    @Override
    public void startIntentSenderForResult(IntentSender intent, int requestCode, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    /**
     * receiver来接收数据
     */
    public class AddFaultReceiver extends BroadcastReceiver {
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
                    if (modelListTemp.get(0).getUserFlag() == 1) {//返回的是运维负责人
                        managerList = modelListTemp;
                        ArrayAdapter<UserModel> adapterManager = new ArrayAdapter<>(NewFaultTaskActivity.this, android.R.layout.simple_spinner_dropdown_item, managerList);
                        spinnerManager.setAdapter(adapterManager);
                        for (int i = 0; i < spinnerManager.getCount(); i++) {
                            if (workerList.get(i).getUserCode().equals(userCode)) {
                                spinnerManager.setSelection(i);
                                break;
                            } else {
                                continue;
                            }
                        }
                    } else {//返回运维人员
                        workerList = modelListTemp;
                        ArrayAdapter<UserModel> adapterWorker = new ArrayAdapter<>(NewFaultTaskActivity.this, android.R.layout.simple_spinner_dropdown_item, workerList);
                        spinnerWorker.setAdapter(adapterWorker);
                        for (int i = 0; i < spinnerWorker.getCount(); i++) {
                            if (workerList.get(i).getUserCode().equals(userCode)) {
                                spinnerWorker.setSelection(i);
                                break;
                            } else {
                                continue;
                            }
                        }
                    }
                }
            } else {//如果失败
                Toast.makeText(NewFaultTaskActivity.this, info, Toast.LENGTH_SHORT).show();
            }


        }
    }

    /**
     * receiver来接收响应数据
     */
    public class AddFaultGetInfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);

            //如果查到数据验证正确
            if (result) {
                Toast.makeText(NewFaultTaskActivity.this, info, Toast.LENGTH_LONG).show();
                onBackPressed();

            } else {//如果失败
                Toast.makeText(NewFaultTaskActivity.this, info, Toast.LENGTH_LONG).show();
            }
        }
    }
}
