package com.ryanrhee;

class Cpu extends PcPart {
    private float clockSpeed;

    private int coreCount;

    private boolean hasIntegratedGraphics;

    private CpuSocketType socketType;

    public Cpu(String partName, int partPrice, int partWattage,
               float cpuClockSpeed,
               int cpuCoreCount,
               boolean cpuHasIntegratedGraphics,
               CpuSocketType cpuSocketType) {
        super(partName, PcPartType.CPU, partPrice, partWattage);

        clockSpeed = cpuClockSpeed;
        coreCount = cpuCoreCount;
        hasIntegratedGraphics = cpuHasIntegratedGraphics;
        socketType = cpuSocketType;
    }

    public float getClockSpeed() {
        return clockSpeed;
    }

    public int getCoreCount() {
        return coreCount;
    }

    public boolean getHasIntegratedGraphics() {
        return hasIntegratedGraphics;
    }

    public CpuSocketType getSocketType() {
        return socketType;
    }
}
