package com.belicoffee.Manager;

public interface IMainView {
    void openScreen(int screenId);

    void openScreen(int screenId, String hisId, String hisName,int type);

    void requestPermission();
}
