package com.tody.SF.common.service;

public class Message {
	String text;
	
	private Message(String text) {//private로 선언되서 외부에서 만들수없다 즉 스프링빈으로 등록해서 사용불가!
		this.text = text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return this.text;
	}
	public static Message newMessage(String text) {
		return new Message(text);
	}
}
