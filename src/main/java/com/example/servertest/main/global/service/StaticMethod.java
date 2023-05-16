package com.example.servertest.main.global.service;

import org.springframework.stereotype.Service;

@Service
public class StaticMethod {
    public static float distance(float lat1, float lon1, float lat2, float lon2){
        float theta = lon1 - lon2;
        float dist = (float) (Math.sin(deg2rad(lat1))* Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta)));
        dist = (float) Math.acos(dist);
        dist = rad2deg(dist);
        dist = (float) (dist * 60*1.1515*1609.344);

        return dist; //단위 meter
    }

    private static float deg2rad(double deg){
        return (float) (deg * Math.PI/180.0);
    }
    //radian(라디안)을 10진수로 변환
    private static float rad2deg(double rad){
        return (float) (rad * 180 / Math.PI);
    }
}
