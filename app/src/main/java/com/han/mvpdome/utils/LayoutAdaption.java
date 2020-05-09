package com.han.mvpdome.utils;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import io.reactivex.annotations.NonNull;

public class LayoutAdaption {
    private static float sNonCompatDensity;
    private static float sNonCompatScaledDensity;

    public static void setCustomDensity(@NonNull AppCompatActivity activity, @NonNull final Application application) {
        DisplayMetrics appDispayMetrics = application.getResources().getDisplayMetrics();

        if (sNonCompatDensity == 0) {
            sNonCompatDensity = appDispayMetrics.density;
            sNonCompatScaledDensity = appDispayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNonCompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        final float targetDensity = appDispayMetrics.widthPixels / 360;
        final float targetScaledDensity = targetDensity * (sNonCompatScaledDensity / sNonCompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDispayMetrics.density = targetDensity;
        appDispayMetrics.scaledDensity = targetScaledDensity;
        appDispayMetrics.densityDpi = targetDensityDpi;

        DisplayMetrics activityMetrics = activity.getResources().getDisplayMetrics();
        activityMetrics.density = targetDensity;
        activityMetrics.scaledDensity = targetScaledDensity;
        activityMetrics.densityDpi = targetDensityDpi;

        Log.e("jinr", "setCustomDensity: " + targetDensity + "---" + targetScaledDensity + "---" + targetDensityDpi);
    }

}
