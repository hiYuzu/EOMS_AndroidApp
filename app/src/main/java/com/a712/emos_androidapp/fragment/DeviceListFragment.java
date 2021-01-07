package com.a712.emos_androidapp.fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.a712.emos_androidapp.DeviceAddActivities.DeviceDetailActivity;
import com.a712.emos_androidapp.DeviceAddActivities.NewFaultTaskActivity;
import com.a712.emos_androidapp.DeviceAddActivities.NewOtherTaskActivity;
import com.a712.emos_androidapp.FinishTaskListActivity;
import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.receiveModel.DeviceModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_COMPANY_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_COMPANY_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DEVICE_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DEVICE_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_END_TIME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_TYPE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_OUTLET_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_OUTLET_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_START_TIME;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_BUNDLE_TITLE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_QUERY_DEVICE_LIST;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_TOTAL;

/**
 * Created by Administrator on 2017/3/27.
 */

public class DeviceListFragment extends Fragment {

    private View view;
    private String qrCode;//设备序列号
    private int queryStart;//查询开始数字
    //存储查询到的list清单
    private List<DeviceModel> listDeviceModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DeviceListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;//最后一个可见列表的位置

    // 声明broadcastreceiver
    private IntentFilter mDeviceListIntentFilter;
    private DeviceListReceiver mDeviceListReceiver;

