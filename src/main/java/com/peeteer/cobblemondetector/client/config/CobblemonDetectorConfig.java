package com.peeteer.cobblemondetector.client.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.peeteer.cobblemondetector.client.util.PokemonLists;

public class CobblemonDetectorConfig {
    public boolean broadcastAllLegendaries = true;
    public boolean broadcastAllMythics = true;
    public boolean broadcastAllStarter = false;
    public String[] broadcastAllowlist = {"Mew", "Mewtwo"};

    public String[] getCombinedAllowlist(){
        List<String> combinedList = new ArrayList<String>();

        if (this.broadcastAllLegendaries) {
            combinedList.addAll(new ArrayList<String>(Arrays.asList(PokemonLists.legendaries)));
        }
        if (this.broadcastAllMythics) {
            combinedList.addAll(new ArrayList<String>(Arrays.asList(PokemonLists.mythics)));
        }
        if (this.broadcastAllStarter) {
            combinedList.addAll(new ArrayList<String>(Arrays.asList(PokemonLists.starter)));
        }

        combinedList.addAll(new ArrayList<String>(Arrays.asList(this.broadcastAllowlist)));

        // turn everything lowercase just in case
        for (int i = 0; i < combinedList.size(); i++) {
            combinedList.set(i, combinedList.get(i).toLowerCase());
        }

        return combinedList.toArray(new String[combinedList.size()]);
    }
}
