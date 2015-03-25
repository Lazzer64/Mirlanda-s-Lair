import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JPanel;

public class Main {
	
	static GameWindow gw = new GameWindow();
	static final String level_dir = ("levels/");
	static final String level_extension = ".dngflr";
	static final String[] level_list = {"square"};
	static Party p;
	static int current_level = 0;
	static Dungeon d = Dungeon.parse(new File(level_dir + level_list[current_level] + level_extension));
	static DungeonPanel dp = new DungeonPanel(d);
	static CreationPanel cp = new CreationPanel();
	static InventoryPanel ip = new InventoryPanel();
	static StatsPanel sp = new StatsPanel();


	public static void main(String[] args) {
		openScreen(cp);
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
		
		openScreen(dp);
		p.leader.setLocation(d.getStart().x, d.getStart().y);
		p.leader.revealRooms();
		p.leader.give(new Item[]{Consumable.red_potion,Consumable.blue_potion,Consumable.red_potion});
		p.leader.give(Map.full_map);
		
//		Hero alvin = new Hero("Alvin", Race.elf, new Ranger());
//		Hero meepo = new Hero("Meepo", Race.goblin, new Mage());
//		
//		p.add(meepo);
//		p.add(alvin);
	}
	
	static boolean running(){
		return gw.isVisible();
	}
	
	public static void repaint(){
		gw.repaint();
	}

	public static void openScreen(JPanel p){
		gw.setContentPane(p);
		gw.setKeyListener((KeyListener) p);
		gw.pack();
		gw.repaint();
	}

	public static void restartGame(){

		loadLevel(0);
		openScreen(cp);
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
		openScreen(dp);
	}

	public static void nextLevel(){
		loadLevel(current_level+1);
		p.leader.revealRooms();
	}

}
