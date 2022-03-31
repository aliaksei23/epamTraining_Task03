package com.company.task03.creator;

import com.company.task03.exception.CustomThreadException;
import com.company.task03.util.PathUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseData {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String PROPERTIES_FAIL = "data/baseData.property";//todo
    private static final int DEFAULT_NUMBER_OF_TRUCKS = 7;
    private static final int DEFAULT_NUMBER_OF_TERMINALS = 3;
    private static final int DEFAULT_CAPACITY_BASE = 1000;
    private static final int DEFAULT_AVAILABLE_CARGO_IN_BASE = 500;

    private static BaseData instance;
    private int trucksQuantity;
    private int terminalQuantity;
    private int baseCapacity;
    private int goodsQuantity;

    public static BaseData getInstance() {
        if (instance == null) {
            instance = new BaseData();
        }
        return instance;
    }

    private BaseData() {
        try {
            getPropertiesFromFile();
            LOGGER.info("Base data read correctly " + PROPERTIES_FAIL);
        } catch (CustomThreadException e) {
            getDefaultProperties();
            LOGGER.error("Default properties are accepted", e);
//            e.printStackTrace();
        }
    }

    public int getTrucksQuantity() {
        return trucksQuantity;
    }

    public int getTerminalQuantity() {
        return terminalQuantity;
    }

    public int getBaseCapacity() {
        return baseCapacity;
    }

    public int getGoodsQuantity() {
        return goodsQuantity;
    }

    private void getPropertiesFromFile() throws CustomThreadException {
        String filePath = PathUtil.getResourcePath(PROPERTIES_FAIL);
        try (FileInputStream fileStream = new FileInputStream(filePath)) {
            Properties property = new Properties();
            property.load(fileStream);
            trucksQuantity = Integer.parseInt(property.getProperty("trucksQuantity"));
            terminalQuantity = Integer.parseInt(property.getProperty("terminalQuantity"));
            baseCapacity = Integer.parseInt(property.getProperty("baseCapacity"));
            goodsQuantity = Integer.parseInt(property.getProperty("goodsQuantity"));
        } catch (IOException exception) {
            LOGGER.warn("Properties not read", exception);
            throw new CustomThreadException("Properties not read", exception);
        }
    }

    private void getDefaultProperties() {
        this.trucksQuantity = DEFAULT_NUMBER_OF_TRUCKS;
        this.terminalQuantity = DEFAULT_NUMBER_OF_TERMINALS;
        this.baseCapacity = DEFAULT_CAPACITY_BASE;
        this.goodsQuantity = DEFAULT_AVAILABLE_CARGO_IN_BASE;
    }
}
