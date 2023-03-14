package com.bancomer.bbva.bbvamovilidad;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ObservableData extends BaseObservable {
    private String resultService;
    private String accesstoken;
    private String userLogin;
    private String registerNotifier;

    @Bindable
    public String getResultService() {
        return resultService;
    }
    @Bindable
    public String getAccesstoken() {
        return accesstoken;
    }
    @Bindable
    public String getUserLogin() {
        return userLogin;
    }

    @Bindable
    public String getRegisterNotifier() {
        return registerNotifier;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
        notifyPropertyChanged(BR.accesstoken);
    }

    public void setResultService(String resultService) {
        this.resultService = resultService;
        notifyPropertyChanged(BR.resultService);
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
        notifyPropertyChanged(BR.userLogin);
    }

    public void setRegisterNotifier(String registerNotifier) {
        this.registerNotifier = registerNotifier;
        notifyPropertyChanged(BR.registerNotifier);
    }
}

