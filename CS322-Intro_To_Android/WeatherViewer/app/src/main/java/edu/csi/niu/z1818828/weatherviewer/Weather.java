// Weather.java
// Maintains one day's weather information
package edu.csi.niu.z1818828.weatherviewer;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Weather {
    public final String dayOfWeek;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String iconURL;

    public Weather(long timeStamp, double minTemp, double maxTemp,
                   double humidity, String description, String iconName) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        this.dayOfWeek = convertTimeStampToDay(timeStamp);

        this.minTemp = numberFormat.format(minTemp) + "\u00B0F";
        this.maxTemp = numberFormat.format(maxTemp) + "\u00B0F";

        this.humidity = numberFormat.getPercentInstance().format(humidity / 100.0);
        this.description = description;
        this.iconURL = "https://openweathermap.org/img/w/" + iconName + ".png";
    }

    private static String convertTimeStampToDay(long timeStamp) {
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        TimeZone timeZone = TimeZone.getDefault();

        calendar.add(Calendar.MILLISECOND, timeZone.getOffset(calendar.getTimeInMillis()));

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE");
        return dateFormatter.format(calendar.getTime());
    }
}
