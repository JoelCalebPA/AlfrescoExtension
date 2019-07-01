package com.domain.platformsample;

import java.io.Serializable;
import java.util.Date;

public class ResponseRst implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String user;
	private Date date;
	private String action;
	private String document;

	public ResponseRst() {
		super();
	}

	public ResponseRst(String user, Date date, String action, String document) {
		super();
		this.user = user;
		this.date = date;
		this.action = action;
		this.document = document;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

}
