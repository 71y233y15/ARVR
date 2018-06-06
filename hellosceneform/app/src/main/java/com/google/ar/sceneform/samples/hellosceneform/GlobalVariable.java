package com.google.ar.sceneform.samples.hellosceneform;

import android.app.Application;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//在 AndroidManifest.xml 的 application 標籤底下新增一個屬性android:name，指定剛剛建立的類別名稱android:name=”.GlobalVariable”
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//public class GlobalVariable extends Application {
public class GlobalVariable extends Application {
    private String Buildpic;     //User 名稱
    private int Builnum;         //User 年紀

    //修改 變數値
    public void setBuildpic(String buildpic){
        this.Buildpic = buildpic;
    }
    public void setBuilnum(int buildnum){
        this.Builnum = buildnum;
    }

    //取得 變數值
    public String getBuildpic() {
        return Buildpic;
    }
    public int getBuilnum(){
        return Builnum;
    }
}