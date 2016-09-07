package com.malalaoshi.android.entity;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 课程UI展示类。
 * Created by tianwei on 3/12/16.
 */
public class CourseDateEntity implements Comparable<CourseDateEntity> {
    //时间表id。从1到35
    private long id;
    //开始时间
    private String start;
    //结束时间
    private String end;
    private boolean available;
    // the day of week. 周一到周日：1~7
    private int day;
    //是否选中
    private boolean choice;
    //是否是标题
    private boolean isTitle;

    //自己是否购买
    private boolean bought;

    //被占用的最后时间
    private long last_occupied_end;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setIsTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    @Override
    public int compareTo(@NonNull CourseDateEntity another) {
        return this.id > another.id ? 1 : -1;
    }

    public static List<CourseDateEntity> format(String jsonStr) throws Exception {
        List<CourseDateEntity> list = new ArrayList<>();
        JSONObject json;
        json = new JSONObject(jsonStr);
        for (int i = 1; i <= 7; i++) {
            JSONArray sections = json.getJSONArray(i + "");
            if (sections.length() != 5) {
                throw new RuntimeException("time section's len is error");
            }
            for (int j = 0; j < 5; j++) {
                JSONObject section = sections.getJSONObject(j);
                CourseDateEntity item = new CourseDateEntity();
                item.setAvailable(section.optBoolean("available"));
                item.setLast_occupied_end(section.optLong("last_occupied_end", 0) * 1000);
                if (item.isAvailable() && item.getLast_occupied_end() > 0) {
                    item.setBought(true);
                }
                item.setEnd(section.optString("end"));
                item.setStart(section.optString("start"));
                item.setId(section.optLong("id"));
                item.setDay(i);
                list.add(item);
            }
        }
        Collections.sort(list);
        List<CourseDateEntity> resList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                resList.add(list.get(i + j * 5));
            }
        }
        list.clear();
        return resList;
    }

    public long getLast_occupied_end() {
        return last_occupied_end;
    }

    public void setLast_occupied_end(long last_occupied_end) {
        this.last_occupied_end = last_occupied_end;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
}
