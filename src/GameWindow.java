import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class GameWindow extends JFrame{

	final static int width = 300;
	final static int height = 300;
	final static ImageIcon icon = new ImageIcon("mirlandaIcon.png");

	public GameWindow(){
		setTitle("Mirlanda's Lair");
		setIconImage(icon.getImage());
		init();
	}

	public void init(){

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		setFocusable(true);

		setContentPane(Main.cp);
		pack();
		setLocationRelativeTo(null);

	}

	public void setKeyListener(KeyListener k){

		for(int i = 0; i < this.getKeyListeners().length; i++){
			this.removeKeyListener(this.getKeyListeners()[i]);	
		}

		this.addKeyListener(k);
	}
}

abstract class GamePanel extends JPanel implements KeyListener{

	public GamePanel(){
		super();
		setPreferredSize(new Dimension(GameWindow.width,GameWindow.height));
		setBounds(0, 0, GameWindow.width, GameWindow.height);
		setLayout(null);
	}

	boolean showPop = false;
	String popText = "";

	public void togglePopup(){
		showPop = !showPop;
	}

	public void openPopup(){
		showPop = true;
	}

	public void closePopup(){
		showPop = false;
	}

	public void setPopText(String text){
		popText = text;
	}

	public void addPopText(String text){
		popText += text;
	}

	public void openPopup(String text){
		popText = text;
		showPop = true;
	}

	public void drawPopup(Graphics g){
		if(showPop){	
			int x = 20;
			int y = 75;
			int size = 150;
			g.setColor(Color.white);
			g.fillRect(x, y, GameWindow.width - x * 2, size);
			g.setColor(Color.black);
			g.drawRect(x, y, GameWindow.width - x * 2, size);

			String line = "";
			String word = "";
			int y_move = y;
			int x_buffer = x + 3;
			for(int i = 0; i < popText.toCharArray().length; i++){
				char c = popText.toCharArray()[i];
				// find words
				if(c != ' ' && i != popText.toCharArray().length - 1){
					word += c;
				} else if(c == ' ') {
					if(getFontMetrics(getFont()).stringWidth(line + word) > (GameWindow.width - x_buffer * 2)){
						y_move += 14;
						g.drawString(line, GameWindow.width/2 - (getFontMetrics(getFont()).stringWidth(line))/2, y_move);
						line = "";
					}

					if(word.compareTo("\t") == 0){
						word += "    ";
					}

					if(word.compareTo("\n") == 0){
						y_move += 14;
						g.drawString(line, GameWindow.width/2 - (getFontMetrics(getFont()).stringWidth(line))/2, y_move);
						line = "";
					} else {
						line += word + " ";
						word = "";
					}
				} else {
					// if end of the line
					if(getFontMetrics(getFont()).stringWidth(line + word) > (GameWindow.width - x_buffer * 2)){
						y_move += 14;
						g.drawString(line, GameWindow.width/2 - (getFontMetrics(getFont()).stringWidth(line))/2, y_move);
						line = "";
					}
					line += word + popText.toCharArray()[popText.length()-1] + " ";
					word = "";
					y_move += 14;
					g.drawString(line, GameWindow.width/2 - (getFontMetrics(getFont()).stringWidth(line))/2, y_move);
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) {
	}
}


