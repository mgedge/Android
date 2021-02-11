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

    /**
     * Weather object to store the weather for a particular day
     *
     * @param timeStamp   - time of the weather
     * @param minTemp     - the minimum temperature of the day
     * @param maxTemp     - the maximum temperature of the day
     * @param humidity    - the humidity of the day expressed as a decimal
     * @param description - brief description of the forecast
     * @param iconName    - the url code for the weather icon
     */
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

    /**
     * Method converts the timestamp to a weekday
     *
     * @param timeStamp - the timestamp of the weather forecast
     * @return a string for the day of the week corresponding with the time stamp
     */
    private static String convertTimeStampToDay(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        TimeZone timeZone = TimeZone.getDefault();

        calendar.add(Calendar.MILLISECOND, timeZone.getOffset(calendar.getTimeInMillis()));

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE");
        return dateFormatter.format(calendar.getTime());
    }
}
