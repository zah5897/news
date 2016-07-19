package com.zhan.app.common.node;

import org.springframework.data.mongodb.core.mapping.Field;

public class Node {
	@Field
	public int type;
	@Field
	public String content;
}
