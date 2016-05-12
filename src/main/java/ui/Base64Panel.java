/**@author:idevcod@163.com
 * @date:2015年9月13日下午4:35:46
 * @description:<TODO>
 */
package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.UnsupportedEncodingException;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import listener.SwingThreadInit;
import listener.SwingThreadInitListener;
import util.StringUtil;
import javax.swing.JCheckBox;

public class Base64Panel extends JPanel implements SwingThreadInitListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EMPTY_STR = "";
	
	private static final double RESIZE_WEIGHT=0.50;

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private JTextArea leftTextArea;
	private JTextArea righttextArea;
	private Base64DocumentListener base64DocumentListener;
	private JButton encodeBtn;
	private JButton decodeBtn;
	private JCheckBox chkbox;

	@SuppressWarnings("unused")
	public Base64Panel() {
		new SwingThreadInit(this);
	}

	private void init() {
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		encodeBtn = new JButton(UIConstant.BASE_ENCODE);
		panel.add(encodeBtn);

		decodeBtn = new JButton(UIConstant.BASE_DECODE);
		panel.add(decodeBtn);

		chkbox = new JCheckBox(UIConstant.BASE64_INST_ENCODE);
		panel.add(chkbox);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(RESIZE_WEIGHT);
		add(splitPane, BorderLayout.CENTER);

		JScrollPane leftScrPane = new JScrollPane();
		splitPane.setLeftComponent(leftScrPane);

		leftTextArea = new JTextArea();

		leftScrPane.setViewportView(leftTextArea);

		JScrollPane rightscrPane = new JScrollPane();
		splitPane.setRightComponent(rightscrPane);

		righttextArea = new JTextArea();
		rightscrPane.setViewportView(righttextArea);

		base64DocumentListener = new Base64DocumentListener();

		initListener();
	}

	private void initListener() {
		encodeBtn.addActionListener((ActionEvent e) -> {
			String text = leftTextArea.getText();
			righttextArea.setText(base64Encode(text));
			;
		});

		decodeBtn.addActionListener((ActionEvent e) -> {
			String text = righttextArea.getText();
			leftTextArea.setText(base64Decode(text));
		});

		chkbox.addItemListener((ItemEvent e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				leftTextArea.getDocument().addDocumentListener(base64DocumentListener);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				leftTextArea.getDocument().removeDocumentListener(base64DocumentListener);
			}
		});
	}

	public String base64Encode(String str) {
		if (StringUtil.isEmpty(str)) {
			return EMPTY_STR;
		}

		Base64 base64 = new Base64();
		try {
			str = base64.encodeToString(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			return EMPTY_STR;
		}

		return str;
	}

	public String base64Decode(String str) {
		if (StringUtil.isEmpty(str)) {
			return EMPTY_STR;
		}

		str = new String(Base64.decodeBase64(str));
		return str;
	}

	class Base64DocumentListener implements DocumentListener {
		@Override
		public void removeUpdate(DocumentEvent e) {
			righttextArea.setText(base64Encode(leftTextArea.getText()));
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			righttextArea.setText(base64Encode(leftTextArea.getText()));
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		}
	}

	@Override
	public void threadInit() {
		init();
	}
}
