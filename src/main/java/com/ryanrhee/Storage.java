package com.ryanrhee;

class Storage extends PcPart {
    private int gbSize;

    private StorageType storageStType;

    private int mbps;

    public Storage(String partName, int partPrice, int partWattage,
                   int storageGbSize,
                   StorageType storageType,
                   int storageMbps) {
        super(partName, PcPartType.STORAGE, partPrice, partWattage);

        gbSize = storageGbSize;
        storageStType = storageType;
        mbps = storageMbps;
    }

    public int getGbSize() {
        return gbSize;
    }

    public StorageType getStorageType() {
        return storageStType;
    }

    public int getMbps() {
        return mbps;
    }
}
