package com.hyunwoong.pestsaver.util.ml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.core.util.Consumer;

import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.hyunwoong.pestsaver.base.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Hyunwoong
 * @when : 2019-12-23 오전 11:57
 * @homepage : https://github.com/gusdnd852
 */
public class ImageClassifier {
    private static ImageClassifier instance = new ImageClassifier();

    public static ImageClassifier getInstance() {
        return instance;
    }

    private FirebaseModelInterpreter interpreter;
    private FirebaseModelInputOutputOptions inputOutputOptions;

    private ImageClassifier() {
        try {
            FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
                    .setAssetFilePath("model.tflite")
                    .build();

            FirebaseModelInterpreterOptions options = new FirebaseModelInterpreterOptions.Builder(localModel)
                    .build();

            interpreter = FirebaseModelInterpreter.getInstance(options);
            inputOutputOptions = new FirebaseModelInputOutputOptions.Builder()
                    .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 224, 224, 3})
                    .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 24})
                    .build();

        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }
    }


    private float[][][][] normalize(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        int batchSize = 0;
        float[][][][] input = new float[1][224][224][3];
        for (int x = 0; x < 224; x++) {
            for (int y = 0; y < 224; y++) {
                int pixel = bitmap.getPixel(x, y);
                input[batchSize][x][y][0] = (Color.red(pixel) - 127) / 128.0f;
                input[batchSize][x][y][1] = (Color.green(pixel) - 127) / 128.0f;
                input[batchSize][x][y][2] = (Color.blue(pixel) - 127) / 128.0f;
            }
        }
        return input;
    }

    private Map<String, Float> readLabel(Context context, float[] probabilities) {
        try (BufferedReader labels = new BufferedReader(new InputStreamReader(context.getAssets().open("labels.txt")))) {
            Map<String, Float> result = new HashMap<>();
            for (float probability : probabilities) {
                result.put(labels.readLine(), probability);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void inference(BaseActivity context, Bitmap bitmap, Consumer<Map<String, Float>> listener) {
        try {
            FirebaseModelInputs inputs = new FirebaseModelInputs.Builder()
                    .add(normalize(bitmap))
                    .build();

            interpreter.run(inputs, inputOutputOptions)
                    .addOnSuccessListener(result -> {
                        System.out.println(result);
                        float[][] output = result.getOutput(0);
                        float[] probabilities = output[0];
                        Map<String, Float> results = readLabel(context, probabilities);
                        listener.accept(results);
                    })
                    .addOnFailureListener(e -> context.hideAndToast("분석에 실패했습니다"));
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }
    }

}
