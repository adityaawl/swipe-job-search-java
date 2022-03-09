package com.example.swipe.utility;

import com.example.swipe.domain.common.Coordinates;

/**
 * Application utility class.
 */
public class ApplicationUtilities {

    private ApplicationUtilities() {
        // Utility class.
    }

    /**
     * Calculates the distance given {@link Coordinates}.
     *
     * @param coordinates1 first {@link Coordinates}
     * @param coordinates2 second {@link Coordinates}
     * @param unit         Unit of measure.
     * @return
     */
    public static double distance(Coordinates coordinates1, Coordinates coordinates2, String unit) {

        if ((coordinates1.getLatitude() == coordinates2.getLatitude()) && (coordinates1.getLongitude() == coordinates2.getLongitude())) {
            return 0;
        } else {
            double theta = coordinates1.getLongitude() - coordinates2.getLongitude();
            double dist = Math.sin(Math.toRadians(coordinates1.getLatitude())) * Math.sin(Math.toRadians(coordinates2.getLatitude()))
                          + Math.cos(Math.toRadians(coordinates1.getLatitude())) * Math.cos(Math.toRadians(coordinates2.getLatitude())) * Math.cos(
                    Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equalsIgnoreCase("KM")) {
                dist = dist * 1.609344;
            } else if (unit.equalsIgnoreCase("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}
