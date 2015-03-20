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
	final static Color Back_Color = new Color(238,238,238);

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

		setBackground(Back_Color);
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

	boolean showPop = false;
	boolean showTarget = false;
	String popText = "";
	String text = "";
	Color textColor = Color.BLACK;

	public GamePanel(){
		super();
		setPreferredSize(new Dimension(GameWindow.width,GameWindow.height));
		setBounds(0, 0, GameWindow.width, GameWindow.height);
		setLayout(null);
	}
	
	public void popCloseOnKey(int keyPressed, int keyNeeded){
		if(keyPressed == keyNeeded){
			closePopup();
		}
	}

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
			centeredText(y,size,popText,g);
		}
	}

	int currentTarget = 0;

	public void drawTargetSelect(Hero[] allies, Character[] enemies, Graphics g){
		int x = 20;
		int y = 75;
		int size = 150;
		int width = GameWindow.width - x * 2;
		int lineSize = 14;
		String s = "";

		g.setColor(Color.white);
		g.fillRect(x, y, width, size);
		g.setColor(Color.black);
		g.drawRect(x, y, width, size);


		if(enemies != null){
			s += "*cHEALTH *b Enemies: * *c \n ";
			for(int i = 0; i < enemies.length; i++){
				s += targetText(enemies[i]);
			}
		}

		s += "*cDEX *b Allies: * *c \n ";
		for(int i = 0; i < allies.length; i++){
			s += targetText(allies[i]);
		}

		wrapedText(s,x + 7,y, lineSize, width,g);
		g.setColor(Color.BLACK);
		g.drawRect(x + 5, y + 2 + lineSize + lineSize*currentTarget, width - 10, lineSize);
	}

	String targetText(Character c){
		return c.name + " *cHEALTH " + c.getHpText() + " *cMANA " + c.getMpText() + " *c \n ";
	}

	public void targetNext(Hero[] allies, Character[] enemies){
		currentTarget++;
		if(enemies != null){
			if(currentTarget == enemies.length) currentTarget ++;
			if(currentTarget > enemies.length + allies.length) currentTarget = 0;
		} else {
			if(currentTarget == allies.length) currentTarget = 0;
		}

	}

	public void targetPrev(Hero[] allies, Character[] enemies){
		currentTarget--;
		if(enemies != null){
			if(currentTarget == enemies.length) currentTarget --;
			if(currentTarget < 0) currentTarget = allies.length + enemies.length;
		} else {
			if(currentTarget < 0) currentTarget = allies.length-1;
		}
	}

	public Character getTarget(Hero[] allies, Character[] enemies){
		Character target;
		if(enemies != null){
			if(currentTarget < enemies.length) target = enemies[currentTarget];
			else target = allies[currentTarget - enemies.length - 1];
			return target;
		} else {
			target = allies[currentTarget];
			return target;
		}
	}


	public void setText(String text){
		this.text = text;
	}

	public void setText(String text, Color textColor){
		this.text = text;
		this.textColor = textColor;
	}

	public void addText(String text){
		setText(this.text + text);
		repaint();
	}

	void wrapedText(String text, int x, int y, int width, Graphics g){
		wrapedText(text,x,y,14,width,g);
	}

	void wrapedText(String text, int x, int y, int spacing, int width, Graphics g){
		int y_move = y + spacing;
		int x_buffer = x;
		int curr_x = x_buffer;
		int end = text.indexOf(' ');
		Font normalFont = getFont();
		g.setColor(Color.BLACK);
		while(end!=-1){ // While there are spaces in the text
			String word = text.substring(0, end); // Isolate the word
			if(word.compareTo("\t") == 0){ // Check if the word is a tab
				word += "    ";
			} else if(word.compareTo("\n") == 0){ // Check if the word is a new line
				y_move += spacing;
				curr_x = x_buffer;
				word = "";
			} else if(word.startsWith("*c")){ // Check if the word is a color change
				g.setColor(parseColor(word.substring(2)));
				word = "";
			} else if(word.compareTo("*b") == 0){ // Check if the word is a bold
				g.setFont(new Font(null, Font.BOLD, g.getFont().getSize()));
				word = "";
			} else if(word.compareTo("*i") == 0){ // Check if the word is a bold
				g.setFont(new Font(null, Font.ITALIC, g.getFont().getSize()));
				word = "";
			} else if(word.compareTo("*") == 0){ // Check for plain text
				g.setFont(normalFont);
				word = "";
			}
			if(word != "") word += " ";
			int wordWidth = getFontMetrics(getFont()).stringWidth(word);
			if(curr_x + wordWidth > x + width){y_move += spacing;curr_x = x_buffer;} // If the word needs to be wrapped
			g.drawString(word, curr_x, y_move); // Draw the String at curr_x and y_move
			text = text.substring(end + 1); // Remove the word from the text String
			curr_x += wordWidth;
			end = text.indexOf(' '); // Find the next space
		}
		int wordWidth = getFontMetrics(getFont()).stringWidth(text);
		if(curr_x + wordWidth > x + width){y_move += spacing;curr_x = x_buffer;} // Check wrap
		g.drawString(text, curr_x, y_move); // Draw the remaining string
		g.setFont(normalFont);
	}

	Color parseColor(String color){
		Color healthColor = new Color(204,0,79);
		Color manaColor = new Color(80,150,255);
		Color strColor = new Color(166,0,65);
		Color dexColor = new Color(65,166,0);
		Color intColor = new Color(36,117,243);

		switch(color){
		case "RED":
			return Color.RED;
		case "BLUE":
			return Color.BLUE;
		case "GREEN":
			return Color.GREEN;
		case "YELLOW":
			return Color.YELLOW;
		case "HEALTH":
			return healthColor;
		case "MANA":
			return manaColor;
		case "STR":
			return strColor;
		case "DEX":
			return dexColor;
		case "INT":
			return intColor;
		default:
			return Color.BLACK;
		}
	}

	void centeredText(int y, int width, String text, Graphics g){

		String line = "";
		String word = "";

		int y_move = y;
		for(int i = 0; i < text.toCharArray().length; i++){
			char c = text.toCharArray()[i];
			// find words
			if(c != ' ' && i != text.toCharArray().length - 1){
				word += c;
			} else if(c == ' ') {
				if(getFontMetrics(getFont()).stringWidth(line + word) > (width)){
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
				if(getFontMetrics(getFont()).stringWidth(line + word) > (width)){
					y_move += 14;
					g.drawString(line, GameWindow.width/2 - (getFontMetrics(getFont()).stringWidth(line))/2, y_move);
					line = "";
				}
				line += word + text.toCharArray()[text.length()-1] + " ";
				word = "";
				y_move += 14;
				g.drawString(line, GameWindow.width/2 - (getFontMetrics(getFont()).stringWidth(line))/2, y_move);
			}
		}
	}

	public void keyTyped(KeyEvent e) {
	}
}

