package com.a712.emos_androidapp.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.a712.emos_androidapp.R;
import com.a712.emos_androidapp.receiveModel.ResultModel;

import com.a712.emos_androidapp.receiveModel.TaskInspectModel;
import com.a712.emos_androidapp.sendParams.DeviceParams;
import com.a712.emos_androidapp.sendParams.FaultParams;
import com.a712.emos_androidapp.sendParams.DeleteFileParams;
import com.a712.emos_androidapp.sendParams.LoginParams;
import com.a712.emos_androidapp.sendParams.SystemInfoParams;
import com.a712.emos_androidapp.sendParams.TaskInspectParams;
import com.a712.emos_androidapp.sendParams.TaskParams;
import com.a712.emos_androidapp.utils.SHAUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import static android.content.ContentValues.TAG;
import static com.a712.emos_androidapp.utils.PublicStringTool.*;


/**
 * Created by Administrator on 2017/3/13.
 */

public class QueryDataIntentService extends IntentService {
    private static String staticUserCode;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param
     */
    public QueryDataIntentService() {
        super("QueryDataIntentService");
    }

    public QueryDataIntentService(String name) {
        super(name);
    }

    /**
     * 从preference中获取用户名
     *
     * @return
     */
    public String getStaticUserCode() {
        Resources resources = getResources();
        SharedPreferences pref = this.getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
        staticUserCode = pref.getString(resources.getString(R.string.preference_username), null);
        return staticUserCode;
    }

    /**
     * 从preference中获取用户名
     *
     * @return
     */
    public static String getStaticUserCode(Context context) {
        Resources resources = context.getResources();
        SharedPreferences pref = context.getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
        staticUserCode = pref.getString(resources.getString(R.string.preference_username), null);
        return staticUserCode;
    }

