/*
 * Created on Mar 21, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.intf;


import com.tathvatech.common.utils.OptionList;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ThreeDOptionType extends OptionType
{

    /**
     * @return
     */
    OptionList getRowOptions();

    /**
     * @return
     */
    OptionList getColumnOptions();

    /**
     * @return
     */
    OptionList getDropDownOptions();

}
