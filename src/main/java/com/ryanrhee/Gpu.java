package com.ryanrhee;

class Gpu extends PcPart {
    private float blenderScore;

    private float rasterizationScore;

    private float length;

    public Gpu(String partName, int partPrice, int partWattage,
               float gpuBlenderScore,
               float gpuRasterizationScore,
               float gpuLength) {
        super(partName, PcPartType.GPU, partPrice, partWattage);

        blenderScore = gpuBlenderScore;
        rasterizationScore = gpuRasterizationScore;
        length = gpuLength;
    }

    public float getBlenderScore() {
        return blenderScore;
    }

    public float getRasterizationScore() {
        return rasterizationScore;
    }

    public float getLength() {
        return length;
    }
}
