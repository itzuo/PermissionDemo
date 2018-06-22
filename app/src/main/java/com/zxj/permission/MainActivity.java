package com.zxj.permission;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zxj.lib_permission.annotation.Permission;
import com.zxj.lib_permission.annotation.PermissionCanceled;
import com.zxj.lib_permission.annotation.PermissionDenied;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    @Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void requestPermission() {
        Toast.makeText(this, "请求一个权限成功（写权限）", Toast.LENGTH_SHORT).show();
    }

    @PermissionCanceled
    private void cancelCode(){
        Toast.makeText(this, "取消授权", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied
    private void denyCode(){
        Toast.makeText(this, "被拒绝 点击了不再提示", Toast.LENGTH_SHORT).show();
    }
}
