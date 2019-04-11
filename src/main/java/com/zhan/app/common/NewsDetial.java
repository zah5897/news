package com.zhan.app.common;

import java.util.List;

import com.zhan.app.common.node.Node;

public class NewsDetial {
	private String id;
	private String title;
	private String from;
	private String publish_time;
	private String detial_url;
	private List<Node> nodes;

	private int type = 0; // 0 为头条，1为百度

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

	public String getDetial_url() {
		return detial_url;
	}

	public void setDetial_url(String detial_url) {
		this.detial_url = detial_url;
	}

 

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
