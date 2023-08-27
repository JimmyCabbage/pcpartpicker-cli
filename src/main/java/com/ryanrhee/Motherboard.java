package com.ryanrhee;

class Motherboard extends PcPart {
    private MotherboardFormFactor formFactor;

    private int numRamSlots;

    private CpuSocketType cpuSocketType;

    public Motherboard(String partName, int partPrice, int partWattage,
                       MotherboardFormFactor motherboardFormFactor,
                       int motherboardNumRamSlots,
                       CpuSocketType motherboardCpuSocketType) {
        super(partName, PcPartType.MOTHERBOARD, partPrice, partWattage);

        formFactor = motherboardFormFactor;
        numRamSlots = motherboardNumRamSlots;
        cpuSocketType = motherboardCpuSocketType;
    }

    public MotherboardFormFactor getFormFactor() {
        return formFactor;
    }

    public int getNumRamSlots() {
        return numRamSlots;
    }

    public CpuSocketType getCpuSocketType() {
        return cpuSocketType;
    }
}
