package com.a712.emos_androidapp.utils;

import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */

public interface PublicStringTool {

    String ACTION_LOGIN = "Service.action.extra.LOGIN";
    String ACTION_UPDATE_APP = "Service.action.extra.ACTION_UPDATE_APP";
    String ACTION_CHANGE_PASSWORD = "Service.action.extra.ACTION_CHANGE_PASSWORD";
    String EXTRA_USERNAME = "Service.action.extra.USERNAME";
    String EXTRA_CHINESE_USERNAME = "Service.action.extra.EXTRA_CHINESE_USERNAME";
    String EXTRA_PASSWORD = "Service.action.extra.PASSWORD";
    String EXTRA_NEW_PASSWORD = "Service.action.extra.EXTRA_NEW_PASSWORD";
    String EXTRA_OUTLECT_CODE = "Service.action.extra.EXTRA_OUTLECT_CODE";
    String EXTRA_USER_FLAG = "Service.action.extra.EXTRA_USER_FLAG";
    String EXTRA_TASK_TYPE = "Service.action.extra.EXTRA_TASK_TYPE";
    String QUERY_START = "Service.action.extra.QUERY_START";
    String EXTRA_BEGIN_DATE = "Service.action.extra.EXTRA_BEGIN_DATE";
    String EXTRA_END_DATE = "Service.action.extra.EXTRA_END_DATE";
    String EXTRA_FT_ID= "Service.action.extra.EXTRA_FT_ID";
    String EXTRA_FT_LIST= "Service.action.extra.EXTRA_FT_LIST";
    String EXTRA_UPDATE_TIME= "Service.action.extra.EXTRA_UPDATE_TIME";
    String EXTRA_FILE_ID= "Service.action.extra.EXTRA_FILE_ID";
    String EXTRA_FT_TYPE = "Service.action.extra.EXTRA_FT_TYPE";
    String EXTRA_INSPECT_LIST = "Service.action.extra.EXTRA_INSPECT_LIST";
    String EXTRA_SYS_CODE = "Service.action.extra.EXTRA_SYS_CODE";
    String EXTRA_SYS_NUM = "Service.action.extra.EXTRA_SYS_NUM";

    String ACTION_SEARCH_NUMBER = "Service.action.extra.SEARCH_NUMBER";
    String EXTRA_DEVICE_NUMBER = "Service.action.extra.DEVICE_NUMBER";
    String EXTRA_DEVICE_ID = "Service.action.extra.EXTRA_DEVICE_ID";
    String EXTRA_OUTLET_DEVICE_ID = "Service.action.extra.EXTRA_OUTLET_DEVICE_ID";

    String ACTION_SEARCH_COUNT = "Service.action.extra.ACTION_SEARCH_COUNT";
    String ACTION_QUERY_FAULT = "Service.action.extra.ACTION_QUERY_FAULT";
    String ACTION_QUERY_FINISHED_FAULT = "Service.action.extra.ACTION_QUERY_FINISHED_FAULT";
    String ACTION_QUERY_TASK = "Service.action.extra.ACTION_QUERY_TASK";
    String ACTION_QUERY_FINISHED_TASK = "Service.action.extra.ACTION_QUERY_FINISHED_TASK";
    String ACTION_QUERY_FAULT_KEY = "Service.action.extra.ACTION_QUERY_FAULT_KEY";
    String ACTION_QUERY_TASK_KEY = "Service.action.extra.ACTION_QUERY_TASK_KEY";
    String ACTION_QUERY_FAULT_DEVICE_CHECKED = "Service.action.extra.ACTION_QUERY_FAULT_DEVICE_CHECKED";
    String ACTION_QUERY_TASK_DEVICE_CHECKED = "Service.action.extra.ACTION_QUERY_TASK_DEVICE_CHECKED";
    String ACTION_UPDATE_FAULT_DEVICE_CHECKED = "Service.action.extra.ACTION_UPDATE_FAULT_DEVICE_CHECKED";
    String ACTION_UPDATE_TASK_DEVICE_CHECKED = "Service.action.extra.ACTION_UPDATE_TASK_DEVICE_CHECKED";
    String ACTION_DELETE_UPLOAD_FILE = "Service.action.extra.ACTION_DELETE_UPLOAD_FILE";
    String ACTION_QUERY_TASK_INSPECT = "Service.action.extra.ACTION_QUERY_TASK_INSPECT";

