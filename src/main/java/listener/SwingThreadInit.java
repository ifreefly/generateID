/**@author:idevcod@163.com
 * @date:2015年9月13日下午9:02:24
 * @description:<TODO>
 */
package listener;


public class SwingThreadInit extends Thread{

	private SwingThreadInitListener listener;
	public SwingThreadInit(SwingThreadInitListener listener) {
		this.listener=listener;
		start();
	}
	
	@Override
	public void run() {
		listener.threadInit();
	}

}
