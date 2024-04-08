package com.tathvatech.forms.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpectedNumericValue
{
	private static Logger logger = LoggerFactory.getLogger(ExpectedNumericValue.class);
	
	Double expectedValue;
	Double lowerLimit; //tolerance Limits
	Double upperLimit; //tolerance Limits
	
	public ExpectedNumericValue(double expectedValue, double lowerLimit, double upperLimit) 
	{
		this.expectedValue = expectedValue;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
	}
	
	public Double getExpectedValue() {
		return expectedValue;
	}
	public void setExpectedValue(Double expectedValue) {
		this.expectedValue = expectedValue;
	}
	public Double getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(Double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	public Double getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(Double upperLimit) {
		this.upperLimit = upperLimit;
	}
	
	public boolean isValueInRange(double actualValue)
	{
		if(actualValue <= upperLimit && actualValue >= lowerLimit)
		{
			return true;
		}
		return false;
	}
	
	public static ExpectedNumericValue getExpectedUpperLower(String expectedValue)
	{
		ExpectedNumericValue returnVal = null;

		//check if th range is correct and select/deselect the pass/fail fox
		if(expectedValue == null || expectedValue.trim().length() == 0)
			return returnVal;
		
		try 
		{
			int begin = expectedValue.lastIndexOf("(");
			int end = expectedValue.lastIndexOf(")");
			if(begin == -1)
			{
				double dotVal = Double.parseDouble(expectedValue.trim());
				returnVal = new ExpectedNumericValue(dotVal, dotVal, dotVal);
			}
			else if(begin > -1 && end > -1 && end > begin)// santity check
			{
				int comaIndex = expectedValue.indexOf(",", begin);
				if (comaIndex > -1)
				{
					double dotVal = Double.parseDouble(expectedValue.substring(0, begin));
					String upperTolString = expectedValue.substring(begin+1, comaIndex);
					if(upperTolString.indexOf("+") > -1)
						upperTolString = upperTolString.substring(upperTolString.indexOf("+") + 1);
					double upperTol = Double.parseDouble(upperTolString);

					String lowerTolString = expectedValue.substring(comaIndex+1, end);
					if(lowerTolString.indexOf("+") > -1)
						lowerTolString = lowerTolString.substring(lowerTolString.indexOf("+") + 1);
					double lowerTol = Double.parseDouble(lowerTolString);

					double upper = dotVal + upperTol;
					double lower = dotVal + lowerTol;
					if(upper < lower)
					{
						double temp = upper;
						upper = lower;
						lower = temp;
					}
					
					returnVal = new ExpectedNumericValue(dotVal, lower, upper);
				}
			}
		} catch (NumberFormatException e) 
		{
			logger.warn("Error parsing expected value '" + expectedValue);
		}
		
		return returnVal;
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Actual:"+ expectedValue + " UpperLimit:" + upperLimit + " LowerLimit:" + lowerLimit;
	}
	
	public static void main(String[] args) {
		ExpectedNumericValue val = getExpectedUpperLower("149.5 (0,-3)");
		System.out.println(val);
	}
	
}
