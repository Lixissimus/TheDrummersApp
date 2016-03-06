package com.lixissimus.thedrummersapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Drum detail screen.
 * This fragment is either contained in a {@link DrumListActivity}
 * in two-pane mode (on tablets) or a {@link DrumDetailActivity}
 * on handsets.
 */
public class DrumDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private TunerContentProvider.DrumItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DrumDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = TunerContentProvider.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.drum_detail, container, false);

        ((TextView) rootView.findViewById(R.id.drumDetailNameText)).setText(mItem.name);
        ((TextView) rootView.findViewById(R.id.batterFreqText)).setText(String.format("%d", mItem.freqBatter));
        ((TextView) rootView.findViewById(R.id.resoFreqText)).setText(String.format("%d", mItem.freqReso));


        return rootView;
    }
}
