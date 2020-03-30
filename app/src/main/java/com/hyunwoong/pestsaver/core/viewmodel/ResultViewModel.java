package com.hyunwoong.pestsaver.core.viewmodel;

import com.hyunwoong.pestsaver.R;
import com.hyunwoong.pestsaver.base.BaseViewModel;
import com.hyunwoong.pestsaver.core.activity.ResultActivity;
import com.hyunwoong.pestsaver.util.data.Data;
import com.hyunwoong.pestsaver.util.view.OnXML;

/**
 * @author : Hyunwoong
 * @when : 2019-12-23 오후 3:16
 * @homepage : https://github.com/gusdnd852
 */
public class ResultViewModel extends BaseViewModel {
    public Data<String> result = new Data<>("");

    @OnXML(resid = R.layout.result)
    public void printResult(ResultActivity activity) {
        String result = activity.convertResultString(5);
        this.result.set(result);
    }
}
