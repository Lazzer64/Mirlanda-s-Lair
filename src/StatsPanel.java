import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class StatsPanel extends GamePanel {

	final static Color divColor = new Color(180,180,180);

	int currentChar = 0;
	Selector<String> charOptions = new Selector<String>(new String[]{"OPTION *cSTR A *c ","OPTION *cDEX B *c ","OPTION *cINT C *c "});

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
		g.drawLine(0, (int)(lineSize * 14.7), getWidth() - xBuffer, (int)(lineSize * 14.7));


		drawPartyIcons(xImageBuffer, lineSize, g);
		g.drawImage(Images.heart_icon, xImageBuffer, lineSize * 3 - imageSize, imageSize, imageSize, null);
		g.drawImage(Images.mana_icon, xImageBuffer, lineSize * 4 - imageSize, imageSize, imageSize, null);
		g.drawImage(Images.str_icon, xImageBuffer, lineSize * 6 - imageSize, imageSize, imageSize, null);
		g.drawImage(Images.dex_icon, xImageBuffer, lineSize * 7 - imageSize, imageSize, imageSize, null);
		g.drawImage(Images.int_icon, xImageBuffer, lineSize * 8 - imageSize, imageSize, imageSize, null);
		g.drawImage(Images.head_icon, xImageBuffer, lineSize * 10 - imageSize, imageSize, imageSize, null);
		g.drawImage(Images.torso_icon, xImageBuffer, lineSize * 11 - imageSize, imageSize, imageSize, null);
		g.drawImage(Images.ring_icon, xImageBuffer, lineSize * 12 - imageSize, imageSize, imageSize, null);
		g.drawImage(Images.legs_icon, xImageBuffer, lineSize * 13 - imageSize, imageSize, imageSize, null);
		g.drawImage(Images.weapon_icon, xImageBuffer, lineSize * 14 - imageSize, imageSize, imageSize, null);

		charOptions.hideBack();
		charOptions.draw(xImageBuffer, lineSize * 15, GameWindow.width - xImageBuffer*2, this, g);

	}

	static BufferedImage getCharacterIcon(Character c) {
		switch(c.getPrimaryStat()){
		case 1: // Strength
			return Images.str_char;
		case 2: // Dexterity
			return Images.dex_char;
		case 3: // Intelligence
			return Images.int_char;
		default: // Balanced
			return Images.bal_char;
		}
	}

	void drawPartyIcons(int xImageBuffer, int lineSize, Graphics g) {
		g.drawImage(getCharacterIcon(Main.p.get(0)), xImageBuffer, lineSize - 7, 10, 10, null);
		for(int i = 0; i < Main.p.size; i++){
			g.drawImage(getCharacterIcon(Main.p.get(i)), xImageBuffer + 10*i, lineSize - 7, 10, 10, null);
			if(Main.p.get(i).dead) g.drawImage(Images.cross, xImageBuffer + 10*i, lineSize - 7, 10, 10, null);
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
		
	}

	void previous(){
		currentChar--;
		if(currentChar < 0){
			currentChar = Main.p.size - 1;
		}
		
	}


	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case Hotkeys.UP:
			charOptions.previous();
			break;
		case Hotkeys.DOWN:
			charOptions.next();
			break;
		}
		
	}

	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case Hotkeys.STATS:
			Main.openScreen(Main.dp);
			break;
		case Hotkeys.RIGHT:
			next();
			break;
		case Hotkeys.LEFT:
			previous();
			break;
		}
	}

}
