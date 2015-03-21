import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Graphics;
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
import java.io.File;
import java.io.IOException;


public class CreationPanel extends GamePanel implements KeyListener{

	JLabel title = new JLabel("Mirlanda's Lair");
	JTextField nameField = new JTextField("Draven");
	JComboBox<Race> raceSelect = new JComboBox<Race>(Race.values());
	JComboBox<Profession> professionSelect = new JComboBox<Profession>(Profession.default_professions);
	JButton startButton = new JButton("Start");
	
	JLabel nameLabel = new JLabel("Name:");
	JLabel raceLabel = new JLabel("Race:");
	JLabel professionLabel = new JLabel("Profession:");
	
	ActionListener end = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Main.endCreation();
		}
		
	};
	
	public CreationPanel() {

		super();
		
		JPanel splash = new GamePanel(){
			public void paint(Graphics g){
				drawSplash(g);
				drawFog(g);
			}

			public void keyPressed(KeyEvent arg0) { }
			public void keyReleased(KeyEvent arg0) { }
		};
		splash.setBounds(0, 0, GameWindow.width, GameWindow.width);

		int 
		leftEdge = 50,
		width = GameWindow.width - leftEdge*2,
		height = 25,
		buffer = 25,
		titleBuffer = 30,
		labelHeight = 15,
		titleY = 20,
		titleHeight = height + 5,
		nameFieldY = titleY + titleHeight + titleBuffer,
		nameLabelY = nameFieldY - labelHeight,
		nameFieldHeight = height,
		raceSelectY = nameFieldY + nameFieldHeight + buffer,
		raceSelectHeight = height,
		raceLabelY = raceSelectY - labelHeight,
		professionSelectY = raceSelectY + raceSelectHeight + buffer,
		professionSelectHeight = height,
		professionLabelY = professionSelectY - labelHeight,
		buttonY = professionSelectY + professionSelectHeight + buffer,
		buttonHeight = height
		;
		
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font("Tahoma", Font.BOLD, 36));
		title.setForeground(Color.WHITE);
		title.setBounds(0,titleY,GameWindow.width,titleHeight);
		
		nameLabel.setBounds(leftEdge, nameLabelY, width, labelHeight);
		nameLabel.setForeground(Color.WHITE);
		
		nameField.setBounds(leftEdge,nameFieldY,width,nameFieldHeight);
		nameField.addActionListener(end);
		
		raceLabel.setBounds(leftEdge, raceLabelY, width, labelHeight);
		raceLabel.setForeground(Color.WHITE);
		
		raceSelect.setBounds(leftEdge, raceSelectY, width, raceSelectHeight);
		
		professionLabel.setBounds(leftEdge, professionLabelY, width, labelHeight);
		professionLabel.setForeground(Color.WHITE);
		
		professionSelect.setBounds(leftEdge, professionSelectY, width, professionSelectHeight);
		
		startButton.setBounds(leftEdge, buttonY, width, buttonHeight);
		startButton.addActionListener(end);
		
		add(title);
		add(nameLabel);
		add(nameField);
		add(raceLabel);
		add(raceSelect);
		add(professionLabel);
		add(professionSelect);
		add(startButton);
		add(splash);
		
	}

	int fogSize = 400;
	void drawFog(Graphics g){
		int xCenter = (GameWindow.width - fogSize)/2;
		int yCenter = (GameWindow.height - fogSize)/2;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, xCenter, GameWindow.height); // left black out
		g.fillRect(0, 0, GameWindow.width, yCenter); // top black out
		g.fillRect(xCenter + fogSize, 0, GameWindow.height, GameWindow.width); // right black out
		g.fillRect(0, yCenter + fogSize, GameWindow.width, GameWindow.height); // bottom black out

		g.drawImage(Images.fog, xCenter, yCenter, fogSize, fogSize, null);

	}


	@Override
	public void keyPressed(KeyEvent e) {

	}


	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == Hotkeys.ENTER){
			Main.endCreation();
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {

	}


}