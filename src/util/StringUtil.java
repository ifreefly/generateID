/**@author:idevcod@163.com
 * @date:2015年9月13日下午7:59:30
 * @description:<TODO>
 */
package util;

public class StringUtil {
	public static final String EMPTY_STR="";
	
	public static boolean isEmpty(String str){
		if (str == null || "".equals(str)) {
			return true;
		}
		
		return false;
	}
}
