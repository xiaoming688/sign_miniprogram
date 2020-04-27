package com.sign.util;


import java.math.BigDecimal;

/**
 * @author Administrator
 * @date 2018\8\16 0016 16:19
 **/
public class MapDistance {


    //地球半径, 单位km
    private final static Double EARTH_RADIUS = 6378.137;


    public MapDistance() {
    }

    /**
     * @param
     * @return 返回弧度值
     * @author canyon
     * 经纬度转化成弧度
     **/
    private static Double Rad(double d) {
        return d * Math.PI / 180d;
    }


    public static Double getDistance(Double firstLatitude, Double firstLongitude, Double secondLatitude, Double secondLongitude) {
        Double firstRadLat = Rad(firstLatitude);
        Double firstRadLon = Rad(firstLongitude);
        Double secondRadLat = Rad(secondLatitude);
        Double secondRadLon = Rad(secondLongitude);

        Double latDiff = firstRadLat - secondRadLat;
        Double lonDiff = firstRadLon - secondRadLon;
        Double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latDiff / 2), 2) +
                Math.cos(firstRadLat) * Math.cos(secondRadLat) * Math.pow(Math.sin(lonDiff / 2), 2)));
        distance = distance * EARTH_RADIUS;

        distance = Math.round(distance * 10000d) / 10000d;
        return distance;
    }

    public static String getKmDistance(Double firstLatitude, Double firstLongitude, Double secondLatitude, Double secondLongitude) {
        Double distance = getDistance(firstLatitude, firstLongitude, secondLatitude, secondLongitude);
        BigDecimal b = new BigDecimal(distance);
        b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
        return b.toString() + "km";
    }


    public static void main(String[] args) {
        MapDistance mapDistance = new MapDistance();
        Double dis = MapDistance.getDistance(31.2553210000, 121.4620020000, 31.2005470000, 121.3269970000);
        System.out.println(dis);
    }
}
