/**@author:idevcod@163.com
 * @date:2015年9月13日下午4:49:49
 * @description:<TODO>
 */
package util;

public class DateUtil {
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @return 算出当月最大是多少天
	 */
	public static int getMaxDay(int year, int month) {
		int[] bigMonth = {1,3,5,7,8,10,12};
		int[] medMonth = {4,6,9,11};
		
		for (int bMonth : bigMonth) {
			if (bMonth == month) {
				return 31;
			}
		}
		
		for (int mMonth : medMonth) {
			if (mMonth == month) {
				return 30;
			}
		}
		
		if (year % 100 == 0 && year % 400 == 0 || year % 4 == 0) {
			return 29;
		}

		return 28;
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @return 算出当月最大是多少天
	 */
	public static int getMaxDay(String year, String month) {
		return getMaxDay(Integer.valueOf(year), Integer.valueOf(month));
	}
}
