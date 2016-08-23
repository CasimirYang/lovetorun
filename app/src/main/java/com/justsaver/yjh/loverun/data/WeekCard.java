package com.justsaver.yjh.loverun.data;

import android.support.v7.widget.AppCompatTextView;
import android.widget.RelativeLayout;

/**
 * Created by yjh on 16/8/12.
 */
public final class WeekCard {
    private String weekNo;
    private String weekTip;
    private int weekLayoutColor;
    private String courseOneFinishTime;
    private String courseTwoFinishTime;
    private String courseThreeFinishTime;

    public WeekCard(String weekNo, String weekTip, int weekLayoutColor) {
        this.weekNo = weekNo;
        this.weekTip = weekTip;
        this.weekLayoutColor = weekLayoutColor;
    }

    public WeekCard(String weekNo, String weekTip, int weekLayoutColor, String courseOneFinishTime, String courseTwoFinishTime, String courseThreeFinishTime) {
        this.weekNo = weekNo;
        this.weekTip = weekTip;
        this.weekLayoutColor = weekLayoutColor;
        this.courseOneFinishTime = courseOneFinishTime;
        this.courseTwoFinishTime = courseTwoFinishTime;
        this.courseThreeFinishTime = courseThreeFinishTime;
    }

    public String getWeekNo() {
        return weekNo;
    }

    public String getWeekTip() {
        return weekTip;
    }

    public int getWeekLayoutColor() {
        return weekLayoutColor;
    }

    public String getCourseOneFinishTime() {
        return courseOneFinishTime;
    }

    public String getCourseThreeFinishTime() {
        return courseThreeFinishTime;
    }

    public String getCourseTwoFinishTime() {
        return courseTwoFinishTime;
    }

    public void setCourseOneFinishTime(String courseOneFinishTime) {
        this.courseOneFinishTime = courseOneFinishTime;
    }

    public void setCourseTwoFinishTime(String courseTwoFinishTime) {
        this.courseTwoFinishTime = courseTwoFinishTime;
    }

    public void setCourseThreeFinishTime(String courseThreeFinishTime) {
        this.courseThreeFinishTime = courseThreeFinishTime;
    }
}
