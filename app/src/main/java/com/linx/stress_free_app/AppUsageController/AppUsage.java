package com.linx.stress_free_app.AppUsageController;

import android.graphics.drawable.Drawable;

public class AppUsage {
    private Drawable icon;
    private String name;
    private String usage;

    public AppUsage(Drawable icon, String name, String usage) {
        this.icon = icon;
        this.name = name;
        this.usage = usage;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
