package com.hyunwoong.pestsaver.core.viewmodel;

import com.hyunwoong.pestsaver.R;
import com.hyunwoong.pestsaver.base.BaseViewModel;
import com.hyunwoong.pestsaver.core.activity.SignUpActivity;
import com.hyunwoong.pestsaver.core.model.User;
import com.hyunwoong.pestsaver.util.data.Data;
import com.hyunwoong.pestsaver.util.view.OnXML;

/**
 * @author : Hyunwoong
 * @when : 2019-11-18 오전 12:51
 * @homepage : https://github.com/gusdnd852
 */
public class SignUpViewModel extends BaseViewModel {
    public Data<String> id = new Data<>();
    public Data<String> pw = new Data<>();
    public Data<String> name = new Data<>();

    @OnXML(resid = R.layout.sign_up)
    public void signUp(SignUpActivity activity) {
        User user = new User();
        user.setId(id.get());
        user.setPw(pw.get());
        user.setName(name.get());

        if (activity.check(user))
            activity.signUp(user);
    }
}
