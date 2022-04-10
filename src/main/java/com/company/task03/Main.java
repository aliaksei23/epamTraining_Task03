package com.company.task03;

import com.company.task03.creator.TruckCreator;
import com.company.task03.entity.Base;
import com.company.task03.entity.Truck;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        TruckCreator truckCreator = TruckCreator.getInstance();
        List<Truck> truckList = truckCreator.createTrucks();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (Truck truck : truckList) {
            executorService.execute(truck);
        }
        System.out.println(Base.getInstance().getGoodsQuantityOnBase());
        executorService.shutdown();
    }

}
