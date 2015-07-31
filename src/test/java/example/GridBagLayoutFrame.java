package example;

/**
 * GridLayoutFrame.java
 * TECHSCORE Javaユーザインタフェース5章 実習課題2
 *
 * Copyright (c) 2004 Four-Dimensional Data, Inc.
 */

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GridBagLayoutFrame extends JFrame {

	public GridBagLayoutFrame() {
		super("GridBag Layout");
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// GridBagLayoutの作成
		GridBagLayout layout = new GridBagLayout();
		super.getContentPane().setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		// ボタンを４つ配置

		JButton first = new JButton("first");
		first.setBackground(Color.red);
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		layout.setConstraints(first, c);
		super.getContentPane().add(first);

		JButton second = new JButton("second");
		second.setBackground(Color.yellow);
		c.gridwidth = 1;
		c.gridy = 1;
		c.gridheight = 2;
		c.fill = GridBagConstraints.VERTICAL;
		layout.setConstraints(second, c);
		super.getContentPane().add(second);

		JButton third = new JButton("third");
		third.setBackground(Color.blue);
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		layout.setConstraints(third, c);
		super.getContentPane().add(third);

		JButton forth = new JButton("fourth");
		forth.setBackground(Color.green);
		c.gridx = 1;
		c.gridy = 2;
		layout.setConstraints(forth, c);
		super.getContentPane().add(forth);

		super.pack();
	}

	public static void main(String args[]) {
		new GridBagLayoutFrame().setVisible(true);
	}

}