package com.zxj.lib_permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zxj.lib_permission.core.IPermission;

/**
 * 工具activity,透明的，用户无感知的
 */
public class PermissionActivity extends Activity {
    private static final String PARAM_PERMISSION = "param_permission";
    private static final String PARAM_REQUEST_CODE = "param_request_code";
    private static IPermission permissionListener;
    private String[] mPermissions;
    private int mRequestCode;

    /**
     * 请求权限
     */
    public static void requestPermission(Context context, String[] permissions, int requestCode, IPermission iPermission) {
        permissionListener = iPermission;
        Intent intent = new Intent(context, PermissionActivity.class);
        //加flage是为了使用户无感知的
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray(PARAM_PERMISSION, permissions);
        bundle.putInt(PARAM_REQUEST_CODE, requestCode);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if(context instanceof Activity){
            //屏蔽Activity的启动动画
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        this.mPermissions = getIntent().getStringArrayExtra(PARAM_PERMISSION);
        this.mRequestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, -1);

        if (mPermissions == null || mRequestCode < 0 || permissionListener == null) {
            this.finish();
            return;
        }

        //检查是否已授权
        if (PermissionUtils.hasPermission(this, mPermissions)) {
            permissionListener.ganted();
            finish();
            return;
        }
        ActivityCompat.requestPermissions(this, this.mPermissions, this.mRequestCode);

    }

    /**
     * grantResults对应于申请的结果，这里的数组对应于申请时的第二个权限字符串数组。
     * 如果你同时申请两个权限，那么grantResults的length就为2，分别记录你两个权限的申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //请求权限成功
        if (PermissionUtils.verifyPermission(this, grantResults)) {
            permissionListener.ganted();
            finish();
            return;
        }

        //用户点击了不再显示
        if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
            permissionListener.denied();
            finish();
            return;
        }

        //用户取消
        permissionListener.cancled();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        //屏蔽Activity的退出动画
        overridePendingTransition(0, 0);
    }
}
