package com.example.lumibeev1;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.provider.Settings;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    ImageView img;
    Button btn1;
    Button btnn;

    SeekBar ssBar;
    SeekBar ssBar1;

    private Timer myTimer;
    int SecCounter=0;
    int LayerCounter=0;

    boolean printstarted=false;
    int curSecs=35;
    int movSecs=10;
    int waitSecs=2;

    int firstLayerSecs=100;

    int maxNlayers=100;

    int printerStatus=0;
    // 0 idle;  1 curing,  2 moving,  3 waiting


    int lastbar=-1;  // 0 curing, 1 maxnlayer, 2pre, 3wait, 4mov, 5 preview 6 maxlum

    float maxlum=1;


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ///PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
       ///// PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
       //// wl.acquire();


        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = 1F;
        getWindow().setAttributes(layout);



        ///////// test disable luminsota' auto su s6
    /*    Context context = getApplicationContext();
        // Change the screen brightness change mode to manual.
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        // Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
        // It will also change the screen brightness for the device.
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);*/



         // --- creo il timer ----
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 1000);


        // --- Bottone 1 ---
        img = (ImageView) findViewById(R.id.imageView1);

        btn1 = (Button) findViewById(R.id.loadbtn);
        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

          // se stampa non ancora partita
          if (printstarted==false) {
              img.setImageResource(R.drawable.blk);

              btnn = (Button) findViewById(R.id.loadbtn);
              btnn.setText("Started");

              printstarted = true;
              ssBar=(SeekBar)findViewById(R.id.seekBarMov);
              ssBar.setVisibility(INVISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarCuring);
              ssBar.setVisibility(INVISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarWait);
              ssBar.setVisibility(INVISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarFirst);
              ssBar.setVisibility(INVISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarNlayers);
              ssBar.setVisibility(INVISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarPrev);
              ssBar.setVisibility(INVISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarMaxLum);
              ssBar.setVisibility(INVISIBLE);
              TextView tex1 =  findViewById(R.id.textLum);
              tex1.setVisibility(INVISIBLE);

              SecCounter=0;
              LayerCounter=0;
              printerStatus=0;
              }
          else
          {
              img.setImageResource(R.drawable.blk);

              btnn = (Button) findViewById(R.id.loadbtn);
              btnn.setText("Stopped");

              printstarted = false;
              ssBar=(SeekBar)findViewById(R.id.seekBarMov);
              ssBar.setVisibility(VISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarCuring);
              ssBar.setVisibility(VISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarWait);
              ssBar.setVisibility(VISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarFirst);
              ssBar.setVisibility(VISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarNlayers);
              ssBar.setVisibility(VISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarPrev);
              ssBar.setVisibility(VISIBLE);
              ssBar=(SeekBar)findViewById(R.id.seekBarMaxLum);
              ssBar.setVisibility(VISIBLE);
              TextView tex1 =  findViewById(R.id.textLum);
              tex1.setVisibility(VISIBLE);

          }


            }

        });


        // Incrementa ultima scroll bar usata, manualmente di 1
        btn1 = (Button) findViewById(R.id.plusbtn);
        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // se stampa non ancora partita
                if (lastbar!=-1) {
                    // 0 curing, 1 maxnlayer, 2pre, 3wait, 4mov, 5 preview==false
                    if (lastbar==0) { ssBar=(SeekBar)findViewById(R.id.seekBarCuring);}
                    if (lastbar==1) { ssBar=(SeekBar)findViewById(R.id.seekBarNlayers);}
                    if (lastbar==2) { ssBar=(SeekBar)findViewById(R.id.seekBarFirst);}
                    if (lastbar==3) { ssBar=(SeekBar)findViewById(R.id.seekBarWait);}
                    if (lastbar==4) { ssBar=(SeekBar)findViewById(R.id.seekBarMov);}
                    if (lastbar==5) { ssBar=(SeekBar)findViewById(R.id.seekBarPrev);}
                    ssBar.incrementProgressBy(1);
                }
            }

                });


        // Decrementa ultima scroll bar usata, manualmente di 1
        btn1 = (Button) findViewById(R.id.minbtn);
        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // se stampa non ancora partita
                if (lastbar!=-1) {
                    // 0 curing, 1 maxnlayer, 2pre, 3wait, 4mov, 5 preview==false
                    if (lastbar==0) { ssBar=(SeekBar)findViewById(R.id.seekBarCuring);}
                    if (lastbar==1) { ssBar=(SeekBar)findViewById(R.id.seekBarNlayers);}
                    if (lastbar==2) { ssBar=(SeekBar)findViewById(R.id.seekBarFirst);}
                    if (lastbar==3) { ssBar=(SeekBar)findViewById(R.id.seekBarWait);}
                    if (lastbar==4) { ssBar=(SeekBar)findViewById(R.id.seekBarMov);}
                    if (lastbar==5) { ssBar=(SeekBar)findViewById(R.id.seekBarPrev);}
                    ssBar.incrementProgressBy(-1);
                }
            }

        });


        // --- scroll barr secondi curing ---
        ssBar=(SeekBar)findViewById(R.id.seekBarCuring);
        // perform seek bar change listener event used for getting the progress value
        ssBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                curSecs=progress;
                TextView tex1 =  findViewById(R.id.textCuring);
                tex1.setText("CUR "+progressChangedValue+"s");
                lastbar=0;  // 0 curing, 1 maxnlayer, 2pre, 3wait, 4mov, 5 preview
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
               // Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue,
                    //    Toast.LENGTH_SHORT).show();
            }
        });

        // --- scroll barr secondi moving ---
        ssBar=(SeekBar)findViewById(R.id.seekBarMov);
        // perform seek bar change listener event used for getting the progress value
        ssBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                movSecs=progress;
                TextView tex1 =  findViewById(R.id.textMov);
                tex1.setText("MOV "+progressChangedValue+"s");
                lastbar=4;  // 0 curing, 1 maxnlayer, 2pre, 3wait, 4mov, 5 preview
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue,
                //    Toast.LENGTH_SHORT).show();
            }
        });

        // --- scroll barr secondi waiting ---
        ssBar=(SeekBar)findViewById(R.id.seekBarWait);
        // perform seek bar change listener event used for getting the progress value
        ssBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                waitSecs=progress;
                TextView tex1 =  findViewById(R.id.textWait);
                tex1.setText("WAIT "+progressChangedValue+"s");
                lastbar=3;  // 0 curing, 1 maxnlayer, 2pre, 3wait, 4mov, 5 preview
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue,
                //    Toast.LENGTH_SHORT).show();
            }
        });

        // --- scroll barr secondi curing Primi 4 Layer ---
        ssBar=(SeekBar)findViewById(R.id.seekBarFirst);
        // perform seek bar change listener event used for getting the progress value
        ssBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                firstLayerSecs=progress;
                TextView tex1 =  findViewById(R.id.textFirst);
                tex1.setText("PRE "+progressChangedValue+"s");
                lastbar=2;  // 0 curing, 1 maxnlayer, 2pre, 3wait, 4mov, 5 preview
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue,
                //    Toast.LENGTH_SHORT).show();
            }
        });


        // --- scroll barr secondi Numero di layer totali ---
        ssBar=(SeekBar)findViewById(R.id.seekBarNlayers);
        // perform seek bar change listener event used for getting the progress value
        ssBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                maxNlayers=progress;
                lastbar=1;  // 0 curing, 1 maxnlayer, 2pre, 3wait, 4mov, 5 preview

                ssBar1=(SeekBar)findViewById(R.id.seekBarPrev);
                ssBar1.setMax(progress);

                TextView tex1 =  findViewById(R.id.textLayers);
                tex1.setText("L"+progressChangedValue+"s");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue,
                //    Toast.LENGTH_SHORT).show();
            }
        });

        // --- scroll barr anteprima di layer  ---
        ssBar=(SeekBar)findViewById(R.id.seekBarPrev);
        // perform seek bar change listener event used for getting the progress value
        ssBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                ///lastbar=5;  // 0 curing, 1 maxnlayer, 2pre, 3wait, 4mov, 5 preview
                String uri = "@drawable/a" + String.valueOf(progress); //imname without extension



                int imageResource = getResources().getIdentifier(uri, null, getPackageName()); //get image  resource
                if (imageResource!=0) { Drawable res = getResources().getDrawable(imageResource); // convert into drawble
                img.setImageDrawable(res); // set as image
                }

                TextView tex1 =  findViewById(R.id.textLayers);
                tex1.setText("Lp"+progressChangedValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue,
                //    Toast.LENGTH_SHORT).show();
            }
        });



        // --- scroll barr massima luminsosita' ---
        ssBar=(SeekBar)findViewById(R.id.seekBarMaxLum);
        // perform seek bar change listener event used for getting the progress value
        ssBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                maxlum =  progress/(float)255;

                WindowManager.LayoutParams layout = getWindow().getAttributes();
                layout.screenBrightness = maxlum;
                getWindow().setAttributes(layout);

                TextView tex1 =  findViewById(R.id.textLum);
                tex1.setText("LU"+progressChangedValue);
                lastbar=6;  // 0 curing, 1 maxnlayer, 2pre, 3wait, 4mov, 5 preview
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue,
                //    Toast.LENGTH_SHORT).show();
            }
        });


        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.imageView1);


        // Set up the user interaction to manually show or hide the system UI.
       // mContentView.setOnClickListener(new View.OnClickListener() {
       //     @Override
         //   public void onClick(View view) {
           //     toggle();
           // }
        //});

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
       // findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }


    //public static int getImageId(Context context, String imageName) {
      //  return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    //}

    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.

            //Do something to the UI thread here
            //img.setImageResource(R.drawable.a0003_240_100);
            //Context c = getApplicationContext();
            //img.setImageResource(getImageId(this, "a"+String.valueOf(count1)));

            if (printstarted == true) {


                // *** Prima cosa incremento il contatore dei secondi ***
                SecCounter++;

                // Ora lo stato della stampante
                if (printerStatus == 0) {
                    printerStatus = 1;
                    /*carica la immagine*/
                    if ((LayerCounter >= 1) && (LayerCounter <= maxNlayers)) {
                        String uri = "@drawable/a" + String.valueOf(LayerCounter); //imname without extension
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName()); //get image  resource

                        if (imageResource!=0)
                        {Drawable res = getResources().getDrawable(imageResource); // convert into drawble
                        img.setImageDrawable(res); } // set as image



                        WindowManager.LayoutParams layout = getWindow().getAttributes();
                        layout.screenBrightness = maxlum;//layout.screenBrightness = 1F;
                        getWindow().setAttributes(layout);

                    }
                } else if ( // Se sono i primi 4 layer il curing dura 100s, se no dura CurSecs secondi
                        (( (SecCounter > curSecs) && (LayerCounter>4) )  ||  ( (SecCounter > firstLayerSecs) && (LayerCounter<=4) ))
                        && (printerStatus == 1)
                          ) {
                    SecCounter = 0;
                    printerStatus = 2;
                    /*nasconde immagine*/
                    String uri = "@drawable/blk";
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName()); //get image  resource
                    Drawable res = getResources().getDrawable(imageResource); // convert into drawble
                    img.setImageDrawable(res); // set as image

                    // e abbassa luminosita'
                    WindowManager.LayoutParams layout = getWindow().getAttributes();
                    layout.screenBrightness = 00;
                    getWindow().setAttributes(layout);

                    /*colpo di flash*/
                    // --- led flash on ---
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        String cameraId = null;
                        try {

                            cameraId = camManager.getCameraIdList()[0];


                            if (LayerCounter < maxNlayers) // Se non e' ultimo layer, un colpo di flash
                              { camManager.setTorchMode(cameraId, true);   //Turn ON
                              try {
                                  //set time in mili
                                  Thread.sleep(75);
                              }catch (Exception e){
                                  e.printStackTrace();
                              }
                              // -- led off---
                              camManager.setTorchMode(cameraId, false); }
                            else // se e' ultimo layer, DUE colpi di flash segnalano che e' finita la stampa
                            { camManager.setTorchMode(cameraId, true);   //Turn ON
                                try {
                                    //set time in mili
                                    Thread.sleep(75);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                camManager.setTorchMode(cameraId, false); // turn OFF
                                try {
                                    //set time in mili
                                    Thread.sleep(200);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                camManager.setTorchMode(cameraId, true);   //Turn ON
                                try {
                                    //set time in mili
                                    Thread.sleep(75);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                camManager.setTorchMode(cameraId, false); // turn OFF
                            }




                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } else if ((SecCounter > movSecs) && (printerStatus == 2)) {
                    SecCounter = 0;
                    printerStatus = 3; /*attende solo fine movimento*/
                } else if ((SecCounter > waitSecs) && (printerStatus == 3)) {
                    SecCounter = 0;
                    printerStatus = 0;
                    LayerCounter++;
                }
                if (LayerCounter > maxNlayers) {
                    printstarted = false; /*la stampa e' finita*/
                }


                // mostra seconds count
                TextView tex1 = findViewById(R.id.textSecs);
                if (printerStatus == 0) {
                    tex1.setText(" I " + SecCounter + "s");
                }
                if (printerStatus == 1) {
                    tex1.setText(" C " + SecCounter + "s");
                }
                if (printerStatus == 2) {
                    tex1.setText(" M " + SecCounter + "s");
                }
                if (printerStatus == 3) {
                    tex1.setText(" W " + SecCounter + "s");
                }

                //mostra layer count
                tex1 = findViewById(R.id.textLayers);
                tex1.setText("L" + LayerCounter);

            }
        }

    };

}
