package com.njau.agricultural_assistant;

import java.util.List;
public class News {

	// 属性(title标题、author作者、pubDate发表日期、commentCount评论数、body新闻内容)
	private String title;
	private String author;
	private String pubDate;
	private int commentCount;
	private String body;

	/**
	 * 构造方法
	 * @param title 标题
	 * @param author 作者
	 * @param pubDate 发表日期
	 * @param commentCount 评论数
	 * @param body 新闻内容
	 */
	public News(String title, String author, String pubDate, int commentCount,
				String body) {
		super();
		this.title = title;
		this.author = author;
		this.pubDate = pubDate;
		this.commentCount = commentCount;
		this.body = body;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
