
## 说明
使用AOP来实现动态权限申请

不仅可以在Activity中使用，还可以在Fragment、Service中使用
## 使用
1、在app的build.gradle中引用 `apply plugin: 'android-aspectjx'`

2、在代码中
```
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
```