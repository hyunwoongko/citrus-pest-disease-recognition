package com.hyunwoong.pestsaver.base;

/**
 * @author : Hyunwoong
 * @when : 2019-11-27 오후 3:58
 * @homepage : https://github.com/gusdnd852
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.hyunwoong.pestsaver.BR;
import com.hyunwoong.pestsaver.R;
import com.hyunwoong.pestsaver.util.data.Preference;

import java.lang.reflect.ParameterizedType;

/**
 * @author : Hyunwoong
 * @when : 2019-11-18 오후 3:42
 * @homepage : https://github.com/gusdnd852
 */
@SuppressWarnings("unchecked")
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

    protected Handler handler = new Handler();
    protected VM viewModel;
    protected V view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = DataBindingUtil.setContentView(this, readXml());
        viewModel = createViewModel();
        view.setLifecycleOwner(this);
        view.setVariable(BR.viewModel, viewModel);
        view.setVariable(BR.activity, this);
    }

    private int readXml() {
        try {
            ActivityInfo activityInfo = getPackageManager()
                    .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);

            Bundle metaData = activityInfo.metaData;
            String fileName = metaData.getString("layout");
            String simpleName = fileName.split("/")[2].split("\\.")[0];
            return getResources().getIdentifier(simpleName, "layout", getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    private VM createViewModel() {
        try {
            String className = ((ParameterizedType) getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[1]
                    .toString()
                    .split(" ")[1];

            Class<VM> clazz = (Class<VM>) Class.forName(className);
            ViewModelProvider.NewInstanceFactory factory = new ViewModelProvider.NewInstanceFactory();
            return new ViewModelProvider(this, factory).get(clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

    public Preference preference() {
        return Preference.getInstance(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_fadein, R.anim.activity_fadeout);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fadein, R.anim.activity_fadeout);
    }

    public void move(Class<? extends android.app.Activity> activity) {
        startActivity(new Intent(this, activity));
    }

    public void moveAndFinish(Class<? extends android.app.Activity> activity) {
        move(activity);
        finish();
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void dialog(String title, String msg, Runnable positive) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("확인", (dialog, which) -> positive.run())
                .setNegativeButton("취소", (dialog, which) -> {
                })
                .setCancelable(false)
                .create()
                .show();
    }

    public void showProgress() {
        viewModel.showProgress();
    }

    public void hideProgress() {
        viewModel.hideProgress();
    }

    public boolean hideAndToast(String msg) {
        hideProgress();
        toast(msg);
        return false;
    }
}