    String ACTION_QUERY_DEVICE = "Service.action.extra.ACTION_QUERY_DEVICE";
    String ACTION_QUERY_WORK_LIST = "Service.action.extra.ACTION_QUERY_WORK_LIST";
    String ACTION_INSERT_FAULT = "Service.action.extra.ACTION_INSERT_FAULT";
    String ACTION_UPDATE_FAULT = "Service.action.extra.ACTION_UPDATE_FAULT";
    String ACTION_INSERT_TASK = "Service.action.extra.ACTION_INSERT_TASK";
    String ACTION_UPDATE_TASK = "Service.action.extra.ACTION_UPDATE_TASK";
    String ACTION_UPDATE_TASK_INSPECT = "Service.action.extra.ACTION_UPDATE_TASK_INSPECT";
    String ACTION_QUERY_DEVICE_DETAIL = "Service.action.extra.ACTION_QUERY_DEVICE_DETAIL";

    //result
    String RESULT_FLAG = "Service.action.extra.RESULT_FLAG";
    String RESULT_INFO = "Service.action.extra.RESULT_INFO";
    String RESULT_EXTRA_DATA = "Service.action.extra.RESULT_EXTRA_DATA";
    String RESULT_TOTAL = "Service.action.extra.RESULT_TOTAL";
    String RESULT_ROWS = "Service.action.extra.RESULT_ROWS";

    //INTENT标识
    String INTENT_LOGIN_RESULT = "Service.action.extra.INTENT_LOGIN_RESULT";
    String INTENT_UPDATE_APP_RESULT = "Service.action.extra.INTENT_UPDATE_APP_RESULT";
    String INTENT_CHANGE_PASSWORD_RESULT = "Service.action.extra.INTENT_CHANGE_PASSWORD_RESULT";
    String INTENT_DEVICE_NUMBER_RESULT = "Service.action.extra.INTENT_DEVICE_NUMBER_RESULT";
    String INTENT_BUNDLE_TITLE = "Service.action.extra.INTENT_BUNDLE_TITLE";
    String INTENT_BUNDLE_TITLE_TYPE = "Service.action.extra.INTENT_BUNDLE_TITLE_TYPE";
    String INTENT_QUERY_TASK_COUNT = "Service.action.extra.INTENT_QUERY_TASK_COUNT";
    String INTENT_QUERY_FAULT_LIST = "Service.action.extra.INTENT_QUERY_FAULT_LIST";
    String INTENT_QUERY_FINISHED_FT_LIST = "Service.action.extra.INTENT_QUERY_FINISHED_FT_LIST";
    String INTENT_QUERY_DEVICE_LIST = "Service.action.extra.INTENT_BUNDLE_SEARCH_CODE";
    String INTENT_QUERY_TASK_LIST = "Service.action.extra.INTENT_QUERY_TASK_LIST";
    String INTENT_QUERY_WORKER_LIST = "Service.action.extra.INTENT_QUERY_WORKER_LIST";
    String INTENT_ADD_FAULT = "Service.action.extra.INTENT_ADD_FAULT";
    String INTENT_UPDATE_FAULT_BASE = "Service.action.extra.INTENT__UPDATE_FAULT_BASE";
    String INTENT_ADD_TASK = "Service.action.extra.INTENT_ADD_TASK";
    String INTENT_UPDATE_TASK_BASE = "Service.action.extra.INTENT_UPDATE_TASK_BASE";
    String INTENT_UPDATE_TASK_INSPECT = "Service.action.extra.INTENT_UPDATE_TASK_INSPECT";
    String INTENT_QUERY_DETAILS = "Service.action.extra.INTENT_QUERY_DETAILS";
    String INTENT_QUERY_FAULT_KEY = "Service.action.extra.INTENT_QUERY_FAULT";
    String INTENT_QUERY_TASK_KEY = "Service.action.extra.INTENT_QUERY_TASK";
    String INTENT_QUERY_FAULT_DEVICE_CHECKED = "Service.action.extra.INTENT_QUERY_FAULT_DEVICE_CHECKED";
    String INTENT_QUERY_TASK_DEVICE_CHECKED = "Service.action.extra.INTENT_QUERY_TASK_DEVICE_CHECKED";
    String INTENT_UPDATE_FAULT_DEVICE_CHECKED = "Service.action.extra.INTENT_UPDATE_FAULT_DEVICE_CHECKED";
    String INTENT_UPDATE_TASK_DEVICE_CHECKED = "Service.action.extra.INTENT_UPDATE_TASK_DEVICE_CHECKED";
    String INTENT_DELETE_UPLOAD_FILE = "Service.action.extra.INTENT_DELETE_UPLOAD_FILE";
    String INTENT_QUERY_TASK_INSPECT = "Service.action.extra.INTENT_QUERY_TASK_INSPECT";

