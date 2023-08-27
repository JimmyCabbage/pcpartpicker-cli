package com.ryanrhee;

abstract class PcPart {
    private String name;

    private PcPartType type;

    private int price;

    private int wattage;

    public PcPart(String partName, PcPartType partType, int partPrice, int partWattage) {
        name = partName;
        type = partType;
        price = partPrice;
        wattage = partWattage;
    }

    public String getName() {
        return name;
    }

    public PcPartType getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public int getWattage() {
        return wattage;
    }
}
