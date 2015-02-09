import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class StatsPanel extends GamePanel {

	final static Color divColor = new Color(180,180,180);

	public void paint(Graphics g){
		int lineSize = 15;
		int xBuffer = 20;
		int xImageBuffer = 5;

		g.setFont(new Font(null, Font.PLAIN, 12));

		g.setColor(Color.black);
		g.drawString(Main.c.title() + ":", xImageBuffer, lineSize + 3);

		text = " \n "
				+ " \n "
				+ " *cHEALTH Hit Points: " + Main.c.health + " / " + Main.c.max_health + " \n "
				+ " *cMANA Mana: " + Main.c.mana + " / " + Main.c.max_mana + " \n "
				+ " \n "
				+ " *cSTR Strength: " + Main.c.strength + " \n "
				+ " *cDEX Dexterity: " + Main.c.dexterity + " \n "
				+ " *cINT Intelligence: " + Main.c.intelligence + " \n "
				+ " \n "
				+ " *c Head: " + Main.c.head + " \n "
				+ "Torso: " + Main.c.torso + " \n "
				+ "Jewelry: " + Main.c.jewelry + " \n "
				+ "Legs: " + Main.c.legs + " \n "
				+ "Weapon: " + Main.c.weapon + " \n "
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
