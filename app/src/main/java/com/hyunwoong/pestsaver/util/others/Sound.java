package com.hyunwoong.pestsaver.util.others;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * @author : Hyunwoong
 * @when : 2019-11-15 오후 7:30
 * @homepage : https://github.com/gusdnd852
 */
public class Sound {

    private static Sound singleton;
    private MediaPlayer musicPlayer;
    private MediaPlayer effectPlayer;
    private int length;

    private Sound() {
        if (musicPlayer == null) {
            musicPlayer = new MediaPlayer();
            effectPlayer = new MediaPlayer();
            length = 0;
        }
    } // 초기화

    public static Sound get() {
        if (singleton == null) singleton = new Sound();
        return singleton;
    }

    public Sound clear() {
        musicPlayer.reset();
        musicPlayer.release();
        musicPlayer = null;
        return this;
    } // 지속적으로 미디어플레이어를 사용하면 객체가 너무 많이 생성되어
    // 에러가 발생하기 떄문에 그 수를 조절하기 위하여, 객체를 죽여줘야함.

    public Sound start(Context context, int resid) {
        clear();
        musicPlayer = MediaPlayer.create(context, resid);
        musicPlayer.start();
        return this;
    } // 뮤직 스타트

    public Sound stop() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            this.clear();
            length = 0;
        }
        return this;
    } // 음악을 멈추고 재생지점을 초기화함.

    public Sound pause() {
        if (musicPlayer != null) {
            musicPlayer.pause();
            length = musicPlayer.getCurrentPosition();
        }
        return this;
    } // 일시정지하고, 현재까지 재생지점을 저장함.

    public Sound resume(Context context, int resId) {
        if (musicPlayer != null) clear();
        musicPlayer = MediaPlayer.create(context, resId);
        musicPlayer.seekTo(length);
        musicPlayer.start();
        return this;
    } // 저장된 지점부터 음악을 재생함.

    public Sound loop(boolean loop) {
        if (musicPlayer != null) {
            musicPlayer.setLooping(loop);
        }
        return this;
    } // 반복재생을 할 것인지 말 것인지 결정함.

    public Sound effectSound(Context context, int resId) {
        effectPlayer.reset();
        effectPlayer.release();
        effectPlayer = null;
        effectPlayer = MediaPlayer.create(context, resId);
        effectPlayer.start();
        effectPlayer.setLooping(false);
        return this;
    } // 효과음 재생용
}
