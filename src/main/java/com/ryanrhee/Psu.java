package com.ryanrhee;

class Psu extends PcPart {
    private int wattageOutput;

    private float length;

    public Psu(String partName, int partPrice, int partWattage,
               int psuWattageOutput,
               float psuLength) {
        super(partName, PcPartType.PSU, partPrice, partWattage);

        wattageOutput = psuWattageOutput;
        length = psuLength;
    }

    public int getWattageOutput() {
        return wattageOutput;
    }

    public float getLength() {
        return length;
    }
}
