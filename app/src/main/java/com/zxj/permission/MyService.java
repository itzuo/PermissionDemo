package com.zxj.permission;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.zxj.lib_permission.annotation.Permission;
import com.zxj.lib_permission.annotation.PermissionCanceled;
import com.zxj.lib_permission.annotation.PermissionDenied;

/**
 * Created by jay on 2018/6/22.
 */

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        requestCamera();

        return super.onStartCommand(intent, flags, startId);
    }

    @Permission(Manifest.permission.CAMERA)
    private void requestCamera() {
        Toast.makeText(getApplicationContext(),"Service中请求权限——通过",Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied()
    private void deny() {
        Log.i(TAG, "Service中请求权限_writeDeny:" );
        Toast.makeText(this, "Service中请求权限_deny", Toast.LENGTH_SHORT).show();

    }

    @PermissionCanceled()
    private void cancel() {
        Log.i(TAG, "Service中请求权限_writeCancel: " );
        Toast.makeText(this, "Service中请求权限_cancel", Toast.LENGTH_SHORT).show();
    }

}
