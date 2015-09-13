package main;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseIDXml {
	final Logger logger = LoggerFactory.getLogger(ParseIDXml.class.getName());
	final String XML_REL_PATH = "code.xml";
	final String EMPTY_STR = "";
	private static ParseIDXml parseIDXml = null;
	private Element rootElement;
	private List<Region> provinces;
	private Map<String, Region> nameProvincesMap;
	private final int MAX_MONTH = 12;
	private final String INVALID_ID = "身份证输入不正确";

	// private Map<String, Region> nameCitiesMap;

	private ParseIDXml() {

	}

	public static ParseIDXml getInstance() {
		if (null == parseIDXml) {
			synchronized (ParseIDXml.class) {
				if (null == parseIDXml) {
					parseIDXml = new ParseIDXml();
				}
			}

		}

		return parseIDXml;
	}

	public void init() {
		rootElement = getRootElement();
		nameProvincesMap = new HashMap<String, Region>();
		provinces = generateProvinceRegions();
	}

	private Element getRootElement() {
		String xmlPath = null;
		Element rootElement = null;
		SAXReader reader = new SAXReader();
		try {
			xmlPath = parseIDXml.getClass().getClassLoader().getResource(XML_REL_PATH).getFile();
			rootElement = reader.read(new File(xmlPath)).getRootElement();
		} catch (final DocumentException e) {
			e.printStackTrace();
		}

		return rootElement;
	}

	public List<String> getProvinces() {
		List<String> provinceStr = new ArrayList<String>();
		for (final Region region : this.provinces) {
			provinceStr.add(region.getName());
		}

		return provinceStr;
	}

	private List<Region> generateProvinceRegions() {
		List<Region> provinces = new ArrayList<Region>();
		@SuppressWarnings("unchecked")
		List<Element> elements = rootElement.elements("Province");
		for (final Element element : elements) {
			final Region region = new Region();
			region.setCode(preHandleStr(element.attributeValue("code")));
			region.setName(preHandleStr(element.attributeValue("name")));
			region.setSubRegions(generateCityRegions(element, region));

			provinces.add(region);

			nameProvincesMap.put(preHandleStr(element.attributeValue("name")), region);
		}

		return provinces;
	}

	private List<Region> generateCityRegions(Element provinceElement, Region provinceRegion) {
		List<Region> citys = new ArrayList<Region>();

		@SuppressWarnings("unchecked")
		List<Element> elements = provinceElement.elements("City");

		for (final Element element : elements) {
			final Region region = new Region();
			region.setCode(preHandleStr(element.attributeValue("code")));
			region.setName(preHandleStr(element.attributeValue("name")));
			region.setSubRegions(generateCountyRegions(element, region));
			citys.add(region);
			provinceRegion.getNameRegionMap().put(preHandleStr(region.getName()), region);
		}

		return citys;
	}

	private List<Region> generateCountyRegions(Element cityElement, Region cityRegion) {
		List<Region> counties = new ArrayList<Region>();

		@SuppressWarnings("unchecked")
		List<Element> elements = cityElement.elements("County");
		for (final Element element : elements) {
			final Region region = new Region();
			region.setCode(preHandleStr(element.attributeValue("code")));
			region.setName(preHandleStr(element.attributeValue("name")));

			counties.add(region);
			cityRegion.getNameRegionMap().put(preHandleStr(region.getName()), region);
		}

		return counties;
	}

	public List<String> getCities(String provinceStr) {
		List<String> cities = new ArrayList<String>();
		Region province = nameProvincesMap.get(provinceStr);
		if (province != null) {
			for (final Region city : province.getSubRegions()) {
				cities.add(city.getName());
			}
		}

		return cities;
	}

	public List<String> getCounties(String provinceStr, String cityStr) {
		List<String> counties = new ArrayList<String>();

		Region province = nameProvincesMap.get(provinceStr);
		Region city = province.getNameRegionMap().get(cityStr);
		for (final Region county : city.getSubRegions()) {
			counties.add(county.getName());
		}

		return counties;
	}

	public void setProvinces(List<Region> provinces) {
		this.provinces = provinces;
	}

	/**
	 * 
	 * @param province
	 * @param city
	 * @param county
	 * @param year
	 * @param month
	 * @param day
	 * @param sex
	 * @return String 身份证
	 */
	public String genId(String province, String city, String county, String year, String month, String day,
			String sex) {
		String birthDate = new StringBuilder().append(year).append(genFullDate(month)).append(genFullDate(day))
				.toString();
		if (!isBirthdateValid(birthDate)) {
			logger.error("[Error]输入的年月日不正确,请重新输入.");
			return EMPTY_STR;
		}

		String locationCode = genLocationCode(province, city, county);
		if (EMPTY_STR.equals(locationCode)) {
			return EMPTY_STR;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(locationCode);
		sb.append(birthDate);
		sb.append(genSeqCode(sex));
		sb.append(genVerifyCode(sb.toString()));

		return sb.toString();
	}

	private String genLocationCode(String province, String city, String county) {
		Region provinceRegion = nameProvincesMap.get(preHandleStr(province));
		if (provinceRegion == null) {
			logger.error("[Error]输入的省份不存在,请重新输入");
			return EMPTY_STR;
		}

		Region cityRegion = provinceRegion.getNameRegionMap().get(preHandleStr(city));

		if (cityRegion == null) {
			logger.error("[Error]输入的市不存在,请重新输入");
			return EMPTY_STR;
		}

		Region countyRegion = cityRegion.getNameRegionMap().get(preHandleStr(county));

		if (countyRegion == null) {
			logger.error("[Error]输入的县/区不存在,请重新输入");
			return EMPTY_STR;
		}

		return new StringBuilder().append(provinceRegion.getCode()).append(cityRegion.getCode())
				.append(countyRegion.getCode()).toString();
	}

	private String preHandleStr(String str) {
		return str.trim();
	}

	private String genFullDate(String date) {
		StringBuilder sb = new StringBuilder();
		if (Integer.valueOf(date) < 10) {
			sb.append("0");
		}

		sb.append(date);
		return sb.toString();
	}

	private String genSeqCode(String sex) {
		StringBuilder sb = new StringBuilder();
		sb.append(genRandomSeq()).append(genSexCode(sex));

		return sb.toString();
	}

	/**
	 * 生成0-99的随机数并转换为String类型
	 * 
	 * @return
	 */
	private String genRandomSeq() {
		int random = (int) (Math.random() * 99);

		if (random > 10) {
			return String.valueOf(random);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("0");
		sb.append(String.valueOf(random));

		return sb.toString();
	}

	/**
	 * 生成性别码
	 * 
	 * @param sex
	 * @return
	 */
	private String genSexCode(String sex) {
		int random = (int) (Math.random() * 4);
		String sexCode = "";

		String male = "男";
		String female = "女";

		if (sex.equals(male)) {
			sexCode = String.valueOf(2 * random + 1);
		} else if (sex.equals(female)) {
			sexCode = String.valueOf(2 * random);
		}

		return sexCode;
	}

	/**
	 * 
	 * @param idCard
	 *            身份证的前17位
	 * @return
	 */
	private String genVerifyCode(String idCard) {
		int LENGH_17_POS = 17;
		if (LENGH_17_POS != idCard.length()) {
			return EMPTY_STR;
		}

		// 身份证每一位的权重
		int[] weight = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		String[] verfiyMap = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

		if (idCard.length() != weight.length) {
			return EMPTY_STR;
		}

		int total = 0;
		for (int i = 0; i < weight.length; i++) {
			total = total + weight[i] * Integer.parseInt(String.valueOf(idCard.charAt(i)));
		}

		return verfiyMap[total % 11];
	}

	/**
	 * 
	 * @param idCard
	 * @return 身份证详细信息
	 */
	public String parseID(String idCard) {
		logger.info("parseID begin...");

		if (null == idCard || idCard.equals(EMPTY_STR)) {
			return EMPTY_STR;
		}

		// 去除左右多余的空格
		idCard = idCard.trim();

		if (!isIDValid(idCard)) {
			logger.debug("id is not 18 number");
			return parseLocation(idCard);
		}

		StringBuilder sb = new StringBuilder();

		String locationCode = idCard.substring(0, 6);
		String location = getLocation(locationCode);

		if (location == null) {
			return sb.append(INVALID_ID).toString();
		}

		if (!isBirthdateValid(idCard.substring(6, 14))) {
			return sb.append(INVALID_ID).toString();
		}

		sb.append("发证地:").append(location).append("\n");
		sb.append("出生日期:").append(idCard.substring(6, 10)).append("年");
		sb.append(idCard.substring(10, 12)).append("月");
		sb.append(idCard.substring(12, 14)).append("日").append("\n");
		sb.append("性别:").append(getSex(idCard.substring(16, 17)));

		logger.info("parseID end...");
		return sb.toString();
	}

	private String parseLocation(String locationCode) {
		if (!isDigital(locationCode)) {
			logger.debug("It is not digital number");
			return EMPTY_STR;
		}

		StringBuilder sb = new StringBuilder(EMPTY_STR);
		if (locationCode.length() >= 6) {
			return getLocation(locationCode);
		}

		Region province = null;
		Region city = null;
		if (locationCode.length() >= 4) {
			province = getRegion(locationCode.substring(0, 2));
			if (province == null) {
				return EMPTY_STR;
			}

			sb.append(province.getName());

			city = getCity(locationCode.substring(2, 4), province);
			if (city == null) {
				return sb.toString();
			}

			sb.append(city.getName());
			return sb.toString();
		}

		if (locationCode.length() >= 2) {
			province = getRegion(locationCode.substring(0, 2));
			if (province == null) {
				return EMPTY_STR;
			}

			sb.append(province.getName());
			return sb.toString();
		}

		return sb.toString();
	}

	/**
	 * @author:idevcod@163.com
	 * @date:2015年8月19日下午11:41:50
	 * @description:判断字符串是否为由数字组成,由数字组成返回true,否则返回false
	 * @param digitalStr
	 * @return true,false;
	 */
	private boolean isDigital(String digitalStr) {
		if (digitalStr == null || digitalStr.equals(EMPTY_STR)) {
			return false;
		}

		String validIDPatter = "[0-9].*?";
		if (!digitalStr.matches(validIDPatter)) {
			return false;
		}

		return true;
	}

	/**
	 * @author dell
	 * @description 检测身份证长度是否合法,以及校验码填写的是否正确
	 * @param idCard
	 *            18位长度的身份证号码
	 * @return
	 */
	private boolean isIDValid(String idCard) {
		String validIDPatter = "[0-9]{17}[0-9X]";
		if (!idCard.matches(validIDPatter)) {
			return false;
		}

		String verifyCode = genVerifyCode(idCard.substring(0, 17));
		if (!idCard.substring(17, 18).equals(verifyCode)) {
			return false;
		}

		return true;
	}

	private String getLocation(String locationStr) {
		String provinceCode = locationStr.substring(0, 2);
		String cityCode = locationStr.substring(2, 4);
		String countyCode = locationStr.substring(4, 6);

		Region province = getRegion(provinceCode);

		if (province == null) {
			return EMPTY_STR;
		}

		Region city = getCity(cityCode, province);

		if (city == null) {
			return EMPTY_STR;
		}

		Region county = getCounty(countyCode, city);
		if (county == null) {
			return EMPTY_STR;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(province.getName().trim()).append(city.getName().trim()).append(county.getName().trim());

		return sb.toString();
	}

	private Region getCity(String cityCode, Region province) {
		Region city = null;

		for (final Region cityReg : province.getSubRegions()) {
			if (cityReg.getCode().equals(cityCode)) {
				city = cityReg;
			}
		}
		return city;
	}

	private Region getCounty(String cityCode, Region province) {
		return getCity(cityCode, province);
	}

	private Region getRegion(String provinceCode) {
		Region province = null;

		for (final Region provinceReg : provinces) {
			if (provinceReg.getCode().equals(provinceCode)) {
				province = provinceReg;
			}
		}

		return province;
	}

	/**
	 * 
	 * @param birthdate检查出生日期是否填写的正确
	 * @return
	 */
	private boolean isBirthdateValid(String birthdate) {
		String year = birthdate.substring(0, 4);
		String month = birthdate.substring(4, 6);
		String day = birthdate.substring(6, 8);

		if (Integer.valueOf(month) > MAX_MONTH) {
			return false;
		}

		if (Integer.valueOf(day) > getMaxDay(year, month)) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return 算出当月最大是多少天
	 */
	private int getMaxDay(int year, int month) {
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
	private int getMaxDay(String year, String month) {
		return getMaxDay(Integer.valueOf(year), Integer.valueOf(month));
	}

	private String getSex(String sexCode) {
		if (Integer.valueOf(sexCode) % 2 == 0) {
			return "女";
		}

		return "男";
	}

	public List<String> batGenID(String fromYear, String toYear, String province, String city, String county,
			String sex) {
		List<String> idList = new ArrayList<String>();

		if (!isDigital(fromYear) || !isDigital(toYear)) {
			String errMsg = "invalid year parameters,please check!";
			logger.error(errMsg);
			throw new InvalidParameterException(errMsg);
		}

		int fromYearInt = Integer.valueOf(fromYear);
		int toYearInt = Integer.valueOf(toYear);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);

		if (toYearInt < fromYearInt || toYearInt > currentYear) {
			String errMsg = "invalid boundaries,please check";
			logger.error(errMsg);
			throw new InvalidParameterException(errMsg);
		}

		int year = fromYearInt;
		int month = 1;
		int day = 1;

		/** 一年最大月份数 */
		int maxMonth = 12;
		/** 当月最大的天数 */
		int maxDay = 0;

		for (; year <= toYearInt; year++) {
			month = 1;
			for (; month <= maxMonth; month++) {
				day = 1;
				maxDay = getMaxDay(year, month);
				for (; day <= maxDay; day++) {
					String tmpID = genId(province, city, county, String.valueOf(year), String.valueOf(month),
							String.valueOf(day), sex);

					// 如果进入该分支,说明某个参数是非法的.因为genID会检查参数的合法性.
					if (tmpID.isEmpty()) {
						logger.error("encounter unknown error.year={} month={} day={}", year, month, day);
						// break;
					}

					idList.add(tmpID);
				}
			}
		}

		return idList;
	}
}
