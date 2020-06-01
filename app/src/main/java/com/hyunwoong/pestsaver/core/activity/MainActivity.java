package com.hyunwoong.pestsaver.core.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import com.hyunwoong.pestsaver.base.BaseActivity;
import com.hyunwoong.pestsaver.core.viewmodel.MainViewModel;
import com.hyunwoong.pestsaver.databinding.MainView;
import com.hyunwoong.pestsaver.util.data.Cache;
import com.hyunwoong.pestsaver.util.ml.ImageClassifier;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author : Hyunwoong
 * @when : 2019-11-18 오전 1:59
 * @homepage : https://github.com/gusdnd852
 */
public class MainActivity extends BaseActivity<MainView, MainViewModel> {
    private Uri mImageUri;
    private Bitmap bitmap;
    private boolean isUploaded = false;

    public boolean isUploaded() {
        return isUploaded;
    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/image");
        startActivityForResult(intent, 2);
    }

    public void classify() {
        showProgress();
        ImageClassifier classifier = ImageClassifier.getInstance();
        classifier.inference(this, bitmap, classificationResults -> {
            hideAndToast("분석 완료");
            Cache.copyResult(classificationResults);
            move(ResultActivity.class);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == -1) {
            if (requestCode == 2 || requestCode == 1) {
                if (requestCode == 2) mImageUri = intent.getData();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(mImageUri);
                    Drawable drawable = Drawable.createFromStream(inputStream, mImageUri.toString());
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
                    view.image.setBackground(drawable);
                    view.image.setText("");
                    this.isUploaded = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}