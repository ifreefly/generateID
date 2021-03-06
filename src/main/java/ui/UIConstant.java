/**@author:idevcod@163.com
 * @date:2015年8月17日上午12:10:10
 * @description:<TODO>
 */
package ui;

public interface UIConstant {
	static final String FEMALE = "女";
	static final String MALE = "男";
	static final String SEX_LABEL = "性别";

	static final String DAY_LABEL = "日";
	static final String MONTH_LABEL = "月";
	static final String YEAR_LABEL = "年";

	static final String COUNTY_LABEL = "县";
	static final String CITY_LABEL = "市";
	static final String REGION_LABEL = "省";

	static final String DECODE_ID_BTN = "解析身份证";
	static final String ENCODE_ID_BTN = "生成身份证";
	static final String MORE_ID_BTN = "更多>>";
	static final String BAR_GEN_ID_BTN = "批量生成指定年份段的身份证";

	static final String FROM = "从";
	static final String TO = "到";
	
	static final String BASE64_TAB_LABEL = "base64解析";
	static final String ID_TAB_LABEL = "身份证解析";
	static final String URLENCODE_TAB_LABEL = "URL编码";
	static final String AES_TAB_LABEL="AES加密";

	static final String EMPTY_STR = "";
	
	static final String LINE_SEPERATOR = System.getProperty("line.separator");
	
	static final String CLOSE_ICON="icon/close.png";
	
	static final String FUNCTION_PANEL="功能面板";
	static final String FUNCTION_CHOOSE="功能选择";
	
	static final String BASE64_INST_ENCODE="即时编码";
	static final String BASE_ENCODE="编码";
	static final String BASE_DECODE="解码";
	
	static final String URL_ENCODE=BASE_ENCODE;
	static final String URL_DECODE=BASE_DECODE;
	static final String URL_INST_ENCODE=BASE64_INST_ENCODE;
}
