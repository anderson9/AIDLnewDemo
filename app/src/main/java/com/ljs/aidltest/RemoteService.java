package com.ljs.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by anderson9 on 2016-10-21.
 */

public class RemoteService extends Service {
    private static final String TAG = "Server"; //Log观察生命周期
    LocationData mMyData;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "[server] onCreate");
        initMyData();//初始化Location
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"[server] onBind");
        return mBinder;

    }
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "[server] onUnbind");
        return super.onUnbind(intent);
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "[server] onDestroy");
        super.onDestroy();
    }
    //通过Aidl生成文件中Stub类实现我们生成接口中的方法
    IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public int getPid() throws RemoteException {
            Log.i(TAG,"[server] getPid()="+android.os.Process.myPid());
            return android.os.Process.myPid();
        }
        @Override
        public LocationData getMyData() throws RemoteException {
            Log.i(TAG,"[server] getMyData()  "+ mMyData.toString());
            return mMyData;
        }
        //Binder驱动底层调用传送数据，由于server ，1对多，一般不设置也可以做权限设置，判断是否需要传递速度给Client。
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };
    /**
     * 初始化MyData数据
     **/
    private void initMyData() {
        mMyData = new LocationData();
        mMyData.setWidth((int)(Math.random()*100));
        mMyData.setHeigth((int)(Math.random()*100));
    }
}
