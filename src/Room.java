import java.awt.Color;
import java.awt.Graphics;

public class Room {

	final static Color 
	green = new Color(150, 240, 40),
	purple = new Color(150, 100, 200),
	violet = new Color(130, 65, 100),
	faded_purple = new Color(220, 200, 250),
	orange = new Color(240, 160, 80),
	faded_orange = new Color(250, 220, 150),
	red = new Color(205, 0, 80),
	yellow = new Color(255, 220, 20),
	faded_yellow = new Color(255, 245, 150),
	pink = new Color(255, 75, 140),
	faded_pink = new Color(255, 180, 220),
	white = new Color(240, 240, 240),
	black = new Color(0, 0, 0),
	blue = new Color(0, 50, 255),
	cyan = new Color(50, 200, 220),
	lightGray = new Color(180, 180, 180),
	darkGray = new Color(100,100,100);


	String name;
	Color color;
	int x,y;
	Viewable viewable = Viewable.hidden;
	enum Viewable {peeked,seen,hidden,unknown};
	Monster monster = null;
	Item[] loot;
	public enum RoomType{

		start(green), 
		boss(red), 
		mini_boss(orange), 
		stair(cyan),
		horizontal_hidden(lightGray),
		room(white),
		treasure(yellow),
		altar(pink),
		vertical_hidden(lightGray),
		no_room(black),
		monster(purple);

		Color roomColor;

		private RoomType(Color c){
			roomColor = c;
		}
	}
	RoomType type;

	public Room(RoomType type){
		this.type = type;
		this.color = type.roomColor;
		this.name = type.toString();

	}

	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}

	public static Room getRoomType(char c){

		switch(c){
		case 'S':
			return new Room(RoomType.start);

		case 'B':
			return new Room(RoomType.boss);

		case 'M':
			return new Room(RoomType.mini_boss);

		case 'D':
			return new Room(RoomType.stair);

		case '|':
			return new Room(RoomType.horizontal_hidden);

		case 'R':
			return new Room(RoomType.room);

		case 'T':
			return new Room(RoomType.treasure);

		case 'A':
			return new Room(RoomType.altar);

		case '-':
			return new Room(RoomType.horizontal_hidden);

		case 'X':
			return new Room(RoomType.no_room);
		}

		return null;
	}

	public static void encounter(RoomType type){
		Room replaceRoom;
		GamePanel panel = ((GamePanel)Main.gw.getContentPane());
		switch(type){
		case altar:
			// Reward for entering altar
			for(Hero c: Main.p){
				c.manaHeal(c.max_mana);
			}
			//
			panel.openPopup(" \n You encounter an altar! \n  \n Your mana was restored.");
			replaceRoom = new Room(RoomType.room);
			replaceRoom.color = faded_pink;
			Main.d.rooms[Main.p.leader.y][Main.p.leader.x] = replaceRoom;
			break;
		case boss:
			break;
		case horizontal_hidden:
			break;
		case mini_boss:
			break;
		case monster:
			break;
		case no_room:
			break;
		case room:
			break;
		case stair:
			break;
		case start:
			break;
		case treasure:
			// Loot from treasure room
			Item[] loot = {LootTable.treasure.rollLoot()};
			Main.p.leader.give(loot);
			//
			panel.openPopup(" \n You find treasure! \n  \n " + Main.itemsToText(loot) + " added to your inventory.");
			replaceRoom = new Room(RoomType.room);
			replaceRoom.color = faded_yellow;
			Main.d.rooms[Main.p.leader.y][Main.p.leader.x] = replaceRoom;
			Main.gw.repaint();
			break;
		case vertical_hidden:
			break;
		default:
			break;

		}
	}
	
	public void draw(int x, int y, Graphics g){
		
		switch(viewable){
		case hidden:
			g.setColor(Room.black);
			break;
		case peeked:
			g.setColor(Room.darkGray);
			break;
		case seen:
			g.setColor(color);
			if(color == white && loot != null && loot.length > 0){
				g.setColor(yellow);
			}
			break;
		case unknown:
			if(!isType(Room.RoomType.start)){
				g.setColor(Room.violet);
			} else {
				g.setColor(Room.green);
			}
			break;
		default:
			if(loot != null && loot.length > 0){
				g.setColor(yellow);
			}
			break;
		}
		
		g.fillRect(x, y, DungeonPanel.cell_size, DungeonPanel.cell_size);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, DungeonPanel.cell_size, DungeonPanel.cell_size);
	}

	public void update(){

	}

	public String toString(){
		return name + " (" + x + "," + y + ")";
	}

	public boolean isType(RoomType r){
		return (r.equals(type));
	}
}

class MonsterRoom extends Room{

	public MonsterRoom(Monster m) {
		super(RoomType.monster);
		monster = m;
	}

	public void update(){
		if(monster.dead){
			Room nr = new Room(RoomType.room);
			nr.viewable = Viewable.seen;
			nr.loot = new Item[] {Consumable.red_potion};
			Main.d.setRoom(nr, x, y);
		}
	}
	
	public void draw(int x, int y, Graphics g){
		
		switch(viewable){
		case hidden:
			g.setColor(black);
			break;
		case peeked:
			g.setColor(darkGray);
			break;
		case seen:
			g.setColor(color);
			if(color == white && loot != null && loot.length > 0){
				g.setColor(yellow);
			}
			break;
		case unknown:
			if(!isType(Room.RoomType.start)){
				g.setColor(violet);
			} else {
				g.setColor(green);
			}
			break;
		default:
			break;
		}
		
		
		g.fillRect(x, y, DungeonPanel.cell_size, DungeonPanel.cell_size);
		g.setColor(Color.RED);
		g.fillRect(x, y, DungeonPanel.cell_size, 3);
		g.setColor(Color.GREEN);
		g.fillRect(x, y, (DungeonPanel.cell_size * monster.health/monster.max_health), 3);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, DungeonPanel.cell_size, DungeonPanel.cell_size);
	}

}