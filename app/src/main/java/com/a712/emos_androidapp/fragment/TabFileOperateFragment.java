package com.a712.emos_androidapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Message;
import android.os.Handler;

import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.receiveModel.BaseModel;
import com.a712.emos_androidapp.receiveModel.FileModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.a712.emos_androidapp.utils.FunctionUtil;
import com.a712.emos_androidapp.utils.MyOkhttp;
import com.a712.emos_androidapp.utils.UploadUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_ACCESS_MODIFY;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_ID;
import static com.a712.emos_androidapp.utils.PublicStringTool.BUNDLE_FT_TYPE;
import static com.a712.emos_androidapp.utils.PublicStringTool.INTENT_DELETE_UPLOAD_FILE;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_FLAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.RESULT_INFO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabFileOperateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabFileOperateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFileOperateFragment extends Fragment implements UploadUtil.OnUploadProcessListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private View view;
    private static String ftId;
    private static String ftType;
    private static boolean accessModify;
    private static RecyclerView recyclerview;
    private GridLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton btnFileOperateSubmit;
    //    private int lastVisibleItem;
    private int page = 1;
    private ItemTouchHelper itemTouchHelper;
    private List<FileModel> fileModels;
    private FileGridAdapter mAdapter;
    private String fileUrl;
    private String requestURL;
    private ProgressDialog progressDialog;
    /**
     * 获取到的图片路径
     */
    private String picPath;
    private Intent lastIntent;
    private Uri photoUri;
    //使用照相机拍照获取图片
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    //使用相册中的图片
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    //从Intent获取图片路径的KEY
    public static final String KEY_PHOTO_PATH = "photo_path";
    private static final String TAG = "SelectPicActivity";
    //去上传文件
    protected static final int TO_UPLOAD_FILE = 1;
    //上传文件响应
    protected static final int UPLOAD_FILE_DONE = 2;  //
    //选择文件
    public static final int TO_SELECT_PHOTO = 3;
    //上传初始化
    private static final int UPLOAD_INIT_PROCESS = 4;
    //上传中
    private static final int UPLOAD_IN_PROCESS = 5;

    // 声明broadcastreceiver
    private IntentFilter mHandleDeleteUploadFileIntentFilter;
    private HandleDeleteUploadFileReceiver mHandleDeleteUploadFileReceiver;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TabFileOperateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFileOperateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFileOperateFragment newInstance(String param1, String param2) {
        TabFileOperateFragment fragment = new TabFileOperateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
            view = inflater.inflate(R.layout.fragment_tab_file_operate, container, false);
            Bundle bundle = this.getActivity().getIntent().getExtras();
            ftId = bundle.getString(BUNDLE_FT_ID);
            ftType = bundle.getString(BUNDLE_FT_TYPE);
            accessModify = bundle.getBoolean(BUNDLE_ACCESS_MODIFY);
            if(FunctionUtil.isFault(ftType)){
                fileUrl = QueryDataIntentService.getFaultFileUrl(getContext(), ftId);
            }else{
                fileUrl = QueryDataIntentService.getTaskFileUrl(getContext(), ftId);
            }
            requestURL = QueryDataIntentService.getImgUploadUrl(getContext());
            //broadcaster接收过滤器
            mHandleDeleteUploadFileIntentFilter = new IntentFilter(INTENT_DELETE_UPLOAD_FILE);
            mHandleDeleteUploadFileReceiver = new HandleDeleteUploadFileReceiver();
            initView();//初始化布局
            setListener();//设置监听事件
            //执行加载数据
            new GetData().execute(fileUrl);
        }
        return view;
    }

    private void initView() {
        recyclerview = (RecyclerView) view.findViewById(R.id.grid_recycler);
        mLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);//设置为一个3列的纵向网格布局
        recyclerview.setLayoutManager(mLayoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.grid_swipe_refresh);
        //调整SwipeRefreshLayout的位置
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        btnFileOperateSubmit = (FloatingActionButton) view.findViewById(R.id.btn_file_operate_submit);
        btnFileOperateSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPhoto();
            }
        });
        if(accessModify){
            btnFileOperateSubmit.setVisibility(View.VISIBLE);
        }else{
            btnFileOperateSubmit.setVisibility(View.GONE);
        }
        progressDialog = new ProgressDialog(getContext());
        lastIntent = getActivity().getIntent();
        //初始化recyclerview布局，否则报警
        fileModels = new ArrayList<>();
        FileModel pages = new FileModel();
        pages.setPage(page);
        fileModels.add(pages);
        recyclerview.setAdapter(new FileGridAdapter(getActivity(),fileModels));
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new GetData().execute(fileUrl);
            }
        });

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = 0;
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager || recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                FileModel moveItem = fileModels.get(from);
                fileModels.remove(from);
                fileModels.add(to, moveItem);
                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        });

