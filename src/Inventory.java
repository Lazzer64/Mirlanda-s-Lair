
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Inventory extends LinkedHashMap<Item, Integer>{


	public String toString(){
		String ret = "";
		for(int i = 0; i < this.size(); i++){
			ret += this.keySet().toArray()[i];
			ret += ": ";
			ret += this.values().toArray()[i];
			if(i != this.size() - 1){
				ret += "\n";
			}
		}
		return ret;
	}

}

class InventoryPanel extends GamePanel{

	int selected = 0;

	public InventoryPanel(){
		super();
		setPreferredSize(new Dimension(GameWindow.width,GameWindow.height));
		setBounds(0, 0, GameWindow.width, GameWindow.height);
		setLayout(null);
	}

	public void paint(Graphics g){

		g.setColor(Color.white);
		g.fillRect(0, 0, GameWindow.height, GameWindow.width);
		g.setColor(Color.black);

		// draw title
		int centered_text_x = (Main.gw.width/2) - (Main.c.title().length() * 12)/4;
		g.drawString(Main.c.title(), centered_text_x, 20);

		drawStats(g, centered_text_x, 34);

		// draw items
		drawItems(Main.c.inventory,g,12,40);
		
		drawPopup(g);

	}

	void drawStats(Graphics g, int x, int y){
		g.drawString("Health: " + Main.c.health + "/" + Main.c.max_health, x, y);
		g.drawString("Mana: " + Main.c.mana + "/" + Main.c.max_mana, x, y + 14);

	}

	void drawItems(Inventory items, Graphics g, int x, int y){
		String itemName = "";
		String itemValue = "";
		int img_size = 10;
		int yChange = 16;


		// popup if there are no items
		if(items.size() <= 0){
			openPopup("Your bag is empty!");
		} else {
			
			closePopup();
			
			// select box
			if(selected > items.size() - 1){
				selected = 0;
			} else if (selected < 0){
				selected = items.size() - 1;
			}
			g.drawRect(x-2, y + selected*yChange + yChange -2, GameWindow.width - x - img_size, yChange - 2);

			for(int i = 0; i < items.size(); i++){
				itemName += items.keySet().toArray()[i];

				itemValue += items.values().toArray()[i] + "x";
				y += yChange;

				//Draw image
				try {
					g.drawImage(ImageIO.read(((Item) (items.keySet().toArray()[i])).getIcon()),x,y, img_size, img_size, null);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// draw item name
				g.setColor(Color.black);
				wrapedText(itemName, img_size + 5 + x, y - 4, GameWindow.width - x, g);
				wrapedText(itemValue, img_size + 5 + x + 220, y - 4, GameWindow.width - x, g);
				itemName = "";
				itemValue = "";


			}
		}
	}

	public Item getSelected(){
		return (Item) Main.c.inventory.keySet().toArray()[selected];
	}

	public void next(){
		this.selected++;
	}

	public void previous(){
		this.selected--;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			previous();
			Main.gw.repaint();
			break;
		case KeyEvent.VK_DOWN:
			next();
			Main.gw.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			if(Main.c.inventory.size() > 0){
				Main.c.use(getSelected());
			} else {
				Main.openScreen(Main.dp);
			}
			Main.gw.repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_I:
			Main.openScreen(Main.dp);
			break;
		}
	}

}

class CombatInventoryPanel extends InventoryPanel {

	JPanel p;

	public CombatInventoryPanel(){
		super();

		this.p = (JPanel) Main.gw.getContentPane();
		// TODO fix no more items
	}

	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			previous();
			Main.gw.repaint();
			break;
		case KeyEvent.VK_DOWN:
			next();
			Main.gw.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			Main.c.use(getSelected());
			Main.openScreen(p);
			Main.gw.repaint();
		}
	}

}