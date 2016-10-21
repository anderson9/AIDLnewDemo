package com.ljs.aidltest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Client";
    private IMyAidlInterface mRemoteService;
    private boolean mIsBound =false;//是否已经绑定
    private TextView mTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "[Client] onCreate");
        setContentView(R.layout.activity_main);
        mTv = (TextView) findViewById(R.id.tv);
        mTv.setText("还未连接");
    }
    //监控远程服务状态链接，ServiceConnection
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteService = IMyAidlInterface.Stub.asInterface(service);//链接上了才会调用
            String pidInfo = null;
            try {
                LocationData myData = mRemoteService.getMyData();
                pidInfo ="客户端Pid"+ Process.myPid()+
                        "服务Pid="+ mRemoteService.getPid() +
                        ", 横坐标 = "+ myData.getWidth() +
                        ", 纵坐标="+ myData.getHeigth();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "[Client] onServiceConnected  "+pidInfo);
            mTv.setText(pidInfo);
            Toast.makeText(MainActivity.this, "绑定上了哦", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //正常顺序默认不调用意外情况销毁调用
            Log.i(TAG, "[Client] onServiceDisconnected");
            mTv.setText("服务与客户端断开");
            mRemoteService = null;
            Toast.makeText(MainActivity.this, "已经断开服务", Toast.LENGTH_SHORT).show();
        }
    };
    public void clickButton(View view){
        switch (view.getId()){
            case R.id.btn_bind:
                bindRemoteService();
                break;
            case R.id.btn_unbind:
                unbindRemoteService();
                break;
            case R.id.btn_kill:
                killRemoteService();
                break;
        }
    }
//绑定远程服务
    private void bindRemoteService(){
        Log.i(TAG, "[Client] bindRemoteService");
        Intent intent = new Intent(MainActivity.this, RemoteService.class);
        intent.setAction("com.ljs.adiltestAction");
        intent.setPackage(getPackageName());//Android5.0不允许直接隐式启动，需要加包名
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        mTv.setText("开始绑定服务");
    }
    //解除绑定
    private void unbindRemoteService(){
        Log.i(TAG, "[Client] unbindRemoteService ==>");
        if(!mIsBound){
            Toast.makeText(MainActivity.this, "不需要解绑已经没有绑定服务", Toast.LENGTH_SHORT).show();
            return;
        }
        unbindService(mConnection);
        mIsBound = false;
        mTv.setText("解除绑定服务");
    }
    //kill远程服务
    private void killRemoteService(){
        Log.i(TAG, "[Client] killRemoteService");
        try {
            android.os.Process.killProcess(mRemoteService.getPid());
            mTv.setText("Kill掉远程服务所在进程");
            Toast.makeText(MainActivity.this, "远程服务杀掉了哦", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "服务进程不存在，杀死服务进程失败", Toast.LENGTH_SHORT).show();

        }
    }
}