class Selector<Type> {
	
	Type[] values;
	String[] text;
	int selection;
	
	Color
	backColor = Color.white,
	borderColor = Color.black,
	selectColor = Color.black,
	highlightColor = new Color(0, 0, 0, 35);
	
	public Selector(Type[] values){
		this.values = values;
		this.selection = 0;
		text = new String[values.length];
		for(int i = 0; i < text.length; i++){
			text[i] = values[i].toString();
		}
	}
	
	public Selector(Type[] values, String[] text){
		this.values = values;
		this.selection = 0;
		this.text = text;
	}
	
	public void next(){
		selection++;
		if(selection > values.length - 1) selection = 0;
	}

	public void previous(){
		selection--;
		if(selection < 0) selection = values.length - 1;
	}
	
	public int getIndex(){
		return selection;
	}
	
	public Type getSelected(){
		return values[selection];
	}

	public void set(int index, Type obj){
		values[index] = obj;
		text[index] = obj.toString();
	}
	
	public void set(int index, Type obj, String txt){
		values[index] = obj;
		text[index] = txt;
	}
	
	public void add(Type obj){
		add(obj,obj.toString());
	}
	
	public void add(Type obj, String txt){
		// Copy and add values
		Type[] newValues = (Type[])new Object[values.length + 1];
		for(int i = 0; i < values.length; i++){
			newValues[i] = values[i];
		}
		newValues[values.length] = obj;
		values = newValues;
		
		// Copy and add texts
		String[] newText = new String[text.length + 1];
		for(int i = 0; i < text.length; i++){
			newText[i] = text[i];
		}
		newText[text.length] = txt;
		text = newText;
	}
	
	public void hideBack(){
		Color clear = new Color(0, 0, 0, 0);
		backColor = clear;
		borderColor = clear;
		//highlightColor = clear;
	}
	
	public void draw(int x, int y, int width, GamePanel pane, Graphics g){
		final int lineHeight = 15;
		final int textAdjust = -3;
		final int xBuffer = 2;
		String t = "";
		for(String s: text){
			t += " " + s + " \n ";
		}
		g.setColor(backColor);
		g.fillRect(x, y, width, lineHeight * text.length); // White box behind
		g.setColor(Color.BLACK);
		pane.wrapedText(t, x + xBuffer, y + textAdjust, lineHeight, width, g); // Text
		g.setColor(highlightColor);
		g.fillRect(x, y + lineHeight * selection, width, lineHeight); // Grey highlight
		g.setColor(borderColor);
		g.drawRect(x, y, width, lineHeight * text.length); // Border line
		g.setColor(selectColor);
		g.drawRect(x, y + lineHeight * selection, width, lineHeight); // Selection border
	}

}


