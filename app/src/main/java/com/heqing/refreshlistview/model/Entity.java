package com.heqing.refreshlistview.model;

/**
 * Created by 何清 on 2016/7/22.
 *
 * @description
 */
public class Entity {

    private String title;
    private String content;
    private String time;

    public Entity() {
    }

    public Entity(String title, String content, String time) {
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
