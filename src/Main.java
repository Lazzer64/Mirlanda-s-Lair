import java.awt.event.KeyListener;
import java.io.File;
import java.util.Stack;

import javax.swing.JPanel;

public class Main {
	
	static GameWindow gw = new GameWindow();
	static final String level_dir = ("levels/");
	static final String level_extension = ".dngflr";
	static final String[] level_list = {"dungeon","dungeon2","dungeon3","dungeon4","dungeon5","dungeon6","huge"};
	static Party p;
	static int current_level = 0;
	static Dungeon d = Dungeon.parse(new File(level_dir + level_list[current_level] + level_extension));
	static DungeonPanel dp = new DungeonPanel(d);
	static CreationPanel cp = new CreationPanel();
	static InventoryPanel ip = new InventoryPanel();
	static StatsPanel sp = new StatsPanel();


	public static void main(String[] args) {
		gw.pushPanel(cp);
		while(running()){
			repaint();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void endCreation() {
		
		String name = cp.nameField.getText();
		Race race = (Race) cp.raceSelect.getSelectedItem();
		Profession profession = (Profession) cp.professionSelect.getSelectedItem();
		Main.p = new Party(new Hero(name,race,profession));
		
		d.removeEncounter(Room.RoomType.mini_boss);
		d.removeEncounter(Room.RoomType.altar);
		d.addEncounter(Room.RoomType.altar, 2);
		d.addEncounter(Room.RoomType.monster, 7);
		d.addEncounter(Room.RoomType.mini_boss, 3);
		
		p.leader.setLocation(d.getStart().x, d.getStart().y);
		p.leader.revealRooms();
		p.leader.give(new Item[]{Consumable.red_potion,Consumable.blue_potion,Consumable.red_potion});
		p.leader.give(Map.full_map);
		
		Hero alvin = new Hero("Alvin", Race.elf, new Ranger());
		Hero meepo = new Hero("Meepo", Race.goblin, new Mage());
		
		p.add(meepo);
		p.add(alvin);
		p.give(new Equipment("MMM",new CombatAction[]{Abilities.bladeFlurry}));
		
		String intro = (" *cRED COMBINE *cWHITE flour, *i baking soda * *cWHITE and *b salt * in small bowl. "
				+ "Beat butter, *cBLUE granulated sugar, *cWHITE brown sugar and vanilla extract "
				+ "in large mixer bowl until *cHEALTH creamy. *cWHITE Add *cYELLOW *b eggs, * *cWHITE one at a time, beating well "
				+ "after each addition. Gradually beat in flour *cGREEN mixture. *cWHITE Stir in morsels "
				+ "and nuts. Drop by rounded tablespoon onto ungreased *cMANA baking sheets. "); 
		
		gw.replacePanel(new CutScene(intro, dp));
	}
	
	static boolean running(){
		return gw.isVisible();
	}
	
	public static void repaint(){
		gw.repaint();
	}

	public static void restartGame(){
		loadLevel(0);
		gw.panels = new Stack<GamePanel>();
		gw.pushPanel(cp);
	}

	public static String itemsToText(Item[] items){
		String text = "[";
		for(Item i: items){
			text += i + ", ";
		}
		text = text.substring(0, text.length()-2);
		text += "]";
		return text;
	}

	public static void loadLevel(int level_number){
		if(level_number >= Main.level_list.length){
			System.out.print("No more levels");
			level_number = 0;
		}
		current_level = level_number;
		d = Dungeon.parse(new File(level_dir + level_list[current_level] + level_extension));
		dp = new DungeonPanel(d);
		p.leader.setLocation(d.getStart().x, d.getStart().y);
		gw.replacePanel(dp);
	}

	public static void nextLevel(){
		loadLevel(current_level+1);
		p.leader.revealRooms();
	}

}
