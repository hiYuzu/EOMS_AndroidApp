package com.a712.emos_androidapp;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.a712.emos_androidapp.fragment.DeviceListFragment;
import com.a712.emos_androidapp.fragment.TaskManagerFragment;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.a712.emos_androidapp.MainActivity.ResultCodeSearch;
import static com.a712.emos_androidapp.utils.PublicStringTool.ACTION_SEARCH_NUMBER;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_BUNDLE_SEARCH_CODE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_DEVICE_NUMBER_RESULT;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_EXTRA_DATA;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;

/**
 * Created by Administrator on 2017/3/23.
 */

public class SearchActivity extends MyEomsActivity {

    private SearchView mSearchView;
    private ImageButton mBackButton;
    private Button mSearchButton;

    private SearchAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private String queryString;//显示在搜索框上的内容
    private List<String> stringList;//存储传回的列表

    // 声明broadcastreceiver
    private IntentFilter mSearchIntentFilter;
    private SearchCodeReceiver mSearchReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        stringList = new ArrayList<>();
        //返回按钮功能
        mBackButton = (ImageButton) findViewById(R.id.image_button_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //搜索按钮功能
        mSearchButton = (Button) findViewById(R.id.btn_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryString = mSearchView.getQuery().toString();
                searchStringMethod(queryString);
            }
        });

        //初始化searchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo =
                searchManager.getSearchableInfo(getComponentName());
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setSearchableInfo(searchableInfo);
        mSearchView.setIconifiedByDefault(false);//设置是否显示缩小图标
        mSearchView.setSubmitButtonEnabled(false);//设置显示搜索按钮
        mSearchView.setQueryHint("请输入设备序列号");//设置暗示字符串
        // mSearchView.setQuery(queryName, false);//设置搜索框里是否有默认值，和是否直接提交查询
        mSearchView.onActionViewExpanded();
        mSearchView.setFocusable(true);//聚焦

        //搜索框事件监听器
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private final String TAG = getClass().getSimpleName();
            //点击键盘中的search按钮时触发
            @Override
            public boolean onQueryTextSubmit(String s) {
                queryString = mSearchView.getQuery().toString();
                searchStringMethod(queryString);
                return true;
            }
            //当输入字符时触发
            @Override
            public boolean onQueryTextChange(String s) {
                /*if (s.isEmpty()) {
                   // mAdapter.clear();//清空所有数据
                    return true;
                }*/
                stringList.clear();
                //进行数据库查询
                QueryDataIntentService.startSearchDeviceNumber(SearchActivity.this, s);
                return true;
            }
        });

        //broadcaster接收过滤器
        mSearchIntentFilter = new IntentFilter(INTENT_DEVICE_NUMBER_RESULT);
        mSearchReceiver = new SearchCodeReceiver();

        //初始化recyclerView和adapter
        mRecyclerView = (RecyclerView)findViewById(R.id.search_recyclerView);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);

        //创建并设置Adapter
        mAdapter = new SearchAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * @Description: 搜索按钮点击方法
     * @auther renchongbin
     * created at 2016/7/11 8:45
     */
    private void searchStringMethod(String queryText) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_BUNDLE_SEARCH_CODE, queryText);
        setResult(ResultCodeSearch, intent);
        finish();

    }
    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(mSearchReceiver, mSearchIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(mSearchReceiver);
    }
    /**
     * receiver来接收数据
     */
    public class SearchCodeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = intent.getBooleanExtra(RESULT_FLAG, false);
            String info = intent.getStringExtra(RESULT_INFO);
            String data = bundle.getString(RESULT_EXTRA_DATA);
            //如果查到数据验证正确
            if (result) {
                List<String> stringListReceiver = gson.fromJson(data, new TypeToken<List<String>>() {
                }.getType());
                stringList.addAll(stringListReceiver);
            } else {//如果失败
                Toast.makeText(SearchActivity.this, info, Toast.LENGTH_SHORT).show();
            }
            mAdapter.notifyDataSetChanged();//要通知adapter进行更新
        }
    }
    /**
     * adapter
     */
    class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context mContext;
        public SearchAdapter(Context context) {
            this.mContext = context;
        }

        /**
         * 初始化布局
         * @param viewGroup
         * @param viewType
         * @return
         */
        //创建新View，加载item布局，被LayoutManager所调用
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview_search, viewGroup, false);
            SearchViewHolder vh = new SearchViewHolder(view);
            return vh;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            final String stringItem = stringList.get(position);//得到字符串
            SearchViewHolder searchViewHolder = (SearchViewHolder) viewHolder;
            searchViewHolder.mTextView.setText(stringItem);
            //设置点击图标将字符串添加到搜索框
            searchViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchView.setQuery(stringItem,false);

                }
            });
            //item点击事件
            searchViewHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchStringMethod(stringItem);
                }
            });
        }
        //获取数据的数量
        @Override
        public int getItemCount() {
            return stringList.size();
        }
    }
    /**
     * 内部类ViewHolder，必须继承viewHolder类来承载视图
     */
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    //好处，对每个控件中的重复部分只加载一次，避免重复加载
    public  static class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageView mImageView;
        private RelativeLayout mRelativeLayout;

        public SearchViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_item);
            mImageView = (ImageView) view.findViewById(R.id.search_img_button);
            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.layout_search_list_item);
        }
    }

}
