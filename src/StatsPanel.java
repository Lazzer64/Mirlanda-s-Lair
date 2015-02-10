import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class StatsPanel extends GamePanel {

	final static Color divColor = new Color(180,180,180);

	int currentChar = 0;
	
	public void paint(Graphics g){

		Hero character = Main.p.get(currentChar);
		
		int lineSize = 15;
		int xBuffer = 20;
		int xImageBuffer = 5;

		g.setFont(new Font(null, Font.PLAIN, 12));

		g.setColor(Color.black);
		g.drawString(character.title() + ":", xImageBuffer, lineSize + 3);

		text = " \n "
				+ " \n "
				+ " *cHEALTH Hit Points: " + character.health + " / " + Main.c.max_health + " \n "
				+ " *cMANA Mana: " + character.mana + " / " + Main.c.max_mana + " \n "
				+ " \n "
				+ " *cSTR Strength: " + character.strength + " \n "
				+ " *cDEX Dexterity: " + character.dexterity + " \n "
				+ " *cINT Intelligence: " + character.intelligence + " \n "
				+ " \n "
				+ " *c Head: " + character.head + " \n "
				+ "Torso: " + character.torso + " \n "
				+ "Jewelry: " + character.jewelry + " \n "
				+ "Legs: " + character.legs + " \n "
				+ "Weapon: " + character.weapon + " \n "
				;


		wrapedText(text, xBuffer, 0, lineSize, 300, g);

		g.setColor(divColor);
		g.drawLine(0, (int)(lineSize * 1.7), getWidth() - xBuffer, (int)(lineSize * 1.7));
		g.drawLine(0, (int)(lineSize * 4.7), getWidth() - xBuffer, (int)(lineSize * 4.7));
		g.drawLine(0, (int)(lineSize * 8.7), getWidth() - xBuffer, (int)(lineSize * 8.7));

		try {
			g.drawImage(ImageIO.read(new File("img/heart_icon.png")), xImageBuffer, lineSize * 3 - 10, 10, 10, null);
			g.drawImage(ImageIO.read(new File("img/mana_icon.png")), xImageBuffer, lineSize * 4 - 10, 10, 10, null);
			g.drawImage(ImageIO.read(new File("img/strength_icon.png")), xImageBuffer, lineSize * 6 - 10, 10, 10, null);
			g.drawImage(ImageIO.read(new File("img/dexterity_icon.png")), xImageBuffer, lineSize * 7 - 10, 10, 10, null);
			g.drawImage(ImageIO.read(new File("img/inteligence_icon.png")), xImageBuffer, lineSize * 8 - 10, 10, 10, null);
			g.drawImage(ImageIO.read(Head.icon), xImageBuffer, lineSize * 10 - 10, 10, 10, null);
			g.drawImage(ImageIO.read(Torso.icon), xImageBuffer, lineSize * 11 - 10, 10, 10, null);
			g.drawImage(ImageIO.read(Jewelry.icon), xImageBuffer, lineSize * 12 - 10, 10, 10, null);
			g.drawImage(ImageIO.read(Legs.icon), xImageBuffer, lineSize * 13 - 10, 10, 10, null);
			g.drawImage(ImageIO.read(Equipment.icon), xImageBuffer, lineSize * 14 - 10, 10, 10, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	void next(){
		currentChar++;
		if(currentChar >= Main.p.size){
			currentChar = 0;
		}
		Main.gw.repaint();
	}

	void previous(){
		currentChar--;
		if(currentChar < 0){
			currentChar = Main.p.size - 1;
		}
		Main.gw.repaint();
	}

	
	public void keyPressed(KeyEvent arg0) {

	}

	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case KeyEvent.VK_C:
			Main.openScreen(Main.dp);
			break;
		case KeyEvent.VK_RIGHT:
			next();
			break;
		case KeyEvent.VK_LEFT:
			previous();
			break;
		}
	}

}
