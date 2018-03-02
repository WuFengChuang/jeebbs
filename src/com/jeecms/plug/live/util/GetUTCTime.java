package com.jeecms.plug.live.util;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class GetUTCTime {

    // 取得本地时间：
    private Calendar cal = Calendar.getInstance();
    // 取得时间偏移量：
    private int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
    // 取得夏令时差：
    private int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

    public static void main(String[] args) {
        GetUTCTime gc = new GetUTCTime();
        long mill = gc.getUTCTimeStr();
        gc.setUTCTime(mill);
    }

    public long getUTCTimeStr() {

        System.out.println("local millis = " + cal.getTimeInMillis()); // 等效System.currentTimeMillis() , 统一值，不分时区

        // 从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        long mills = cal.getTimeInMillis();
        System.out.println("UTC = " + mills);
        return mills;
    }

    public void setUTCTime(long millis) {

        cal.setTimeInMillis(millis);

        SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //2017-08-15T01:00:00Z
        String time = foo.format(cal.getTime());
        System.out.println("GMT time= " + time);

        // 从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, (zoneOffset + dstOffset));
        time = foo.format(cal.getTime());
        System.out.println("Local time = " + time);

    }
}
