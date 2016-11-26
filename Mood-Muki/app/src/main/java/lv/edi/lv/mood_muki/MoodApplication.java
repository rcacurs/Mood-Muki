package lv.edi.lv.mood_muki;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.muki.core.MukiCupApi;

/**
 * Created by richards on 16.26.11.
 */

public class MoodApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    String mukiCode;
    String mukiKey;
    MainActivity mainActivity;
    private static final String TAG = "mood-muki app";

    MukiCupApi mMukiCupApi;
    @Override
    public void onCreate() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        mukiCode = prefs.getString("setting_mug_code", "0003759");
        Log.d("Mood-Moki app", "preferences set muki code: "+mukiCode);
        super.onCreate();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

        if(key.equals("setting_mug_code")){
            mukiCode = prefs.getString("setting_mug_code", "0003759");
            Log.d(TAG, "Muki code preference changed to " + mukiCode);

            if(mainActivity!=null){
                Log.d(TAG, "preparing to request cup id");
                //mainActivity.request();
            }
        }

    }
}
