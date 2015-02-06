import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JPanel;

public class Main {

	static final String level_dir = ("levels/");
	static final String level_extension = ".dngflr";
	static final String[] level_list = {"dungeon","dungeon2","dungeon3","dungeon4","dungeon5","dungeon6"};
	static Hero c = new Hero("Kirito",999,999,999);
	static int current_level = 0;
	static Dungeon d = Dungeon.parse(new File(level_dir + level_list[current_level] + level_extension));
	static DungeonPanel dp = new DungeonPanel(d);
	static CreationPanel cp = new CreationPanel();
	static InventoryPanel ip = new InventoryPanel();
	static StatsPanel sp = new StatsPanel();
	static GameWindow gw = new GameWindow();

	static LootTable treasureTable = new LootTable();

	public static void main(String[] args) {
	}

	public static void endCreation(){

		d.removeEncounter(Room.RoomType.mini_boss);
		d.removeEncounter(Room.RoomType.altar);
		d.addEncounter(Room.RoomType.altar, 2);
		d.addEncounter(Room.RoomType.monster, 7);
		d.addEncounter(Room.RoomType.mini_boss, 3);

		openScreen(dp);
		c.setLocation(d.getStart().x, d.getStart().y);
		c.revealRooms();
		c.give(new Item[]{Consumable.red_potion,Consumable.blue_potion,Consumable.red_potion});
		c.give(Map.full_map);
		c.give(Equipment.stick);
		c.give(Jewelry.heal_ring);
		c.give(Jewelry.burn_ring);
	}

	public static void openScreen(JPanel p){
		gw.setContentPane(p);
		gw.setKeyListener((KeyListener) p);
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
		c.setLocation(d.getStart().x, d.getStart().y);
		openScreen(dp);
	}

	public static void nextLevel(){
		loadLevel(current_level+1);
		c.revealRooms();
	}

}
