package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Calendar;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.ParseIDXml;

public class UI {

	final Logger logger = LoggerFactory.getLogger(UI.class.getName());
	public JFrame frame;
	private final JTabbedPane idTabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private final JPanel idPanel = new JPanel();
	private final JPanel base64Panel = new JPanel();

	private final JPanel idOptionsPanel = new JPanel();
	private final JPanel baseFuncPanel = new JPanel();
	private final JPanel moreFuncPanel = new JPanel();
	private final JPanel batGenIDPanel = new JPanel();
	private final JLabel fromYearLabel = new JLabel(UIConstant.FROM);
	private final JTextField fromYearField = new JTextField();
	private final JLabel toYearLabel = new JLabel(UIConstant.TO);
	private final JTextField toYearField = new JTextField();
	private final JButton batGenIDBtn = new JButton(UIConstant.BAR_GEN_ID_BTN);
	private boolean morePanelFlag = false;

	private final JPanel regionsPanel = new JPanel();
	private final JComboBox<String> proBox = new JComboBox<String>();
	private JComboBox<String> cityBox = new JComboBox<String>();
	private final JLabel provinceLabel = new JLabel(UIConstant.REGION_LABEL);
	private final JLabel cityLabel = new JLabel(UIConstant.CITY_LABEL);
	private final JLabel countyLabel = new JLabel(UIConstant.COUNTY_LABEL);
	private JComboBox<String> countyBox = new JComboBox<String>();

	private final JPanel BirthdatePanel = new JPanel();
	private final JLabel YearLabel = new JLabel(UIConstant.YEAR_LABEL);
	private final JComboBox<String> yearBox = new JComboBox<String>();
	private final JLabel monthLabel = new JLabel(UIConstant.MONTH_LABEL);
	private final JComboBox<String> monthBox = new JComboBox<String>();
	private final JLabel dayLabel = new JLabel(UIConstant.DAY_LABEL);
	private final JComboBox<String> dayBox = new JComboBox<String>();

	private final JPanel sexPanel = new JPanel();
	private final JLabel sexLabel = new JLabel(UIConstant.SEX_LABEL);
	private final JComboBox<String> sexBox = new JComboBox<String>();
	private final JScrollPane textScrollPane = new JScrollPane();
	private final JTextArea textArea = new JTextArea();

	private String currentProvince = null;
	private String currentCity = null;
	private String currentCounty = null;

	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";

	private String currentSex = "";

	private ParseIDXml parseIDXml;
	
	private ImageIcon closeIcon;

	private final JButton genIDBtn = new JButton(UIConstant.ENCODE_ID_BTN);
	private final JButton deIDBtn = new JButton(UIConstant.DECODE_ID_BTN);
	private final JButton moreIDBtn = new JButton(UIConstant.MORE_ID_BTN);

	/**
	 * @wbp.parser.entryPoint
	 */
	public UI() {
		frame = new JFrame();
		frame.add(idTabbedPane);
		
		setTabbedPanel();
		
		setIDPanel();
		init();// 有对控件进行改动

		frame.setMinimumSize(new Dimension(1000, 500));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setTabbedPanel() {
		closeIcon=new ImageIcon(this.getClass().getClassLoader().getResource(UIConstant.CLOSE_ICON).getPath());
		idTabbedPane.addTab(UIConstant.ID_TAB_LABEL, closeIcon,idPanel,null);
		idTabbedPane.addTab(UIConstant.BASE64_TAB_LABEL,closeIcon, base64Panel);
	}

	private void setIDPanel() {
		idPanel.setLayout(new BorderLayout());
		idPanel.add(idOptionsPanel, BorderLayout.NORTH);

		idOptionsPanel.setLayout(new BorderLayout());
		idOptionsPanel.add(baseFuncPanel, BorderLayout.NORTH);
		idOptionsPanel.add(moreFuncPanel, BorderLayout.SOUTH);
		setMoreIDPanel();
		moreFuncPanel.setVisible(morePanelFlag);

		setBaseIDPanel();

		textArea.setRows(5);
		idPanel.add(textScrollPane, BorderLayout.CENTER);
		textScrollPane.setViewportView(textArea);
	}

	private void setMoreIDPanel() {
		moreFuncPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		moreFuncPanel.add(batGenIDPanel);

		fromYearField.setColumns(5);
		toYearField.setColumns(5);
		batGenIDPanel.add(fromYearLabel);
		batGenIDPanel.add(fromYearField);
		batGenIDPanel.add(toYearLabel);
		batGenIDPanel.add(toYearField);
		batGenIDPanel.add(batGenIDBtn);
		
		batGenIDBtn.addActionListener((ActionEvent e) ->{
				String fromYear = fromYearField.getText().trim();
				String toYear = toYearField.getText().trim();

				List<String> idList = parseIDXml.batGenID(fromYear, toYear, currentProvince, currentCity, currentCounty,
						currentSex);

				addIDListToTextArea(idList);
		});
	}

	private void addIDListToTextArea(List<String> idList) {
		// 先清空之前的显示
		textArea.setText("");
		for (String id : idList) {
			textArea.append(id);
			textArea.append(UIConstant.LINE_SEPERATOR);
		}
	}

	private void setBaseIDPanel() {
		baseFuncPanel.add(regionsPanel);
		regionsPanel.add(provinceLabel);
		regionsPanel.add(proBox);
		regionsPanel.add(cityLabel);
		regionsPanel.add(cityBox);
		regionsPanel.add(countyLabel);
		regionsPanel.add(countyBox);

		baseFuncPanel.add(BirthdatePanel);
		BirthdatePanel.add(YearLabel);
		BirthdatePanel.add(yearBox);
		BirthdatePanel.add(monthLabel);
		BirthdatePanel.add(monthBox);
		BirthdatePanel.add(dayLabel);
		BirthdatePanel.add(dayBox);

		baseFuncPanel.add(sexPanel);
		sexPanel.setLayout(new BoxLayout(sexPanel, BoxLayout.X_AXIS));
		sexPanel.add(sexLabel);
		sexPanel.add(sexBox);

		genIDBtn.addActionListener((ActionEvent e) -> {
			String id = parseIDXml.genId(currentProvince, currentCity, currentCounty, currentYear, currentMonth,
					currentDay, currentSex);
			textArea.setText(id);
		});

		deIDBtn.addActionListener((ActionEvent e) -> {
			String idInfo = parseIDXml.parseID(textArea.getText());
			textArea.setText(idInfo);
		});

		moreIDBtn.addActionListener((ActionEvent e) -> {
			morePanelFlag = morePanelFlag ? false : true;
			moreFuncPanel.setVisible(morePanelFlag);
		});

		baseFuncPanel.add(genIDBtn);
		baseFuncPanel.add(deIDBtn);
		baseFuncPanel.add(moreIDBtn);
	}

	private void init() {
		parseIDXml = ParseIDXml.getInstance();
		parseIDXml.init();
		proBox.addItemListener((ItemEvent e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				currentProvince = ((String) proBox.getSelectedItem()).trim();
				addCity(currentProvince);
			}
		});

		cityBox.addItemListener((ItemEvent e) -> {
			// TODO Auto-generated method stub
			if (e.getStateChange() == ItemEvent.SELECTED) {
				currentCity = ((String) cityBox.getSelectedItem()).trim();
				addCounty(currentProvince, currentCity);
			}
		});

		countyBox.addItemListener((ItemEvent e) -> {
			// TODO Auto-generated method stub
			if (e.getStateChange() == ItemEvent.SELECTED) {
				currentCounty = (String) countyBox.getSelectedItem();
			}
		});

		yearBox.addItemListener((ItemEvent e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				currentYear = (String) yearBox.getSelectedItem();
			}
		});

