package com.malalaoshi.android.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.malalaoshi.android.R;
import com.malalaoshi.android.core.base.BaseActivity;
import com.malalaoshi.android.core.network.api.ApiExecutor;
import com.malalaoshi.android.core.network.api.BaseApiContext;
import com.malalaoshi.android.core.usercenter.UserManager;
import com.malalaoshi.android.core.usercenter.api.AddStudentNameApi;
import com.malalaoshi.android.core.usercenter.entity.AddStudentName;
import com.malalaoshi.android.core.view.TitleBarView;
import com.malalaoshi.android.util.MiscUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Modify the name of student
 * Created by kang on 16/1/25.
 */
public class ModifyUserNameActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener {

    public static String TAG = "ModifyUserNameActivity";
    public static int RESULT_CODE_NAME = 0x001;
    public static String EXTRA_USER_NAME = "username";

    @Bind(R.id.titleBar)
    TitleBarView titleBar;

    @Bind(R.id.et_value)
    EditText etUserName;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_modify_singlevalue);
        ButterKnife.bind(this);
        etUserName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        titleBar.setOnTitleBarClickListener(this);
        initDatas();
    }

    private void initDatas() {
        Intent intent = getIntent();
        userName = intent.getStringExtra(EXTRA_USER_NAME);
        if (userName == null) {
            userName = "";
        }
        etUserName.setMaxLines(30);
        etUserName.setText(userName);
        etUserName.setSelection(userName.length());
        titleBar.setTitle("更改名字");
    }

    @Override
    public void onTitleLeftClick() {
        this.finish();
    }

    @Override
    public void onTitleRightClick() {
        postModifyUserName();

    }

    private static final class ModifyStudentNameRequest extends BaseApiContext<ModifyUserNameActivity, AddStudentName> {

        private String name;

        public ModifyStudentNameRequest(ModifyUserNameActivity activity, String name) {
            super(activity);
            this.name = name;
        }

        @Override
        public AddStudentName request() throws Exception {
            return new AddStudentNameApi().get(this.name);
        }

        @Override
        public void onApiSuccess(@NonNull AddStudentName data) {
            get().onChangeStudentNameSuccess(data);
        }
    }

    private void onChangeStudentNameSuccess(AddStudentName data) {
        if (data.isDone()) {
            MiscUtil.toast(R.string.usercenter_set_student_succeed);
            updateStuName();
            setActivityResult();
        } else {
            Log.i(TAG, "Set student's name failed.");
            MiscUtil.toast(R.string.usercenter_set_student_failed);
        }
    }

    private void postModifyUserName() {
        userName = etUserName.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            MiscUtil.toast(R.string.usercenter_student_empty);
            return;
        }
        ApiExecutor.exec(new ModifyStudentNameRequest(this,userName));
    }

    private void updateStuName() {
        if (!TextUtils.isEmpty(userName)) {
            UserManager.getInstance().setStuName(userName);
        }
    }

    private void setActivityResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_NAME, userName);
        setResult(RESULT_CODE_NAME, intent);
        finish();
    }

    @Override
    protected String getStatName() {
        return "修改学生姓名";
    }
}
