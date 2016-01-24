import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CutScene extends GamePanel {

	public static void main(String args[]){
		
		String text = 
				(" *cRED COMBINE *cWHITE flour, *i baking soda * *cWHITE and *b salt * in small bowl. "
				+ "Beat butter, *cBLUE granulated sugar, *cWHITE brown sugar and vanilla extract "
				+ "in large mixer bowl until *cHEALTH creamy. *cWHITE Add *cYELLOW *b eggs, * *cWHITE one at a time, beating well "
				+ "after each addition. Gradually beat in flour *cGREEN mixture. *cWHITE Stir in morsels "
				+ "and nuts. Drop by rounded tablespoon onto ungreased *cMANA baking sheets. ");
		
		CutScene b = new CutScene(":^)",null);
		CutScene cutscene = new CutScene(text,b);
		
		Main.gw.pushPanel(cutscene);
			
		while(true){
			Main.gw.repaint();
			try { Thread.sleep(20); } catch (InterruptedException e) {}
		}
	}
	
	String textFlags = "*cWHITE *l ";
	int scrollSpeed = 1;
	int position = GameWindow.height;
	int buffer = 20;
	GamePanel nextPanel;
	
	CutScene(String text, GamePanel next){
		super();
		this.text = text;
		this.nextPanel = next;
	}
	
	public void paint(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, GameWindow.width, GameWindow.height);
		g.setColor(Color.white);
		position -= scrollSpeed;
		wrapedText(textFlags + text, buffer + 3, position, GameWindow.width - buffer*2, g);
		if(position < 0) {
			g.setColor(Color.GRAY);
			g.drawString("Press any key to continue", GameWindow.width/4, GameWindow.height - buffer);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(nextPanel != null){
			Main.gw.replacePanel(nextPanel);
		}
	}
}
