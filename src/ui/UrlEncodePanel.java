/**@author:idevcod@163.com
 * @date:2015年9月13日下午9:34:47
 * @description:<TODO>
 */
package ui;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import listener.SwingThreadInit;
import listener.SwingThreadInitListener;
import util.StringUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class UrlEncodePanel extends JPanel implements SwingThreadInitListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private static final double RESIZE_WEIGHT = 0.5;
	private JPanel panel;
	private JSplitPane splitPane;
	private JComboBox<String> comboBox ;
	private String charset="UTF-8";
	private JButton encodeBtn;
	private JButton decodeBtn;
	private JTextArea leftTextArea;
	private JTextArea rigthTextArea;
	private JCheckBox chkbox;
	private URLDocumentListener urListener;
	
	@SuppressWarnings("unused")
	public UrlEncodePanel() {
		new SwingThreadInit(this);
	}

	private void init() {
		setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		encodeBtn = new JButton(UIConstant.URL_ENCODE);
		panel.add(encodeBtn);
		
		decodeBtn = new JButton(UIConstant.URL_DECODE);
		panel.add(decodeBtn);
		
		initCombox();
		panel.add(comboBox);
		
		chkbox = new JCheckBox(UIConstant.BASE64_INST_ENCODE);
		panel.add(chkbox);

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(RESIZE_WEIGHT);
		add(splitPane, BorderLayout.CENTER);
		
		JScrollPane leftScroPane = new JScrollPane();
		splitPane.setLeftComponent(leftScroPane);
		
		leftTextArea = new JTextArea();
		leftScroPane.setViewportView(leftTextArea);
		
		JScrollPane rightScroPane = new JScrollPane();
		splitPane.setRightComponent(rightScroPane);
		
		rigthTextArea = new JTextArea();
		rightScroPane.setViewportView(rigthTextArea);
		
		urListener = new URLDocumentListener();
		
		initListener();
	}

	private void initCombox() {
		comboBox = new JComboBox<String>();
		comboBox.addItem("UTF-8");
		comboBox.addItem("GB2312");
	}
	
	private void initListener() {
		chkbox.addItemListener((ItemEvent e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				leftTextArea.getDocument().addDocumentListener(urListener);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				leftTextArea.getDocument().removeDocumentListener(urListener);
			}
		});
		
		encodeBtn.addActionListener((ActionEvent e)->{
			encode();
		});
		
		decodeBtn.addActionListener((ActionEvent e)->{
			decode();
		});
		
		comboBox.addItemListener((ItemEvent e)->{
			if (e.getStateChange() == ItemEvent.SELECTED) {
				charset=(String)e.getItem();
				logger.debug(charset);
			}
		});
		
		
	}
	
	private void encode(){
		rigthTextArea.setText(UrlEncode(leftTextArea.getText()));
	}
	
	private void decode(){
		leftTextArea.setText(urlDecode(rigthTextArea.getText()));
	}

	public String UrlEncode(String str){
		if (StringUtil.isEmpty(str)) {
			return StringUtil.EMPTY_STR;
		}
		
		String result=StringUtil.EMPTY_STR;
		try {
			result= URLEncoder.encode(str, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		
		return result;
	}
	
	public String urlDecode(String str){
		if (StringUtil.isEmpty(str)) {
			return StringUtil.EMPTY_STR;
		}
		
		String result = StringUtil.EMPTY_STR;
		try {
			result= URLDecoder.decode(str,charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
				
		return result;
	}
	

	@Override
	public void threadInit() {
		init();
	}

	class URLDocumentListener implements DocumentListener{

		@Override
		public void insertUpdate(DocumentEvent e) {
			encode();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			encode();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		}
		
	}
}
