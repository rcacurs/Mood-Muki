package lv.edi.lv.mood_muki;

import java.util.Vector;

/**
 * Created by richards on 16.26.11.
 */

public class SleepState {
    public static final String READY = "READY";
    public static final String FRESH = "FRESH";
    public static final String TIRED = "TIRED";
    public static final String HARD_DAY = "HARD_DAY";
    public static final String NOT_SLEPT = "NOT_SLEPT";

    public static final String BALANCED = "BALANCED";
    public static final String INACTIVE = "INACTIVE";
    public static final String STRESSED = "STRESSED";
    public static final String NO_DATA = "NO_DATA";

    public String currentReadiness = "NO_DATA";
    public String currentActivity = "NO_DATA";

    public SleepState(){
        this(SleepState.NO_DATA, SleepState.NO_DATA);
    }

    public SleepState(String currentReadiness, String currentActivity){
        this.currentReadiness = currentReadiness;
        this.currentActivity = currentActivity;
    }

    public SleepState(SleepState src){
        this(src.currentReadiness, src.currentActivity);
    }

    public SleepState(String httpResponse){
        String[] values = httpResponse.split(" ");
        if(values.length == 3){
            int person = Integer.parseInt(values[0]);
            int readiness = Integer.parseInt(values[1]);
            int activity = Integer.parseInt(values[2]);

            if(readiness==0) currentReadiness = READY;
            if(readiness==1) currentReadiness = FRESH;
            if(readiness==2) currentReadiness = TIRED;
            if(readiness==3) currentReadiness = HARD_DAY;
            if(readiness==4) currentReadiness = NOT_SLEPT;

            if(activity==0) currentActivity = BALANCED;
            if(activity==1) currentActivity = INACTIVE;
            if(activity==2) currentActivity = STRESSED;
        } else{
            this.currentReadiness = NO_DATA;
            this.currentActivity = NO_DATA;
        }

    }

    public String getSleepStateMessage(){

        if(currentReadiness.equals(SleepState.READY)){
            return "Ready to go!";
        }
        if(currentReadiness.equals(SleepState.FRESH)){
            return "Fresh!";
        }

        if(currentReadiness.equals(SleepState.TIRED)){
            return "Tired?";
        }

        if(currentReadiness.equals(SleepState.HARD_DAY)){
            return "Hard day...";
        }

        if(currentReadiness.equals(SleepState.NOT_SLEPT)){
            return "Take a nap!";
        }

        return "Unknown";
    }

    public String[] getSleepStateAdvice(){
        String[] advice = new String[3];
        if(currentReadiness.equals(SleepState.READY)){
            advice[0]="You've slept well!";
            advice[1]="Take one cup";
            advice[2]="for the taste!";
            return advice;
        }

        if(currentReadiness.equals(SleepState.FRESH)){
            advice[0]="Just a bit tired?";
            advice[1]="This coffe will";
            advice[2]="be enough!";
            return advice;
        }

        if(currentReadiness.equals(SleepState.TIRED)){
            advice[0]="Take a 2nd cup";
            advice[1]="in 4h and go to";
            advice[2]="sleep early!";
            return advice;
        }

        if(currentReadiness.equals(SleepState.HARD_DAY)){
            advice[0]="You should sleep";
            advice[1]="more... take 2 more";
            advice[2]="cups every 2.5h";
            return advice;
        }

        if(currentReadiness.equals(SleepState.NOT_SLEPT)){
            advice[0]="You should not be";
            advice[1]="working! Sleep more ";
            advice[2]="to be effective";
            return advice;
        }




        advice[0] = "Current status";
        advice[1] = "of activity";
        advice[2] = "is not known!";
        return advice;

    }

    public String getActivityMessage(){
        if(currentActivity.equals(SleepState.BALANCED)){
            return "Balanced!";
        }

        if(currentActivity.equals(SleepState.INACTIVE)){
            return "Move It!";
        }

        if(currentActivity.equals(SleepState.STRESSED)){
            return "Chill!";
        }
        return "Unknown";
    }

    public String[] getActivityAdvice(){
        String[] advice = new String[3];

        if(currentActivity.equals(SleepState.BALANCED)){
            advice[0]="Keep it up! You are";
            advice[1]="active but not tiring";
            advice[2]="yourself";
            return advice;
        }

        if(currentActivity.equals(SleepState.INACTIVE)){
            advice[0]="Being active for a";
            advice[1]="few minutes every ";
            advice[2]="hour is great for you";
            return advice;
        }

        if(currentActivity.equals(SleepState.STRESSED)){
            advice[0]="You are too active. ";
            advice[1]="Too much stress";
            advice[2]="interferes with sleep";
            return advice;
        }

        advice[0] = "Current status";
        advice[1] = "of activity";
        advice[2] = "is not known!";
        return advice;
    }

    public static Vector<SleepState> getAllStates(){
        Vector<SleepState> states = new Vector<SleepState>(15);

        states.add(new SleepState(SleepState.READY, SleepState.BALANCED));
        states.add(new SleepState(SleepState.READY, SleepState.INACTIVE));
        states.add(new SleepState(SleepState.READY, SleepState.STRESSED));

        states.add(new SleepState(SleepState.FRESH, SleepState.BALANCED));
        states.add(new SleepState(SleepState.FRESH, SleepState.INACTIVE));
        states.add(new SleepState(SleepState.FRESH, SleepState.STRESSED));

        states.add(new SleepState(SleepState.TIRED, SleepState.BALANCED));
        states.add(new SleepState(SleepState.TIRED, SleepState.INACTIVE));
        states.add(new SleepState(SleepState.TIRED, SleepState.STRESSED));

        states.add(new SleepState(SleepState.HARD_DAY, SleepState.BALANCED));
        states.add(new SleepState(SleepState.HARD_DAY, SleepState.INACTIVE));
        states.add(new SleepState(SleepState.HARD_DAY, SleepState.STRESSED));

        states.add(new SleepState(SleepState.NOT_SLEPT, SleepState.BALANCED));
        states.add(new SleepState(SleepState.NOT_SLEPT, SleepState.INACTIVE));
        states.add(new SleepState(SleepState.NOT_SLEPT, SleepState.STRESSED));
        return states;
    }

    public boolean equal(SleepState sleepState){
        if(sleepState.currentReadiness.equals(this.currentReadiness) && sleepState.currentActivity.equals(this.currentActivity)){
            return true;
        } else{
            return false;
        }
    }
}
