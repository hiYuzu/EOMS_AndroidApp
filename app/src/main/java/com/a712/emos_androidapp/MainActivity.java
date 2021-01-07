package com.a712.emos_androidapp;


import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a712.emos_androidapp.download.DownLoadUtils;
import com.a712.emos_androidapp.download.DownloadApk;
import com.a712.emos_androidapp.fragment.DeviceListFragment;
import com.a712.emos_androidapp.fragment.TaskManagerFragment;
import com.a712.emos_androidapp.receiveModel.SystemInfoModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.a712.emos_androidapp.utils.SystemParams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.transform.Result;

import static com.a712.emos_androidapp.utils.PublicStringTool.EXTRA_CHINESE_USERNAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_BUNDLE_SCAN_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_BUNDLE_SEARCH_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_CHANGE_PASSWORD_RESULT;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_UPDATE_APP_RESULT;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;

/**
 * Created by Administrator on 2017/3/13.
 */

public class MainActivity extends MyEomsActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EomsApplication eomsApplication;
    public static final int ResultCodeSearch = 1;
    public static final int ResultCodeScan = 2;
    private static final int PAGE_TASK = 0;
    private static final int PAGE_DEVICE = 1;
    private static boolean isUpdateFlag = false;
    private static String appCode;
    private static String appNum;
    private DrawerLayout drawer;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private TaskManagerFragment taskManagerFragment;
    private DeviceListFragment deviceListFragment;
    private ArrayList<String> mListTitle;//页卡标题集合
    private ArrayList<Fragment> fragments;//fragment集合
    // 声明broadcastreceiver
    private IntentFilter mChangePasswordIntentFilter;
    private ChangePasswordResultReceiver mChangePasswordResultReceiver;
    // 声明broadcastReceiver
    private IntentFilter mUpdateAppResultIntentFilter;
    private UpdateAppResultReceiver mUpdateAppResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout_main);
        //获取全局变量
        eomsApplication = (EomsApplication) getApplication();
        //赋值版本信息
        appCode = this.getString(R.string.app_code);
        appNum = this.getString(R.string.app_num);
        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化抽屉和导航栏
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //初始化viewpager
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mListTitle = new ArrayList<>();
        mListTitle.add(getString(R.string.main_tab1));
        mListTitle.add(getString(R.string.main_tab2));
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        //把tabLayout和viewpager绑定
        mTabLayout.setupWithViewPager(mViewPager);

        //把fragment添加到arrayList中
        fragments = new ArrayList<>();
        taskManagerFragment = new TaskManagerFragment();
        deviceListFragment = new DeviceListFragment();
        fragments.add(taskManagerFragment);
        fragments.add(deviceListFragment);
        //最后将adapter和fragment绑定
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);
        //mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //从登录界面得到传过来的中文名并传给任务管理fragment
       /* Bundle bundleTask = this.getIntent().getExtras();
        fragments.get(0).setArguments(bundleTask);*/

        putQrCodeToFragment("");
        //接收过滤器
        mChangePasswordIntentFilter = new IntentFilter(INTENT_CHANGE_PASSWORD_RESULT);
        mChangePasswordResultReceiver = new ChangePasswordResultReceiver();
        mUpdateAppResultIntentFilter = new IntentFilter(INTENT_UPDATE_APP_RESULT);
        mUpdateAppResultReceiver = new UpdateAppResultReceiver();
        //发送检查更新请求
        QueryDataIntentService.startActionUpdate(MainActivity.this,appCode,appNum);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册下载广播接收器
        DownloadApk.registerBroadcast(this);
        registerReceiver(mChangePasswordResultReceiver, mChangePasswordIntentFilter);
        registerReceiver(mUpdateAppResultReceiver, mUpdateAppResultIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DownloadApk.unregisterBroadcast(this);
        unregisterReceiver(mChangePasswordResultReceiver);
        unregisterReceiver(mUpdateAppResultReceiver);
    }

    /**
     * 把值序列号传给fragment
     */
    private void putQrCodeToFragment(String s) {

        deviceListFragment.setQrCode(s);

    }

    /**
     * setresult返回方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String s = "";
        if (resultCode == ResultCodeSearch) {
            s = data.getStringExtra(INTENT_BUNDLE_SEARCH_CODE);
        } else if (resultCode == ResultCodeScan) {
            s = data.getStringExtra(INTENT_BUNDLE_SCAN_CODE);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
        if(deviceListFragment != null){
            putQrCodeToFragment(s);
            deviceListFragment.refreshDeviceListData(s);
        }
        mViewPager.setCurrentItem(PAGE_DEVICE);//设置显示设备列表页面
    }

    /**
     * 抽屉里选择方法
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //修改密码按钮
        if (id == R.id.menu_password) {
            showChangePasswordDialog();//修改密码
        }
        //分享按钮
        else if (id == R.id.menu_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.menu_share_text));
            intent.setType("text/plain");
            //设置分享列表的标题，并且每次都显示分享列表
            startActivity(Intent.createChooser(intent, getString(R.string.menu_share_to)));
        }
        //注销按钮
        else if (id == R.id.menu_logout) {
            Intent mainActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(mainActivityIntent);
            MainActivity.this.finish();
        }
        //查询设置按钮
        else if (id == R.id.menu_select) {
            showSelectSetDialog();
        }
        //设置更新按钮
        else if(id == R.id.menu_update){
            if(!isUpdateFlag){
                QueryDataIntentService.startActionUpdate(MainActivity.this,appCode,appNum);
                isUpdateFlag = true;
            }
            Toast.makeText(MainActivity.this,"已经提交检查更新请求，请等待检查结果......",Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 弹出修改密码弹出框
     */
    private void showChangePasswordDialog() {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.change_password);

        //list of items
        builder.setView(linearLayout);
        final EditText editTextOriginal = (EditText) linearLayout.findViewById(R.id.edit_original_password);
        final EditText editTextNewPassword = (EditText) linearLayout.findViewById(R.id.edit_new_password);
        final EditText editTextConfirmPassword = (EditText) linearLayout.findViewById(R.id.edit_confirm_password);

        //确定按钮
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        String originalPassword = editTextOriginal.getText().toString();
                        String newPassword = editTextNewPassword.getText().toString();
                        String confirmPassword = editTextConfirmPassword.getText().toString();
                        if (newPassword.equals(confirmPassword)) {
                            QueryDataIntentService.startChangePassword(MainActivity.this, originalPassword, newPassword);
                        } else {
                            Toast.makeText(MainActivity.this, "两次输入新密码不一致，更新失败。", Toast.LENGTH_SHORT).show();

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
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    /**
     * 弹出更新APP信息对对话框
     */
    private void showUpdateSetDialog(String nowVersion, final String updateVersion, final String updateUrl, String updateInfo) {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_update_info, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.update_info);
        //list of items
        builder.setView(linearLayout);
        final TextView tvAppNowVersion = (TextView) linearLayout.findViewById(R.id.tv_app_now_version);
        final TextView tvAppUpdateVersion = (TextView) linearLayout.findViewById(R.id.tv_app_update_version);
        final TextView tvAppUpdateInfo = (TextView) linearLayout.findViewById(R.id.tv_app_update_info);
        tvAppNowVersion.setText(nowVersion);
        tvAppUpdateVersion.setText(updateVersion);
        tvAppUpdateInfo.setText(updateInfo);
        //确定按钮
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除已存在的Apk
                        DownloadApk.removeFile(MainActivity.this);
                        //如果手机已经启动下载程序，执行downloadApk。否则跳转到设置界面
                        if (DownLoadUtils.getInstance(getApplicationContext()).canDownload()) {
                            DownloadApk.downloadApk(getApplicationContext(), updateUrl, "eoms更新", "eoms");
                        } else {
                            DownLoadUtils.getInstance(getApplicationContext()).skipToDownloadManager();
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
     * 弹出查询设置弹出框
     */
    private void showSelectSetDialog() {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_select_set, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.select_set);
        //list of items
        builder.setView(linearLayout);
        final Button btnSelectBeginDate = (Button) linearLayout.findViewById(R.id.btn_select_begin_date);
        final Button btnSelectEndDate = (Button) linearLayout.findViewById(R.id.btn_select_end_date);
        final TextView tvSelectBeginDate = (TextView) linearLayout.findViewById(R.id.tv_select_begin_date);
        final TextView tvSelectEndDate = (TextView) linearLayout.findViewById(R.id.tv_select_end_date);
        tvSelectBeginDate.setText(eomsApplication.getSelectBeginDate());
        tvSelectEndDate.setText(eomsApplication.getSelectEndDate());
        btnSelectBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(tvSelectBeginDate);
            }
        });
        btnSelectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(tvSelectEndDate);
            }
        });
        //确定按钮
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        String beginDate = tvSelectBeginDate.getText().toString();
                        String EndDate = tvSelectEndDate.getText().toString();
                        eomsApplication.setSelectBeginDate(beginDate);
                        eomsApplication.setSelectEndDate(EndDate);
                        taskManagerFragment.refreshData();
                    }
                });
        //清空按钮
        String clearText = getString(R.string.clear);
        builder.setNeutralButton(clearText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
        //重写“BUTTON_NEUTRAL”，截取监听
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSelectBeginDate.setText("");
                tvSelectEndDate.setText("");
                Toast.makeText(MainActivity.this, "已清空！", Toast.LENGTH_LONG).show();
            }
        });
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
        DatePickerDialog dd = new DatePickerDialog(MainActivity.this,
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

 /*   *//**
     * 抽屉栏点击后退键方法
     *//*
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    /**
     * 加载菜单按钮方法
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 菜单按钮点击方法
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.search_settings) {
            Intent mainActivityIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(mainActivityIntent, 1);


            return true;
        } else if (id == R.id.scan_settings) {
            Intent mainActivityIntent = new Intent(MainActivity.this, MipcaActivityCapture.class);
            startActivityForResult(mainActivityIntent, 2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 手机退出键监听方法
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                // 创建退出对话框
                AlertDialog isExit = new AlertDialog.Builder(this).
                        setTitle("提示").setMessage("确定要退出应用吗？").
                        //确定按钮方法
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).
                        //取消按钮方法
                                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();

                // 显示对话框
                isExit.show();
            }
        }
        return false;
    }


    /**
     * PagerAdapter
     */
    public class PagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> mFragments;

        public PagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mListTitle.get(position);//页卡标题
        }
    }

    //用receiver来接受数据
    public class ChangePasswordResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);

            Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();


        }
    }

    //用receiver来接受数据
    public class UpdateAppResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context v, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = intent.getBooleanExtra(RESULT_FLAG, false);
            String info = intent.getStringExtra(RESULT_INFO);
            String data = bundle.getString(RESULT_EXTRA_DATA);
            //如果查到数据验证正确
            if (result) {
                List<SystemInfoModel> modelListTemp = gson.fromJson(data, new TypeToken<List<SystemInfoModel>>() {
                }.getType());
                if(modelListTemp != null && modelListTemp.size()>0){
                    String nowVersion = v.getString(R.string.app_version);
                    String updateVersion = modelListTemp.get(0).getSysVersion();
                    String updateUrl = modelListTemp.get(0).getSysUrl();
                    String updateInfo = modelListTemp.get(0).getSysMemo();
                    showUpdateSetDialog(nowVersion,updateVersion,updateUrl,updateInfo);
                }
            } else {//如果失败
                if(isUpdateFlag){
                    Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
                }
            };
            isUpdateFlag = false;
        }
    }

}
