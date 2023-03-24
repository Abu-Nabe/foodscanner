package com.example.unifood.Main.PushNotification;
public class Data
{
    private int icon;
    private String body;
    private String title;

    public Data(int icon, String body, String title) {
        this.icon = icon;
        this.body = body;
        this.title = title;
    }

    public Data()
    {

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

