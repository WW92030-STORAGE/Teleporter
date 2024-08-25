package com.teleport.util;

import net.minecraft.world.entity.Entity;

import java.awt.*;
import java.net.URI;
import java.util.HashMap;

public class Reference {
    public static final String MODID = "teleport";
    public static final String NAME = "TELEPORTER";
    public static final String VERSION = "1.0.0";

    public static final double DEG = 180.0 / Math.PI;
    public static final double TAU = 2.0 * Math.PI;
    public static final double EPSILON = 0.000000001;

    public static HashMap<Entity, Long> active = new HashMap<Entity, Long>();

    public static double atan(double dx, double dy) {
        double res = Math.atan2(dx, dy);
        //	if (dx < 0) res += Math.PI;
        res = (res % TAU) + 10 * TAU;
        return res % TAU;
    }
}
