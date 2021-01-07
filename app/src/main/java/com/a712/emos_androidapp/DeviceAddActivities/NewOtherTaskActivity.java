package com.a712.emos_androidapp.DeviceAddActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
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

import com.a712.emos_androidapp.receiveModel.TaskModel;
import com.a712.emos_androidapp.receiveModel.UserModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.a712.emos_androidapp.utils.zxing.decoding.Intents;
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
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_WORKER_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_ADD_TASK;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_WORKER_LIST;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_TOTAL;

/**
 * Created by Administrator on 2017/4/10.
 */

public class NewOtherTaskActivity extends MyEomsActivity {

    private String userCode;
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    //声明控件
    private Spinner spinnerType;
    private Spinner spinnerManager;
    private Spinner spinnerWorker;
    private Button buttonTaskEndDate;
    private Button buttonTaskEndTime;
    private Button buttonSubmit;
    private TextView textViewTaskEndDate;
    private TextView textViewTaskEndTime;
    private AppCompatEditText editTextDescription;
    private AppCompatEditText editTaskRemark;
    //存储要提交的数据
    private TaskModel taskModel;
    // 声明broadCastReceiver
    private IntentFilter mQueryWorkerIntentFilter;
    private QueryWorkerReceiver mQueryWorkerReceiver;

    private IntentFilter mAddTaskIntentFilter;
    private AddTaskReceiver mAddTaskReceiver;

    private String stringDate;
    private String stringTime;
    //存储用户列表
    private ArrayList<UserModel> workerList;
    private ArrayList<UserModel> managerList;
    //时间计时器
    Calendar c;
    int year, month, day, hour, min;
    java.sql.Time timeValue;
    SimpleDateFormat format;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_other);

        //获取登录用户
        Resources resources = getResources();
        SharedPreferences pref = getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
        userCode = pref.getString(resources.getString(R.string.preference_username), null);

        //从前一个页面得到设备列表数据
        taskModel = new TaskModel();
        Bundle bundle = this.getIntent().getExtras();
        taskModel.setOutletDeviceId(bundle.getInt(BUNDLE_DEVICE_ID));
        taskModel.setDeviceName(bundle.getString(BUNDLE_DEVICE_NAME));
        taskModel.setCompanyCode(bundle.getString(BUNDLE_COMPANY_CODE));
        taskModel.setCompanyName(bundle.getString(BUNDLE_COMPANY_NAME));
        taskModel.setOutletCode(bundle.getString(BUNDLE_OUTLET_CODE));
        taskModel.setOutletName(bundle.getString(BUNDLE_OUTLET_NAME));