		monthBox.addItemListener((ItemEvent e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				currentMonth = (String) monthBox.getSelectedItem();
				addDay(getMaxDay());
			}
		});

		dayBox.addItemListener((ItemEvent e) -> {
			currentDay = (String) dayBox.getSelectedItem();
		});

		sexBox.addItemListener((ItemEvent e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				currentSex = (String) sexBox.getSelectedItem();
			}
		});

		addProvince(parseIDXml.getProvinces());

		addYear();
		addMonth();

		addSex();
	}

	public void addProvince(List<String> provinces) {
		for (String province : provinces) {
			proBox.addItem(province);
		}
	}

	public void addCity(String provinceName) {
		cityBox.removeAllItems();
		List<String> cities = parseIDXml.getCities(provinceName);

		for (String city : cities) {
			cityBox.addItem(city);
		}
	}

	public void addCounty(String provinceName, String cityName) {
		List<String> counties = parseIDXml.getCounties(provinceName, cityName);
		if (counties == null) {
			logger.error("[Error]get counties failed");
			return;
		}

		countyBox.removeAllItems();
		for (String county : counties) {
			countyBox.addItem(county);
		}
	}

	private void addYear() {
		yearBox.removeAllItems();

		// 现在应该没有1890年的身份证了;当前计算机的计数方法将会在2038年到期.
		for (int year = 1890; year < 2038; year++) {
			yearBox.addItem(String.valueOf(year));
		}

		currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		// 将年份设为当前年份的18年前
		String yearBefore18 = String.valueOf(Integer.valueOf(currentYear) - 18);
		yearBox.setSelectedItem(yearBefore18);
	}

	private void addMonth() {
		monthBox.removeAllItems();

		for (int month = 1; month <= 12; month++) {
			monthBox.addItem(String.valueOf(month));
		}

		currentMonth = (String) monthBox.getSelectedItem();
	}

	private void addDay(int maxDay) {
		int previosSelectedDay = dayBox.getSelectedIndex();

		dayBox.removeAllItems();

		for (int day = 1; day <= maxDay; day++) {
			dayBox.addItem(String.valueOf(day));
		}

		logger.debug("previous selected day={}", previosSelectedDay);
		if (previosSelectedDay == -1) {
			dayBox.setSelectedIndex(0);
		} else if (previosSelectedDay <= maxDay) {
			dayBox.setSelectedIndex(previosSelectedDay);
		}
	}

	private void addSex() {
		sexBox.removeAllItems();

		String[] sexs = { UIConstant.MALE, UIConstant.FEMALE };

		for (String sex : sexs) {
			sexBox.addItem(sex);
		}

	}

	private int getMaxDay() {
		String[] bigMonth = { "1", "3", "5", "7", "8", "10", "12" };
		String[] medMonth = { "4", "6", "9", "11" };

		for (String bMonth : bigMonth) {
			if (currentMonth.equals(bMonth)) {
				return 31;
			}
		}

		for (String mMonth : medMonth) {
			if (mMonth.equals(currentMonth)) {
				return 30;
			}
		}

		int yearInt = Integer.valueOf(currentYear);
		if (yearInt % 100 == 0 && yearInt % 400 == 0 || yearInt % 4 == 0) {
			return 29;
		}

		return 28;
	}
}
