package lv.edi.lv.mood_muki;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muki.core.MukiCupApi;
import com.muki.core.MukiCupCallback;
import com.muki.core.model.Action;
import com.muki.core.model.DeviceInfo;
import com.muki.core.model.ErrorCode;
import com.muki.core.model.ImageProperties;
import com.muki.core.util.ImageUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "mood-muki activity";
    private EditText mSerialNumberEdit;
    private TextView mCupIdText;
    private TextView mDeviceInfoText;
    private ImageView mCupImage;
    private SeekBar mContrastSeekBar;
    private ProgressDialog mProgressDialog;

    private Bitmap mImage;
    private Bitmap b; // bitmap generated with our content
    private Bitmap coffeMugBM;
    private Bitmap excerciseBM;
    private Bitmap greatBM;
    private Bitmap sleepyBM;
    private Bitmap greatInvBM;
    private Bitmap dangerBM;
    private int mContrast = ImageProperties.DEFAULT_CONTRACT;

    private String mCupId="PAULIG_MUKI_3C1DF1";
    private MoodApplication app;
    private Timer timer;

    int SIZEX=176, SIZEY = 264;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (MoodApplication) getApplication();
        app.mainActivity = this;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading. Please wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        app.mMukiCupApi = new MukiCupApi(getApplicationContext(), new MukiCupCallback() {
            @Override
            public void onCupConnected() {
                //showToast("Cup connected");
            }

            @Override
            public void onCupDisconnected() {
                showToast("Cup disconnected");
            }

            @Override
            public void onDeviceInfo(DeviceInfo deviceInfo) {
                hideProgress();
                mDeviceInfoText.setText(deviceInfo.toString());
            }

            @Override
            public void onImageCleared() {
                showToast("Image cleared");
            }

            @Override
            public void onImageSent() {
                showToast("Image sent");
            }

            @Override
            public void onError(Action action, ErrorCode errorCode) {
                //showToast("Error:" + errorCode + " on action:" + action);
            }
        });

        //mSerialNumberEdit = (EditText) findViewById(R.id.serailNumberText);
        //mCupIdText = (TextView) findViewById(R.id.cupIdText);
        //mDeviceInfoText = (TextView) findViewById(R.id.deviceInfoText);
        mCupImage = (ImageView) findViewById(R.id.imageSrc);
        //mContrastSeekBar = (SeekBar) findViewById(R.id.contrastSeekBar);
