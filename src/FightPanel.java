import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.PriorityQueue;

class FightPanel extends GamePanel{

	Dungeon dungeon;
	Character currentChar;
	Monster c2;
	Character target = c2;
	Hero[] allies;
	Monster[] enemies;
	Character[] order;
	int selected = 0;
	int currentTurn = 0;
	boolean target_select = false;

	public FightPanel(Hero[] allies, Monster[] enemies){
		super();
		this.allies = allies;
		this.enemies = enemies;
		c2 = enemies[0];
		order = getTurnOrder();
		currentChar = order[0];
		setPreferredSize(new Dimension(GameWindow.width,GameWindow.height));
		setBounds(0, 0, GameWindow.width, GameWindow.height);
		setLayout(null);
		setText("You encounter a *cRED *b hostile! * ");
	}

	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		drawAllyStats(currentChar,5,15,g);
		drawEnemyStats(c2,5,15,g);
		g.drawString("vs.", (GameWindow.width - 24)/2, 27);
		drawCombatText(5,50,75,g);
		drawCombatActions(currentChar.getCombatActions(),15,125,g);
		g.setColor(Color.BLACK);
		drawPopup(g);
		textColor = Color.black;
		if(this.showTarget) this.drawTargetSelect(allies, enemies, g);
	}

	void drawAllyStats(Character c, int x, int y, Graphics g){
		g.drawString(c.name, x, y);
		g.drawString("Health: " + c.health + "/" + c.max_health, x, y + 12);
		g.drawString("Mana: " + c.mana + "/" + c.max_mana, x, y + (12 * 2));
	}

	void drawEnemyStats(Character c, int x, int y, Graphics g){

		String t = "";
		int x_space = 0;		
		if(c != null){
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
	}

	void drawCombatText(int x, int y, int size, Graphics g){
		g.setColor(Color.white);
		g.fillRect(x, y, GameWindow.width - x * 2, size);
		g.setColor(Color.black);
		g.drawRect(x, y, GameWindow.width - x * 2, size);
		g.setColor(textColor);
		wrapedText(text, x+3, y, GameWindow.width - x - 3, g);
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
				abilityName += " - *cBLUE " + a[i].getCost() + " *c ";
			}
			y += yChange;

			// draw ability name
			g.setColor(Color.black);
			wrapedText(abilityName, x, y - 4, GameWindow.width - x, g);
			abilityName = "";


		}


	}

	public void turn(){

		CombatAction c1Act = getSelected();

		if(currentChar.mana >= c1Act.getCost()){
			// TODO if you don't have enough mana do not skip turn
			setText(currentChar.name + " uses " + currentChar.useCombatAction(c1Act, target));

			if(((Hero)currentChar).jewelry.effect.use(currentChar,c2)){
				addText(" \n Your jewelry " + ((Hero)currentChar).jewelry.effect.getFlavor());
			}

		} else {
			setText("You do not have enough *cBLUE mana *c to use that!");
		}

		if(deadGroup(enemies)){
			setText("You have defated the hostiles.");
		}

		nextTurn();
		while(currentChar.getClass().equals(Monster.class)){ // If not player turn
			CombatAction c2Act = ((Monster) currentChar).action();
			Character enemyTarget = this.getNextLivingAlly();
			c2.useCombatAction(c2Act, enemyTarget);
			addText(" \n " + c2.name + " retaliated, hitting " + enemyTarget.name + " with " + c2Act.getFlavorText());
			nextTurn();
			if(enemyTarget.health <= 0){
				addText(" *b " + enemyTarget.name + " * has been *b *cRED Slain * *c ");
			}
			if(deadGroup(allies)){
				setText("You have been defeated.");
				break;
			}
		}

	}

	void checkHealths(){

		if(deadGroup(allies)){
			Main.restartGame();
		}

		if(deadGroup(enemies)){
			giveRewards();
		}

	}

	boolean deadGroup(Character[] chars){
		for(Character c: chars){
			if(c.health > 0) return false;
		}
		return true;
	}

	void tickStatus(){
		if(currentChar.statusTurns > 0){
			currentChar.status.effect.use(c2, currentChar);
			addText(currentChar.status.effect.getFlavor());
			currentChar.tickStatus();
		}

		if(c2.statusTurns > 0){
			c2.status.effect.use(currentChar, c2);
			addText(c2.status.effect.getFlavor());
			c2.tickStatus();
		}

	}

	void giveRewards(){
		Main.openScreen(Main.dp);
		int initLevel = currentChar.level;
		// Rewards for killing enemy
		Item[] loot = enemies[0].getLoot();
		int exp_reward = enemies[0].level;
		Main.c.grantExp(exp_reward);
		Main.c.give(loot);
		//
		GamePanel panel = ((GamePanel)Main.gw.getContentPane());
		panel.openPopup(
						"  \n -  -  -  Reward  -  -  -"
						+" \n " + exp_reward + " exp."
						+" \n " + Main.itemsToText(loot));
		if(currentChar.level > initLevel){
			((GamePanel)Main.gw.getContentPane()).openPopup(((GamePanel)Main.gw.getContentPane()).popText 
					+ " \n  \n LEVEL UP!"
					+ " \n You are now level " + currentChar.level);
		}
	}

	CombatAction getSelected(){
		return currentChar.getCombatActions()[selected];
	}

	void next(){
		this.selected++;
	}

	void previous(){
		this.selected--;
	}

	Character[] getTurnOrder(){

		PriorityQueue<Character> pq = new PriorityQueue<Character>(new Comparator<Character>() {
			public int compare(Character a, Character b) {
				return -(a.dexterity - b.dexterity);
			}});

		for(Character c: allies) if(c.health > 0) pq.add(c);
		for(Character c: enemies) if(c.health > 0)pq.add(c);

		Character[] order = new Character[pq.size()];
		for(int i = 0; i < order.length; i++)order[i] = pq.poll();

		return order;
	}

	void nextTurn(){

		currentTurn++;
		if(currentTurn >= order.length){
			currentTurn = 0;
			order = getTurnOrder();
		}
		currentChar = order[currentTurn];
		if(currentChar.health <= 0){
			if(currentChar == c2)c2 = getNextLivingEnemy();
			nextTurn();
		}
	}

	Monster getNextLivingEnemy(){
		for(Monster m: enemies){
			if(m.health > 0){
				return m;
			}
		}
		return null;
	}

	Hero getNextLivingAlly(){
		for(Hero h: allies){
			if(h.health > 0){
				return h;
			}
		}
		return null;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if(showTarget){
			switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				targetPrev(allies, enemies);
				Main.gw.repaint();
				break;
			case KeyEvent.VK_DOWN:
				targetNext(allies, enemies);
				Main.gw.repaint();
				break;
			case KeyEvent.VK_RIGHT:
				target = getTarget(allies, enemies);
				showTarget = false;
				turn();
				Main.gw.repaint();
				break;
			}
			return;
		}

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
			if(getSelected().targeted()){
				showTarget = true;
				Main.gw.repaint();
				break;
			}
			target = c2;
			turn();
			Main.gw.repaint();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_K:
			Main.openScreen(Main.dp);
			Main.gw.repaint();
			break;
		}
	}
}