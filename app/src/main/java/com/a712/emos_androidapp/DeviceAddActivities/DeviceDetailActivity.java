package com.a712.emos_androidapp.DeviceAddActivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.a712.emos_androidapp.MyEomsActivity;
import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.receiveModel.DeviceModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import javax.xml.transform.Result;

import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DEVICE_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_DETAILS;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_TOTAL;

/**
 * Created by Administrator on 2017/4/10.
 */

public class DeviceDetailActivity extends MyEomsActivity {
    //声明控件
    private TextView textViewDeviceName;
    private TextView textViewDeviceType;
    private TextView textViewDeviceModel;
    private TextView textViewMfr;
    private TextView textViewCompany;
    private TextView textViewOutlet;
    private TextView textViewInstallTime;

    private DeviceModel deviceModel;
    private int outletDeviceId;

    // 声明broadcastreceiver
    private IntentFilter mDetailsIntentFilter;
    private DeviceDetailReceiver mDetailsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        //从前一个页面得到设备ID
        Bundle bundle = this.getIntent().getExtras();
        outletDeviceId = bundle.getInt(BUNDLE_DEVICE_ID);

        //broadcaster接收过滤器
        mDetailsIntentFilter = new IntentFilter(INTENT_QUERY_DETAILS);
        mDetailsReceiver = new DeviceDetailReceiver();
        deviceModel = new DeviceModel();
        //tool bar设置
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_device_detail);
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
        QueryDataIntentService.startQueryDeviceDetail(DeviceDetailActivity.this, outletDeviceId);
    }

    /**
     * 初始化
     */
    private void initView() {
        textViewDeviceName = (TextView) findViewById(R.id.tv_detail_device_name);
        textViewDeviceType = (TextView) findViewById(R.id.tv_detail_device_type);
        textViewDeviceModel = (TextView) findViewById(R.id.tv_detail_device_number);
        textViewMfr = (TextView) findViewById(R.id.tv_detail_mfr);
        textViewCompany = (TextView) findViewById(R.id.tv_detail_company);
        textViewOutlet = (TextView) findViewById(R.id.tv_detail_outlet);
        textViewInstallTime = (TextView) findViewById(R.id.tv_detail_install_time);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(mDetailsReceiver, mDetailsIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(mDetailsReceiver);
    }

    /**
     * receiver来接收数据
     */
    public class DeviceDetailReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);
            String data = bundle.getString(RESULT_EXTRA_DATA);
            int total = bundle.getInt(RESULT_TOTAL);
            //如果查到数据验证正确
            if (result) {
                if (total > 0) {
                    List<DeviceModel> listTemp = gson.fromJson(data, new TypeToken<List<DeviceModel>>() {
                    }.getType());

                    deviceModel.setDeviceName(listTemp.get(0).getDeviceName());
                    deviceModel.setDeviceTypeName(listTemp.get(0).getDeviceTypeName());
                    deviceModel.setDeviceModel(listTemp.get(0).getDeviceModel());
                    deviceModel.setMfrName(listTemp.get(0).getMfrName());
                    deviceModel.setCompanyName(listTemp.get(0).getCompanyName());
                    deviceModel.setOutletName(listTemp.get(0).getOutletName());
                    deviceModel.setInstallTime(listTemp.get(0).getInstallTime());
                    setTextView();
                }
            } else {//如果失败
                Toast.makeText(DeviceDetailActivity.this, info, Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 设置TextView
     */
    private void setTextView() {
        textViewDeviceName.setText(deviceModel.getDeviceName());
        textViewDeviceType.setText(deviceModel.getDeviceTypeName());
        textViewDeviceModel.setText(deviceModel.getDeviceModel());
        textViewMfr.setText(deviceModel.getMfrName());
        textViewCompany.setText(deviceModel.getCompanyName());
        textViewOutlet.setText(deviceModel.getOutletName());
        textViewInstallTime.setText(deviceModel.getInstallTime());
    }
}
