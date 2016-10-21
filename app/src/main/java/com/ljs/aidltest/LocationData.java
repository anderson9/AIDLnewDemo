package com.ljs.aidltest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anderson9 on 2016-10-21.
 */

public class LocationData implements Parcelable {
    int width;//代表横
    int heigth;//代表纵
    public LocationData(){

    }

    public LocationData(Parcel in){
        readFromParcel(in);//对象通过Binder从Parcel返回拿到
    }
    @Override
    public int describeContents() {
        return 0;//描绘类型信息一般不用管
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);//数据写入容器Parcel
        dest.writeInt(heigth);
    }
    public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {
        //内部类实现对象反序列化
        @Override
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }
        @Override
        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };
    /** 从Parcel中读取数据 **/
    public void readFromParcel(Parcel in){
        width = in.readInt();
        heigth = in.readInt();
    }
    public int getHeigth() {
        return heigth;
    }
    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    @Override
    public String toString() {
        return "width = "+ width + ", heigth="+ heigth;
    }
}

