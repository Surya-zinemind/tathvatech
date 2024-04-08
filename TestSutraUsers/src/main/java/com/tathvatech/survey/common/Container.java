package com.tathvatech.survey.common;

import com.tathvatech.survey.enums.ContainerBase;

public interface Container extends ContainerBase
{
	public AbstractOrderedLayout getChildrenLayoutContainer();
	
	public AbstractOrderedLayout getManageChildrenControlsLayoutArea();

}
