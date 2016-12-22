package com.sleep.domain;

public class NewInfo {

	private int id;
	private int sorts;
	private long uploadDate;
	private String content;
	private String title;
	private String imageUrl;
	public NewInfo() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSorts() {
		return sorts;
	}
	public void setSorts(int sorts) {
		this.sorts = sorts;
	}
	public long getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(long uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Override
	public String toString() {
		return "NewInfo [id=" + id + ", sorts=" + sorts + ", uploadDate=" + uploadDate + ", content=" + content
				+ ", title=" + title + ", imageUrl=" + imageUrl + "]";
	}
	
	
	

	

}