//        //recyclerview滚动监听
//        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
//                // 滑动状态停止并且剩余少于两个item时，自动加载下一页
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 2 >= mLayoutManager.getItemCount()) {
//                    new GetData().execute(fileUrl);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //获取加载的最后一个可见视图在适配器的位置。
//                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//            }
//        });
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
        getActivity().registerReceiver(mHandleDeleteUploadFileReceiver, mHandleDeleteUploadFileIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mHandleDeleteUploadFileReceiver);
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

    private class GetData extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //设置swipeRefreshLayout为刷新状态
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return MyOkhttp.get(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                Gson gson = new Gson();
                if (fileModels == null || fileModels.size() == 0) {
                    fileModels = gson.fromJson(result, new TypeToken<List<FileModel>>() {
                    }.getType());
                    FileModel pages = new FileModel();
                    pages.setPage(page);
                    fileModels.add(pages);
                } else {
                    fileModels.clear();
                    List<FileModel> more = gson.fromJson(result, new TypeToken<List<FileModel>>() {
                    }.getType());
                    fileModels.addAll(more);
                    FileModel pages = new FileModel();
                    pages.setPage(page);
                    fileModels.add(pages);
                }
                if (mAdapter == null) {
                    recyclerview.setAdapter(mAdapter = new FileGridAdapter(getActivity(), fileModels));
                    mAdapter.setOnItemClickListener(new FileGridAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            int position = recyclerview.getChildAdapterPosition(view);
//                            SnackbarUtil.ShortSnackbar(coordinatorLayout, "点击第" + position + "个", SnackbarUtil.Info).show();
                        }

                        @Override
                        public void onItemLongClick(View view) {
                            itemTouchHelper.startDrag(recyclerview.getChildViewHolder(view));
                            int position = recyclerview.getChildAdapterPosition(view);
                            final String fileId = fileModels.get(position).getFileId();
                            // 创建退出对话框
                            AlertDialog dialogDelete = new AlertDialog.Builder(getActivity()).
                                    setTitle("提示").setMessage("确定要删除此图片吗？").
                                    setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            QueryDataIntentService.startDeleteUploadFile(getContext(),fileId,ftType);
                                        }
                                    }).
                                    setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create();

                            // 显示对话框
                            dialogDelete.show();
                        }
                    });
                    itemTouchHelper.attachToRecyclerView(recyclerview);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
            //停止swipeRefreshLayout加载动画
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            /**-----------------*/
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(getContext(), "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            doPhoto(requestCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO)  //从相册取图片，有些手机有异常情况，请注意
        {
            if (data == null) {
                Toast.makeText(getContext(), "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(getContext(), "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
        } else {
            if (photoUri != null) {
                picPath = photoUri.getPath();
            }
        }
        Log.i(TAG, "imagePath = " + picPath);
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
            handler.sendEmptyMessage(TO_UPLOAD_FILE);
        } else {
            Toast.makeText(getContext(), "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 上传服务器响应回调
     */
    @Override
    public void onUploadDone(int responseCode, String message) {
        progressDialog.dismiss();
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    private void toUploadFile() {
        Snackbar.make(getView(), "正在上传中...", Snackbar.LENGTH_LONG).show();
        progressDialog.setMessage("正在上传文件...");
        progressDialog.show();
        String fileKey = "img";
        UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态

        Map<String, String> params = new HashMap<String, String>();
        params.put("tfId", ftId);
        params.put("tfType", ftType);
        params.put("userCode", QueryDataIntentService.getStaticUserCode(getContext()));
        uploadUtil.uploadFile(picPath, fileKey, requestURL, params);
    }

    @Override
    public void onUploadProcess(int uploadSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        handler.sendMessage(msg);
    }

    @Override
    public void initUpload(int fileSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:
                    toUploadFile();
                    break;
                case UPLOAD_INIT_PROCESS:
                    break;
                case UPLOAD_IN_PROCESS:
                    break;
                case UPLOAD_FILE_DONE:
                    if (msg.arg1 == UploadUtil.UPLOAD_SUCCESS_CODE && msg.obj != null) {
                        String returnString = "";
                        try {
                            Gson gson = new Gson();
                            BaseModel baseModel = gson.fromJson(msg.obj.toString(), new TypeToken<BaseModel>() {
                            }.getType());
                            if (baseModel != null && baseModel.getFlag()) {
                                returnString = baseModel.getInfo();
                                new GetData().execute(fileUrl);
                            }
                        } catch (Exception e) {
                        }
                        Toast.makeText(getContext(), "上传结果：" + returnString + "\n耗时：" + UploadUtil.getRequestTime() + "秒", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "上传结果：" + msg.obj + "\n耗时：" + UploadUtil.getRequestTime() + "秒", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * receiver来接收数据--删除已上传文件
     */
    public class HandleDeleteUploadFileReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = bundle.getBoolean(RESULT_FLAG, false);
            String info = bundle.getString(RESULT_INFO);
            //如果查到数据验证正确
            if (result) {
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
                new GetData().execute(fileUrl);
            } else {//如果失败
                Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
