package com.hyunwoong.pestsaver.core.viewmodel;

import com.hyunwoong.pestsaver.R;
import com.hyunwoong.pestsaver.base.BaseViewModel;
import com.hyunwoong.pestsaver.core.activity.MainActivity;
import com.hyunwoong.pestsaver.util.data.Data;
import com.hyunwoong.pestsaver.util.view.OnXML;

/**
 * @author : Hyunwoong
 * @when : 2019-11-18 오전 2:01
 * @homepage : https://github.com/gusdnd852
 */
public class MainViewModel extends BaseViewModel {
    public Data<String> greeting = new Data<>();

    @OnXML(resid = R.layout.main)
    public void openAlbum(MainActivity activity) {
        activity.openAlbum();
    }

    @OnXML(resid = R.layout.main)
    public void classify(MainActivity activity) {
        if (activity.isUploaded()) {
            activity.classify();
        } else {
            activity.toast("먼저 이미지를 선택하세요");
        }
    }
}