    //是否刷新数据
    private boolean mIsRefreshing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_device, container, false);


            //初始化列表
            listDeviceModel = new ArrayList<>();
            //broadcaster接收过滤器
            mDeviceListIntentFilter = new IntentFilter(INTENT_QUERY_DEVICE_LIST);
            mDeviceListReceiver = new DeviceListReceiver();
            //下拉刷新操作
            // swipeRefreshLayout.setRefreshing(false);  停止刷新
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_device_fragment);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshDeviceListData("");

                }
            });
            swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);//设置颜色

            //初始化recyclerView和adapter
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_device_fragment);
            //创建默认的线性LayoutManager
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
            mRecyclerView.setHasFixedSize(true);
            //创建并设置Adapter
            mAdapter = new DeviceListAdapter(getActivity());
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
                    if (mAdapter.getItemCount() >= 6) {//设置范围不然会出现下拉和上拉重合情况
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                            //更新上拉滑动状态
                            mAdapter.changeMoreStatus(DeviceListAdapter.LOADING_MORE);
                            needMoreDeviceListData(qrCode);

                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();//设置最后一个可见列表的位置
                }
            });
            refreshDeviceListData(qrCode);
        }
        return view;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * 刷新数据方法
     */
    public void refreshDeviceListData(String tempQrCode) {
        qrCode = tempQrCode;
        queryStart = 0;
        if (listDeviceModel.size() > 0) {
            listDeviceModel.clear();
        }
        mIsRefreshing = true;
        QueryDataIntentService.startQueryDeviceList(getActivity(), tempQrCode, queryStart);
    }


    /**
     * 请求更多数据
     */
    private void needMoreDeviceListData(String tempQrCode) {
        queryStart += 10;
        QueryDataIntentService.startQueryDeviceList(getActivity(), tempQrCode, queryStart);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("设备管理fragment", "onStart: ");
        getActivity().registerReceiver(mDeviceListReceiver, mDeviceListIntentFilter);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("设备管理fragment", "onStop: ");
        getActivity().unregisterReceiver(mDeviceListReceiver);
    }

    /**
     * receiver来接收数据
     */
    public class DeviceListReceiver extends BroadcastReceiver {
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
                    List<DeviceModel> modelListTemp = gson.fromJson(data, new TypeToken<List<DeviceModel>>() {
                    }.getType());
                    listDeviceModel.addAll(modelListTemp);
                }
            } else {//如果失败
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
            }
            mIsRefreshing = false;
            //通知更新，改变更新状态，停止下拉刷新动作
            mAdapter.notifyDataSetChanged();//要通知adapter进行更新
            mAdapter.changeMoreStatus(DeviceListAdapter.PULL_UP_LOAD_MORE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 定义adapter
     */
    class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context mContext;
        private static final int TYPE_EMPTY = -1;  //无数据
        private static final int TYPE_ITEM = 0;  //普通Item View
        private static final int TYPE_FOOTER = 1;  //顶部FootView

        public DeviceListAdapter(Context context) {
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
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device_list, viewGroup, false);
                DeviceListViewHolder vh = new DeviceListViewHolder(view);
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
            if (holder instanceof DeviceListViewHolder) {
                if (listDeviceModel != null && listDeviceModel.size() > 0) {
                    final DeviceListViewHolder deviceListViewHolder = (DeviceListViewHolder) holder;
                    deviceListViewHolder.tvDeviceName.setText(listDeviceModel.get(position).getDeviceName());
                    deviceListViewHolder.tvCompanyName.setText(listDeviceModel.get(position).getCompanyName());
                    deviceListViewHolder.tvOutletName.setText(listDeviceModel.get(position).getOutletName());
                    deviceListViewHolder.tvQrCode.setText(listDeviceModel.get(position).getQrCode());
                    //按钮点击方法
                    deviceListViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popup = new PopupMenu(getActivity(), deviceListViewHolder.imageButton);
                            //Inflating the Popup using xml file
                            popup.getMenuInflater().inflate(R.menu.popup_menu_device, popup.getMenu());
                            //registering popup with OnMenuItemClickListener
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {
                                    //Log.d("进入菜单", "onMenuItemClick: ");
                                    if (mIsRefreshing) {
                                        Toast.makeText(getActivity(), "正在刷新数据中，不能进行此操作！", Toast.LENGTH_SHORT).show();
                                        return true;
                                    }
                                    int id = item.getItemId();
                                    int outletDeviceId = listDeviceModel.get(position).getOutletDeviceId();
                                    String ouletDeviceName = listDeviceModel.get(position).getDeviceName();
                                    String companyCode = listDeviceModel.get(position).getCompanyCode();
                                    String companyName = listDeviceModel.get(position).getCompanyName();
                                    String outletCode = listDeviceModel.get(position).getOutletCode();
                                    String outletName = listDeviceModel.get(position).getOutletName();
                                    if (id == R.id.new_fault_task) {
                                        Intent mainActivityIntent = new Intent(getActivity(), NewFaultTaskActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(BUNDLE_DEVICE_ID, outletDeviceId);
                                        bundle.putString(BUNDLE_DEVICE_NAME, ouletDeviceName);
                                        bundle.putString(BUNDLE_COMPANY_CODE, companyCode);
                                        bundle.putString(BUNDLE_COMPANY_NAME, companyName);
                                        bundle.putString(BUNDLE_OUTLET_CODE, outletCode);
                                        bundle.putString(BUNDLE_OUTLET_NAME, outletName);
                                        mainActivityIntent.putExtras(bundle);
                                        startActivity(mainActivityIntent);
                                    } else if (id == R.id.new_other_task) {
                                        Intent mainActivityIntent = new Intent(getActivity(), NewOtherTaskActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(BUNDLE_DEVICE_ID, outletDeviceId);
                                        bundle.putString(BUNDLE_DEVICE_NAME, ouletDeviceName);
                                        bundle.putString(BUNDLE_COMPANY_CODE, companyCode);
                                        bundle.putString(BUNDLE_COMPANY_NAME, companyName);
                                        bundle.putString(BUNDLE_OUTLET_CODE, outletCode);
                                        bundle.putString(BUNDLE_OUTLET_NAME, outletName);
                                        mainActivityIntent.putExtras(bundle);
                                        startActivity(mainActivityIntent);
                                    } else if (id == R.id.device_fault) {
                                        showSelectSetDialog(outletDeviceId, true);
                                    } else if (id == R.id.device_other) {
                                        showSelectSetDialog(outletDeviceId, false);
                                    } else if (id == R.id.device_detail) {
                                        Intent mainActivityIntent = new Intent(getActivity(), DeviceDetailActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(BUNDLE_DEVICE_ID, outletDeviceId);
                                        bundle.putString(BUNDLE_DEVICE_NAME, ouletDeviceName);
                                        bundle.putString(BUNDLE_COMPANY_CODE, companyCode);
                                        bundle.putString(BUNDLE_COMPANY_NAME, companyName);
                                        bundle.putString(BUNDLE_OUTLET_CODE, outletCode);
                                        bundle.putString(BUNDLE_OUTLET_NAME, outletName);
                                        mainActivityIntent.putExtras(bundle);
                                        startActivity(mainActivityIntent);
                                    }
                                    return true;
                                }

                            });

                            popup.show(); //showing popup menu
                        }
                        // deviceListViewHolder.spinner.setSelection(int position);
                    });
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
            return listDeviceModel.size() + 1;
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
    public static class DeviceListViewHolder extends RecyclerView.ViewHolder {
        private ImageButton imageButton;
        private TextView tvDeviceName;
        private TextView tvCompanyName;
        private TextView tvOutletName;
        private TextView tvQrCode;

        public DeviceListViewHolder(View view) {
            super(view);
            imageButton = (ImageButton) view.findViewById(R.id.img_select_button);
            tvDeviceName = (TextView) view.findViewById(R.id.tv_device_name);
            tvCompanyName = (TextView) view.findViewById(R.id.tv_device_company);
            tvOutletName = (TextView) view.findViewById(R.id.tv_outlet_name);
            tvQrCode = (TextView) view.findViewById(R.id.tv_device_qr_code);
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

    /**
     * 弹出查询条件设置弹出框
     */
    private void showSelectSetDialog(final int outletDeviceId, final boolean isFault) {
        LinearLayout linearLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_task_select_item, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.task_select_item);
        //list of items
        builder.setView(linearLayout);
        final Button btnTaskBeginDate = (Button) linearLayout.findViewById(R.id.btn_task_begin_date);
        final Button btnTaskEndDate = (Button) linearLayout.findViewById(R.id.btn_task_end_date);
        final TextView tvTaskBeginDate = (TextView) linearLayout.findViewById(R.id.tv_task_begin_date);
        final TextView tvTaskEndDate = (TextView) linearLayout.findViewById(R.id.tv_task_end_date);
        final AppCompatSpinner spTaskType = (AppCompatSpinner) linearLayout.findViewById(R.id.sp_task_type);
        //设置默认值
        SimpleDateFormat dDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dDateFormat.format(new java.util.Date());
        tvTaskBeginDate.setText(date);
        tvTaskEndDate.setText(date);
        btnTaskBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(tvTaskBeginDate);
            }
        });
        btnTaskEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(tvTaskEndDate);
            }
        });
        //赋值任务类型
        ArrayAdapter<CharSequence> adapter;
        if (isFault) {
            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.fault_type, android.R.layout.simple_spinner_item);
        } else {
            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.task_type, android.R.layout.simple_spinner_item);
        }
        spTaskType.setAdapter(adapter);
        //确定按钮
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        String beginDate = tvTaskBeginDate.getText().toString();
                        String endDate = tvTaskEndDate.getText().toString();
                        String ftType = "";
                        if (isFault) {
                            if (spTaskType.getSelectedItemPosition() == 0) {
                                ftType = "501";
                            } else {
                                ftType = "502";
                            }
                        } else {
                            if (spTaskType.getSelectedItemPosition() == 0) {
                                ftType = "601";
                            } else if (spTaskType.getSelectedItemPosition() == 1) {
                                ftType = "602";
                            } else if (spTaskType.getSelectedItemPosition() == 2) {
                                ftType = "603";
                            } else if (spTaskType.getSelectedItemPosition() == 3) {
                                ftType = "604";
                            } else if (spTaskType.getSelectedItemPosition() == 4) {
                                ftType = "605";
                            }
                        }
                        Intent finishTaskActivityIntent = new Intent(getActivity(), FinishTaskListActivity.class);
                        Bundle bundle = new Bundle();
                        finishTaskActivityIntent.putExtras(bundle);
                        bundle.putInt(BUNDLE_DEVICE_ID, outletDeviceId);
                        bundle.putString(BUNDLE_START_TIME, beginDate);
                        bundle.putString(BUNDLE_END_TIME, endDate);
                        bundle.putString(BUNDLE_FT_TYPE, ftType);
                        bundle.putString(INTENT_BUNDLE_TITLE, spTaskType.getSelectedItem().toString());
                        finishTaskActivityIntent.putExtras(bundle);
                        startActivity(finishTaskActivityIntent);
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

    public class MyEmptyHolder extends RecyclerView.ViewHolder {
        TextView tv_empty;

        public MyEmptyHolder(View itemView) {
            super(itemView);
            tv_empty = (TextView) itemView.findViewById(R.id.tv_empty);
        }
    }

}
