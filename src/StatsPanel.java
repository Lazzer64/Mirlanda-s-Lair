import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
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
		int imageSize = 10;

		g.setFont(new Font(null, Font.PLAIN, 12));

		g.setColor(Color.black);
		g.drawString(character.title() + ":", xImageBuffer + imageSize * (Main.p.size) + 5, lineSize + 3);

		text = " \n "
				+ " \n "
				+ " *cHEALTH Hit Points: " + character.getHpText() + " \n "
				+ " *cMANA Mana: " + character.mana + "/" + character.max_mana + " \n "
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
			drawPartyIcons(xImageBuffer, lineSize, g);
			g.drawImage(ImageIO.read(new File("img/heart_icon.png")), xImageBuffer, lineSize * 3 - imageSize, imageSize, imageSize, null);
			g.drawImage(ImageIO.read(new File("img/mana_icon.png")), xImageBuffer, lineSize * 4 - imageSize, imageSize, imageSize, null);
			g.drawImage(ImageIO.read(new File("img/strength_icon.png")), xImageBuffer, lineSize * 6 - imageSize, imageSize, imageSize, null);
			g.drawImage(ImageIO.read(new File("img/dexterity_icon.png")), xImageBuffer, lineSize * 7 - imageSize, imageSize, imageSize, null);
			g.drawImage(ImageIO.read(new File("img/inteligence_icon.png")), xImageBuffer, lineSize * 8 - imageSize, imageSize, imageSize, null);
			g.drawImage(ImageIO.read(Head.icon), xImageBuffer, lineSize * 10 - imageSize, imageSize, imageSize, null);
			g.drawImage(ImageIO.read(Torso.icon), xImageBuffer, lineSize * 11 - imageSize, imageSize, imageSize, null);
			g.drawImage(ImageIO.read(Jewelry.icon), xImageBuffer, lineSize * 12 - imageSize, imageSize, imageSize, null);
			g.drawImage(ImageIO.read(Legs.icon), xImageBuffer, lineSize * 13 - imageSize, imageSize, imageSize, null);
			g.drawImage(ImageIO.read(Equipment.icon), xImageBuffer, lineSize * 14 - imageSize, imageSize, imageSize, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static Image getCharacterIcon(Character c) throws IOException {
		switch(c.getPrimaryStat()){
		case 1: // Strength
			return ImageIO.read(new File("img/str_character_icon.png"));
		case 2: // Dexterity
			return ImageIO.read(new File("img/dex_character_icon.png"));
		case 3: // Intelligence
			return ImageIO.read(new File("img/int_character_icon.png"));
		default: // Balanced
			return ImageIO.read(new File("img/bal_character_icon.png"));	
		}
	}
	
	void drawPartyIcons(int xImageBuffer, int lineSize, Graphics g) throws IOException {
		g.drawImage(getCharacterIcon(Main.p.get(0)), xImageBuffer, lineSize - 7, 10, 10, null);
		for(int i = 0; i < Main.p.size; i++){
			g.drawImage(getCharacterIcon(Main.p.get(i)), xImageBuffer + 10*i, lineSize - 7, 10, 10, null);
			if(Main.p.get(i).dead) g.drawImage(ImageIO.read(new File("img/cross.png")), xImageBuffer + 10*i, lineSize - 7, 10, 10, null);
		}

		g.setColor(Color.WHITE);
		for(int i = 0; i < Main.p.size; i++){
			g.drawRect(xImageBuffer * 2 * (i + 1) - 5, lineSize - 7, 9, 9);
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(xImageBuffer * 2 * (currentChar + 1) - 5, lineSize - 7, 9, 9);
		
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