    /**
     * 登录方法
     *
     * @param context
     * @param username 用户名
     * @param password 密码
     */
    public static void startActionLogin(Context context, String username, String password) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        try {
            username = URLEncoder.encode(username, "utf-8");
            password = URLEncoder.encode(password, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        intent.setAction(ACTION_LOGIN);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startService(intent);
    }

    /**
     * 登录方法
     *
     * @param context
     * @param sysCode 系统编码
     * @param sysNum  系统
     */
    public static void startActionUpdate(Context context, String sysCode, String sysNum) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_UPDATE_APP);
        intent.putExtra(EXTRA_SYS_CODE, sysCode);
        intent.putExtra(EXTRA_SYS_NUM, sysNum);
        context.startService(intent);
    }

    /**
     * 修改密码方法
     *
     * @param context
     * @param originalPassword
     * @param newPassword
     */
    public static void startChangePassword(Context context, String originalPassword, String newPassword) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_CHANGE_PASSWORD);
        intent.putExtra(EXTRA_PASSWORD, originalPassword);
        intent.putExtra(EXTRA_NEW_PASSWORD, newPassword);
        context.startService(intent);
    }

    /**
     * 搜索设备号码
     *
     * @param context
     * @param text
     */
    public static void startSearchDeviceNumber(Context context, String text) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        try {
            text = URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        intent.setAction(ACTION_SEARCH_NUMBER);
        intent.putExtra(EXTRA_DEVICE_NUMBER, text);
        context.startService(intent);
    }

    /**
     * 查询设备列表
     *
     * @param context
     * @param queryStart
     */
    public static void startQueryDeviceList(Context context, String qrCode, int queryStart) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        try {
            qrCode = URLEncoder.encode(qrCode, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        intent.setAction(ACTION_QUERY_DEVICE);
        intent.putExtra(EXTRA_DEVICE_NUMBER, qrCode);
        intent.putExtra(QUERY_START, queryStart);
        context.startService(intent);
    }

    /**
     * 查询任务数量
     *
     * @param context
     * @param userFlag
     */
    public static void startQueryTaskCount(Context context, int userFlag, String beginDate, String endDate) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_SEARCH_COUNT);
        intent.putExtra(EXTRA_USER_FLAG, userFlag);
        intent.putExtra(EXTRA_BEGIN_DATE, beginDate);
        intent.putExtra(EXTRA_END_DATE, endDate);
        context.startService(intent);
    }

    /**
     * 查询故障
     *
     * @param context
     * @param faultId
     */
    public static void startQueryFaultKey(Context context, String faultId) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_FAULT_KEY);
        intent.putExtra(EXTRA_FT_ID, faultId);
        context.startService(intent);
    }

    /**
     * 查询故障更换部件
     *
     * @param context
     * @param faultId
     */
    public static void startQueryFaultDeviceChecked(Context context, String faultId) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_FAULT_DEVICE_CHECKED);
        intent.putExtra(EXTRA_FT_ID, faultId);
        context.startService(intent);
    }

    /**
     * 更新故障更换部件
     *
     * @param context
     * @param faultId
     * @param list
     * @param updateTime
     */
    public static void startUpdateFaultDeviceChecked(Context context, String faultId, String[] list, String updateTime) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_UPDATE_FAULT_DEVICE_CHECKED);
        intent.putExtra(EXTRA_FT_ID, faultId);
        intent.putExtra(EXTRA_FT_LIST, list);
        intent.putExtra(EXTRA_UPDATE_TIME, updateTime);
        context.startService(intent);
    }

    /**
     * 查询故障列表
     *
     * @param context
     */
    public static void startQueryFaultList(Context context, int userFlag, int queryStart, String beginDate, String endDate) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_FAULT);
        intent.putExtra(EXTRA_USER_FLAG, userFlag);
        intent.putExtra(QUERY_START, queryStart);
        intent.putExtra(EXTRA_BEGIN_DATE, beginDate);
        intent.putExtra(EXTRA_END_DATE, endDate);
        context.startService(intent);
    }

    /**
     * 查询已完成故障列表
     *
     * @param context
     */
    public static void startFinishedQueryFaultList(Context context, int outletDeviceId, String ftType, int queryStart, String beginDate, String endDate) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_FINISHED_FAULT);
        intent.putExtra(EXTRA_OUTLET_DEVICE_ID, outletDeviceId);
        intent.putExtra(EXTRA_FT_TYPE, ftType);
        intent.putExtra(QUERY_START, queryStart);
        intent.putExtra(EXTRA_BEGIN_DATE, beginDate);
        intent.putExtra(EXTRA_END_DATE, endDate);
        context.startService(intent);
    }

    /**
     * 更新任务巡检项
     *
     * @param context
     * @param taskId
     */
    public static void startQueryTaskInspect(Context context, String taskId) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_TASK_INSPECT);
        intent.putExtra(EXTRA_FT_ID, taskId);
        context.startService(intent);
    }

    /**
     * 更新任务更换部件
     *
     * @param context
     * @param taskId
     * @param list
     * @param updateTime
     */
    public static void startUpdateTaskDeviceChecked(Context context, String taskId, String[] list, String updateTime) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_UPDATE_TASK_DEVICE_CHECKED);
        intent.putExtra(EXTRA_FT_ID, taskId);
        intent.putExtra(EXTRA_FT_LIST, list);
        intent.putExtra(EXTRA_UPDATE_TIME, updateTime);
        context.startService(intent);
    }

    /**
     * 查询任务更换部件
     *
     * @param context
     * @param taskId
     */
    public static void startQueryTaskDeviceChecked(Context context, String taskId) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_TASK_DEVICE_CHECKED);
        intent.putExtra(EXTRA_FT_ID, taskId);
        context.startService(intent);
    }

    /**
     * 查询任务列表
     *
     * @param context
     * @param taskId
     */
    public static void startQueryTaskKey(Context context, String taskId) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_TASK_KEY);
        intent.putExtra(EXTRA_FT_ID, taskId);
        context.startService(intent);
    }

    /**
     * 查询任务列表
     *
     * @param context
     * @param taskType
     * @param userFlag
     * @param queryStart
     */
    public static void startQueryTaskList(Context context, String taskType, int userFlag, int queryStart, String beginDate, String endDate) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_TASK);
        intent.putExtra(EXTRA_TASK_TYPE, taskType);
        intent.putExtra(EXTRA_USER_FLAG, userFlag);
        intent.putExtra(QUERY_START, queryStart);
        intent.putExtra(EXTRA_BEGIN_DATE, beginDate);
        intent.putExtra(EXTRA_END_DATE, endDate);
        context.startService(intent);
    }

    /**
     * 查询已完成任务列表
     *
     * @param context
     */
    public static void startQueryFinishedTaskList(Context context, int outletDeviceId, String ftType, int queryStart, String beginDate, String endDate) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_FINISHED_TASK);
        intent.putExtra(EXTRA_OUTLET_DEVICE_ID, outletDeviceId);
        intent.putExtra(EXTRA_FT_TYPE, ftType);
        intent.putExtra(QUERY_START, queryStart);
        intent.putExtra(EXTRA_BEGIN_DATE, beginDate);
        intent.putExtra(EXTRA_END_DATE, endDate);
        context.startService(intent);
    }

    /**
     * 查询人员任务列表
     *
     * @param context
     * @param outletCode
     * @param userFlag
     */
    public static void startQueryWorkList(Context context, String outletCode, int userFlag) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_WORK_LIST);
        intent.putExtra(EXTRA_OUTLECT_CODE, outletCode);
        intent.putExtra(EXTRA_USER_FLAG, userFlag);
        context.startService(intent);
    }

    public static void startInsertFault(Context context, Bundle bundle) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_INSERT_FAULT);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startUpdateFault(Context context, Bundle bundle) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_UPDATE_FAULT);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startInsertTask(Context context, Bundle bundle) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_INSERT_TASK);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startUpdateTask(Context context, Bundle bundle) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_UPDATE_TASK);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startUpdateTaskInspect(Context context, String stringInspect) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_UPDATE_TASK_INSPECT);
        intent.putExtra(EXTRA_INSPECT_LIST, stringInspect);
        context.startService(intent);
    }

    public static void startQueryDeviceDetail(Context context, int outletDeviceId) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_QUERY_DEVICE_DETAIL);
        intent.putExtra(EXTRA_DEVICE_ID, outletDeviceId);
        context.startService(intent);
    }

    public static String getFaultFileUrl(Context context, String faultId) {
        String servlet = "PhoneFaultController";
        String method = "queryFile";
        String param = "faultId=" + faultId;
        return getFileUrl(context, servlet, method, param);
    }

    public static String getTaskFileUrl(Context context, String taskId) {
        String servlet = "PhoneTaskController";
        String method = "queryFile";
        String param = "taskId=" + taskId;
        return getFileUrl(context, servlet, method, param);
    }

    public static String getImgUploadUrl(Context context) {
        String servlet = "PhoneFileOperateController";
        String method = "uploadImg";
        return getFileUrl(context, servlet, method, null);
    }

    /**
     * 删除已上传的文件
     *
     * @param context
     * @param fileId
     * @param tfType
     */
    public static void startDeleteUploadFile(Context context, String fileId, String tfType) {
        Intent intent = new Intent(context, QueryDataIntentService.class);
        intent.setAction(ACTION_DELETE_UPLOAD_FILE);
        intent.putExtra(EXTRA_FILE_ID, fileId);
        intent.putExtra(EXTRA_FT_TYPE, tfType);
        context.startService(intent);
    }

    /**
     * startService时启动此方法
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            //处理登录信息
            if (ACTION_LOGIN.equals(action)) {
                final String username = intent.getStringExtra(EXTRA_USERNAME);
                final String password = intent.getStringExtra(EXTRA_PASSWORD);
                try {
                    handleActionLogin(username, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (ACTION_UPDATE_APP.equals(action)) {
                final String sysCode = intent.getStringExtra(EXTRA_SYS_CODE);
                final String sysNum = intent.getStringExtra(EXTRA_SYS_NUM);
                try {
                    handleActionUpdateApp(sysCode, sysNum);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (ACTION_CHANGE_PASSWORD.equals(action)) {
                final String originalPassword = intent.getStringExtra(EXTRA_PASSWORD);
                final String newPassword = intent.getStringExtra(EXTRA_NEW_PASSWORD);
                try {
                    handleChangePassword(originalPassword, newPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_SEARCH_NUMBER.equals(action)) {
                final String deviceNumber = intent.getStringExtra(EXTRA_DEVICE_NUMBER);
                try {
                    handleSearchDeviceNumber(deviceNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_QUERY_DEVICE.equals(action)) {
                final String deviceNumber = intent.getStringExtra(EXTRA_DEVICE_NUMBER);
                final int queryStart = intent.getIntExtra(QUERY_START, 0);
                try {
                    handleQueryDeviceList(deviceNumber, queryStart);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_SEARCH_COUNT.equals(action)) {
                final int userFlag = intent.getIntExtra(EXTRA_USER_FLAG, 0);
                final String beginDate = intent.getStringExtra(EXTRA_BEGIN_DATE);
                final String endDate = intent.getStringExtra(EXTRA_END_DATE);
                try {
                    handleQueryTaskCount(userFlag, beginDate, endDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_QUERY_FAULT_DEVICE_CHECKED.equals(action)) {
                final String faultId = intent.getStringExtra(EXTRA_FT_ID);
                try {
                    handleQueryFaultDeviceChecked(faultId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_UPDATE_FAULT_DEVICE_CHECKED.equals(action)) {
                final String faultId = intent.getStringExtra(EXTRA_FT_ID);
                final String[] list = intent.getStringArrayExtra(EXTRA_FT_LIST);
                final String updateTime = intent.getStringExtra(EXTRA_UPDATE_TIME);
                try {
                    handleUpdateFaultDeviceChecked(faultId, list, updateTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_QUERY_FAULT_KEY.equals(action)) {
                final String faultId = intent.getStringExtra(EXTRA_FT_ID);
                try {
                    handleQueryFaultKey(faultId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_QUERY_FAULT.equals(action)) {
                final int userFlag = intent.getIntExtra(EXTRA_USER_FLAG, 0);
                final int queryStart = intent.getIntExtra(QUERY_START, 0);
                final String beginDate = intent.getStringExtra(EXTRA_BEGIN_DATE);
                final String endDate = intent.getStringExtra(EXTRA_END_DATE);
                try {
                    handleQueryFaultList(userFlag, queryStart, beginDate, endDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (ACTION_QUERY_FINISHED_FAULT.equals(action)) {
                final int outletDeviceId = intent.getIntExtra(EXTRA_OUTLET_DEVICE_ID, 0);
                final String ftType = intent.getStringExtra(EXTRA_FT_TYPE);
                final int queryStart = intent.getIntExtra(QUERY_START, 0);
                final String beginDate = intent.getStringExtra(EXTRA_BEGIN_DATE);
                final String endDate = intent.getStringExtra(EXTRA_END_DATE);
                try {
                    handleQueryFinishedFaultList(outletDeviceId, ftType,queryStart, beginDate, endDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_QUERY_TASK_INSPECT.equals(action)) {
                final String taskId = intent.getStringExtra(EXTRA_FT_ID);
                try {
                    handleQueryTaskInspect(taskId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_UPDATE_TASK_DEVICE_CHECKED.equals(action)) {
                final String taskId = intent.getStringExtra(EXTRA_FT_ID);
                final String[] list = intent.getStringArrayExtra(EXTRA_FT_LIST);
                final String updateTime = intent.getStringExtra(EXTRA_UPDATE_TIME);
                try {
                    handleUpdateTaskDeviceChecked(taskId, list, updateTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_QUERY_TASK_DEVICE_CHECKED.equals(action)) {
                final String taskId = intent.getStringExtra(EXTRA_FT_ID);
                try {
                    handleQueryTaskDeviceChecked(taskId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_QUERY_TASK_KEY.equals(action)) {
                final String taskId = intent.getStringExtra(EXTRA_FT_ID);
                try {
                    handleQueryTaskKey(taskId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_QUERY_TASK.equals(action)) {
                final String taskType = intent.getStringExtra(EXTRA_TASK_TYPE);
                final int userFlag = intent.getIntExtra(EXTRA_USER_FLAG, 0);
                final int queryStart = intent.getIntExtra(QUERY_START, 0);
                final String beginDate = intent.getStringExtra(EXTRA_BEGIN_DATE);
                final String endDate = intent.getStringExtra(EXTRA_END_DATE);
                try {
                    handleQueryTaskList(taskType, userFlag, queryStart, beginDate, endDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (ACTION_QUERY_FINISHED_TASK.equals(action)) {
                final int outletDeviceId = intent.getIntExtra(EXTRA_OUTLET_DEVICE_ID, 0);
                final String ftType = intent.getStringExtra(EXTRA_FT_TYPE);
                final int queryStart = intent.getIntExtra(QUERY_START, 0);
                final String beginDate = intent.getStringExtra(EXTRA_BEGIN_DATE);
                final String endDate = intent.getStringExtra(EXTRA_END_DATE);
                try {
                    handleQueryFinishedTaskList(outletDeviceId, ftType, queryStart, beginDate, endDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_QUERY_WORK_LIST.equals(action)) {
                final int userFlag = intent.getIntExtra(EXTRA_USER_FLAG, 0);
                final String outletCode = intent.getStringExtra(EXTRA_OUTLECT_CODE);
                try {
                    handleQueryWorkerList(userFlag, outletCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_INSERT_FAULT.equals(action)) {
                final Bundle bundle = intent.getExtras();
                try {
                    handleInsertFault(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_UPDATE_FAULT.equals(action)) {
                final Bundle bundle = intent.getExtras();
                try {
                    handleUpdateFault(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_INSERT_TASK.equals(action)) {
                final Bundle bundle = intent.getExtras();
                try {
                    handleInsertTask(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_UPDATE_TASK.equals(action)) {
                final Bundle bundle = intent.getExtras();
                try {
                    handleUpdateTask(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_UPDATE_TASK_INSPECT.equals(action)) {
                final String stringInspect = intent.getStringExtra(EXTRA_INSPECT_LIST);
                try {
                    handleUpdateTaskInspect(stringInspect);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_QUERY_DEVICE_DETAIL.equals(action)) {
                final int id = intent.getIntExtra(EXTRA_DEVICE_ID, 0);
                try {
                    handleQueryDeviceDetail(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_DELETE_UPLOAD_FILE.equals(action)) {
                final Bundle bundle = intent.getExtras();
                try {
                    handleDeleteUploadFile(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理登录数据
     *
     * @param username
     * @param password
     * @throws Exception
     */
    private void handleActionLogin(String username, String password) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();

        loginParams.setUserCode(username);
        loginParams.setUserPassword(SHAUtil.shaEncode(password));//密码加密
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneLoginController", "validateLogin", jsonObject);//传入servlet，方法，数据

        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        Intent resultIntent = new Intent(INTENT_LOGIN_RESULT);
        Bundle bundle = new Bundle();
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);

    }

    /**
     * 处理登录数据
     *
     * @param sysCode
     * @param sysNum
     * @throws Exception
     */
    private void handleActionUpdateApp(String sysCode, String sysNum) throws Exception {
        //发送数据
        Gson gson = new Gson();
        SystemInfoParams systemInfoParams = new SystemInfoParams();

        systemInfoParams.setSysCode(sysCode);
        systemInfoParams.setSysNumber(sysNum);
        String jsonObject = gson.toJson(systemInfoParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneLoginController", "validateUpdate", jsonObject);//传入servlet，方法，数据

        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        Intent resultIntent = new Intent(INTENT_UPDATE_APP_RESULT);
        Bundle bundle = new Bundle();
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);

    }

    /**
     * 修改密码
     *
     * @param originalPassword
     * @param newPassword
     * @throws Exception
     */
    private void handleChangePassword(String originalPassword, String newPassword) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();

        loginParams.setUserCode(getStaticUserCode());
        loginParams.setUserPassword(originalPassword);//密码加密
        loginParams.setNewPassword(newPassword);

        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneLoginController", "changeUserPassword", jsonObject);//传入servlet，方法，数据

        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        Intent resultIntent = new Intent(INTENT_CHANGE_PASSWORD_RESULT);
        Bundle bundle = new Bundle();
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);

    }

    /**
     * 处理查询设备编码方法
     *
     * @param text
     * @throws Exception
     */
    private void handleSearchDeviceNumber(String text) throws Exception {
        //发送数据
        Gson gson = new Gson();
        Resources resources = getResources();
        SharedPreferences pref = this.getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
        String username = pref.getString(resources.getString(R.string.preference_username), null);

        DeviceParams deviceCode = new DeviceParams();
        deviceCode.setUserCode(username);
        deviceCode.setQrCode(text);

        String jsonObject = gson.toJson(deviceCode);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneDeviceController", "searchDeviceNumber", jsonObject);//传入servlet，方法，数据

        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        Intent resultIntent = new Intent(INTENT_DEVICE_NUMBER_RESULT);
        Bundle bundle = new Bundle();
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 查询设备列表
     *
     * @param qrCode
     * @param queryStart
     * @throws Exception
     */
    private void handleQueryDeviceList(String qrCode, int queryStart) throws Exception {
        //发送数据
        Gson gson = new Gson();
        DeviceParams deviceParams = new DeviceParams();
        deviceParams.setUserCode(getStaticUserCode());
        deviceParams.setQrCode(qrCode);
        deviceParams.setRowStart(queryStart);
        String jsonObject = gson.toJson(deviceParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneDeviceController", "queryDeviceList", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_DEVICE_LIST);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 查询未完成任务数量
     *
     * @throws Exception
     */
    private void handleQueryTaskCount(int userFlag, String beginDate, String endDate) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setUserFlag(userFlag);
        loginParams.setBeginDate(beginDate);
        loginParams.setEndDate(endDate);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneLoginController", "queryTaskCount", jsonObject);

        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_TASK_COUNT);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);

    }

    /**
     * 更新任务部件
     *
     * @param
     * @throws Exception
     */
    private void handleUpdateTaskDeviceChecked(String taskId, String[] list, String updateTime) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setFtId(taskId);
        loginParams.setList(list);
        loginParams.setUpdateTime(updateTime);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneTaskController", "updateTaskDeviceChecked", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_UPDATE_TASK_DEVICE_CHECKED);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 查询任务巡检项
     *
     * @throws Exception
     */
    private void handleQueryTaskInspect(String taskId) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setFtId(taskId);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneTaskController", "queryTaskInspect", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_TASK_INSPECT);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 查询任务更换部件
     *
     * @throws Exception
     */
    private void handleQueryTaskDeviceChecked(String taskId) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setFtId(taskId);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneTaskController", "queryTaskDeviceChecked", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_TASK_DEVICE_CHECKED);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 处理任务列表-主键做为条件
     *
     * @throws Exception
     */
    private void handleQueryTaskKey(String taskId) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setFtId(taskId);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneTaskController", "queryTaskKey", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_TASK_KEY);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 查询任务列表
     *
     * @throws Exception
     */
    private void handleQueryTaskList(String taskType, int userFlag, int queryStart, String beginDate, String endDate) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setTaskType(taskType);
        loginParams.setUserFlag(userFlag);
        loginParams.setRowStart(queryStart);
        loginParams.setBeginDate(beginDate);
        loginParams.setEndDate(endDate);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneTaskController", "queryTaskInfo", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_FAULT_LIST);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 查询已完成任务列表
     *
     * @throws Exception
     */
    private void handleQueryFinishedTaskList(int outletDeviceId, String ftType, int queryStart, String beginDate, String endDate) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setOutletDeviceId(outletDeviceId);
        loginParams.setTaskType(ftType);
        loginParams.setRowStart(queryStart);
        loginParams.setBeginDate(beginDate);
        loginParams.setEndDate(endDate);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneTaskController", "queryFinishedTaskInfo", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_FINISHED_FT_LIST);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 更新故障部件
     *
     * @param
     * @throws Exception
     */
    private void handleUpdateFaultDeviceChecked(String faultId, String[] list, String updateTime) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setFtId(faultId);
        loginParams.setList(list);
        loginParams.setUpdateTime(updateTime);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneFaultController", "updateFaultDeviceChecked", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_UPDATE_FAULT_DEVICE_CHECKED);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 查询故障更换部件
     *
     * @throws Exception
     */
    private void handleQueryFaultDeviceChecked(String faultId) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setFtId(faultId);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneFaultController", "queryFaultDeviceChecked", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_FAULT_DEVICE_CHECKED);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 处理故障-主键做为条件
     *
     * @throws Exception
     */
    private void handleQueryFaultKey(String faultId) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setFtId(faultId);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneFaultController", "queryFaultKey", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_FAULT_KEY);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 处理故障列表
     *
     * @throws Exception
     */
    private void handleQueryFaultList(int userFlag, int queryStart, String beginDate, String endDate) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setUserFlag(userFlag);
        loginParams.setRowStart(queryStart);
        loginParams.setBeginDate(beginDate);
        loginParams.setEndDate(endDate);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneFaultController", "queryFaultInfo", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_FAULT_LIST);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 处理故障列表
     *
     * @throws Exception
     */
    private void handleQueryFinishedFaultList(int outletDeviceId, String ftType,int queryStart, String beginDate, String endDate) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setOutletDeviceId(outletDeviceId);
        loginParams.setTaskType(ftType);
        loginParams.setRowStart(queryStart);
        loginParams.setBeginDate(beginDate);
        loginParams.setEndDate(endDate);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneFaultController", "queryFinishedFaultInfo", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_FINISHED_FT_LIST);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 查询人员名单
     *
     * @param userFlag
     * @throws Exception
     */
    private void handleQueryWorkerList(int userFlag, String outletCode) throws Exception {
        //发送数据
        Gson gson = new Gson();
        LoginParams loginParams = new LoginParams();
        loginParams.setUserCode(getStaticUserCode());
        loginParams.setOutletCode(outletCode);
        loginParams.setUserFlag(userFlag);
        String jsonObject = gson.toJson(loginParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneLoginController", "queryOrgAuthTypeUser", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_WORKER_LIST);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 插入故障数据
     *
     * @param
     * @throws Exception
     */
    private void handleInsertFault(Bundle sendBundle) throws Exception {
        //发送数据
        Gson gson = new Gson();
        FaultParams faultParams = new FaultParams();

        faultParams.setOutletDeviceId(Integer.toString(sendBundle.getInt(BUNDLE_DEVICE_ID)));
        faultParams.setDeviceName(sendBundle.getString(BUNDLE_DEVICE_NAME));
        faultParams.setCompanyCode(sendBundle.getString(BUNDLE_COMPANY_CODE));
        faultParams.setCompanyName(sendBundle.getString(BUNDLE_COMPANY_NAME));
        faultParams.setOutletCode(sendBundle.getString(BUNDLE_OUTLET_CODE));
        faultParams.setOutletName(sendBundle.getString(BUNDLE_OUTLET_NAME));
        faultParams.setFaultType(sendBundle.getString(BUNDLE_FT_TYPE));
        faultParams.setFaultTypeName(sendBundle.getString(BUNDLE_FT_TYPENAME));
        faultParams.setWorkCode(sendBundle.getString(BUNDLE_WORKER_CODE));
        faultParams.setManageCode(sendBundle.getString(BUNDLE_MANAGE_CODE));
        faultParams.setFaultBeginTime(sendBundle.getString(BUNDLE_START_TIME));
        faultParams.setFaultEndTime(sendBundle.getString(BUNDLE_END_TIME));
        faultParams.setFaultDescription(sendBundle.getString(BUNDLE_DESCRIPTION));
        faultParams.setFaultRemark(sendBundle.getString(BUNDLE_FT_REMARK));
        faultParams.setRecordFlag(sendBundle.getString(BUNDLE_RECORD_FLAG));
        faultParams.setRecordTime(sendBundle.getString(BUNDLE_RECORD_TIME));
        faultParams.setPhoneUserCode(getStaticUserCode());

        String jsonObject = gson.toJson(faultParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneFaultController", "insertFault", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_ADD_FAULT);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 更新故障基本数据
     *
     * @param
     * @throws Exception
     */
    private void handleUpdateFault(Bundle sendBundle) throws Exception {
        //发送数据
        Gson gson = new Gson();
        FaultParams faultParams = new FaultParams();

        faultParams.setFaultId(sendBundle.getString(BUNDLE_FT_ID));
        faultParams.setOutletDeviceId(Integer.toString(sendBundle.getInt(BUNDLE_DEVICE_ID)));
        faultParams.setIsSolve(sendBundle.getString(BUNDLE_IS_SOLVE));
        faultParams.setWorkCode(sendBundle.getString(BUNDLE_WORKER_CODE));
        faultParams.setFaultHandleTime(sendBundle.getString(BUNDLE_HANDLE_TIME));
        faultParams.setRecordFlag(sendBundle.getString(BUNDLE_RECORD_FLAG));
        faultParams.setRecordTime(sendBundle.getString(BUNDLE_RECORD_TIME));
        faultParams.setFaultReason(sendBundle.getString(BUNDLE_FT_REASON));
        faultParams.setFaultDeal(sendBundle.getString(BUNDLE_FT_DEAL));
        faultParams.setFaultDescription(sendBundle.getString(BUNDLE_DESCRIPTION));
        faultParams.setFaultRemark(sendBundle.getString(BUNDLE_FT_REMARK));
        faultParams.setPhoneUserCode(getStaticUserCode());

        String jsonObject = gson.toJson(faultParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneFaultController", "updateFault", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_UPDATE_FAULT_BASE);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 插入任务数据
     *
     * @param
     * @throws Exception
     */
    private void handleInsertTask(Bundle sendBundle) throws Exception {
        //发送数据
        Gson gson = new Gson();
        TaskParams taskParams = new TaskParams();

        taskParams.setOutletDeviceId(Integer.toString(sendBundle.getInt(BUNDLE_DEVICE_ID)));
        taskParams.setDeviceName(sendBundle.getString(BUNDLE_DEVICE_NAME));
        taskParams.setCompanyCode(sendBundle.getString(BUNDLE_COMPANY_CODE));
        taskParams.setCompanyName(sendBundle.getString(BUNDLE_COMPANY_NAME));
        taskParams.setOutletCode(sendBundle.getString(BUNDLE_OUTLET_CODE));
        taskParams.setOutletName(sendBundle.getString(BUNDLE_OUTLET_NAME));
        taskParams.setTaskType(sendBundle.getString(BUNDLE_FT_TYPE));
        taskParams.setTaskTypeName(sendBundle.getString(BUNDLE_FT_TYPENAME));
        taskParams.setWorkCode(sendBundle.getString(BUNDLE_WORKER_CODE));
        taskParams.setManageCode(sendBundle.getString(BUNDLE_MANAGE_CODE));
        taskParams.setTaskEndTime(sendBundle.getString(BUNDLE_END_TIME));
        taskParams.setTaskDescription(sendBundle.getString(BUNDLE_DESCRIPTION));
        taskParams.setTaskRemark(sendBundle.getString(BUNDLE_FT_REMARK));
        taskParams.setPhoneUserCode(getStaticUserCode());

        String jsonObject = gson.toJson(taskParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneTaskController", "insertTask", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_ADD_TASK);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 更新任务基本数据
     *
     * @param
     * @throws Exception
     */
    private void handleUpdateTask(Bundle sendBundle) throws Exception {
        //发送数据
        Gson gson = new Gson();
        TaskParams taskParams = new TaskParams();

        taskParams.setTaskId(sendBundle.getString(BUNDLE_FT_ID));
        taskParams.setOutletDeviceId(Integer.toString(sendBundle.getInt(BUNDLE_DEVICE_ID)));
        taskParams.setIsSolve(sendBundle.getString(BUNDLE_IS_SOLVE));
        taskParams.setWorkCode(sendBundle.getString(BUNDLE_WORKER_CODE));
        taskParams.setTaskHandleTime(sendBundle.getString(BUNDLE_HANDLE_TIME));
        taskParams.setTaskDescription(sendBundle.getString(BUNDLE_DESCRIPTION));
        taskParams.setTaskRemark(sendBundle.getString(BUNDLE_FT_REMARK));
        taskParams.setPhoneUserCode(getStaticUserCode());

        String jsonObject = gson.toJson(taskParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneTaskController", "updateTask", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_UPDATE_TASK_BASE);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 更新任务报表数据
     *
     * @param
     * @throws Exception
     */
    private void handleUpdateTaskInspect(String stringInspect) throws Exception {
        //发送数据
        Gson gson = new Gson();
        List<TaskInspectParams> listTaskInspectParam = new ArrayList<>();
        List<TaskInspectModel> listTaskInspectModel = gson.fromJson(stringInspect, new TypeToken<List<TaskInspectModel>>() {
        }.getType());
        if (listTaskInspectModel != null && listTaskInspectModel.size() > 0) {
            for (TaskInspectModel temp : listTaskInspectModel) {
                TaskInspectParams taskInspectParams = new TaskInspectParams();
                taskInspectParams.setTaskInspectId(temp.getTaskInspectId());
                taskInspectParams.setTaskId(temp.getTaskId());
                taskInspectParams.setInspectId(temp.getInspectId());
                taskInspectParams.setItemType(temp.getItemType());
                if (temp.getItemTypeName() != null && !temp.getItemTypeName().isEmpty()) {
                    taskInspectParams.setItemTypeName(URLEncoder.encode(temp.getItemTypeName(), "utf-8"));
                } else {
                    taskInspectParams.setItemTypeName(temp.getItemTypeName());
                }
                if (temp.getItemName() != null && !temp.getItemName().isEmpty()) {
                    taskInspectParams.setItemName(URLEncoder.encode(temp.getItemName(), "utf-8"));
                } else {
                    taskInspectParams.setItemName(temp.getItemName());
                }
                if (temp.getItemContent() != null && !temp.getItemContent().isEmpty()) {
                    taskInspectParams.setItemContent(URLEncoder.encode(temp.getItemContent(), "utf-8"));
                } else {
                    taskInspectParams.setItemContent(temp.getItemContent());
                }

                taskInspectParams.setItemResult(temp.getItemResult());
                if (temp.getItemResultName() != null && !temp.getItemResultName().isEmpty()) {
                    taskInspectParams.setItemResultName(URLEncoder.encode(temp.getItemResultName(), "utf-8"));
                } else {
                    taskInspectParams.setItemResultName(temp.getItemResultName());
                }

                if (temp.getItemRemark() != null && !temp.getItemRemark().isEmpty()) {
                    taskInspectParams.setItemRemark(URLEncoder.encode(temp.getItemRemark(), "utf-8"));
                } else {
                    taskInspectParams.setItemRemark(temp.getItemRemark());
                }
                taskInspectParams.setUserCode(getStaticUserCode());
                listTaskInspectParam.add(taskInspectParams);
            }
        }
        String jsonObject = gson.toJson(listTaskInspectParam);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneTaskController", "updateTaskInspect", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_UPDATE_TASK_INSPECT);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 查询设备详情
     *
     * @param outletDeviceId
     * @throws Exception
     */
    private void handleQueryDeviceDetail(int outletDeviceId) throws Exception {
        //发送数据
        Gson gson = new Gson();
        DeviceParams deviceParams = new DeviceParams();

        deviceParams.setOutletDeviceId(outletDeviceId);

        String jsonObject = gson.toJson(deviceParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneDeviceController", "queryDeviceDetail", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_QUERY_DETAILS);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putInt(RESULT_TOTAL, response.getTotal());
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            bundle.putString(RESULT_EXTRA_DATA, gson.toJson(response.getRows()));//把对象转换成json传递
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 删除已上传图片
     *
     * @param
     * @throws Exception
     */
    private void handleDeleteUploadFile(Bundle sendBundle) throws Exception {
        //发送数据
        Gson gson = new Gson();
        DeleteFileParams deleteFileParams = new DeleteFileParams();

        deleteFileParams.setFileId(sendBundle.getString(EXTRA_FILE_ID));
        deleteFileParams.setTfType(sendBundle.getString(EXTRA_FT_TYPE));

        String jsonObject = gson.toJson(deleteFileParams);//对象数据转换成json字符串
        String strResponse = queryDataFromServer("PhoneFileOperateController", "deleteFile", jsonObject);
        //接收回应
        ResultModel response = gson.fromJson(strResponse, new TypeToken<ResultModel>() {
        }.getType());
        //创建bundle传递数据
        Intent resultIntent = new Intent(INTENT_DELETE_UPLOAD_FILE);
        Bundle bundle = new Bundle();
        //如果可以接收到结果，也就是连接成功
        if (response != null) {
            bundle.putBoolean(RESULT_FLAG, response.isFlag());
            bundle.putString(RESULT_INFO, response.getInfo());
            resultIntent.putExtras(bundle);
        } else {
            bundle.putBoolean(RESULT_FLAG, false);
            bundle.putString(RESULT_INFO, this.getString(R.string.no_connect_response));
            resultIntent.putExtras(bundle);
        }
        sendBroadcast(resultIntent);
    }

    /**
     * 获取与服务器交互URL
     *
     * @param servlet
     * @param method
     * @param param
     * @return
     */
    static String getFileUrl(Context context, String servlet, String method, String param) {
        Resources resources = context.getResources();
        String defaultStrServer = resources.getString(R.string.default_server_address);
        SharedPreferences pref = context.getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
        String strServer = pref.getString(resources.getString(R.string.preference_server_address), defaultStrServer);
        //拼出服务器地址
        String strUrl = "";
        if (param != null && !param.isEmpty()) {
            strUrl = "http://" + strServer + "/common/" + servlet + "/" + method + "?" + param;
        } else {
            strUrl = "http://" + strServer + "/common/" + servlet + "/" + method;
        }

        return strUrl;
    }

    /**
     * 和后台进行数据交互方法
     *
     * @param servlet servlet名字
     * @param method  请求方法名
     * @param json    请求数据json 字符串
     * @return
     */

    private String queryDataFromServer(String servlet, String method, String json) {

        Resources resources = getResources();
        SharedPreferences pref = getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);

        String defaultStrServer = resources.getString(R.string.default_server_address);
        String strServer = pref.getString(resources.getString(R.string.preference_server_address), defaultStrServer);
        //拼出服务器地址
        String strUrl = "http://" + strServer + "/common/" + servlet + "/" + method;
        //打印服务器地址
        Log.d(TAG, "服务器地址:" + strUrl);

        StringBuilder strResponse = new StringBuilder();
        BufferedReader reader;
        // String params = "submitData="+json;
        String params = "data=" + json;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Length", String.valueOf(params.length()));
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            //connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            connection.connect();

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            //打印发送字段
            Log.d(TAG, "sendInfo ------ " + params);
            outputStream.writeBytes(params);
            outputStream.flush();
            outputStream.close();

            int resultCode = connection.getResponseCode();

            if (resultCode == HttpURLConnection.HTTP_OK) {
                String readline;
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((readline = reader.readLine()) != null) {
                    strResponse.append(readline);
                }
                reader.close();
                //打印接收字段
                Log.d(TAG, "receiveInfo ------- " + strResponse.toString());

            } else {
                Log.d(TAG, "Http response :" + String.valueOf(resultCode));

            }

        } catch (MalformedURLException e) {
            Log.d(TAG, "Malformed URL Exception." + e);

        } catch (IOException e) {
            //服务器连接失败，请检查网络或服务器地址。
            Log.d(TAG, "IO Exception." + e);

        } catch (Exception e) {
            Log.d(TAG, "Unknown Exception" + e);
        } finally {

        }

        return strResponse.toString();
    }
}
