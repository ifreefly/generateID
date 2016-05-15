/**@author:idevcod@163.com
 * @date:2015年11月1日下午11:37:17
 * @description:<TODO>
 */
package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import listener.SwingThreadInit;
import listener.SwingThreadInitListener;
import util.sql.SqliteHelper;

public class AESPanel extends JPanel implements SwingThreadInitListener {
	private static final String SAVE = "保存";
	private static final String DELETE = "删除";
	private static final String ADD = "增加";
	/**
	 * 
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private static final String ASTERISKS = "************************";
	private static final long serialVersionUID = 1L;
	private final String EMPTY_STRING = "";

	private JScrollPane scrollPane;
	private JTable table;
	private JPasswordField passwordField;

	private DataTableModel dataTableModel;

	private static final String PASSOWRD = "password";
	private static final int DEFAULT_ROW_HEIGHT = 25;

	private final String CREATE_ACCOUNT_TABLE = "create table if not exists Account(" + "id integer PRIMARY KEY AUTOINCREMENT,"
			+ "project nvarchar(100)," + "username nvarchar(100)," + "password varchar(50)," + "workKey char(64)"
			+ ");";

	@SuppressWarnings("unused")
	public AESPanel() {
		new SwingThreadInit(this);
	}

	private void init() {
		initUI();
		initModel();
	}

	/**
	 * @author:idevcod@163.com
	 * @date:2015年12月27日下午10:27:23
	 * @description:初始化数据库等资源
	 */
	private void initModel() {
		initTable();
		queryDataAndShowInUI();
	}

	/**
	 * @author:idevcod@163.com
	 * @date:2015年12月27日下午11:53:22
	 * @description:初始化Account表,如果不存在就创建
	 */
	private void initTable() {
		System.out.println(CREATE_ACCOUNT_TABLE);
		SqliteHelper.getInstance().execute(CREATE_ACCOUNT_TABLE);
	}

	private void queryDataAndShowInUI() {
		// TODO Auto-generated method stub

	}

	private void initUI() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		JButton addBtn = new JButton(ADD);

		panel.add(addBtn);

		JButton delBtn = new JButton(DELETE);

		panel.add(delBtn);

		JButton saveBtn = new JButton(SAVE);

		panel.add(saveBtn);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		panel.add(passwordField);

		dataTableModel = new DataTableModel();
		table = new JTable(dataTableModel);
		table.setFillsViewportHeight(true);
		table.getColumn(PASSOWRD).setCellRenderer(new PasswordRender());
		table.getColumn(PASSOWRD).setCellEditor(new PasswordEditor());
		table.setRowHeight(DEFAULT_ROW_HEIGHT);

		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataTableModel.addRow();
			}
		});

		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if (index < 0) {
					logger.debug("Could not get selected row.");
					return;
				}

				dataTableModel.removeRow(index);
			}
		});

		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if (index < 0) {
					logger.debug("Could not get selected row.");
					return;
				}

				save(index);
			}

		});
	}

	@Override
	public void threadInit() {
		init();
	}

	class DataTableModel extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String[] columnNames = { "project", "userName", PASSOWRD };
		private List<Account> accountRows = new ArrayList<Account>();

		@Override
		public int getRowCount() {
			return accountRows.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return true;
		};

		@Override
		public Object getValueAt(int row, int col) {
			Account account = accountRows.get(row);
			Object value = null;
			switch (col) {
			case 0: {
				value = account.getProject();
				break;
			}
			case 1: {
				value = account.getUserName();
				break;
			}
			case 2: {
				value = account.getPassword();
				break;
			}
			}
			return value;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			Account account = accountRows.get(row);
			switch (col) {
			case 0: {
				account.setProject((String) value);
				break;
			}
			case 1: {
				account.setUserName((String) value);
				break;
			}
			case 2: {
				account.setPassword((String) value);
				break;
			}
			}

			fireTableCellUpdated(row, col);
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public void addRow() {
			accountRows.add(new Account());
			fireTableRowsInserted(0, 0);
		}

		public void removeRow(int row) {
			if (row < 0 || row > accountRows.size()) {
				return;
			}
			accountRows.remove(row);
			fireTableRowsDeleted(0, 0);
		}

		public Account getRow(int rowIndex) {
			if (rowIndex < 0 || rowIndex > accountRows.size()) {
				logger.error("Invalid rowindex.You need to select a row to save.");
				return null;
			}

			return accountRows.get(rowIndex);
		}
	}

	class Account {
		private int id;
		private String project;
		private String userName;
		private String password;
		private String workKey;

		public Account() {
			project = "";
			userName = "";
			password = "";
		}

		public String getProject() {
			return project;
		}

		public void setProject(String project) {
			this.project = project;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getWorkKey() {
			return workKey;
		}

		public void setWorkKey(String workKey) {
			this.workKey = workKey;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	private class PasswordRender extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			int length = 0;
			if (value instanceof String) {
				length = ((String) value).length();
			} else if (value instanceof char[]) {
				length = ((char[]) value).length;
			}

			logger.debug("getTableCellRendererComponent ,value is {}",(String)value);
			Account account = dataTableModel.getRow(row);
			account.setPassword((String)value);
			setText(asterisks(length));
			return this;
		}

	}

	private class PasswordEditor extends AbstractCellEditor implements TableCellEditor {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JPasswordField passwordField;

		public PasswordEditor() {
			super();

			passwordField = new JPasswordField();
			passwordField.setEchoChar('*');
		}

		@Override
		public Object getCellEditorValue() {
			logger.debug("getCellEditorValue");
			return new String(passwordField.getPassword());
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			// 先将当前单元格的值获取到并设置到passwordField中.
			passwordField.setText((String) value);
			return passwordField;
		}
	}

	private String asterisks(int length) {
		if (length > ASTERISKS.length()) {
			StringBuilder sb = new StringBuilder(length);
			for (int i = 0; i < length; i++) {
				sb.append('*');
			}
			return sb.toString();
		} else {
			return ASTERISKS.substring(0, length);
		}
	}

	private boolean save(int rowIndex) {
		Account account = dataTableModel.getRow(rowIndex);
		String sql = EMPTY_STRING;
		if (account.getId() == 0) {
			sql = createInsertSql(account);
		} else {
			sql = createUpdateSql(account);
		}

		return SqliteHelper.getInstance().execute(sql);
	}

	private String createUpdateSql(Account account) {
		Object[] arguments = { account.getProject(), account.getUserName(), account.getPassword(),
				account.getWorkKey() };

		String updateSql = MessageFormat.format(
				"update Account set project = ''{0}'', username = ''{1}'',password = ''{2}'', workKey=''{3}''",
				arguments);

		logger.debug("update sql is {}", updateSql);
		return updateSql;
	}

	private String createInsertSql(Account account) {
		Object[] arguments = { account.getProject(), account.getUserName(), account.getPassword(),
				account.getWorkKey() };

		String insertSql = MessageFormat.format(
				"insert into Account (project,username,password,workKey) values (''{0}'',''{1}'',''{2}'',''{3}'')",
				arguments);

		logger.debug("insertSql is {}", insertSql);
		return insertSql;
	}
}
