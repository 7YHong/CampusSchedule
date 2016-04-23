package cn.qiyanghong.campusschedule.model;

import java.util.Calendar;

/**
 * Created by QYH on 2016/4/23.
 */
public class TodoData {
    public TodoData(long time,String title,String detail) {
        this.time = time;
        this.title=title;
        this.detail=detail;
        //ID以分钟为单位
        this.id=(int)(getTime()/1000/60);

        date = Calendar.getInstance();
        date.setTimeInMillis(time);

        timeLabel = String.format("%02d月%02d日 %02d:%02d",
                date.get(Calendar.MONTH)+1,
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE));
    }

    public long getTime() {
        return time;
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    @Override
    public String toString() {
//        StringBuffer str=new StringBuffer();
//        str.append(time).append(",");
//        str.append(title).append(",");
//        str.append(detail).append(";");
//        return str.toString();
        return getTimeLabel();
    }

    public int getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setTime(long time) {
        this.time = time;
        this.id=(int)(getTime()/1000/60);
    }

    private Calendar date;

    private String timeLabel="";
    private long time = 0;
    private int id;
    private String title,detail;
}

