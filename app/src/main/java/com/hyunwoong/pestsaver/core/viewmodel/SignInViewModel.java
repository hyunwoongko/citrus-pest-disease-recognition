package com.hyunwoong.pestsaver.core.viewmodel;

import com.hyunwoong.pestsaver.R;
import com.hyunwoong.pestsaver.base.BaseViewModel;
import com.hyunwoong.pestsaver.core.activity.SignInActivity;
import com.hyunwoong.pestsaver.core.activity.SignUpActivity;
import com.hyunwoong.pestsaver.core.model.User;
import com.hyunwoong.pestsaver.util.data.Data;
import com.hyunwoong.pestsaver.util.view.OnXML;

/**
 * @author : Hyunwoong
 * @when : 2019-11-15 오후 3:14
 * @homepage : https://github.com/gusdnd852
 */
public class SignInViewModel extends BaseViewModel {
    public Data<String> id = new Data<>();
    public Data<String> pw = new Data<>();
    public Data<Boolean> stay = new Data<>(false);

    @OnXML(resid = R.layout.sign_in)
    public void signIn(SignInActivity activity) {
        User user = new User();
        user.setId(id.get());
        user.setPw(pw.get());

        if (activity.check(user))
            activity.cachedSignIn(stay.get(), user);
    }

    @OnXML(resid = R.layout.sign_in)
    public void moveToSignUp(SignInActivity activity) {
        activity.move(SignUpActivity.class);
    }
}
