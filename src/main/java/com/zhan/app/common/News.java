package com.zhan.app.common;

import com.zhan.app.annotation.Table;

@Table(name = "spider_news_simple")
public class News {
	private String id;
	private String title;
	private String icon;
	private String news_abstract;
	private String url;
	private String from;
	private String publish_time;
	private int type=0; //0 为头条，1为百度
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getNews_abstract() {
		return news_abstract;
	}
	public void setNews_abstract(String news_abstract) {
		this.news_abstract = news_abstract;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
