package com.hqkang.api.restserver.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Saying {
	
	public String name;
	
	public Saying() {
		
	}
	
	public Saying(String name) {
		this.name = name;
	}
}
