import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JComboBox;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JSeparator;

import java.awt.Component;

import javax.swing.Box;

import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.Color;


public class CreationPanel extends JPanel implements KeyListener{
	private JTextField nameField;

	static String name;
	static Profession profession;
	static Race race;


	/**
	 * Create the panel.
	 */
	public CreationPanel() {

		setPreferredSize(new Dimension(GameWindow.width, GameWindow.height));
		setBounds(0, 0, GameWindow.width, GameWindow.height);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{52, 247, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{63, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JLabel lblNewJgoodiesTitle = DefaultComponentFactory.getInstance().createTitle("Mirlanda's Lair");
		lblNewJgoodiesTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
		GridBagConstraints gbc_lblNewJgoodiesTitle = new GridBagConstraints();
		gbc_lblNewJgoodiesTitle.gridwidth = 4;
		gbc_lblNewJgoodiesTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewJgoodiesTitle.gridx = 0;
		gbc_lblNewJgoodiesTitle.gridy = 0;
		add(lblNewJgoodiesTitle, gbc_lblNewJgoodiesTitle);

		JSeparator separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_separator_2.gridx = 1;
		gbc_separator_2.gridy = 1;
		add(separator_2, gbc_separator_2);

		JLabel lblNewLabel = new JLabel("Name");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		add(lblNewLabel, gbc_lblNewLabel);

		nameField = new JTextField();
		nameField.setText("Draven");
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.insets = new Insets(0, 0, 5, 5);
		gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameField.gridx = 1;
		gbc_nameField.gridy = 2;
		add(nameField, gbc_nameField);
		nameField.setColumns(10);

		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 1;
		gbc_separator_1.gridy = 3;
		add(separator_1, gbc_separator_1);

		JLabel lblRace = new JLabel("Race");
		GridBagConstraints gbc_lblRace = new GridBagConstraints();
		gbc_lblRace.anchor = GridBagConstraints.EAST;
		gbc_lblRace.insets = new Insets(0, 0, 5, 5);
		gbc_lblRace.gridx = 0;
		gbc_lblRace.gridy = 4;
		add(lblRace, gbc_lblRace);

		final JComboBox raceBox = new JComboBox();
		raceBox.setEditable(true);
		raceBox.setModel(new DefaultComboBoxModel(Race.values()));
		GridBagConstraints gbc_raceBox = new GridBagConstraints();
		gbc_raceBox.insets = new Insets(0, 0, 5, 5);
		gbc_raceBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_raceBox.gridx = 1;
		gbc_raceBox.gridy = 4;
		add(raceBox, gbc_raceBox);

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 3;
		gbc_separator.gridy = 5;
		add(separator, gbc_separator);

		JLabel lblClass = new JLabel("Class");
		GridBagConstraints gbc_lblClass = new GridBagConstraints();
		gbc_lblClass.anchor = GridBagConstraints.EAST;
		gbc_lblClass.insets = new Insets(0, 0, 5, 5);
		gbc_lblClass.gridx = 0;
		gbc_lblClass.gridy = 6;
		add(lblClass, gbc_lblClass);

		final JComboBox classBox = new JComboBox();
		classBox.setEditable(true);
		classBox.setModel(new DefaultComboBoxModel(Profession.default_professions));
		GridBagConstraints gbc_classBox = new GridBagConstraints();
		gbc_classBox.insets = new Insets(0, 0, 5, 5);
		gbc_classBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_classBox.gridx = 1;
		gbc_classBox.gridy = 6;
		add(classBox, gbc_classBox);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.gridheight = 5;
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 2;
		gbc_horizontalStrut.gridy = 2;
		add(horizontalStrut, gbc_horizontalStrut);

		JSeparator separator_3 = new JSeparator();
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.insets = new Insets(0, 0, 5, 5);
		gbc_separator_3.gridx = 1;
		gbc_separator_3.gridy = 7;
		add(separator_3, gbc_separator_3);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// END CREATION EVENT
				CreationPanel.name = nameField.getText();
				race = Race.values()[raceBox.getSelectedIndex()];
				profession = Profession.default_professions[classBox.getSelectedIndex()];
				Main.p = new Party(new Hero(name,race,profession));
				//Main.p.leader.setWeapon(profession.weapon);
				Main.endCreation();
			}
		});
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.insets = new Insets(0, 0, 5, 5);
		gbc_btnStart.gridx = 1;
		gbc_btnStart.gridy = 8;
		add(btnStart, gbc_btnStart);

	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}


}
