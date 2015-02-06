import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class StatsPanel extends GamePanel {

	final static Color healthColor = new Color(204,0,79);
	final static Color manaColor = new Color(80,150,255);
	final static Color strColor = new Color(166,0,65);
	final static Color dexColor = new Color(65,166,0);
	final static Color intColor = new Color(36,117,243);
	final static Color divColor = new Color(180,180,180);
	
	public void paint(Graphics g){
		int lineSize = 15;
		int xBuffer = 20;
		int xImageBuffer = 5;
		
		g.setFont(new Font(null, Font.PLAIN, 12));
		g.setColor(Color.black);
		g.drawString(Main.c.title() + ":", xImageBuffer, lineSize + 3);
		g.setColor(divColor);
		g.drawLine(0, (int)(lineSize * 1.7), getWidth() - xBuffer, (int)(lineSize * 1.7));
		g.setColor(healthColor);
		g.drawString("Hit Points: " + Main.c.health + " / " + Main.c.max_health, xBuffer, lineSize * 3);
		g.setColor(manaColor);
		g.drawString("Mana: " + Main.c.mana + " / " + Main.c.max_mana, xBuffer, lineSize * 4);
		g.setColor(divColor);
		g.drawLine(0, (int)(lineSize * 4.7), getWidth() - xBuffer, (int)(lineSize * 4.7));
		g.setColor(strColor);
		g.drawString("Strength: " + Main.c.strength, xBuffer, lineSize * 6);
		g.setColor(dexColor);
		g.drawString("Dexterity: " + Main.c.dexterity, xBuffer, lineSize * 7);
		g.setColor(intColor);
		g.drawString("Intelligence: " + Main.c.intelligence, xBuffer, lineSize * 8);
		g.setColor(divColor);
		g.drawLine(0, (int)(lineSize * 8.7), getWidth() - xBuffer, (int)(lineSize * 8.7));
		g.setColor(Color.black);
		g.drawString("Head: " + Main.c.head, xBuffer, lineSize * 10);
		g.drawString("Torso: " + Main.c.torso, xBuffer, lineSize * 11);
		g.drawString("Jewelry: " + Main.c.jewelry, xBuffer, lineSize * 12);
		g.drawString("Legs: " + Main.c.legs, xBuffer, lineSize * 13);
		g.drawString("Weapon: " + Main.c.weapon, xBuffer, lineSize * 14);
		
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
	
	public void keyPressed(KeyEvent arg0) {

	}

	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case KeyEvent.VK_C:
			Main.openScreen(Main.dp);
			break;
		}
	}

}
