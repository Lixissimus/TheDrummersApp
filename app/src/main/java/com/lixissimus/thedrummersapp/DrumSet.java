package com.lixissimus.thedrummersapp;


import java.util.HashMap;

public class DrumSet {

    private int _id;
    private String configName;
    
    private HashMap<String, Drum> drums = new HashMap<>();
    private String drumsString = "";
    private String freqsString = "";
    private boolean stringsValid = false;

    public DrumSet() {
    }

    public DrumSet(String name) {
        this.configName = name;
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
            drumsString += drum.getName() + ", ";
            freqsString += drum.getBatterFreq() + ", " + drum.getResoFreq() + ", ";
        }

        // remove the last ', '
        drumsString = drumsString.substring(0, drumsString.length() - 2);
        freqsString = freqsString.substring(0, freqsString.length() - 2);

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
