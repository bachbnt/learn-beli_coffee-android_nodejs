package com.belicoffee.Manager;

public class ScreenManager {
    IMainView iMainView;
    static ScreenManager screenManager;

    private ScreenManager() {
    }

    public static ScreenManager getInstance() {
        if (screenManager == null)
            screenManager = new ScreenManager();
        return screenManager;
    }

    public void setListener(IMainView iMainView) {
        this.iMainView = iMainView;
    }

    public void openScreen(int screenId) {
        iMainView.openScreen(screenId);
    }

    public void openScreen(int screenId, String hisId, String hisName,int type) {
        iMainView.openScreen(screenId, hisId,hisName,type);
    }

}
