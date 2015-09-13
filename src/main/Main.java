package main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ui.UI;

public class Main {
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new UI();
	}
}
