package com.hireach.congestiontracingstandalone.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class Util {

    public static Point createPoint(double lat, double lon) {
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(lon, lat));
        point.setSRID(4326);

        return point;
    }
}
