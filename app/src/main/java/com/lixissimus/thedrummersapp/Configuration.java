package com.lixissimus.thedrummersapp;


import java.util.Arrays;

public class Configuration {

    private int _id;
    private String _name;
    private String _toms;

    public Configuration() {
    }

    public Configuration(String name) {
        this._name = name;
    }

    public Configuration(String _name, int[] toms) {
        this._name = _name;
        set_toms(toms);
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    // return the toms String as an int array again
    public int[] get_toms() {
        String stringVals[] = _toms.split(",");

        int[] intVals = new int[stringVals.length];
        int i = 0;
        for (String s : stringVals) {
            intVals[i] = Integer.parseInt(s.trim());
            i++;
        }

        return intVals;
    }

    // return toms String
    public String get_tomsString() {
        return this._toms;
    }

    // store tom values as String, concatenated with ', '
    public void set_toms(int[] toms) {
        this._toms = Arrays.toString(toms).replaceAll("\\[|\\]", "");
    }
}
