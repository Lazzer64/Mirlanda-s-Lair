import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;

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
	
	Selector<CombatAction> actionSelect;

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
		setText("You encounter a *cRED *b hostile! * *c ");
		if(currentChar.getClass().equals(Monster.class)) turn();
		actionSelect  = getCombatActionSelector(currentChar);
		
	}

	public void paint(Graphics g){

		int 
		statsY = 15,
		vsY = statsY + 12,
		combatTextY = statsY + 35,
		combatTextSize = 100,
		orderY = combatTextY + combatTextSize + 5,
		combatActionsY = orderY + 5;

		g.setColor(Color.BLACK);
		drawAllyStats(currentChar,5,statsY,g);
		drawEnemyStats(c2,5,statsY,g);
		g.drawString("vs.", (GameWindow.width - 24)/2, vsY);
		drawCombatText(5,combatTextY,combatTextSize,g);
		drawOrder((GameWindow.width - ((order.length + 1) * 10))/2,orderY,g);
		//drawCombatActions(currentChar.getCombatActions(),15,combatActionsY,g);
		actionSelect.borderColor = new Color(0,0,0,0);
		actionSelect.backColor = new Color(0,0,0,0);
		actionSelect.draw(5, combatActionsY + 14, GameWindow.width - 5*2, this, g);
		g.setColor(Color.BLACK);
		drawPopup(g);
		textColor = Color.black;
		if(this.showTarget) this.drawTargetSelect(allies, enemies, g);
	}

	void drawAllyStats(Character c, int x, int y, Graphics g){
		g.drawString(c.name, x, y);
		g.drawString("Health: " + c.getHpText(), x, y + 12);
		g.drawString("Mana: " + c.mana + "/" + c.max_mana, x, y + (12 * 2));
	}

	void drawEnemyStats(Character c, int x, int y, Graphics g){

		String t = "";
		int x_space = 0;		
		if(c != null){
			t = "lvl " + c.level + " " + c.name;
			x_space = this.getFontMetrics(this.getFont()).stringWidth(t);
			g.drawString(t, GameWindow.width - x_space - x, y);

			t = "Health: " + c.getHpText();
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

	void drawOrder(int x, int y, Graphics g){
		int imageSize = 10;
		int i = 0;
		for(Character c: order){
			try {
				if(c.getClass().equals(Hero.class)){
					g.drawImage(StatsPanel.getCharacterIcon(c),x,y,null);
				}
				if(c.getClass().equals(Monster.class)){
					g.drawImage(ImageIO.read(new File("img/enemy_icon.png")),x,y,null);
				}
			} catch (IOException e) {e.printStackTrace();}
			g.setColor(Color.white);
			if(i == currentTurn) g.setColor(Color.BLACK);
			g.drawRect(x, y, imageSize - 1, imageSize - 1);
			x += imageSize;
			i++;
		}
	}

	Selector<CombatAction>  getCombatActionSelector(Character c){
		CombatAction[] selecActions = c.getCombatActions();
		String[] selectText = new String[selecActions.length];
		for(int i = 0; i < selecActions.length; i++){
			selectText[i] = selecActions[i].toString() + " - *cBLUE " + selecActions[i].getCost() + " *c ";
		}
		return new Selector<CombatAction>(selecActions,selectText);
	}
	
	public void turn(){

		if(currentChar.getClass().equals(Hero.class)){
			heroTurn((Hero)currentChar);
		}
		while(currentChar.getClass().equals(Monster.class) && !deadGroup(allies) && !deadGroup(enemies)){
			monsterTurn((Monster)currentChar);
		}
		actionSelect = getCombatActionSelector(currentChar);
	}

	void heroTurn(Hero c){
		if(target != null){
			CombatAction c1Act = getSelected();
			if(c.mana >= c1Act.getCost()){
				setText(c.name + " uses " + c.useCombatAction(c1Act, target));

				if((c).jewelry.effect.use(c,c2)){
					addText(" \n " + c.name + "'s jewelry" + (c).jewelry.effect.getFlavor());
				}

				if(c2.dead) c2 = getNextLivingEnemy();

				nextTurn();
			} else {
				setText("You dont have enought *cMANA mana *c to do that.");
			}

			if(deadGroup(enemies)){
				setText("You have defated the hostiles.");
			}
		}
	}

	void monsterTurn(Monster c){
		CombatAction c2Act = c.action();
		Character enemyTarget = this.getNextLivingAlly();
		if(enemyTarget != null){
			c2.useCombatAction(c2Act, enemyTarget);
			addText(" \n " + c2.name + " retaliated, hitting " + enemyTarget.name + " with " + c2Act.getFlavorText());
			nextTurn();
			if(enemyTarget.dead){
				addText(" *b " + enemyTarget.name + " * has been *b *cRED Slain * *c ");
			}
		}
		if(deadGroup(allies)){
			setText("You have been defeated.");
			
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
			if(!c.dead) return false;
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
		String s = "";
		int exp_reward = 0;
		Main.gw.popPanel();

		ArrayList<Item> sumLoot = new ArrayList<Item>();
		for(Monster m: enemies){
			for(Item i: m.getLoot()){
				if(i != Item.none){
					sumLoot.add(i);
				}
			}
		}

		Item[] loot = new Item[sumLoot.size()];
		sumLoot.toArray(loot);
		for(Monster m: enemies)exp_reward += m.exp();

		Main.p.give(loot);
		s = "Reward: \n " + Main.itemsToText(loot);
		for(Hero c: allies){
			int initLevel = c.level;
			GamePanel panel = ((GamePanel)Main.gw.getContentPane());
			if(!c.dead)c.grantExp(exp_reward);
			if(c.level > initLevel){
				s += " \n " + c.name + " is now level " + c.level;
			}
			panel.openPopup(s);
		}
	}

	CombatAction getSelected(){
		//return currentChar.getCombatActions()[selected];
		return actionSelect.getSelected();
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

		for(Character c: allies) if(!c.dead) pq.add(c);
		for(Character c: enemies) if(!c.dead)pq.add(c);

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
		if(currentChar.dead){
			if(currentChar == c2)c2 = getNextLivingEnemy();
			nextTurn();
		}
	}

	Monster getNextLivingEnemy(){
		for(Monster m: enemies){
			if(!m.dead){
				return m;
			}
		}
		return null;
	}

	Hero getNextLivingAlly(){
		for(Hero h: allies){
			if(!h.dead){
				return h;
			}
		}
		return null;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if(showTarget){
			switch(e.getKeyCode()){
			case Hotkeys.UP:
				targetPrev(allies, enemies);
				
				break;
			case Hotkeys.DOWN:
				targetNext(allies, enemies);
				
				break;
			case Hotkeys.SELECT:
				target = getTarget(allies, enemies);
				showTarget = false;
				turn();
				
				break;
			case Hotkeys.CANCEL:
				showTarget = false;
				
				break;
			}
			return;
		}

		checkHealths();
		switch(e.getKeyCode()){
		case Hotkeys.UP:
			if(!target_select){
				//previous();
				actionSelect.previous();
			}
			
			break;
		case Hotkeys.DOWN:
			if(!target_select){
				//next();
				actionSelect.next();
			}
			
			break;
		case Hotkeys.SELECT:
			if(getSelected().targeted()){
				showTarget = true;
				
				break;
			}
			target = c2;
			turn();
			
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		// TODO Remove after testing
		case KeyEvent.VK_K:
			Main.gw.popPanel();
			
			break;
		}
	}
}