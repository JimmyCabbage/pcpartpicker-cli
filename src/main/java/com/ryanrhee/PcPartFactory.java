package com.ryanrhee;

class PcPartFactory {
    public PcPart getPart(String[] partValues) {
        String partName = partValues[0];
        PcPartType partType = PcPartType.valueOf(partValues[1].toUpperCase());
        int partPrice = Integer.parseInt(partValues[2]);
        int partWattage = Integer.parseInt(partValues[3]);

        switch (partType) {
            case CPU:
                return parseCpu(partName, partPrice, partWattage, partValues);
            case GPU:
                return parseGpu(partName, partPrice, partWattage, partValues);
            case MOTHERBOARD:
                return parseMotherboard(partName, partPrice, partWattage, partValues);
            case PSU:
                return parsePsu(partName, partPrice, partWattage, partValues);
            case RAM:
                return parseRam(partName, partPrice, partWattage, partValues);
            case CASE:
                return parseCase(partName, partPrice, partWattage, partValues);
            case STORAGE:
                return parseStorage(partName, partPrice, partWattage, partValues);
        }

        return null;
    }

    private PcPart parseCpu(String partName, int partPrice, int partWattage, String[] partValues) {
        float clockSpeed = Float.parseFloat(partValues[4]);
        int coreCount = Integer.parseInt(partValues[5]);
        boolean hasIntegratedGraphics = partValues[6].equalsIgnoreCase("YES");
        CpuSocketType socketType = CpuSocketType.valueOf(partValues[7].toUpperCase());

        return new Cpu(partName, partPrice, partWattage,
                       clockSpeed, coreCount, hasIntegratedGraphics, socketType);
    }

    private PcPart parseGpu(String partName, int partPrice, int partWattage, String[] partValues) {
        float blenderScore = Float.parseFloat(partValues[4]);
        float rasterizationScore = Float.parseFloat(partValues[5]);
        float length = Float.parseFloat(partValues[6]);

        return new Gpu(partName, partPrice, partWattage,
                       blenderScore, rasterizationScore, length);
    }

    private PcPart parseMotherboard(String partName, int partPrice, int partWattage, String[] partValues) {
        MotherboardFormFactor formFactor = MotherboardFormFactor.valueOf(partValues[4].toUpperCase());
        int numRamSlots = Integer.parseInt(partValues[5]);
        CpuSocketType cpuSocketType = CpuSocketType.valueOf(partValues[7].toUpperCase());

        return new Motherboard(partName, partPrice, partWattage,
                               formFactor, numRamSlots, cpuSocketType);
    }

    private PcPart parsePsu(String partName, int partPrice, int partWattage, String[] partValues) {
        int wattageOutput = Integer.parseInt(partValues[4]);
        float length = Float.parseFloat(partValues[5]);

        return new Psu(partName, partPrice, partWattage,
                       wattageOutput, length);
    }

    private PcPart parseRam(String partName, int partPrice, int partWattage, String[] partValues) {
        int numSticks = Integer.parseInt(partValues[4]);
        int gbSize = Integer.parseInt(partValues[5]);
        int speed = Integer.parseInt(partValues[6]);

        return new Ram(partName, partPrice, partWattage,
                       numSticks, gbSize, speed);
    }

    private PcPart parseCase(String partName, int partPrice, int partWattage, String[] partValues) {
        MotherboardFormFactor formFactor = MotherboardFormFactor.valueOf(partValues[4].toUpperCase());
        float maxPsuSize = Float.parseFloat(partValues[5]);
        float maxGpuSize = Float.parseFloat(partValues[6]);

        return new Case(partName, partPrice, partWattage,
                        formFactor, maxPsuSize, maxGpuSize);
    }

    private PcPart parseStorage(String partName, int partPrice, int partWattage, String[] partValues) {
        int gbSize = Integer.parseInt(partValues[4]);
        StorageType type = StorageType.valueOf(partValues[5].toUpperCase());
        int mbps = Integer.parseInt(partValues[6]);

        return new Storage(partName, partPrice, partWattage, gbSize, type, mbps);
    }
}
