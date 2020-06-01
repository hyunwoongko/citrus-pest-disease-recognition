package com.hyunwoong.pestsaver.core.activity;

import com.hyunwoong.pestsaver.base.BaseActivity;
import com.hyunwoong.pestsaver.core.model.User;
import com.hyunwoong.pestsaver.core.viewmodel.SignInViewModel;
import com.hyunwoong.pestsaver.databinding.SignInView;
import com.hyunwoong.pestsaver.util.data.Cache;
import com.hyunwoong.pestsaver.util.data.RxFirebase;
import com.hyunwoong.pestsaver.util.others.Strings;

/**
 * @author : Hyunwoong
 * @when : 2019-11-15 오후 3:12
 * @homepage : https://github.com/gusdnd852
 */
public class SignInActivity extends BaseActivity<SignInView, SignInViewModel> {

    public boolean check(User user) {
        if (Strings.empty(user.getId())) return hideAndToast("아이디를 입력해주세요.");
        else if (Strings.empty(user.getPw())) return hideAndToast("비밀번호를 입력해주세요.");
        else return true;
    }

    public void staySignedIn(boolean stay, User user) {
        if (stay) {
            preference().setString("id", user.getId());
            preference().setString("pw", user.getPw());
        } else {
            preference().setString("id", null);
            preference().setString("pw", null);
        }
    }

    public void signIn(boolean stay, User user) {
        RxFirebase.signIn()
                .success(u -> this.staySignedIn(stay, u))
                .success(u -> moveAndFinish(MainActivity.class))
                .fail(u -> hideAndToast("로그인에 실패했습니다."))
                .subscribe(user);
    }

    public void cachedSignIn(boolean stay, User user) {
        this.showProgress();
        String key = Strings.key(user.getId());

        RxFirebase.from("user")
                .child(key)
                .access(User.class)
                .next(Cache::copyUser)
                .next(u -> signIn(stay, user))
                .subscribe();
    }
}