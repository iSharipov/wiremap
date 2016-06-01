package com.isharipov.utils;

import java.util.List;

/**
 * Created by Илья on 31.05.2016.
 */
public class Pair<L, L1> {
    public List<String> getMacList() {
        return macList;
    }

    public void setMacList(List<String> macList) {
        this.macList = macList;
    }

    public List<String> getLevelListl() {
        return levelListl;
    }

    public void setLevelListl(List<String> levelListl) {
        this.levelListl = levelListl;
    }

    private List<String> macList;
    private List<String> levelListl;

    public Pair(List<String> macList, List<String> levelList) {
        this.macList = macList;
        this.levelListl = levelList;
    }
}

