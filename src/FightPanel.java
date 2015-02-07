import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

class FightPanel extends GamePanel{

	Dungeon dungeon;
	Hero c1;
	Monster c2;
	Character target = c2;
	int selected = 0;
	boolean target_select = false;

	public FightPanel(Hero c1, Monster c2){
		super();
		this.c1 = c1;
		this.c2 = c2;
		setPreferredSize(new Dimension(GameWindow.width,GameWindow.height));
		setBounds(0, 0, GameWindow.width, GameWindow.height);
		setLayout(null);
		setText("You encounter a *cRED *b hostile! * ");
	}

	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		drawAllyStats(c1,5,15,g);
		drawEnemyStats(c2,5,15,g);
		g.drawString("vs.", (GameWindow.width - 24)/2, 27);
		drawCombatText(5,50,75,g);
		drawCombatActions(Main.c.getCombatActions(),15,125,g);
		g.setColor(Color.BLACK);
		drawPopup(g);
		textColor = Color.black;
	}

	private void drawAllyStats(Character c, int x, int y, Graphics g){
		g.drawString(Main.c.name, x, y);
		g.drawString("Health: " + c.health + "/" + c.max_health, x, y + 12);
		g.drawString("Mana: " + c.mana + "/" + c.max_mana, x, y + (12 * 2));
	}

	private void drawEnemyStats(Character c, int x, int y, Graphics g){

		String t = "";
		int x_space = 0;		

		t = c.name;
		x_space = this.getFontMetrics(this.getFont()).stringWidth(t);
		g.drawString(t, GameWindow.width - x_space - x, y);

		t = "Health: " + c.health + "/" + c.max_health;
		x_space = this.getFontMetrics(this.getFont()).stringWidth(t);
		g.drawString(t, GameWindow.width - x_space - x, y + 12);

		t = "Mana: " + c.mana + "/" + c.max_mana;
		x_space = this.getFontMetrics(this.getFont()).stringWidth(t);
		g.drawString(t, GameWindow.width - x_space - x, y + (12 * 2));
	}

	void drawCombatText(int x, int y, int size, Graphics g){
		g.setColor(Color.white);
		g.fillRect(x, y, GameWindow.width - x * 2, size);
		g.setColor(Color.black);
		g.drawRect(x, y, GameWindow.width - x * 2, size);
		g.setColor(textColor);
		wrapedText(x+3, y, GameWindow.width - x - 3, text, g);
	}

	void drawCombatActions(CombatAction[] a, int x, int y, Graphics g){

		g.setColor(Color.black);

		String abilityName = "";
		int yChange = 16;

		g.drawLine(x, y + yChange/2, GameWindow.width - x, y + yChange/2);

		// select box
		if(selected > a.length - 1){
			selected = 0;
		} else if (selected < 0){
			selected = a.length - 1;
		}
		g.drawRect(x-2, y + selected*yChange + yChange -2, GameWindow.width - x - 12, yChange - 2);

		for(int i = 0; i < a.length; i++){
			abilityName += a[i];
			if(a[i].getCost() > 0){
				abilityName += " - " + a[i].getCost();
			}
			y += yChange;

			// draw ability name
			g.setColor(Color.black);
			g.drawString(abilityName, x, y + yChange - 6);
			abilityName = "";


		}
	}

	public void turn(){

		CombatAction c1Act = getSelected();
		CombatAction c2Act = c2.action();

		if(c1.mana >= c1Act.getCost()){
			executeActions(c1Act,c2Act);
		} else {
			setText("You do not have enough *cBLUE mana *c to use that!");
		}

	}

	void checkHealths(){
		if(c1.health <= 0){
			Main.restartGame();
		} else if(c2.health <= 0){
			giveRewards();
		}
	}

	void executeActions(CombatAction c1Act, CombatAction c2Act){
		c1.useCombatAction(c1Act, c2);
		setText("You use " + c1Act.getFlavorText());

		if(c1.jewelry.effect.use(c1,c2)){
			addText(" \n Your jewelry " + c1.jewelry.effect.getFlavor());
		}

		c2.useCombatAction(c2Act, c1);
		addText(" \n The enemy retaliated with " + c2Act.getFlavorText());

		//tickStatus();
		
		if(c1.health <= 0){
			setText("You have been *b *cRED Slain");
		} else if(c2.health <= 0){
			setText("You have *b *cRED defeated * *c " + c2.name + ".");
		}
		
	}

	void tickStatus(){
		if(c1.statusTurns > 0){
			c1.status.effect.use(c2, c1);
			addText(c1.status.effect.getFlavor());
			c1.tickStatus();
		}

		if(c2.statusTurns > 0){
			c2.status.effect.use(c1, c2);
			addText(c2.status.effect.getFlavor());
			c2.tickStatus();
		}

	}

	void giveRewards(){
		Main.openScreen(Main.dp);
		int initLevel = c1.level;
		// Rewards for killing enemy
		Item[] loot = c2.getLoot();
		int exp_reward = c2.level;
		Main.c.grantExp(exp_reward);
		Main.c.give(loot);
		//
		GamePanel panel = ((GamePanel)Main.gw.getContentPane());
		panel.openPopup(
				" \n You have defeated " + c2.name + "!"
						+" \n  \n -  -  -  Reward  -  -  -"
						+" \n " + exp_reward + " exp."
						+" \n " + Main.itemsToText(loot));
		if(c1.level > initLevel){
			((GamePanel)Main.gw.getContentPane()).openPopup(((GamePanel)Main.gw.getContentPane()).popText 
					+ " \n  \n LEVEL UP!"
					+ " \n You are now level " + c1.level);
		}
	}

	public CombatAction getSelected(){
		return Main.c.getCombatActions()[selected];
	}

	public void next(){
		this.selected++;
	}

	public void previous(){
		this.selected--;
	}

	public void openTargetSelect(){
		this.setPopText(" \n Choose a target: \n \n \n \n \n \n 1. " + c1.name + " \t \t \t \t 2. " + c2.name);
		this.target_select = true;
		this.openPopup();
		Main.gw.repaint();
	}

	public void closeTargetSelect(){
		this.setPopText("");
		this.target_select = false;
		this.closePopup();
		Main.gw.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		checkHealths();
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			if(!target_select){
				previous();
			}
			Main.gw.repaint();
			break;
		case KeyEvent.VK_DOWN:
			if(!target_select){
				next();
			}
			Main.gw.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			turn();
			Main.gw.repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_K:
			Main.openScreen(Main.dp);
			Main.gw.repaint();
			break;
		case KeyEvent.VK_1:
			if(target_select){
				target = c1;
				closeTargetSelect();
				turn();
			}
			break;
		case KeyEvent.VK_2:
			if(target_select){
				target = c2;
				closeTargetSelect();
				turn();
			}
			break;
		}
	}
}