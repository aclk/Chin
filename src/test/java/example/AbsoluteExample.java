package example;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AbsoluteExample extends JFrame {

	public static void main(String[] args) {
		AbsoluteExample frame = new AbsoluteExample();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(10, 10, 300, 200);
		frame.setTitle("ƒ^ƒCƒgƒ‹");
		frame.setVisible(true);
	}

	AbsoluteExample() {
		GridBagLayout layout = new GridBagLayout();
		JPanel p = new JPanel();
		p.setLayout(layout);

		GridBagConstraints gbc = new GridBagConstraints();

		JButton button1 = new JButton("Google");

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.weightx = 1.0d;
		gbc.weighty = 1.0d;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.insets = new Insets(10, 0, 10, 0);
		layout.setConstraints(button1, gbc);

		JButton button2 = new JButton("Yahoo!");
		button2.setFont(new Font("Arial", Font.PLAIN, 30));

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.weightx = 1.0d;
		gbc.weighty = 1.0d;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5);
		layout.setConstraints(button2, gbc);

		JButton button3 = new JButton("MSN");

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1.0d;
		gbc.weighty = 1.0d;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 0);
		layout.setConstraints(button3, gbc);

		p.add(button1);
		p.add(button2);
		p.add(button3);

		getContentPane().add(p, BorderLayout.CENTER);
	}
}