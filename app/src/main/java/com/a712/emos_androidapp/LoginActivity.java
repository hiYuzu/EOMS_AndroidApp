package com.a712.emos_androidapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.a712.emos_androidapp.receiveModel.UserModel;
import com.a712.emos_androidapp.service.QueryDataIntentService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static android.Manifest.permission.READ_CONTACTS;
import static com.a712.emos_androidapp.utils.PublicStringTool.*;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends MyEomsActivity implements LoaderCallbacks<Cursor> {


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // 声明broadcastReceiver
    private IntentFilter mLoginResultIntentFilter;
    private LoginResultReceiver mLoginResultReceiver;
    //声明控件
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Resources resources;
    private SharedPreferences pref;
    private CheckBox mSaveCheckBox;//记住密码控件
    private Button mEmailSignInButton;
    private Button mChangeServerButton;
    private boolean isSavePassword;//记住密码标识
    private Spinner spinner;//spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        resources = getResources();

        //接收过滤器
        mLoginResultIntentFilter = new IntentFilter(INTENT_LOGIN_RESULT);
        mLoginResultReceiver = new LoginResultReceiver();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        // populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mSaveCheckBox = (CheckBox) findViewById(R.id.chk_save_password);
        spinner = (Spinner) findViewById(R.id.spinner_login);
        //初始化显示的内容
        initInputText();
        //保存密码按钮点击
        mSaveCheckBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果选中记住密码
                if (mSaveCheckBox.isChecked()) {
                    isSavePassword = true;
                }
                //没选中则存储一下
                else {
                    pref = getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    mPasswordView.setText(null);
                    isSavePassword = false;
                    editor.putString(resources.getString(R.string.preference_saved_password), null);
                    editor.putBoolean(resources.getString(R.string.preference_save_password), false);
                    editor.apply();
                }
            }
        });
        //设置下拉选监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                //Toast.makeText(LoginActivity.this, "你点击的是:"+languages[pos], Toast.LENGTH_SHORT).show();
                //String[] languages = getResources().getStringArray(R.array.worker);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        //登录按钮触发事件
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //把账号和密码保存到sharedPreference
                pref = getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                String username = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                String[] workerCode = getResources().getStringArray(R.array.worker);
                editor.putString(resources.getString(R.string.preference_username), username);
                editor.putBoolean(resources.getString(R.string.preference_save_password), isSavePassword);
                editor.putInt(resources.getString(R.string.preference_worker), spinner.getSelectedItemPosition());
                //如果记住密码标识为true，则存入密码
                if (isSavePassword) {
                    editor.putString(resources.getString(R.string.preference_saved_password), password);
                }
                editor.apply();//提交

                //发送验证方法
                attemptLogin();
            }
        });
        //修改服务器按钮点击方法
        mChangeServerButton = (Button) findViewById(R.id.btn_change_server);
        mChangeServerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeServerDialog();
            }
        });
        requestRWPermission();
    }

    /**
     * 初始化显示用户名和记住密码按钮和密码
     */
    private void initInputText() {
        //得到数据
        pref = getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
        String username = pref.getString(resources.getString(R.string.preference_username), null);
        isSavePassword = pref.getBoolean(resources.getString(R.string.preference_save_password), false);
        int workerFlag = pref.getInt(resources.getString(R.string.preference_worker), 0);
        //编辑框和密码框显示sharedPreference数据
        mEmailView.setText(username);
        mSaveCheckBox.setChecked(isSavePassword);
        if (isSavePassword) {
            String password = pref.getString(resources.getString(R.string.preference_saved_password), null);
            mPasswordView.setText(password);
        }
        //Toast.makeText(LoginActivity.this, worker, Toast.LENGTH_SHORT).show();
        if (workerFlag == 1)
            spinner.setSelection(1);
        else
            spinner.setSelection(0);
    }

    /**
     * 弹出修改服务器弹出框
     */

    private void showChangeServerDialog() {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_change_server, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.change_server_title);

        //list of items
        builder.setView(linearLayout);
        final EditText editText = (EditText) linearLayout.findViewById(R.id.edit_server);


        //确定按钮
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        String server = editText.getText().toString();
                        pref = getSharedPreferences(resources.getString(R.string.preference), Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(resources.getString(R.string.preference_server_address), server);
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

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
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //如果输入有空
        if (email.isEmpty() || password.isEmpty()) {
            Toast toast = Toast.makeText(LoginActivity.this, R.string.error_message_username_password_empty, Toast.LENGTH_SHORT);
            toast.show();
            return;

        } else {
            //弹出进度框
            showProgress(true);
            //进行数据查询的操作
            QueryDataIntentService.startActionLogin(LoginActivity.this, email, password);

        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

     private void populateAutoComplete() {
       if (!mayRequestContacts()) {
           return;
       }

       getLoaderManager().initLoader(0, null, this);
   }

   private boolean mayRequestContacts() {
       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
           return true;
       }
       if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
           return true;
       }
       if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
           Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                   .setAction(android.R.string.ok, new View.OnClickListener() {
                       @Override
                       @TargetApi(Build.VERSION_CODES.M)
                       public void onClick(View v) {
                           requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                       }
                   });
       } else {
           requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
       }
       return false;
   }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mLoginResultReceiver, mLoginResultIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mLoginResultReceiver);
    }

    /**
     * 请求读写权限
     */
    private void requestRWPermission() {
        String[] rwPermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        try {
            //判断是否为android6.0系统版本，如果是，需要动态添加权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean isGranted = true;
                for (String permission : rwPermissions) {
                    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                        isGranted = false;
                        break;
                    }
                }
                if (!isGranted) {
                    ActivityCompat.requestPermissions(this, rwPermissions, 101);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //用receiver来接受数据
    public class LoginResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //boolean result = intent.getBooleanExtra(RESULT_FLAG, false);
            Gson gson = new Gson();
            Bundle bundle = intent.getExtras();
            boolean result = intent.getBooleanExtra(RESULT_FLAG, false);
            String info = intent.getStringExtra(RESULT_INFO);
            String data = bundle.getString(RESULT_EXTRA_DATA);
            //如果查到数据验证正确
            if (result) {
                List<UserModel> modelListTemp = gson.fromJson(data, new TypeToken<List<UserModel>>() {
                }.getType());
                String receiveUserName = modelListTemp.get(0).getUserName();
                //开启主页面，并把中文名传过去
                Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString(EXTRA_CHINESE_USERNAME, receiveUserName);
                mainActivityIntent.putExtras(bundle1);
                startActivity(mainActivityIntent);
                LoginActivity.this.finish();

            } else {//如果失败
                Toast toast = Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT);
                toast.show();
            }
            showProgress(false);
        }
    }
}

