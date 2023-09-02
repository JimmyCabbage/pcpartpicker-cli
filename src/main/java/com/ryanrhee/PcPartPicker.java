package com.ryanrhee;

import java.util.Comparator;
import java.util.List;

class PcPartPicker {
    private List<PcPart> cpus;
    private List<PcPart> motherboards;
    private List<PcPart> gpus;
    private List<PcPart> psus;
    private List<PcPart> rams;
    private List<PcPart> cases;
    private List<PcPart> storages;

    public PcPartPicker(List<PcPart> pcParts) {
        cpus         = filterByPartType(pcParts, PcPartType.CPU);
        motherboards = filterByPartType(pcParts, PcPartType.MOTHERBOARD);
        gpus         = filterByPartType(pcParts, PcPartType.GPU);
        psus         = filterByPartType(pcParts, PcPartType.PSU);
        rams         = filterByPartType(pcParts, PcPartType.RAM);
        cases        = filterByPartType(pcParts, PcPartType.CASE);
        storages     = filterByPartType(pcParts, PcPartType.STORAGE);
    }

    private List<PcPart> filterByPartType(List<PcPart> parts, PcPartType partType) {
        return parts.stream()
                .filter(p -> p.getType() == partType)
                .toList();
    }

    public List<PcPart> pickParts(int budget, PcWorkType workType,
                                  boolean smallPreference, int storageSpace,
                                  boolean fastFileSpeed) {
        List<PcPart> possibleCpus = cpus.stream().filter(c -> c.getPrice() <= budget)
                .sorted(Comparator.comparing(c -> rankCpu((Cpu)c, workType, smallPreference, fastFileSpeed)).reversed())
                .toList();
        List<PcPart> possibleGpus = gpus.stream().filter(g -> g.getPrice() <= budget)
                .sorted(Comparator.comparing(g -> rankGpu((Gpu)g, workType, smallPreference)).reversed())
                .toList();

        for (PcPart cpu : possibleCpus) {
            for (PcPart gpu : possibleGpus) {
                List<PcPart> parts = tryPickParts(budget, workType,
                                                  smallPreference, storageSpace,
                                                  fastFileSpeed,
                                                  cpu, gpu);
                if (parts != null) {
                    return parts;
                }
            }
        }

        return null;
    }

    //a god does not dwell here
    private List<PcPart> tryPickParts(int budget, PcWorkType workType,
                                      boolean smallPreference, int storageSpace,
                                      boolean fastFileSpeed,
                                      PcPart cpu, PcPart gpu) {
        List<PcPart> possibleRams = rams.stream().filter(r -> r.getPrice() <= budget)
                .sorted(Comparator.comparing(r -> rankRam((Ram)r)).reversed())
                .toList();
        List<PcPart> possibleMotherboards = motherboards.stream().filter(m -> m.getPrice() <= budget)
                .sorted(Comparator.comparing(m -> rankMotherboard((Motherboard)m, smallPreference)).reversed())
                .toList();

        for (PcPart ram : possibleRams) {
            if (getPartsPrice(List.of(cpu, gpu, ram)) >= budget) {
                continue;
            }

            for (PcPart motherboard : possibleMotherboards) {
                if (getPartsPrice(List.of(cpu, gpu, ram, motherboard)) >= budget) {
                    continue;
                }

                List<PcPart> possibleStorages = storages.stream().filter(s -> s.getPrice() <= budget)
                        .filter(s -> ((Storage)s).getGbSize() >= storageSpace)
                        .filter(s -> !fastFileSpeed || !(((Storage) s).getStorageType() == StorageType.HDD))
                        .sorted(Comparator.comparing(s -> rankStorage((Storage)s, fastFileSpeed)).reversed())
                        .toList();
                for (PcPart storage : possibleStorages) {
                    if (getPartsPrice(List.of(cpu, gpu, ram, motherboard, storage)) >= budget) {
                        continue;
                    }

                    int wattageRequirement = getPartsWattage(List.of(cpu, gpu, ram, motherboard, storage));
                    List<PcPart> possiblePsus = psus.stream().filter(p -> p.getPrice() <= budget)
                            .filter(p -> ((Psu)p).getWattageOutput() > wattageRequirement)
                            .toList();
                    for (PcPart psu : possiblePsus) {
                        if (getPartsPrice(List.of(cpu, gpu, ram, motherboard, storage, psu)) >= budget) {
                            continue;
                        }

                        List<PcPart> possibleCases = cases.stream().filter(c -> c.getPrice() <= budget)
                                .filter(c -> doPartsFitInCase((Case)c, (Gpu)gpu, (Motherboard)motherboard, (Psu)psu))
                                .sorted(Comparator.comparing(c -> rankCase((Case)c, smallPreference)).reversed())
                                .toList();
                        for (PcPart pcCase : possibleCases) {
                            List<PcPart> parts = List.of(cpu, gpu, ram, motherboard, storage, psu, pcCase);

                            if (getPartsPrice(parts) >= budget) {
                                continue;
                            }

                            return parts;
                        }
                    }
                }
            }
        }

        return null;
    }

