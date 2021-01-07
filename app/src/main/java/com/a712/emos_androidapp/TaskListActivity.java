package com.a712.emos_androidapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.a712.emos_androidapp.DeviceAddActivities.NewFaultTaskActivity;
import com.a712.emos_androidapp.DeviceAddActivities.ProcessFaultActivity;
import com.a712.emos_androidapp.DeviceAddActivities.ProcessTaskActivity;
import com.a712.emos_androidapp.receiveModel.FaultModel;
import com.a712.emos_androidapp.receiveModel.TaskModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.a712.emos_androidapp.utils.FunctionUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_ACCESS_MODIFY;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_ACCESS_MODIFY_DAYS;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_COMPANY_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_COMPANY_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DEVICE_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DEVICE_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_TYPE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_TYPENAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_OUTLET_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_OUTLET_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_RECORD_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_RECORD_TIME;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_BUNDLE_TITLE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_BUNDLE_TITLE_TYPE;

import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_FAULT_LIST;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_TOTAL;


/**
 * Created by Administrator on 2017/3/29.
 */

public class TaskListActivity extends MyEomsActivity {
    private EomsApplication eomsApplication;
    private String strTitle;//标题文字
    private String titleType;//标题类型标识
    private boolean isTitleFault;//判断是否是故障标题
    private int userFlag;//用户类型标识
    private int queryStart;//查询开始数字
    //存储查询到的list清单
    private List<FaultModel> listFaultModel;
    private List<TaskModel> listTaskModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TaskListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;//最后一个可见列表的位置

    // 声明broadcastreceiver
    private IntentFilter mTaskListIntentFilter;
    private TaskListReceiver mTaskListReceiver;

    //是否刷新数据
    private boolean mIsRefreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        //获取全局变量
        eomsApplication = (EomsApplication) getApplication();
        //bundle获取传递过来的数据
        savedInstanceState = this.getIntent().getExtras();
        strTitle = savedInstanceState.getString(INTENT_BUNDLE_TITLE);
        titleType = savedInstanceState.getString(INTENT_BUNDLE_TITLE_TYPE);
        isTitleFault = strTitle.equals(getString(R.string.main_fragment_content1));
        //从sharedPreference获取数据
        Resources resources = getResources();
        SharedPreferences pref = getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
        userFlag = pref.getInt(resources.getString(R.string.preference_worker), 0);
        //初始化列表
        listFaultModel = new ArrayList<>();
        listTaskModel = new ArrayList<>();

        //broadcaster接收过滤器
        mTaskListIntentFilter = new IntentFilter(INTENT_QUERY_FAULT_LIST);
        mTaskListReceiver = new TaskListReceiver();

