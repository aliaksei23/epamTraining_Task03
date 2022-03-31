package com.company.task03.util;

import com.company.task03.exception.CustomThreadException;

import java.net.URL;

public class PathUtil {

    public static String getResourcePath(String resourceName) throws CustomThreadException {
        final int pathStartPosition = 6;
        ClassLoader loader = PathUtil.class.getClassLoader();
        URL resource = loader.getResource(resourceName);
        if (resource == null) {
            throw new CustomThreadException("Resource " + resourceName + " is not found");
        }
        return resource.toString().substring(pathStartPosition);
    }

}
