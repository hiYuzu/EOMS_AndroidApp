package com.a712.emos_androidapp.DeviceAddActivities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.a712.emos_androidapp.MyEomsActivity;
import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.fragment.TabFaultBaseFragment;
import com.a712.emos_androidapp.fragment.TabFileOperateFragment;
import com.a712.emos_androidapp.fragment.TabPartDeviceFragment;
import com.a712.emos_androidapp.receiveModel.DeviceCheckedModel;

import java.util.ArrayList;
import java.util.List;

import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_ACCESS_MODIFY;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_COMPANY_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DEVICE_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_DEVICE_NAME;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_TYPE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_OUTLET_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_OUTLET_NAME;

public class ProcessFaultActivity extends MyEomsActivity
        implements TabPartDeviceFragment.OnListFragmentInteractionListener
        ,TabFaultBaseFragment.OnFragmentInteractionListener,TabFileOperateFragment.OnFragmentInteractionListener {

    private static String ftType;
    ViewPager dViewPager = null;
    TabLayout dTabLayout;
    ArrayList<String> titleContainer;
    TabFaultBaseFragment tabFaultBaseFragment;
    TabFileOperateFragment tabFileOperateFragment;
    TabPartDeviceFragment tabFaultDeviceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收参数
        savedInstanceState = this.getIntent().getExtras();
        ftType = savedInstanceState.getString(BUNDLE_FT_TYPE);
        if(ftType == null || ftType.isEmpty()){
            Toast.makeText(getApplicationContext(),"未获得传递参数，请刷新后重试",Toast.LENGTH_LONG);
            finish();
        }
        setContentView(R.layout.activity_process_fault);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle(getString(R.string.main_fragment_content1));
        setSupportActionBar(toolbar);
        //tool bar设置
        toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上一个界面并关闭此界面
                onBackPressed();
            }
        });
        initViewPage(savedInstanceState);
        final String showDetail = "【"+ savedInstanceState.getString(BUNDLE_COMPANY_NAME)
                            +"】,【"+ savedInstanceState.getString(BUNDLE_OUTLET_NAME)
                            +"】,【"+savedInstanceState.getString(BUNDLE_DEVICE_NAME)+"】";
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, showDetail, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    protected void initViewPage(Bundle bundle) {
        dViewPager = (ViewPager) this.findViewById(R.id.deal_viewpager);
        dTabLayout = (TabLayout) findViewById(R.id.deal_tablayout);
        //页签项
        titleContainer = new ArrayList<>();
        titleContainer.add("基本信息");
        titleContainer.add("更换部件");
        titleContainer.add("上传图片");
        dTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        //把tabLayout和viewpager绑定
        dTabLayout.setupWithViewPager(dViewPager);

        List<Fragment> listFragment = new ArrayList<>();
        tabFaultBaseFragment = new TabFaultBaseFragment();
        Bundle bundleBase = new Bundle();
        bundleBase.putInt(BUNDLE_DEVICE_ID, bundle.getInt(BUNDLE_DEVICE_ID));
        bundleBase.putString(BUNDLE_FT_ID, bundle.getString(BUNDLE_FT_ID));
        bundleBase.putString(BUNDLE_FT_TYPE,bundle.getString(BUNDLE_FT_TYPE));
        bundleBase.putString(BUNDLE_OUTLET_CODE,bundle.getString(BUNDLE_OUTLET_CODE));
        bundleBase.putBoolean(BUNDLE_ACCESS_MODIFY,bundle.getBoolean(BUNDLE_ACCESS_MODIFY));
        tabFaultBaseFragment.setArguments(bundleBase);
        listFragment.add(tabFaultBaseFragment);
        tabFaultDeviceFragment = new TabPartDeviceFragment().newInstance(1);
        Bundle bundleDevice = new Bundle();
        bundleDevice.putInt(BUNDLE_DEVICE_ID, bundle.getInt(BUNDLE_DEVICE_ID));
        bundleDevice.putString(BUNDLE_FT_ID, bundle.getString(BUNDLE_FT_ID));
        bundleDevice.putString(BUNDLE_FT_TYPE,bundle.getString(BUNDLE_FT_TYPE));
        bundleDevice.putBoolean(BUNDLE_ACCESS_MODIFY,bundle.getBoolean(BUNDLE_ACCESS_MODIFY));
        tabFaultDeviceFragment.setArguments(bundleDevice);
        listFragment.add(tabFaultDeviceFragment);
        tabFileOperateFragment = new TabFileOperateFragment();
        Bundle bundleFile = new Bundle();
        bundleFile.putInt(BUNDLE_DEVICE_ID, bundle.getInt(BUNDLE_DEVICE_ID));
        bundleFile.putString(BUNDLE_FT_ID, bundle.getString(BUNDLE_FT_ID));
        bundleFile.putString(BUNDLE_FT_TYPE,bundle.getString(BUNDLE_FT_TYPE));
        bundleFile.putBoolean(BUNDLE_ACCESS_MODIFY,bundle.getBoolean(BUNDLE_ACCESS_MODIFY));
        tabFileOperateFragment.setArguments(bundleFile);
        listFragment.add(tabFileOperateFragment);
        TabFragmentPagerAdapter tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), listFragment);
        dViewPager.setAdapter(tabFragmentPagerAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DeviceCheckedModel item) {

    }

    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
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
            return titleContainer.get(position);//页卡标题
        }
    }

}
