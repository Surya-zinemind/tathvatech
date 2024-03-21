package com.tathvatech.forms.response;

public class FormResponseStats 
{
	int totalQCount = 0;
	int totalACount = 0;
	int commentsCount = 0;
	int passCount = 0;
	int failCount = 0;
	int dimentionalFailCount = 0; // where the item is isNumeric
	int naCount = 0;
	
	int percentCompleteLegacy; // we already have this calculated in formREsponse, so have to keep it else all the old ones will return null as we were not tracking qCounts
	
	int percentComplete;
	
	public int getTotalQCount() {
		return totalQCount;
	}
	public void setTotalQCount(int totalQCount) {
		this.totalQCount = totalQCount;
	}
	public int getTotalACount() {
		return totalACount;
	}
	public void setTotalACount(int totalACount) {
		this.totalACount = totalACount;
	}
	public int getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}
	public int getPassCount() {
		return passCount;
	}
	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public int getDimentionalFailCount() {
		return dimentionalFailCount;
	}
	public void setDimentionalFailCount(int dimentionalFailCount) {
		this.dimentionalFailCount = dimentionalFailCount;
	}
	public int getNaCount() {
		return naCount;
	}
	public void setNaCount(int naCount) {
		this.naCount = naCount;
	}

	public void setPercentCompleteLegacy(int pComplete)
	{
		this.percentCompleteLegacy = pComplete;
	}
	
	public int getPercentComplete()
	{
		// in legacy the totalQCount is not tracked and will be 0 so get the percentComplete
		// in new percentComplete is not stored but calculated.
		if(totalQCount == 0)
		{
			return percentCompleteLegacy;
		}
		else
		{
			int percentComplete = 0;
			if(totalQCount > 0)
			{
				percentComplete = totalACount*100/totalQCount; 
			}
			return percentComplete;
		}
	}
	
	public void setPercentComplete(int per)
	{
		this.percentCompleteLegacy = per;
	}
	
}
