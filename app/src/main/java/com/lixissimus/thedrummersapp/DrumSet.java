package com.lixissimus.thedrummersapp;


import java.util.HashMap;
import java.util.Map;

public class DrumSet {

    private int _id;
    private String configName;
    
    private Map<String, Drum> drums = new HashMap<>();
    private String drumsString = "";
    private String freqsString = "";
    private boolean stringsValid = false;

    public DrumSet() {
    }

    public DrumSet(String name) {
        this.configName = name;
    }

    public DrumSet(String name, String drumsString, String freqsString) {
        this.configName = name;

        String[] drumsStringVals = drumsString.split(",");
        String[] freqsStringVals = freqsString.split(",");

        // we need to have twice as many frequencies as drums
        if ((2 * drumsStringVals.length) != freqsStringVals.length) {
            throw new AssertionError();
        }

        int i = 0;
        for (String drumName : drumsStringVals) {
            Drum d = new Drum(drumName);
            d.setBatterFreq(Integer.parseInt(freqsStringVals[i*2].trim()));
            d.setResoFreq(Integer.parseInt(freqsStringVals[i * 2 + 1].trim()));
            addDrum(d);
            i++;
        }
    }

    public void addDrum(Drum drum) {
        if (!drums.containsKey(drum.getName())) {
            drums.put(drum.getName(), drum);
            stringsValid = false;
        }
    }
    
    public void removeDrum(String name) {
        if (drums.containsKey(name)) {
            drums.remove(name);
            stringsValid = false;
        }
    }

    public Drum getDrum(String name) {
        return drums.get(name);
    }

    public void updateDrum(Drum drum) {
        if (drums.containsKey(drum.getName())) {
            drums.put(drum.getName(), drum);
            stringsValid = false;
        }
    }

    public Map<String, Drum> getDrums() {
        return drums;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getDrumsString() {
        if (stringsValid) {
            return drumsString;
        }

        buildStrings();
        return getDrumsString();
    }

    public String getFreqsString() {
        if (stringsValid) {
            return freqsString;
        }

        buildStrings();
        return getFreqsString();
    }

    private void buildStrings() {
        drumsString = "";
        freqsString = "";
        for (Drum drum : drums.values()) {
            drumsString += drum.getName() + ",";
            freqsString += drum.getBatterFreq() + "," + drum.getResoFreq() + ",";
        }

        // remove the last ','
        drumsString = drumsString.substring(0, drumsString.length() - 1);
        freqsString = freqsString.substring(0, freqsString.length() - 1);

        stringsValid = true;
    }


    public static class Drum {

        private String name;
        private int batterFreq;
        private int resoFreq;

        public Drum(String name) {
            this.name = name;
        }

        public Drum(String name, int batterFreq, int resoFreq) {
            this.name = name;
            this.batterFreq = batterFreq;
            this.resoFreq = resoFreq;
        }

        public String getName() {
            return name;
        }

        public int getBatterFreq() {
            return batterFreq;
        }

        public void setBatterFreq(int batterFreq) {
            this.batterFreq = batterFreq;
        }

        public int getResoFreq() {
            return resoFreq;
        }

        public void setResoFreq(int resoFreq) {
            this.resoFreq = resoFreq;
        }
    }
}
