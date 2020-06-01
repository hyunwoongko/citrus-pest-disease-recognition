package com.hyunwoong.pestsaver.base;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.hyunwoong.pestsaver.util.data.Data;

/**
 * @author : Hyunwoong
 * @when : 2019-11-27 오후 3:58
 * @homepage : https://github.com/gusdnd852
 */
public class BaseViewModel extends ViewModel {
    public Data<Integer> progressBar = new Data<>(View.GONE);

    public void showProgress() {
        this.progressBar.setValue(View.VISIBLE);
    }

    public void hideProgress() {
        this.progressBar.setValue(View.GONE);
    }
}
