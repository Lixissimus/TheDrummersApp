package com.lixissimus.thedrummersapp;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.Yin;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DrummersApp";

    final int SAMPLING_RATE = 8000;

    private boolean recording = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = (Button) findViewById(R.id.startButton);
        Button stopButton = (Button) findViewById(R.id.stopButton);

        startButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        startRecording();
                    }
                }
        );

        stopButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        stopRecording();
                    }
                }
        );
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void startRecording() {
        if (recording) {
            return;
        }
        Log.i(TAG, "Start recording");
        recording = true;

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "micThread started...");

                        final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
                        final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
                        final int BUFFER_SIZE = AudioRecord.getMinBufferSize(
                                SAMPLING_RATE, CHANNEL, FORMAT);

                        short[] buffer = new short[BUFFER_SIZE];

                        Log.d(TAG, "Creating the AudioRecord");
                        AudioRecord recorder = new AudioRecord(
                                MediaRecorder.AudioSource.MIC,
                                SAMPLING_RATE,
                                CHANNEL,
                                FORMAT,
                                BUFFER_SIZE * 10
                        );

                        Log.d(TAG, "AudioRecord recording...");
                        recorder.startRecording();

                        while (recording) {
                            int read = recorder.read(buffer, 0, buffer.length);

                            onNewBufferRead(buffer, read);

                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        recorder.stop();
                        recorder.release();
                    }
                }
        ).start();
    }

    private void stopRecording() {
        if (!recording) {
            return;
        }
        Log.i(TAG, "Stop Recording");
        recording = false;
    }

    private void onNewBufferRead(short[] buffer, int size) {
        Yin pitchDetector = new Yin(SAMPLING_RATE, size);

        final PitchDetectionResult result = pitchDetector.getPitch(shortBufferToFloat(buffer));
        float freq = result.getPitch();
        final float roundedFreq = Math.round(freq * 10) / 10.0f;


        if (freq < 0) {
            return;
        }

        final TextView textField = (TextView) findViewById(R.id.textViewFrequency);

        textField.post(new Runnable() {
            @Override
            public void run() {
                textField.setText(String.format("%.1f", roundedFreq));
            }
        });
    }

    private float[] shortBufferToFloat(short[] sBuffer) {
        float[] fBuffer = new float[sBuffer.length];
        int i = 0;
        for (short b : sBuffer) {
            fBuffer[i] = ((float) b) / (float) Math.pow(2, 15);
            i++;
        }

        return fBuffer;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.lixissimus.thedrummersapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse(null),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.lixissimus.thedrummersapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
