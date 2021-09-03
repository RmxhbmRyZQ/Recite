package cn.flandre.review.data.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import cn.flandre.review.logic.enumerate.ReviewMode;

public class ShareHelper {
    public static int getDatabaseVersion(Context context, int def){
        SharedPreferences sharedPreferences = context.getSharedPreferences("review", Activity.MODE_PRIVATE);
        return sharedPreferences.getInt("version", def);
    }

    public static void setDatabaseVersion(Context context, int version){
        SharedPreferences sharedPreferences = context.getSharedPreferences("review", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("version", version);
        edit.apply();
    }

    public static ReviewMode getReviewMode(Context context, ReviewMode def){
        SharedPreferences sharedPreferences = context.getSharedPreferences("review", Activity.MODE_PRIVATE);
        return ReviewMode.parseInt(sharedPreferences.getInt("reviewMode", def.getIndex()));
    }
    public static void setReviewMode(Context context, ReviewMode reviewMode){
        SharedPreferences sharedPreferences = context.getSharedPreferences("review", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("reviewMode", reviewMode.getIndex());
        edit.apply();
    }
}
