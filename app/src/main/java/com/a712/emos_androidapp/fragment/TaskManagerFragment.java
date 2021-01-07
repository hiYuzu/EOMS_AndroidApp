package com.a712.emos_androidapp.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a712.emos_androidapp.EomsApplication;
import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.TaskListActivity;
import com.a712.emos_androidapp.receiveModel.StatusCountModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.a712.emos_androidapp.utils.BadgeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import static com.a712.emos_androidapp.utils.PublicStringTool.EXTRA_CHINESE_USERNAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_BUNDLE_TITLE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_BUNDLE_TITLE_TYPE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_TASK_COUNT;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;

/**
 * Created by Administrator on 2017/3/27.
 */

public class TaskManagerFragment extends Fragment {
    private EomsApplication eomsApplication;
    private View view;
    private int userFlag;
    private LinearLayout layoutDeviceFault;
    private LinearLayout layoutDeviceSearch;
    private LinearLayout layoutDeviceCorrect;
    private LinearLayout layoutDeviceCheckout;
    private LinearLayout layoutDeviceControl;
    private LinearLayout layoutDeviceDrift;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private int[] arr = new int[6];
    private HashMap<Integer,BadgeView> bvMap = new HashMap<Integer,BadgeView>();
    // 声明broadcastreceiver
    private IntentFilter mIntentFilter;
    private TaskCountReceiver mResultReceiver;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        //获取全局参数
        eomsApplication = (EomsApplication)getActivity().getApplication();
        //接收过滤器
        mIntentFilter = new IntentFilter(INTENT_QUERY_TASK_COUNT);
        mResultReceiver = new TaskCountReceiver();
        //取得登录名和用户标识
        Resources resources = getResources();
        SharedPreferences pref = getActivity().getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
        userFlag = pref.getInt(resources.getString(R.string.preference_worker), 0);

        textView1 = (TextView) view.findViewById(R.id.tv_name_title_distinction);
        textView2 = (TextView) view.findViewById(R.id.tv_user_name);
        textView3 = (TextView) view.findViewById(R.id.tv_task_distinction);
        //取的登录用户中文名
        Bundle bundle = getActivity().getIntent().getExtras();
        String receivedUserName = bundle.getString(EXTRA_CHINESE_USERNAME);
        textView2.setText(receivedUserName);
        if (userFlag == 1) {
            textView1.setText("运维负责人");
            textView3.setText("待监管任务");
        } else {
            textView1.setText("运维人员");
            textView3.setText("待完成任务");
        }
        //根据登录用户查询个数
        QueryDataIntentService.startQueryTaskCount(getActivity(), userFlag,
                eomsApplication.getSelectBeginDate(),eomsApplication.getSelectEndDate());

        layoutDeviceFault = (LinearLayout) view.findViewById(R.id.layout_device_fault);
        layoutDeviceFault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getString(R.string.main_fragment_content1);
                sendDataToTaskActivity(s, "500");
            }
        });
        layoutDeviceSearch = (LinearLayout) view.findViewById(R.id.layout_device_search);
        layoutDeviceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getString(R.string.main_fragment_content2);
                sendDataToTaskActivity(s, "601");
            }
        });
        layoutDeviceCorrect = (LinearLayout) view.findViewById(R.id.layout_device_correct);
        layoutDeviceCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getString(R.string.main_fragment_content3);
                sendDataToTaskActivity(s, "603");
            }
        });
        layoutDeviceCheckout = (LinearLayout) view.findViewById(R.id.layout_device_checkout);
        layoutDeviceCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getString(R.string.main_fragment_content4);
                sendDataToTaskActivity(s, "602");
            }
        });
        layoutDeviceControl = (LinearLayout) view.findViewById(R.id.layout_device_control);
        layoutDeviceControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getString(R.string.main_fragment_content5);
                sendDataToTaskActivity(s, "604");
            }
        });
        layoutDeviceDrift = (LinearLayout) view.findViewById(R.id.layout_device_drift);
        layoutDeviceDrift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getString(R.string.main_fragment_content6);
                sendDataToTaskActivity(s, "605");
            }
        });

        //下拉刷新操作
        // swipeRefreshLayout.setRefreshing(false);  停止刷新
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_main_fragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);//设置颜色
        return view;
    }

    /**
     * 刷新数据方法
     */
    public void refreshData() {
        QueryDataIntentService.startQueryTaskCount(getActivity(),
                userFlag,eomsApplication.getSelectBeginDate(),eomsApplication.getSelectEndDate());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("任务管理fragment", "onStart: ");
        getActivity().registerReceiver(mResultReceiver, mIntentFilter);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("任务管理fragment", "onStop: ");
        getActivity().unregisterReceiver(mResultReceiver);
    }

    /**
     * 为控件设置数字
     */
    private void setBadgeViewNumber() {
        initBadgeView(R.id.img_device_fault, arr[0]);
        initBadgeView(R.id.img_device_search, arr[1]);
        initBadgeView(R.id.img_device_correct, arr[2]);
        initBadgeView(R.id.img_device_checkout, arr[3]);
        initBadgeView(R.id.img_device_control, arr[4]);
        initBadgeView(R.id.img_device_drift, arr[5]);
    }

    /**
     * 设置badgeView的数字
     *
     * @param id
     * @param number
     */
    private void initBadgeView(int id, int number) {
        if(bvMap != null && bvMap.containsKey(id)){
            BadgeView badgeView = bvMap.get(id);
            badgeView.setBadgeCount(number);
        }else{
            BadgeView badgeView = new BadgeView(getActivity());
            badgeView.setTargetView(view.findViewById(id));
            badgeView.setBadgeCount(number);
            bvMap.put(id,badgeView);
        }
    }

    /**
     * 开启task列表页面并发送数据
     *
     * @param title
     */
    private void sendDataToTaskActivity(String title, String titleType) {
        Intent mainActivityIntent = new Intent(getActivity(), TaskListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_BUNDLE_TITLE, title);
        bundle.putString(INTENT_BUNDLE_TITLE_TYPE, titleType);
        mainActivityIntent.putExtras(bundle);
        startActivity(mainActivityIntent);
    }

    //用receiver来接受数据
    public class TaskCountReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);
            String data = bundle.getString(RESULT_EXTRA_DATA);


            //如果查到数据验证正确
            if (result) {

                List<StatusCountModel> modelList = gson.fromJson(data, new TypeToken<List<StatusCountModel>>() {
                }.getType());
                //清空数组
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = 0;
                }
                for (StatusCountModel temp : modelList) {
                    int faultCount = 0;
                    if (temp.getStatusCode().equals("601")) {
                        arr[1] = Integer.parseInt(temp.getStatusCount());
                    } else if (temp.getStatusCode().equals("602")) {
                        arr[3] = Integer.parseInt(temp.getStatusCount());
                    } else if (temp.getStatusCode().equals("603")) {
                        arr[2] = Integer.parseInt(temp.getStatusCount());
                    } else if (temp.getStatusCode().equals("604")) {
                        arr[4] = Integer.parseInt(temp.getStatusCount());
                    } else if (temp.getStatusCode().equals("605")) {
                        arr[5] = Integer.parseInt(temp.getStatusCount());
                    } else if(temp.getStatusCode().equals("501") || temp.getStatusCode().equals("502")) {
                        faultCount = Integer.parseInt(temp.getStatusCount());
                        arr[0] += faultCount;
                    }
                }
                setBadgeViewNumber();
            } else {//如果失败
                Toast toast = Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT);
                toast.show();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