        //tool bar设置
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_task_list);
        //设置toolbar标题，需要在setSupport之前调用
        toolbar.setTitle(getString(R.string.task_list));
        toolbar.setSubtitle(strTitle);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上一个界面并关闭此界面
                onBackPressed();
            }
        });

        //下拉刷新操作
        // swipeRefreshLayout.setRefreshing(false);  停止刷新
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_task_list);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListData();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);//设置颜色

        //初始化recyclerView和adapter
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_task_list);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        mAdapter = new TaskListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        //刷新数据时不能scroll
        mRecyclerView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mIsRefreshing) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );

        //上拉加载更多功能，RecyclerView滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //如果滑动到最下面一个位置
                if (mAdapter.getItemCount() >= 6) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                        //更新上拉滑动状态
                        mAdapter.changeMoreStatus(TaskListAdapter.LOADING_MORE);
                        needMoreListData();

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();//设置最后一个可见列表的位置
            }
        });

        //加载出数据
        refreshListData();
    }

    /**
     * 自定义刷新数据方法
     */
    private void refreshListData() {
        queryStart = 0;
        if (isTitleFault) {
            if (listFaultModel.size() > 0) {
                listFaultModel.clear();
                mIsRefreshing = true;
            }
            QueryDataIntentService.startQueryFaultList(TaskListActivity.this, userFlag, queryStart
                    , eomsApplication.getSelectBeginDate(), eomsApplication.getSelectEndDate());
        } else {
            if (listTaskModel.size() > 0) {
                listTaskModel.clear();
                mIsRefreshing = true;
            }
            QueryDataIntentService.startQueryTaskList(TaskListActivity.this, titleType, userFlag, queryStart
                    , eomsApplication.getSelectBeginDate(), eomsApplication.getSelectEndDate());
        }

    }

    /**
     * 请求更多数据
     */
    private void needMoreListData() {
        queryStart += 10;
        if (isTitleFault) {
            QueryDataIntentService.startQueryFaultList(TaskListActivity.this, userFlag, queryStart
                    , eomsApplication.getSelectBeginDate(), eomsApplication.getSelectEndDate());
        } else {
            QueryDataIntentService.startQueryTaskList(TaskListActivity.this, titleType, userFlag, queryStart
                    , eomsApplication.getSelectBeginDate(), eomsApplication.getSelectEndDate());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(mTaskListReceiver, mTaskListIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(mTaskListReceiver);
    }

    /**
     * receiver来接收数据
     */
    public class TaskListReceiver extends BroadcastReceiver {
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
                    Snackbar.make(mRecyclerView, "无数据，如有疑问，可以登录运维网站确认！", Snackbar.LENGTH_LONG).show();
                } else if (total > 5 && total <= queryStart) {
                    Snackbar.make(mRecyclerView, "无更多数据!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (isTitleFault) {
                        List<FaultModel> modelListTemp = gson.fromJson(data, new TypeToken<List<FaultModel>>() {
                        }.getType());
                        listFaultModel.addAll(modelListTemp);
                    } else {
                        //Log.d("进入", "获取任务列表 ");
                        List<TaskModel> modelListTemp = gson.fromJson(data, new TypeToken<List<TaskModel>>() {
                        }.getType());
                        listTaskModel.addAll(modelListTemp);
                    }
                }
            } else {//如果失败
                Toast.makeText(TaskListActivity.this, info, Toast.LENGTH_SHORT).show();
            }
            mIsRefreshing = false;
            //通知更新，改变更新状态，停止下拉刷新动作
            mAdapter.notifyDataSetChanged();//要通知adapter进行更新
            mAdapter.changeMoreStatus(TaskListAdapter.PULL_UP_LOAD_MORE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 定义adapter
     */
    class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context mContext;
        private static final int TYPE_EMPTY = -1;  //无数据
        private static final int TYPE_ITEM = 0;  //普通Item View
        private static final int TYPE_FOOTER = 1;  //顶部FootView


        public TaskListAdapter(Context context) {
            this.mContext = context;
        }

        /**
         * 初始化布局
         *
         * @param viewGroup
         * @param viewType
         * @return
         */
        //创建新View，加载item布局，被LayoutManager所调用
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            if (viewType == TYPE_EMPTY) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_empty, viewGroup, false);
                return new MyEmptyHolder(view);
            } else if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task_list, viewGroup, false);
                TaskListViewHolder vh = new TaskListViewHolder(view);
                return vh;
            } else if (viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_foot_view, viewGroup, false);
                FootViewHolder footViewHolder = new FootViewHolder(view);
                return footViewHolder;
            }
            return null;
        }

        /**
         * 负责将数据绑定到Item的视图上
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof TaskListViewHolder) {
                if (isTitleFault) {
                    if (listFaultModel.size() > 0) {
                        final TaskListViewHolder taskListViewHolder = (TaskListViewHolder) holder;
                        taskListViewHolder.tvTitleTaskFaultTime.setVisibility(View.VISIBLE);
                        taskListViewHolder.tvTitleTaskFaultType.setVisibility(View.VISIBLE);

                        taskListViewHolder.tvTaskCompany.setText(listFaultModel.get(position).getCompanyName());
                        taskListViewHolder.tvTaskOutlet.setText(listFaultModel.get(position).getOutletName());
                        taskListViewHolder.tvTaskDevice.setText(listFaultModel.get(position).getDeviceName());
                        taskListViewHolder.tvTaskFaultTime.setVisibility(View.VISIBLE);
                        taskListViewHolder.tvTaskFaultTime.setText(listFaultModel.get(position).getFaultBeginTime());
                        taskListViewHolder.tvTaskFaultType.setVisibility(View.VISIBLE);
                        taskListViewHolder.tvTaskFaultType.setText(listFaultModel.get(position).getFaultTypeName());
                        taskListViewHolder.tvTaskFinishTime.setText(listFaultModel.get(position).getFaultEndTime());
                        taskListViewHolder.tvTaskHandleTime.setText(listFaultModel.get(position).getFaultHandleTime());
                        taskListViewHolder.tvTaskManager.setText(listFaultModel.get(position).getManageName());
                        taskListViewHolder.tvTaskWorker.setText(listFaultModel.get(position).getWorkName());
                        taskListViewHolder.tvTaskDescription.setText(listFaultModel.get(position).getFaultDescription());
                        //按钮点击方法
                        taskListViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopupMenu popup = new PopupMenu(TaskListActivity.this, taskListViewHolder.imageButton);
                                //Inflating the Popup using xml file
                                popup.getMenuInflater().inflate(R.menu.popup_menu_process, popup.getMenu());
                                //registering popup with OnMenuItemClickListener
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        //Log.d("进入菜单", "onMenuItemClick: ");
                                        if (mIsRefreshing) {
                                            Toast.makeText(TaskListActivity.this, "正在刷新数据中，不能进行此操作！", Toast.LENGTH_SHORT).show();
                                            return true;
                                        }
                                        int id = item.getItemId();
                                        String faultId = listFaultModel.get(position).getFaultId();
                                        int ouletDeviceId = listFaultModel.get(position).getOutletDeviceId();
                                        String ouletDeviceName = listFaultModel.get(position).getDeviceName();
                                        String companyName = listFaultModel.get(position).getCompanyName();
                                        String outletCode = listFaultModel.get(position).getOutletCode();
                                        String outletName = listFaultModel.get(position).getOutletName();
                                        String faultType = listFaultModel.get(position).getFaultType();
                                        String faultTypeName = listFaultModel.get(position).getFaultTypeName();
                                        String recordFlag = listFaultModel.get(position).getRecordFlag();
                                        String recordTime = listFaultModel.get(position).getRecordTime();
                                        if (id == R.id.update_process_result) {
                                            Intent mainActivityIntent = new Intent(TaskListActivity.this, ProcessFaultActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString(BUNDLE_FT_ID, faultId);
                                            bundle.putInt(BUNDLE_DEVICE_ID, ouletDeviceId);
                                            bundle.putString(BUNDLE_DEVICE_NAME, ouletDeviceName);
                                            bundle.putString(BUNDLE_COMPANY_NAME, companyName);
                                            bundle.putString(BUNDLE_OUTLET_CODE, outletCode);
                                            bundle.putString(BUNDLE_OUTLET_NAME, outletName);
                                            bundle.putString(BUNDLE_FT_TYPE, faultType);
                                            bundle.putString(BUNDLE_FT_TYPENAME, faultTypeName);
                                            bundle.putString(BUNDLE_RECORD_FLAG, recordFlag);
                                            bundle.putString(BUNDLE_RECORD_TIME, recordTime);
                                            bundle.putBoolean(BUNDLE_ACCESS_MODIFY, true);
                                            mainActivityIntent.putExtras(bundle);
                                            startActivity(mainActivityIntent);
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                });
                                popup.show();
                            }
                        });
                    }

                } else {
                    if (listTaskModel.size() > 0) {
                        final TaskListViewHolder taskListViewHolder = (TaskListViewHolder) holder;
                        taskListViewHolder.tvTaskCompany.setText(listTaskModel.get(position).getCompanyName());
                        taskListViewHolder.tvTaskOutlet.setText(listTaskModel.get(position).getOutletName());
                        taskListViewHolder.tvTaskDevice.setText(listTaskModel.get(position).getDeviceName());
                        taskListViewHolder.tvTaskFinishTime.setText(listTaskModel.get(position).getTaskEndTime());
                        taskListViewHolder.tvTaskHandleTime.setText(listTaskModel.get(position).getTaskHandleTime());
                        taskListViewHolder.tvTaskManager.setText(listTaskModel.get(position).getManageName());
                        taskListViewHolder.tvTaskWorker.setText(listTaskModel.get(position).getWorkName());
                        taskListViewHolder.tvTaskDescription.setText(listTaskModel.get(position).getTaskDescription());
                        //按钮点击方法
                        taskListViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopupMenu popup = new PopupMenu(TaskListActivity.this, taskListViewHolder.imageButton);
                                //Inflating the Popup using xml file
                                popup.getMenuInflater().inflate(R.menu.popup_menu_process, popup.getMenu());
                                //registering popup with OnMenuItemClickListener
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        //Log.d("进入菜单", "onMenuItemClick: ");
                                        if (mIsRefreshing) {
                                            Toast.makeText(TaskListActivity.this, "正在刷新数据中，不能进行此操作！", Toast.LENGTH_SHORT).show();
                                            return true;
                                        }
                                        int id = item.getItemId();
                                        String taskId = listTaskModel.get(position).getTaskId();
                                        int ouletDeviceId = listTaskModel.get(position).getOutletDeviceId();
                                        String ouletDeviceName = listTaskModel.get(position).getDeviceName();
                                        String companyName = listTaskModel.get(position).getCompanyName();
                                        String outletCode = listTaskModel.get(position).getOutletCode();
                                        String outletName = listTaskModel.get(position).getOutletName();
                                        String taskType = listTaskModel.get(position).getTaskType();
                                        String taskTypeName = listTaskModel.get(position).getTaskTypeName();
                                        if (id == R.id.update_process_result) {
                                            Intent mainActivityIntent = new Intent(getApplicationContext(), ProcessTaskActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString(BUNDLE_FT_ID, taskId);
                                            bundle.putInt(BUNDLE_DEVICE_ID, ouletDeviceId);
                                            bundle.putString(BUNDLE_DEVICE_NAME, ouletDeviceName);
                                            bundle.putString(BUNDLE_COMPANY_NAME, companyName);
                                            bundle.putString(BUNDLE_OUTLET_CODE, outletCode);
                                            bundle.putString(BUNDLE_OUTLET_NAME, outletName);
                                            bundle.putString(BUNDLE_FT_TYPE, taskType);
                                            bundle.putString(BUNDLE_FT_TYPENAME, taskTypeName);
                                            bundle.putBoolean(BUNDLE_ACCESS_MODIFY, true);
                                            mainActivityIntent.putExtras(bundle);
                                            startActivity(mainActivityIntent);
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                });
                                popup.show();
                            }
                        });
                    }

                }
            } else if (holder instanceof FootViewHolder) {
                FootViewHolder footHolder = (FootViewHolder) holder;
                switch (load_more_status) {
                    case PULL_UP_LOAD_MORE:
                        footHolder.setProgressBar(false);
                        //footHolder.setText("上拉加载更多...");
                        //footHolder.setText("");
                        break;
                    case LOADING_MORE:
                        // footHolder.setText("正在加载更多数据...");
                        footHolder.setProgressBar(true);
                        break;
                }
            }

        }

        /**
         * 改变下拉加载更多状态
         *
         * @param status
         */
        public static final int PULL_UP_LOAD_MORE = 0;//0是不显示加载
        public static final int LOADING_MORE = 1;//一是显示下拉加载更多进度条
        private int load_more_status;

        public void changeMoreStatus(int status) {
            load_more_status = status;
            notifyDataSetChanged();
        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            if (isTitleFault) {
                return listFaultModel.size() + 1;
            } else {
                return listTaskModel.size() + 1;
            }
        }

        /**
         * 重写返回加载布局的类型
         *
         * @param position
         * @return
         */
        @Override
        public int getItemViewType(int position) {
            // 最后一个item设置为footerView
            if (getItemCount() <= 1 && !mIsRefreshing) {
                return TYPE_EMPTY;
            } else if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }
    }

    /**
     * 内部类ViewHolder，必须继承viewHolder类来承载视图
     */
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    //好处，对每个控件中的重复部分只加载一次，避免重复加载
    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        private ImageButton imageButton;
        private TextView tvTaskCompany;
        private TextView tvTaskOutlet;
        private TextView tvTaskDevice;
        private TextView tvTaskFaultType;
        private TextView tvTaskFaultTime;
        private TextView tvTaskFinishTime;
        private TextView tvTaskHandleTime;
        private TextView tvTaskManager;
        private TextView tvTaskWorker;
        private TextView tvTaskDescription;

        private TextView tvTitleTaskFaultTime;
        private TextView tvTitleTaskFaultType;

        public TaskListViewHolder(View view) {
            super(view);
            imageButton = (ImageButton) view.findViewById(R.id.img_select_process_button);
            tvTaskCompany = (TextView) view.findViewById(R.id.tv_task_company);
            tvTaskOutlet = (TextView) view.findViewById(R.id.tv_task_outlet);
            tvTaskDevice = (TextView) view.findViewById(R.id.tv_task_device);
            tvTaskFaultType = (TextView) view.findViewById(R.id.tv_task_fault_type);
            tvTaskFaultTime = (TextView) view.findViewById(R.id.tv_task_fault_time);
            tvTaskFinishTime = (TextView) view.findViewById(R.id.tv_task_finish_time);
            tvTaskHandleTime = (TextView) view.findViewById(R.id.tv_task_handle_time);
            tvTaskManager = (TextView) view.findViewById(R.id.tv_task_manager);
            tvTaskWorker = (TextView) view.findViewById(R.id.tv_task_worker);
            tvTaskDescription = (TextView) view.findViewById(R.id.tv_task_description);

            tvTitleTaskFaultTime = (TextView) view.findViewById(R.id.tv_task_title_fault_time);
            tvTitleTaskFaultType = (TextView) view.findViewById(R.id.tv_task_title_fault_type);


        }

    }

    public static class FootViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public FootViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_loading_foot);
        }

        public void setProgressBar(boolean isVisibility) {
            if (isVisibility) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class MyEmptyHolder extends RecyclerView.ViewHolder {
        TextView tv_empty;

        public MyEmptyHolder(View itemView) {
            super(itemView);
            tv_empty = (TextView) itemView.findViewById(R.id.tv_empty);
        }
    }

}