    String INTENT_BUNDLE_SEARCH_CODE = "Service.action.extra.INTENT_BUNDLE_SEARCH_CODE";
    String INTENT_BUNDLE_SCAN_CODE = "Service.action.extra.INTENT_BUNDLE_SCAN_CODE";

    String BUNDLE_FT_ID= "Service.action.extra.BUNDLE_FT_ID";
    String BUNDLE_DEVICE_ID = "Service.action.extra.BUNDLE_DEVICE_ID";
    String BUNDLE_DEVICE_NAME = "Service.action.extra.BUNDLE_DEVICE_NAME";
    String BUNDLE_FT_TYPE= "Service.action.extra.BUNDLE_FT_TYPE";
    String BUNDLE_FT_TYPENAME= "Service.action.extra.BUNDLE_FT_TYPENAME";
    String BUNDLE_OUTLET_CODE = "Service.action.extra.BUNDLE_OUTLET_CODE";
    String BUNDLE_OUTLET_NAME = "Service.action.extra.BUNDLE_OUTLET_NAME";
    String BUNDLE_COMPANY_CODE = "Service.action.extra.BUNDLE_COMPANY_CODE";
    String BUNDLE_COMPANY_NAME = "Service.action.extra.BUNDLE_COMPANY_NAME";
    String BUNDLE_WORKER_CODE = "Service.action.extra.BUNDLE_WORKER_CODE";
    String BUNDLE_MANAGE_CODE = "Service.action.extra.BUNDLE_MANAGE_CODE";
    String BUNDLE_START_TIME = "Service.action.extra.BUNDLE_START_TIME";
    String BUNDLE_END_TIME = "Service.action.extra.BUNDLE_END_TIME";
    String BUNDLE_DESCRIPTION = "Service.action.extra.BUNDLE_DESCRIPTION";
    String BUNDLE_RECORD_FLAG = "Service.action.extra.BUNDLE_RECORDFLAG";
    String BUNDLE_RECORD_TIME = "Service.action.extra.BUNDLE_RECORD_TIME";
    String BUNDLE_IS_SOLVE = "Service.action.extra.BUNDLE_IS_SOLVE";
    String BUNDLE_HANDLE_TIME = "Service.action.extra.BUNDLE_HANDLE_TIME";
    String BUNDLE_FT_REASON = "Service.action.extra.BUNDLE_REASON";
    String BUNDLE_FT_DEAL = "Service.action.extra.BUNDLE_DEAL";
    String BUNDLE_FT_REMARK = "Service.action.extra.BUNDLE_FT_REMARK";
    String BUNDLE_ACCESS_MODIFY = "Service.action.extra.BUNDLE_ACCESS_MODIFY";
    int BUNDLE_ACCESS_MODIFY_DAYS = 30;

}
