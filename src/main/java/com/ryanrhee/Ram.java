package com.ryanrhee;

class Ram extends PcPart {
    private int numSticks;

    private int gbSize;

    private int speed;

    public Ram(String partName, int partPrice, int partWattage,
               int ramNumSticks,
               int ramGbSize,
               int ramSpeed) {
        super(partName, PcPartType.RAM, partPrice, partWattage);

        numSticks = ramNumSticks;
        gbSize = ramGbSize;
        speed = ramSpeed;
    }

    public int getNumSticks() {
        return numSticks;
    }

    public int getGbSize() {
        return gbSize;
    }

    public int getSize() {
        return speed;
    }
}