//        mContrastSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                mContrast = i - 100;
//                showProgress();
//                //setupImage();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        //reset(null);

        // get coffe mug bitmap
        coffeMugBM = BitmapFactory.decodeResource(getResources(), R.drawable.coffe_cup);
        coffeMugBM = Bitmap.createScaledBitmap (coffeMugBM, 30, 30, true);

        excerciseBM = BitmapFactory.decodeResource(getResources(), R.drawable.exercise);
        excerciseBM = Bitmap.createScaledBitmap (excerciseBM, 30, 30, true);

        greatBM = BitmapFactory.decodeResource(getResources(), R.drawable.great);
        greatBM = Bitmap.createScaledBitmap (greatBM, 30, 30, true);

        greatInvBM = BitmapFactory.decodeResource(getResources(), R.drawable.greatn);
        greatInvBM = Bitmap.createScaledBitmap (greatInvBM, 30, 30, true);

        sleepyBM = BitmapFactory.decodeResource(getResources(), R.drawable.sleepy);
        sleepyBM = Bitmap.createScaledBitmap (sleepyBM, 30, 30, false);

        dangerBM = BitmapFactory.decodeResource(getResources(), R.drawable.dangern);
        dangerBM = Bitmap.createScaledBitmap (dangerBM, 30, 30, false);

        b =  Bitmap.createBitmap(SIZEX, SIZEY, Bitmap.Config.ARGB_8888);
        mCupImage.setImageBitmap(b);


        timer = new Timer();


        timer.scheduleAtFixedRate(new TimerTask(){

            final Vector<SleepState> states = SleepState.getAllStates();
            String inputLine;
            String response;
            public void run(){
                Log.d(TAG, "TIMER TASK");
                try {
                    URL yahoo = new URL("http://zzzyield.azurewebsites.net/state.php");
                    URLConnection yc = yahoo.openConnection();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    yc.getInputStream()));

                    while ((inputLine = in.readLine()) != null){
                        response = new String(inputLine);
                        Log.d(TAG, "response line " + inputLine);



                    }
                    Log.d(TAG, "http closed!");
                        in.close();
                } catch(Exception ex){
                    Log.d(TAG, "Network exception");
                }
                Log.d(TAG, "input line "+inputLine);
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        SleepState state = new SleepState(response);
                        updateScreen(state);
                    }
                });


            }
        }, 0, 15000);






    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(timer!=null){
            timer.cancel();

        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //mCupIdText.setText(mCupId);
    }

    private void setupImage() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap result = Bitmap.createBitmap(b);
                ImageUtils.convertImageToCupImage(result, mContrast);
                return result;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                //mCupImage.setImageBitmap(bitmap);
                hideProgress();
            }
        }.execute();
    }

    public void crop(View view) {
        showProgress();
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
        mImage = ImageUtils.cropImage(image, new Point(100, 0));
        image.recycle();
        setupImage();
    }

    public void reset(View view) {
        showProgress();
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);

        Log.d(TAG, "bitmap config: "+image.getConfig());

        mImage = ImageUtils.scaleBitmapToCupSize(image);
        mContrast = ImageProperties.DEFAULT_CONTRACT;
        //
        // mContrastSeekBar.setProgress(100);
        setupImage();
        image.recycle();
    }

    public void send(View view) {
        //showProgress();
        app.mMukiCupApi.sendImage(b, new ImageProperties(mContrast), mCupId);
    }

    public void clear(View view) {
        showProgress();
        app.mMukiCupApi.clearImage(mCupId);
    }

    public void request() {
        Log.d(TAG, "request cup id");
        //String serialNumber = mSerialNumberEdit.getText().toString();
        showProgress();
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    String serialNumber = strings[0];
                    return MukiCupApi.cupIdentifierFromSerialNumber(serialNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                mCupId = s;
                Log.d(TAG, "Obtained cub ID: "+mCupId);
                mCupIdText.setText(mCupId);
                hideProgress();
            }
        }.execute(app.mukiCode);
    }

    public void deviceInfo(View view) {
        showProgress();
        app.mMukiCupApi.getDeviceInfo(mCupId);
    }

    private void showToast(final String text) {
        hideProgress();
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void showProgress() {
        mProgressDialog.show();
    }

    private void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateScreen(SleepState sleepState){
        // drawing own bitmap

        b =  Bitmap.createBitmap(SIZEX, SIZEY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        canvas.drawColor(Color.WHITE);

        // paint readiness
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30.0f);
        canvas.drawText(sleepState.getSleepStateMessage(), 10, 35, textPaint);

        // draw restness level via cup count
        if(sleepState.currentReadiness.equals(SleepState.HARD_DAY)) {
            canvas.drawBitmap(coffeMugBM, 40, 40, null);
            canvas.drawBitmap(coffeMugBM, 70, 40, null);
            canvas.drawBitmap(coffeMugBM, 100, 40, null);
        }

        if(sleepState.currentReadiness.equals(SleepState.TIRED)) {
            canvas.drawBitmap(coffeMugBM, 55, 40, null);
            canvas.drawBitmap(coffeMugBM, 85, 40, null);
        }

        if(sleepState.currentReadiness.equals(SleepState.FRESH)) {
            canvas.drawBitmap(coffeMugBM, 70, 40, null);
        }

        if(sleepState.currentReadiness.equals(SleepState.READY)){
            canvas.drawBitmap(greatBM, 70, 40, null);
        }

        if(sleepState.currentReadiness.equals(SleepState.NOT_SLEPT)){
            canvas.drawBitmap(sleepyBM, 70, 40, null);
        }

        // draw advice text
        Paint textPaint2 = new Paint();
        textPaint2.setColor(Color.BLACK);
        textPaint2.setTypeface(Typeface.create((Typeface)null, Typeface.BOLD));
        textPaint2.setTextSize(16.0f);
        String[] advice1 = sleepState.getSleepStateAdvice();
        canvas.drawText(advice1[0], 10,  85, textPaint2);
        canvas.drawText(advice1[1], 10, 103, textPaint2);
        canvas.drawText(advice1[2], 10, 121, textPaint2);


        // draw lower section background
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0 , SIZEY/2, SIZEX, SIZEY, backgroundPaint);

        // fill lower section
        // activity state

        Paint textPaint3 = new Paint();
        textPaint3.setColor(Color.WHITE);
        textPaint3.setTextSize(30.0f);
        canvas.drawText(sleepState.getActivityMessage(), 10, SIZEY/2+5+30, textPaint3);

        // paint excercise icons

        if(sleepState.currentActivity.equals(SleepState.BALANCED)){
            canvas.drawBitmap(greatInvBM, 70, SIZEY/2 + 40, null);
        }

        if(sleepState.currentActivity.equals(SleepState.INACTIVE)){
            canvas.drawBitmap(excerciseBM, 70, SIZEY/2 + 40, null);
        }

        if(sleepState.currentActivity.equals(SleepState.STRESSED)){
            canvas.drawBitmap(dangerBM, 70, SIZEY/2 + 40, null);
        }
        // draw restness level via cup count
//
//        canvas.drawBitmap(excerciseBM, 40, SIZEY/2 + 40, null);
//        canvas.drawBitmap(excerciseBM, 70, SIZEY/2 + 40, null);

        // draw advice
        Paint textPaint4 = new Paint();
        textPaint4.setColor(Color.WHITE);
        textPaint4.setTypeface(Typeface.create((Typeface)null, Typeface.BOLD));
        textPaint4.setTextSize(16.0f);
        String[] advice2 = sleepState.getActivityAdvice();
        canvas.drawText(advice2[0], 10,  SIZEY/2 + 85, textPaint4);
        canvas.drawText(advice2[1], 10, SIZEY/2 + 103, textPaint4);
        canvas.drawText(advice2[2], 10, SIZEY/2 + 121, textPaint4);


//        textPaint2.setColor(Color.BLACK);
//        textPaint2.setTextSize(20.0f);
        //textPaint2.setTypeface(Typeface.create((Typeface)null, Typeface.BOLD_ITALIC))

        mCupImage.setImageBitmap(b);
        app.mMukiCupApi.sendImage(b, new ImageProperties(mContrast), mCupId);
    }
}
