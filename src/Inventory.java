
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
		String t = "Inventory:";
		g.drawString(t, 135 - getFontMetrics(getFont()).stringWidth(t)/2, 20);

		// draw items
		drawItems(Main.p.leader.inventory,g,12,20);
		if(showTarget)drawTargetSelect(Main.p.members, null, g);
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

			// select box
			if(selected > items.size() - 1){
				selected = 0;
			} else if (selected < 0){
				selected = items.size() - 1;
			}
			g.drawRect(x-2, y + selected*yChange + yChange -2, GameWindow.width - x - img_size, img_size + 3);

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

		Character[] na = null;
		if(this.showPop) showPop = !showPop;

		if(showTarget){
			switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				targetPrev(Main.p.members, na);
				Main.gw.repaint();
				break;
			case KeyEvent.VK_DOWN:
				targetNext(Main.p.members, na);
				Main.gw.repaint();
				break;
			case KeyEvent.VK_RIGHT:
				this.getTarget(Main.p.members, na).use(getSelected());
				this.showTarget = false;
				Main.gw.repaint();
				break;
			}
			return;
		}

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
				this.showTarget = true;
			} else {
				Main.openScreen(Main.dp);
			}
			Main.gw.repaint();
			break;
		case KeyEvent.VK_1:
			openPopup(getSelected().getDescription());
			Main.gw.repaint();
			break;
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