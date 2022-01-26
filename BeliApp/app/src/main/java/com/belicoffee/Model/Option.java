package com.belicoffee.Model;

public class Option {
    int iconImage;
    String titleText;

    public Option(int iconImage, String titleText) {
        this.iconImage = iconImage;
        this.titleText = titleText;
    }

    public int getIconImage() {
        return iconImage;
    }

    public void setIconImage(int iconImage) {
        this.iconImage = iconImage;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }
}
