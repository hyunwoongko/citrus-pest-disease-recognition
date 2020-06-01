package com.hyunwoong.pestsaver.core.activity;

import android.annotation.SuppressLint;

import com.hyunwoong.pestsaver.base.BaseActivity;
import com.hyunwoong.pestsaver.core.viewmodel.ResultViewModel;
import com.hyunwoong.pestsaver.databinding.ResultView;
import com.hyunwoong.pestsaver.util.data.Cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author : Hyunwoong
 * @when : 2019-12-23 오후 2:08
 * @homepage : https://github.com/gusdnd852
 */
public class ResultActivity extends BaseActivity<ResultView, ResultViewModel> {

    public String convertResultString(int count) {
        Map<String, Float> result = Cache.readResults();
        List<Map.Entry<String, Float>> resultList = sortByProb(result);
        return makeResult(resultList, count);
    }

    @SuppressLint("DefaultLocale")
    public String makeResult(List<Map.Entry<String, Float>> resultList, int count) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String resultName = resultList.get(i).getKey();
            String resultProb = String.format("%.3f", resultList.get(i).getValue() * 100);
            String resultString = (i + 1) + "위 (" + resultProb + "%) : " + resultName + "\n\n";
            output.append(resultString);
        }
        return output.toString();
    }

    public <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortByProb(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        return list;
    }
}
