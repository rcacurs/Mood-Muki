package lv.edi.lv.mood_muki;

/**
 * Created by richards on 16.26.11.
 */

public class SleepState {

    private String currentReadiness = "NO_DATA";
    private String currentActivity = "NO_DATA";

    public SleepState(String currentReadiness, String currentActivity){
        this.currentReadiness = currentReadiness;
        this.currentActivity = currentActivity;
    }
}
