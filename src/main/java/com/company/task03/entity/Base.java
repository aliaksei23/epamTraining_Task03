package com.company.task03.entity;

import com.company.task03.creator.BaseData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Base {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ReentrantLock lock = new ReentrantLock(true);
    private static final Condition condition = lock.newCondition();
    private static final AtomicBoolean create = new AtomicBoolean(false);
    private static Base instance;

    private List<Terminal> availableTerminals;
    private List<Terminal> occupiedTerminals;
    private AtomicInteger baseCapacity;
    private AtomicInteger goodsQuantityOnBase;

    public static Base getInstance() {
        if (!create.get()) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new Base();
                    create.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private Base() {
        BaseData baseData = BaseData.getInstance();
        int terminalsQuantity = baseData.getTerminalQuantity();
        availableTerminals = new ArrayList<>();
        for (int i = 0; i < terminalsQuantity; i++) {
            Terminal terminal = new Terminal();
            availableTerminals.add(terminal);
        }
        baseCapacity = new AtomicInteger(baseData.getBaseCapacity());
        goodsQuantityOnBase = new AtomicInteger(baseData.getGoodsQuantity());
        occupiedTerminals = new ArrayList<>();
    }

    public List<Terminal> getAvailableTerminals() {
        return availableTerminals;
    }

    public AtomicInteger getBaseCapacity() {
        return baseCapacity;
    }

    public AtomicInteger getGoodsQuantityOnBase() {
        return goodsQuantityOnBase;
    }

    public Terminal getAvailableTerminal(Truck truck) throws InterruptedException {
        Terminal terminal;
        truck.setState(TruckState.WAITING);
        try {
            lock.lock();
            while ((terminal = findFreeTerminal()) == null || checkFreePleaseOnBase(truck)) {
                LOGGER.info("Truck" + truck.getIdTruck() + " waiting for free terminal");
                condition.await();
            }
            terminal.setFree(false);
            availableTerminals.remove(terminal);
            occupiedTerminals.add(terminal);
            LOGGER.info("Terminal " + terminal.getIdTerminal() + " is take truck " + truck.getIdTruck());
        } finally {
            lock.unlock();
        }
        return terminal;
    }

    public void releaseTerminal(Terminal terminal) {
        try {
            lock.lock();
            terminal.setFree(true);
            occupiedTerminals.remove(terminal);
            availableTerminals.add(terminal);
            LOGGER.info("\tTERMINAL ID {} is free", terminal.getIdTerminal());
        } finally {
            condition.signalAll();
            lock.unlock();
        }
    }

    private Terminal findFreeTerminal() {
        Terminal terminal = null;
        for (Terminal t : availableTerminals) {
            if (t.getIsFree()) {
                terminal = t;
                break;
            }
        }
        return terminal;
    }

    private boolean checkFreePleaseOnBase(Truck truck) {
        int goodsInTruck = truck.getTruckCapacity();
        int freeCapacityBase = baseCapacity.get() - goodsQuantityOnBase.get();
        return goodsInTruck > freeCapacityBase;
    }
}
