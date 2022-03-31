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
    private static final ReentrantLock lockInstance = new ReentrantLock(true);
    private static final ReentrantLock lockTerminal = new ReentrantLock(true);
    private static final Condition condition = lockTerminal.newCondition();
    private static final AtomicBoolean create = new AtomicBoolean(false);
    private static Base instance;

    private List<Terminal> availableTerminals;
    private List<Terminal> occupiedTerminals;
    private AtomicInteger baseCapacity;
    private AtomicInteger goodsQuantityOnBase;

    public static Base getInstance() {
        if (!create.get()) {
            try {
                lockInstance.lock();
                if (instance == null) {
                    instance = new Base();
                    create.set(true);
                }
            } finally {
                lockInstance.unlock();
            }
        }
        return instance;
    }

    private Base() {
        BaseData baseData = BaseData.getInstance();
        int terminalsQuantity = baseData.getTerminalQuantity();
        availableTerminals = new ArrayList<>();
        for (int i = 0; i < terminalsQuantity; i++) {
            Terminal terminal = new Terminal();// todo
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
        try {
            lockTerminal.lock();
            while ((terminal = findFreeTerminal()) == null || checkFreePleaseForGoods(truck)) {
                LOGGER.info("Truck" + truck.getIdTruck() + " waiting for free terminal");
                condition.await();
            }
            terminal.setFree(false);
            availableTerminals.remove(terminal);
            occupiedTerminals.add(terminal);
            LOGGER.info("Terminal " + terminal.getIdTerminal() + " is take truck " + truck.getIdTruck());
        } finally {
            lockTerminal.unlock();
        }
        return terminal;
    }

    public void releaseTerminal(Terminal terminal) {
        try {
            lockTerminal.lock();
            terminal.setFree(true);
            occupiedTerminals.remove(terminal);
            availableTerminals.add(terminal);
            LOGGER.info("\tTERMINAL ID {} is free", terminal.getIdTerminal());
        } finally {
            condition.signalAll();
            lockTerminal.unlock();
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
//        terminal = availableTerminals.stream()
//                .filter(Terminal::getIsFree)
//                .findFirst()
//                .orElse(null);
        return terminal;
    }

    private boolean checkFreePleaseForGoods(Truck truck) {
        int goodsInTruck = truck.getTruckCapacity();
        int freeCapacityBase = baseCapacity.get() - goodsQuantityOnBase.get();
        return goodsInTruck > freeCapacityBase;
    }
}
