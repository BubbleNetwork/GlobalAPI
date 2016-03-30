package com.thebubblenetwork.api.global.anticheat;

public class ViolationWrapper{
    private double totalvl,addedvl;
    private String player,violation;

    public ViolationWrapper(String player, String violation, double totalvl, double addedvl) {
        this.totalvl = totalvl;
        this.violation = violation;
        this.player = player;
        this.addedvl = addedvl;
    }

    public double getTotalVL() {
        return totalvl;
    }

    public double getAddedVL() {
        return addedvl;
    }

    public String getPlayer() {
        return player;
    }

    public String getViolation() {
        return violation;
    }
}
