package com.ryanrhee;

class Case extends PcPart {
    private MotherboardFormFactor formFactor;

    private float maxPsuSize;

    private float maxGpuSize;

    public Case(String partName, int partPrice, int partWattage,
                MotherboardFormFactor caseFormFactor,
                float caseMaxPsuSize,
                float caseMaxGpuSize) {
        super(partName, PcPartType.CASE, partPrice, partWattage);

        formFactor = caseFormFactor;
        maxPsuSize = caseMaxPsuSize;
        maxGpuSize = caseMaxGpuSize;
    }

    public MotherboardFormFactor getFormFactor() {
        return formFactor;
    }

    public float getMaxPsuSize() {
        return maxPsuSize;
    }

    public float getMaxGpuSize() {
        return maxGpuSize;
    }
}
