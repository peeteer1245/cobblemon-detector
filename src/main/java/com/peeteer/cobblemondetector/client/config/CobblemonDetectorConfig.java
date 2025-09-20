package com.peeteer.cobblemondetector.client.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.peeteer.cobblemondetector.client.util.PokemonLists;

public class CobblemonDetectorConfig {
    public boolean broadcastAllLegendaries = true;
    public boolean broadcastAllMythics = true;
    public boolean broadcastAllStarter = false;
    public boolean broadcastAllBabies = false;
    public boolean broadcastAllUltraBeasts = false;
    public boolean broadcastAllShinies = true;
    public boolean broadcastAllParadox = false;
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
        if (this.broadcastAllBabies) {
            combinedList.addAll(new ArrayList<String>(Arrays.asList(PokemonLists.babies)));
        }
        if (this.broadcastAllUltraBeasts) {
            combinedList.addAll(new ArrayList<String>(Arrays.asList(PokemonLists.ultra_beasts)));
        }
        if (this.broadcastAllParadox) {
            combinedList.addAll(new ArrayList<String>(Arrays.asList(PokemonLists.paradox_mons)));
        }

        combinedList.addAll(new ArrayList<String>(Arrays.asList(this.broadcastAllowlist)));

        // turn everything lowercase just in case
        for (int i = 0; i < combinedList.size(); i++) {
            combinedList.set(i, combinedList.get(i).toLowerCase());
        }

        return combinedList.toArray(new String[combinedList.size()]);
    }
}
