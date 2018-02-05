package ca.aswinualberta.subbook;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Units {

    public static int pxToDP(int px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float density = (float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        return (int)pxToDP((double) px, (int)density);
    }

    public static int pxToDP(int px, int density) {
        return (int)pxToDP((double) px, density);
    }

    public static double pxToDP(double px, int density) {
        return px / density;
    }

    public static int dpToPX(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float density = (float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        return (int)dpToPX((double) dp, (int)density);
    }

    public static int dpToPX(int dp, int density) {
        return (int)dpToPX((double) dp, density);
    }

    public static double dpToPX(double dp, int density) {
        return dp * density;
    }
}
