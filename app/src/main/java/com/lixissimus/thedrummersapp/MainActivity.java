package com.lixissimus.thedrummersapp;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.Yin;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DrummersApp";

    final int SAMPLING_RATE = 8000;

    private boolean recording = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Stop recording due to onPause event");
        stopRecording();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Start recording due to onResume event");
        startRecording();
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
                        final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
                        final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
                        final int BUFFER_SIZE = AudioRecord.getMinBufferSize(
                                SAMPLING_RATE, CHANNEL, FORMAT);

                        short[] buffer = new short[BUFFER_SIZE];

                        AudioRecord recorder = new AudioRecord(
                                MediaRecorder.AudioSource.MIC,
                                SAMPLING_RATE,
                                CHANNEL,
                                FORMAT,
                                BUFFER_SIZE * 10
                        );

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
}
