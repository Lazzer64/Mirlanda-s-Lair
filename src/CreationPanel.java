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

	static JLabel title = new JLabel("Mirlanda's Lair");
	static JTextField nameField = new JTextField("Draven");
	static JComboBox<Race> raceSelect = new JComboBox<Race>(Race.values());
	static JComboBox<Profession> professionSelect = new JComboBox<Profession>(Profession.default_professions);
	static JButton startButton = new JButton("Start");
	
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

			public void keyPressed(KeyEvent arg0) { }
			public void keyReleased(KeyEvent arg0) { }
		};
		splash.setBounds(0, 0, 300, 300);

		int 
		leftEdge = 50,
		width = GameWindow.width - leftEdge*2,
		height = 25,
		buffer = 25,
		titleY = 20,
		titleHeight = height - 5,
		nameFieldY = titleY + titleHeight + buffer,
		nameFieldHeight = height,
		raceSelectY = nameFieldY + nameFieldHeight + buffer,
		raceSelectHeight = height,
		professionSelectY = raceSelectY + raceSelectHeight + buffer,
		professionSelectHeight = height,
		buttonY = professionSelectY + professionSelectHeight + buffer,
		buttonHeight = height
		;
		
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font("Tahoma", Font.BOLD, 24));
		title.setForeground(Color.white);
		title.setBounds(leftEdge,titleY,width,titleHeight);
		
		nameField.setBounds(leftEdge,nameFieldY,width,nameFieldHeight);
		nameField.addActionListener(end);
		
		raceSelect.setBounds(leftEdge, raceSelectY, width, raceSelectHeight);
		
		professionSelect.setBounds(leftEdge, professionSelectY, width, professionSelectHeight);
		
		startButton.setBounds(leftEdge, buttonY, width, buttonHeight);
		startButton.addActionListener(end);
		
		add(title);
		add(nameField);
		add(raceSelect);
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