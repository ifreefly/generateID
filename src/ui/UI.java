package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UI {

	final Logger logger = LoggerFactory.getLogger(UI.class.getName());
	public JFrame frame;

	private final EnhancedTabPanel tabbedPane = new EnhancedTabPanel(JTabbedPane.TOP);


	private JScrollPane treeScrollPanel;
	private JTree tree = null;

	/**
	 * @wbp.parser.entryPoint
	 */
	public UI() {
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		addTreePanel();
		addTabbedPanel(UIConstant.ID_TAB_LABEL, new IDPanel());
		addTabbedPanel(UIConstant.BASE64_TAB_LABEL,new Base64Panel());

		frame.add(tabbedPane, BorderLayout.CENTER);
		
		frame.setMinimumSize(new Dimension(1200, 500));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void addTreePanel() {
	
		// TODO Auto-generated method stub
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(UIConstant.FUNCTION_PANEL);
		createNodes(rootNode);
		
		tree = new JTree(rootNode);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener((TreeSelectionEvent e) -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	
			if (node == null)
				return;
	
			String tabTitle = (String) node.getUserObject();
			if (!node.isLeaf()) {
				return;
			}
			
			if (UIConstant.ID_TAB_LABEL.equals(tabTitle) && !tabbedPane.isTabExist(tabTitle)) {
				tabbedPane.addCloseComponent(UIConstant.ID_TAB_LABEL, new IDPanel());
				return;
			}
			
			if (UIConstant.BASE64_TAB_LABEL.equals(tabTitle) && !tabbedPane.isTabExist(tabTitle)) {
				tabbedPane.addCloseComponent(UIConstant.BASE64_TAB_LABEL, new Base64Panel());
				return;
			}
			
			if (UIConstant.URLENCODE_TAB_LABEL.equals(tabTitle) && !tabbedPane.isTabExist(tabTitle)) {
				tabbedPane.addCloseComponent(UIConstant.URLENCODE_TAB_LABEL, new UrlEncodePanel());
				return;
			}
			
		});
	
		treeScrollPanel = new JScrollPane(tree);
		frame.add(treeScrollPanel, BorderLayout.WEST);
	}

	private void createNodes(DefaultMutableTreeNode rootNode) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode function = null;

		category = new DefaultMutableTreeNode(UIConstant.FUNCTION_CHOOSE);
		rootNode.add(category);

		function = new DefaultMutableTreeNode(UIConstant.ID_TAB_LABEL);
		category.add(function);

		function = new DefaultMutableTreeNode(UIConstant.BASE64_TAB_LABEL);
		category.add(function);
		
		function = new DefaultMutableTreeNode(UIConstant.URLENCODE_TAB_LABEL);
		category.add(function);
	}

	private void addTabbedPanel(String title,Component component) {
		if (component ==null) {
			logger.error("component is null");
			return;
		}
		
		tabbedPane.addCloseComponent(title, component);
	}

}