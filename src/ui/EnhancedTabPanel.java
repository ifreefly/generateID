/**@author:idevcod@163.com
 * @date:2015年9月13日下午2:50:55
 * @description:<TODO>
 */
package ui;

import java.awt.Component;

import javax.swing.JTabbedPane;

public class EnhancedTabPanel extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TOP = 1;

	public static final int WRAP_TAB_LAYOUT = 0;

	public EnhancedTabPanel() {
		this(TOP, WRAP_TAB_LAYOUT);
	}

	public EnhancedTabPanel(int tabPlacement) {
		this(tabPlacement,WRAP_TAB_LAYOUT);
	}
	
	public EnhancedTabPanel(int tabPlacement,int tabLayoutPolicy) {
		super(tabPlacement,tabLayoutPolicy);
	}
	
	public void addCloseComponent(String title, Component component) {
		add(title,component);
		setTabComponentAt(indexOfComponent(component),new ButtonPanel(this));
	}
	
	/** 根据title判断是否已经存在对应的tabPanel*/
	public boolean isTabExist(String title){
		int tabCount = getTabCount();
		
		for (int i = 0; i < tabCount; i++) {
			if (title.equals(getTitleAt(i))) {
				return true;
			}
		}
		
		return false;
	}
}
