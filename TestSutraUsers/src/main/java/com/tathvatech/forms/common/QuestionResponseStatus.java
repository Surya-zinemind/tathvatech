package com.tathvatech.forms.common;

public class QuestionResponseStatus 
{
	public static enum ResultStatusEnum {Pass, Fail, NA};
	
	private boolean isQuestionCountedInTotalCount = false;
	private boolean isQuestionNumeric = false; // where the item is marked as isNumeric
	private boolean isAnswered = false;
	private ResultStatusEnum result = null;
	private boolean isCommentEntered = false;
	public boolean isQuestionCountedInTotalCount() {
		return isQuestionCountedInTotalCount;
	}
	public void setQuestionCountedInTotalCount(boolean isQuestionCountedInTotalCount) {
		this.isQuestionCountedInTotalCount = isQuestionCountedInTotalCount;
	}
	public boolean isQuestionNumeric() {
		return isQuestionNumeric;
	}
	public void setQuestionNumeric(boolean isQuestionNumeric) {
		this.isQuestionNumeric = isQuestionNumeric;
	}
	public boolean isAnswered() {
		return isAnswered;
	}
	public void setAnswered(boolean isAnswered) {
		this.isAnswered = isAnswered;
	}
	public ResultStatusEnum getResult() {
		return result;
	}
	public void setResult(ResultStatusEnum result) {
		this.result = result;
	}
	public boolean isCommentEntered() {
		return isCommentEntered;
	}
	public void setCommentEntered(boolean isCommentEntered) {
		this.isCommentEntered = isCommentEntered;
	}
}
