/**@author:idevcod@163.com
 * @date:2015年9月13日下午8:58:40
 * @description:<TODO>
 */
package listener;

public interface SwingThreadInitListener {
	
	/**
	 * @author:idevcod@163.com
	 * @date:2015年11月11日下午11:50:24
	 * @description:将启动过程的耗时操作放到该方法中实现,该方法中会使用线程的方式启动
	 */
	public void threadInit();
}
