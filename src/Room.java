import java.awt.Color;

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
			Monster Boss = Monsters.dragon(Main.p.leader.level);
			Boss.level = 8;
			Main.gw.pushPanel(new FightPanel(Main.p.members,new Monster[] {Boss}));
			//
			replaceRoom = new Room(RoomType.room);
			replaceRoom.color = faded_pink; // change to faded red?
			Main.d.rooms[Main.p.leader.y][Main.p.leader.x] = replaceRoom;
			Main.gw.repaint();
			panel.closePopup();
			break;
		case horizontal_hidden:
			panel.closePopup();
			break;
		case mini_boss:
			// Mini boss encounter
			Monster miniB = Monsters.guard(Main.p.leader.level);
			miniB.level = 3;
			Main.gw.pushPanel(new FightPanel(Main.p.members,new Monster[] {miniB}));
			//
			replaceRoom = new Room(RoomType.room);
			replaceRoom.color = faded_orange;
			Main.d.rooms[Main.p.leader.y][Main.p.leader.x] = replaceRoom;
			Main.gw.repaint();
			break;
		case monster:
			// Monster encounter
			Monster enemy = Monsters.skeleton(Main.p.leader.level);
			Main.gw.pushPanel(new FightPanel(Main.p.members,new Monster[] {enemy}));
			//
			Main.gw.repaint();
			replaceRoom = new Room(RoomType.room);
			replaceRoom.color = faded_purple;
			Main.d.rooms[Main.p.leader.y][Main.p.leader.x] = replaceRoom;
			break;
		case no_room:
			break;
		case room:
			panel.closePopup();
			break;
		case stair:
			panel.closePopup();
			Main.nextLevel();
			break;
		case start:
			panel.closePopup();
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

	public String toString(){
		return name + " (" + x + "," + y + ")";
	}

	public boolean isType(RoomType r){
		return (r.equals(type));
	}
}