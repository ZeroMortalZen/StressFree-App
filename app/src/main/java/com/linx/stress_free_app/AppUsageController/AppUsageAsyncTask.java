package com.linx.stress_free_app.AppUsageController;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.format.Formatter;

import androidx.recyclerview.widget.RecyclerView;

import com.linx.stress_free_app.StressSystem.HelperClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppUsageAsyncTask extends AsyncTask<Void, Void, List<AppUsage>> {
    private Context context;
    private RecyclerView recyclerView;
    private AppUsageAdapter adapter;
    HelperClass helperClass = new HelperClass();

    public AppUsageAsyncTask(Context context, RecyclerView recyclerView, AppUsageAdapter adapter) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
    }

    @Override
    protected List<AppUsage> doInBackground(Void... voids) {
        // Load app usage data here and return a list of AppUsage objects
        List<AppUsage> appUsages = new ArrayList<>();

        //Total AppData Usage(To be stored in Database)
        long totalUsage = 0;

        // load app usage data from PackageManager
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            // Get app icon
            Drawable icon = pm.getApplicationIcon(packageInfo);

            // Get app name
            String appName = pm.getApplicationLabel(packageInfo).toString();

            // Get app usage data
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - 1000 * 60 * 60 * 24, System.currentTimeMillis());
            for (UsageStats usageStats : usageStatsList) {
                if (usageStats.getPackageName().equals(packageInfo.packageName)) {
                    long usageTime = usageStats.getTotalTimeInForeground();
                    totalUsage += usageTime;
                    helperClass.setTotalAppUsage(totalUsage);
                    String appUsage = Formatter.formatShortFileSize(context, usageTime);
                    appUsages.add(new AppUsage(icon, appName, appUsage));
                    break;
                }
            }
        }

        // Sort app usage data in descending order (from most used to least used)
        Collections.sort(appUsages);
        return appUsages;
    }



    @Override
    protected void onPostExecute(List<AppUsage> appUsages) {
        adapter = new AppUsageAdapter(appUsages);
        recyclerView.setAdapter(adapter);
    }
}

