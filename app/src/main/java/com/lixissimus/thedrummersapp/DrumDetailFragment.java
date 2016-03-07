package com.lixissimus.thedrummersapp;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.Yin;

/**
 * A fragment representing a single Drum detail screen.
 * This fragment is either contained in a {@link DrumListActivity}
 * in two-pane mode (on tablets) or a {@link DrumDetailActivity}
 * on handsets.
 */
public class DrumDetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The content this fragment is presenting.
     */
    private DrumSet.Drum drum;

    private Context context;
    private View rootView;

    // recording parameters
    final int SAMPLING_RATE = 8000;
    private boolean recording = false;

    private TextView targetFreqText;
    private TextView currentFreqText;
    private TextView freqDiffText;
    private TextView probabilityText;

    private int targetFreq;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DrumDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity().getApplicationContext();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            TunerContentProvider cp = new TunerContentProvider(context);
            drum = cp.getDrumsMap().get(getArguments().getString(ARG_ITEM_ID));

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRecording();
    }

    @Override
    public void onResume() {
        super.onResume();
        startRecording();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.drum_detail, container, false);

        targetFreqText = (TextView) rootView.findViewById(R.id.targetFreqText);
        currentFreqText = (TextView) rootView.findViewById(R.id.currentFreqText);
        freqDiffText = (TextView) rootView.findViewById(R.id.freqDiffText);
        probabilityText = (TextView) rootView.findViewById(R.id.probabilityText);

        targetFreqText.setText(String.format("%d", drum.getBatterFreq()));
        targetFreq = drum.getBatterFreq();

        ((TextView) rootView.findViewById(R.id.drumDetailNameText)).setText(drum.getName());
        ((TextView) rootView.findViewById(R.id.batterFreqText)).setText(String.format("%d", drum.getBatterFreq()));
        ((TextView) rootView.findViewById(R.id.resoFreqText)).setText(String.format("%d", drum.getResoFreq()));


        return rootView;
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

        currentFreqText.post(new Runnable() {
            @Override
            public void run() {
                currentFreqText.setText(String.format("%.1f", roundedFreq));
                freqDiffText.setText(String.format("%.1f", roundedFreq - targetFreq));
                probabilityText.setText(String.format("%.2f", result.getProbability()));
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
