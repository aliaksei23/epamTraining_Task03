package com.company.task03.entity;

import com.company.task03.util.TruckIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class Truck extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();
    private int idTruck;
    private int truckCapacity;
    private boolean isFull;
    private boolean isPerishableGoods;

    private final Base base;


    {
        idTruck = TruckIdGenerator.generateId();
        base = Base.getInstance();
    }

    public Truck() {
    }

    public Truck(int truckCapacity, boolean isFull, boolean isPerishableGoods) {
        this.truckCapacity = truckCapacity;
        this.isFull = isFull;
        this.isPerishableGoods = isPerishableGoods;
        if (isPerishableGoods) {
            this.setTruckPriority();
        }
        LOGGER.info("Truck " + idTruck + " created with capacity = " + truckCapacity);
    }

    public int getIdTruck() {
        return idTruck;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public void setTruckCapacity(int truckCapacity) {
        this.truckCapacity = truckCapacity;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public boolean isPerishableGoods() {
        return isPerishableGoods;
    }

    public void setPerishableGoods(boolean perishableGoods) {
        isPerishableGoods = perishableGoods;
    }

    @Override
    public void run() {
        Base base = Base.getInstance();
        Terminal terminal;
        try {
            terminal = base.getAvailableTerminal(this);
            loadUnloadProcess(terminal);
            base.releaseTerminal(terminal);
        } catch (InterruptedException e) {
            LOGGER.warn("Truck ID {} is interrupted", this.idTruck, e);
        }


    }

    private void load(Base base) throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        base.getGoodsQuantityOnBase().getAndAdd(-truckCapacity);
    }

    private void unload(Base base) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        base.getGoodsQuantityOnBase().getAndAdd(truckCapacity);
    }

    private void setTruckPriority() {
        this.setPriority(MAX_PRIORITY);
        LOGGER.info("Truck " + getIdTruck() + "get MAX_PRIORITY");
    }

    private void loadUnloadProcess(Terminal terminal) throws InterruptedException {
        Base base = Base.getInstance();
        if (!isFull) {
            LOGGER.info("Truck " + getIdTruck() + " start load");
            load(base);
        }
        if (isFull) {
            LOGGER.info("Truck " + getIdTruck() + " start unload");
            unload(base);
        }
        LOGGER.info("Truck " + getIdTruck() + " complete");
        LOGGER.info("Goods on Base = " + Base.getInstance().getGoodsQuantityOnBase());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Truck.class.getSimpleName() + "[", "]")
                .add("idTruck=" + idTruck)
                .add("truckCapacity=" + truckCapacity)
                .add("isFull=" + isFull)
                .add("isPerishableGoods=" + isPerishableGoods)
                .add("base=" + base)
                .toString();
    }
}
