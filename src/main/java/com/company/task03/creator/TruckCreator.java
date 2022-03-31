package com.company.task03.creator;

import com.company.task03.entity.Base;
import com.company.task03.entity.Truck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.Random;

public class TruckCreator {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int MIN_TRUCK_CAPACITY = 40;
    private static final int MAX_TRUCK_CAPACITY = 50;

    private static TruckCreator instance;
    private Base base;
    private Random random;
    private int truckQuantity;

    public static TruckCreator getInstance() {
        if (instance == null) {
            instance = new TruckCreator();
        }
        return instance;
    }

    private TruckCreator() {
        base = Base.getInstance();
        BaseData baseData = BaseData.getInstance();
        this.random = new Random();
        this.truckQuantity = baseData.getTrucksQuantity();
    }

    public Deque<Truck> createTrucks() {
        Deque<Truck> truckQueue = new ArrayDeque<>();
        for (int i = 0; i < truckQuantity; i++) {
            Truck truck = createTruck();
            truckQueue.addLast(truck);
        }
        LOGGER.info(truckQuantity + " trucks created");
        return truckQueue;
    }

    private Truck createTruck() {
        boolean isFull = random.nextBoolean();
        boolean isPerishableGoods = random.nextBoolean();
        int truckCapacity = random.nextInt(MAX_TRUCK_CAPACITY - MIN_TRUCK_CAPACITY) + MIN_TRUCK_CAPACITY;
        return new Truck(truckCapacity, isFull, isPerishableGoods);
    }
}