//tool bar设置
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_other);
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
        mQueryWorkerIntentFilter = new IntentFilter(INTENT_QUERY_WORKER_LIST);
        mQueryWorkerReceiver = new QueryWorkerReceiver();

        mAddTaskIntentFilter = new IntentFilter(INTENT_ADD_TASK);
        mAddTaskReceiver = new AddTaskReceiver();

        //给spinner赋值
        String[] mItems = getResources().getStringArray(R.array.task_type);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mItems);
        spinnerType.setAdapter(adapter);

        //初始化时间
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        buttonTaskEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });
        buttonTaskEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    setButtonSubmit();
                    Snackbar.make(v, "已提交新增任务，请等待处理结果......", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        //初始化list
        workerList = new ArrayList<>();
        managerList = new ArrayList<>();
        //查询人员和负责人下拉框
        QueryDataIntentService.startQueryWorkList(NewOtherTaskActivity.this, taskModel.getOutletCode(), 0);
        QueryDataIntentService.startQueryWorkList(NewOtherTaskActivity.this, taskModel.getOutletCode(), 1);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        spinnerType = (Spinner) findViewById(R.id.spinner_task_type);
        spinnerManager = (Spinner) findViewById(R.id.spinner_task_manager);
        spinnerWorker = (Spinner) findViewById(R.id.spinner_task_worker);
        buttonTaskEndDate = (Button) findViewById(R.id.btn_task_end_date);
        buttonTaskEndTime = (Button) findViewById(R.id.btn_task_end_time);
        textViewTaskEndDate = (TextView) findViewById(R.id.tv_task_end_date);
        textViewTaskEndTime = (TextView) findViewById(R.id.tv_task_end_time);
        editTextDescription = (AppCompatEditText) findViewById(R.id.edit_task_description);
        editTaskRemark = (AppCompatEditText) findViewById(R.id.edit_task_remark);
        buttonSubmit = (Button) findViewById(R.id.btn_task_submit);
        //设置默认时间
        SimpleDateFormat dDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dDateFormat.format(new java.util.Date());
        SimpleDateFormat tDateFormat = new SimpleDateFormat("HH:mm:00");
        String time = tDateFormat.format(new java.util.Date());
        textViewTaskEndDate.setText(date);
        textViewTaskEndTime.setText(time);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(mQueryWorkerReceiver, mQueryWorkerIntentFilter);
        registerReceiver(mAddTaskReceiver, mAddTaskIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(mQueryWorkerReceiver);
        unregisterReceiver(mAddTaskReceiver);
    }

    /**
     * 显示日期选择器
     */
    private void showDate() {
        DatePickerDialog dd = new DatePickerDialog(NewOtherTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            //两个要对应
                            String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            Date date = formatter.parse(dateInString);
                            stringDate = formatter.format(date).toString();
                            textViewTaskEndDate.setText(stringDate);

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
     */
    private void showTime() {
        TimePickerDialog td = new TimePickerDialog(NewOtherTaskActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        try {
                            String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                            format = new SimpleDateFormat("HH:mm");
                            timeValue = new java.sql.Time(format.parse(dtStart).getTime());

                            stringTime = String.valueOf(timeValue);
                            textViewTaskEndTime.setText(stringTime);
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

    /**
     * 提交数据
     */
    private void setButtonSubmit() {
        if (spinnerType.getSelectedItemPosition() == 0) {
            taskModel.setTaskType("601");
        } else if (spinnerType.getSelectedItemPosition() == 1) {
            taskModel.setTaskType("602");
        } else if (spinnerType.getSelectedItemPosition() == 2) {
            taskModel.setTaskType("603");
        } else if (spinnerType.getSelectedItemPosition() == 3) {
            taskModel.setTaskType("604");
        } else if(spinnerType.getSelectedItemPosition() == 4){
            taskModel.setTaskType("605");
        }
        taskModel.setTaskTypeName(spinnerType.getSelectedItem().toString());
        String workerCode = workerList.get(spinnerWorker.getSelectedItemPosition()).getUserCode();
        taskModel.setWorkCode(workerCode);
        String managerCode = managerList.get(spinnerManager.getSelectedItemPosition()).getUserCode();
        taskModel.setManageCode(managerCode);
        String faultEndTime = textViewTaskEndDate.getText() + " " + textViewTaskEndTime.getText();
        taskModel.setTaskEndTime(faultEndTime);
//        String faultDescription = editTextDescription.getText() + "";
        String deviceName = taskModel.getDeviceName();
        String companyName = taskModel.getCompanyName();
        String outletName = taskModel.getOutletName();
        String taskTypeName = taskModel.getTaskTypeName();
        String taskDescription = editTextDescription.getText().toString();
        String taskRemark = editTaskRemark.getText().toString();
        //在存入bundle时要设置utf-8编码
        try {
            deviceName = URLEncoder.encode(deviceName, "utf-8");
            companyName = URLEncoder.encode(companyName, "utf-8");
            outletName = URLEncoder.encode(outletName, "utf-8");
            taskTypeName = URLEncoder.encode(taskTypeName, "utf-8");
            taskDescription = URLEncoder.encode(taskDescription, "utf-8");
            taskRemark = URLEncoder.encode(taskRemark, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("新增任务转义错误：",e.getMessage());
        }
        // Log.d("提交信息：", outletDeviceId + "-"+ faultTypeCode + "-"+ workerCode+ "-"+ managerCode + "-"+ faultStartTime+ "-"+ faultEndTime+ "-"+ faultDescription);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_DEVICE_ID, taskModel.getOutletDeviceId());
        bundle.putString(BUNDLE_DEVICE_NAME, deviceName);
        bundle.putString(BUNDLE_COMPANY_CODE, taskModel.getCompanyCode());
        bundle.putString(BUNDLE_COMPANY_NAME, companyName);
        bundle.putString(BUNDLE_OUTLET_CODE, taskModel.getOutletCode());
        bundle.putString(BUNDLE_OUTLET_NAME, outletName);
        bundle.putString(BUNDLE_FT_TYPE, taskModel.getTaskType());
        bundle.putString(BUNDLE_FT_TYPE, taskModel.getTaskType());
        bundle.putString(BUNDLE_FT_TYPENAME,taskTypeName);
        bundle.putString(BUNDLE_WORKER_CODE, taskModel.getWorkCode());
        bundle.putString(BUNDLE_MANAGE_CODE, taskModel.getManageCode());
        bundle.putString(BUNDLE_END_TIME, taskModel.getTaskEndTime());
        bundle.putString(BUNDLE_DESCRIPTION, taskDescription);
        bundle.putString(BUNDLE_FT_REMARK, taskRemark);

        QueryDataIntentService.startInsertTask(NewOtherTaskActivity.this, bundle);
    }

    /**
     * receiver来接收数据
     */
    public class QueryWorkerReceiver extends BroadcastReceiver {
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
                        ArrayAdapter<UserModel> adapterManager = new ArrayAdapter<>(NewOtherTaskActivity.this, android.R.layout.simple_spinner_dropdown_item, managerList);
                        spinnerManager.setAdapter(adapterManager);
                        for(int i=0;i<spinnerManager.getCount();i++){
                            if(workerList.get(i).getUserCode().equals(userCode)){
                                spinnerManager.setSelection(i);
                                break;
                            }else{
                                continue;
                            }
                        }

                    } else {//返回运维人员
                        workerList = modelListTemp;
                        ArrayAdapter<UserModel> adapterWorker = new ArrayAdapter<>(NewOtherTaskActivity.this, android.R.layout.simple_spinner_dropdown_item, workerList);
                        spinnerWorker.setAdapter(adapterWorker);
                        for(int i=0;i<spinnerWorker.getCount();i++){
                            if(workerList.get(i).getUserCode().equals(userCode)){
                                spinnerWorker.setSelection(i);
                                break;
                            }else{
                                continue;
                            }
                        }
                    }
                }

            } else {//如果失败
                Toast.makeText(NewOtherTaskActivity.this, info, Toast.LENGTH_SHORT).show();
            }


        }
    }

    /**
     * receiver来接收响应数据
     */
    public class AddTaskReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);

            //如果查到数据验证正确
            if (result) {
                Toast.makeText(NewOtherTaskActivity.this, info, Toast.LENGTH_LONG).show();
                onBackPressed();

            } else {//如果失败
                Toast.makeText(NewOtherTaskActivity.this, info, Toast.LENGTH_LONG).show();
            }


        }
    }
}
