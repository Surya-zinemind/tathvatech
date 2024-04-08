package com.tathvatech.survey.common;


import com.tathvatech.survey.enums.BomCellTypeEnum;

public class TCell {
	int cellIndex;
	BomCellTypeEnum cellType;
	String label;
	Object value;
	public int getCellIndex() {
		return cellIndex;
	}
	public void setCellIndex(int cellIndex) {
		this.cellIndex = cellIndex;
	}
	public BomCellTypeEnum getCellType() {
		return cellType;
	}
	public void setCellType(BomCellTypeEnum cellType) {
		this.cellType = cellType;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