    private int rankCpu(Cpu cpu, PcWorkType workType, boolean smallPreference, boolean fastFileSpeed) {
        int rank = 0;

        if (smallPreference && cpu.getHasIntegratedGraphics()) {
            rank += 10;
        }
        if (fastFileSpeed) {
            rank += (int)(cpu.getClockSpeed() * 2);
        }
        if (workType == PcWorkType.GAMING) {
            rank += (int)(cpu.getClockSpeed() * 6);
            rank += cpu.getCoreCount() * 2;
        }
        if (workType == PcWorkType.VFX) {
            rank += (int)(cpu.getClockSpeed() * 2);
            rank += cpu.getCoreCount() * 3;
        }

        return rank;
    }

    private int rankGpu(Gpu gpu, PcWorkType workType, boolean smallPreference) {
        int rank = 0;

        if (smallPreference) {
            rank -= (int)gpu.getLength();
        }
        if (workType == PcWorkType.GAMING) {
            rank += (int)(gpu.getRasterizationScore());
        }
        if (workType == PcWorkType.VFX) {
            rank += (int)(gpu.getBlenderScore());
        }

        return rank;
    }

    private int rankRam(Ram ram) {
        return ram.getGbSize() + ram.getSpeed();
    }

    private int rankMotherboard(Motherboard motherboard, boolean smallPreference) {
        int rank = 0;

        if (smallPreference && motherboard.getFormFactor() == MotherboardFormFactor.MATX) {
            rank += 250;
        }
        else if (!smallPreference && motherboard.getFormFactor() == MotherboardFormFactor.MATX) {
            rank -= 100;
        }
        rank -= motherboard.getPrice();

        return rank;
    }

    private int rankStorage(Storage storage, boolean fastFileSpeed) {
        int rank = 0;

        rank += storage.getGbSize();
        if (fastFileSpeed &&
                (storage.getMbps() < 1000 || storage.getStorageType() == StorageType.HDD)) {
            rank -= 20000;
        }

        return rank;
    }

    private int rankCase(Case pcCase, boolean smallPreference) {
        int rank = 0;

        if (smallPreference && pcCase.getFormFactor() == MotherboardFormFactor.MATX) {
            rank += 1000;
        }

        return rank;
    }

    private boolean doPartsFitInCase(Case pcCase, Gpu gpu, Motherboard motherboard, Psu psu) {
        if (pcCase.getMaxGpuSize() + 5 < gpu.getLength()) {
            return false;
        }

        if (pcCase.getFormFactor() != motherboard.getFormFactor()) {
            return false;
        }

        if (pcCase.getMaxPsuSize() + 5 < psu.getLength()) {
            return false;
        }

        return true;
    }

    private int getPartsPrice(List<PcPart> parts) {
        int price = 0;
        for (PcPart part : parts) {
            price += part.getPrice();
        }

        return price;
    }

    private int getPartsWattage(List<PcPart> parts) {
        int wattage = 0;
        for (PcPart part : parts) {
            wattage += part.getWattage();
        }

        return wattage;
    }
}
