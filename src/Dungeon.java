import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


public class Dungeon {

	Room[][] rooms;

	private void setRoom(Room r, int x, int y){

		if(rooms == null){
			rooms = new Room[20][20];
		}

		// set new room
		rooms[y][x] = r;
	}

	public void setAllRoomsViewable(Room.Viewable v){
		for(Room[] j: Main.d.rooms){
			for(Room i: j){
				if(!i.isType(Room.RoomType.no_room)){
					i.viewable = v;
				}

			}
		}
	}

	public void setNotSeenRoomsViewable(Room.Viewable v){
		for(Room[] j: Main.d.rooms){
			for(Room i: j){
				if(!i.isType(Room.RoomType.no_room) && !i.viewable.equals(Room.Viewable.seen)){
					i.viewable = v;
				}

			}
		}
	}

	public Room[] getSurrounding(int x, int y){
		ArrayList<Room> r = new ArrayList<Room>();


		if((y+1) < rooms.length && !rooms[y+1][x].isType(Room.RoomType.no_room)){r.add(rooms[y+1][x]);}

		if((y-1) > 0 && !rooms[y-1][x].isType(Room.RoomType.no_room)){r.add(rooms[y-1][x]);}

		if((x+1) < rooms.length && !rooms[y][x+1].isType(Room.RoomType.no_room)){r.add(rooms[y][x+1]);}

		if((x-1) > 0 && !rooms[y][x-1].isType(Room.RoomType.no_room)){r.add(rooms[y][x-1]);}

		Room[] ret = new Room[r.size()];
		r.toArray(ret);
		return ret;

	}

	public Room[] getSurrounding(int x, int y, int distance){
		ArrayList<Room> r = new ArrayList<Room>();

		if(distance == 1){
			return getSurrounding(x, y);
		}

		for(Room i: getSurrounding(x, y, distance - 1)){
			r.add(i);
			for(Room e: getSurrounding(i.x,i.y)){
				r.add(e);
			}
		}

		Room[] ret = new Room[r.size()];
		r.toArray(ret);
		return ret;

	}

	public void addEncounter(Room.RoomType type, int num){
		int rollx = (int) (Math.random() * 20);
		int rolly = (int) (Math.random() * 20);

		for(int i = 0; i < num; i++){
			while(!rooms[rolly][rollx].isType(Room.RoomType.room)){
				rollx = (int) (Math.random() * 20);
				rolly = (int) (Math.random() * 20);
			}
			Room addedRoom = new Room(type);
			addedRoom.setLocation(rollx, rolly);
			rooms[rolly][rollx] = addedRoom;
		}
	}

	public void removeEncounter(Room.RoomType type){
		for(Room[] i: rooms){
			for(Room room: i){
				if(room.isType(type)){
					rooms[room.y][room.x] = new Room(Room.RoomType.room);
				}
			}
		}
	}

	public Room getStart(){
		for(Room[] i: rooms){
			for(Room room: i){
				if(room.type.equals(Room.RoomType.start)){
					return room;
				}
			}
		}
		return rooms[5][6]; // Default start position if none is found
	}

	static Dungeon parse(File f){
		Dungeon d = new Dungeon();
		int x = 0;
		int y = 0;
		try{
			RandomAccessFile dung = new RandomAccessFile(f,"r");
			String line = dung.readLine();

			while(line != null){
				char[] charLine = line.toCharArray();

				for(int i = 0; i < charLine.length; i++){
					if(charLine[i] != '\t' && charLine[i] != ' '){
						Room current = Room.getRoomType(charLine[i]);
						current.setLocation(x, y);
						d.setRoom(current, x, y);
						x++;
					}
				}

				line = dung.readLine();
				x = 0;
				y++;
			}


		} catch (FileNotFoundException e) {
			System.err.println("Could not find file " + f);
			e.printStackTrace();
		} catch (Exception e){

		}
		return d;
	}

}



class Room {

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
			Main.c.manaHeal(Main.c.max_mana);
			//
			panel.openPopup(" \n You encounter an altar! \n  \n Your mana was restored.");
			replaceRoom = new Room(RoomType.room);
			replaceRoom.color = faded_pink;
			Main.d.rooms[Main.c.y][Main.c.x] = replaceRoom;
			break;
		case boss:
			Monster Boss = Monsters.dragon();
			Boss.level = 8;
			Main.openScreen(new FightPanel(Main.c,Boss));
			//
			replaceRoom = new Room(RoomType.room);
			replaceRoom.color = faded_pink; // change to faded red?
			Main.d.rooms[Main.c.y][Main.c.x] = replaceRoom;
			Main.gw.repaint();
			panel.closePopup();
			break;
		case horizontal_hidden:
			panel.closePopup();
			break;
		case mini_boss:
			// Mini boss encounter
			Monster miniB = Monsters.guard();
			miniB.level = 3;
			Main.openScreen(new FightPanel(Main.c,miniB));
			//
			replaceRoom = new Room(RoomType.room);
			replaceRoom.color = faded_orange;
			Main.d.rooms[Main.c.y][Main.c.x] = replaceRoom;
			Main.gw.repaint();
			break;
		case monster:
			// Monster encounter
			Monster enemy = Monsters.skeleton();
			Main.openScreen(new FightPanel(Main.c,enemy));
			//
			Main.gw.repaint();
			replaceRoom = new Room(RoomType.room);
			replaceRoom.color = faded_purple;
			Main.d.rooms[Main.c.y][Main.c.x] = replaceRoom;
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
			Main.c.give(loot);
			//
			panel.openPopup(" \n You find treasure! \n  \n " + Main.itemsToText(loot) + " added to your inventory.");
			replaceRoom = new Room(RoomType.room);
			replaceRoom.color = faded_yellow;
			Main.d.rooms[Main.c.y][Main.c.x] = replaceRoom;
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

// Dungeon Panel

class DungeonPanel extends GamePanel{

	Dungeon dungeon;

	public DungeonPanel(Dungeon d){
		super();
		dungeon = d;
		setPreferredSize(new Dimension(GameWindow.width,GameWindow.height));
		setBounds(0, 0, GameWindow.width, GameWindow.height);
		setLayout(null);
	}

	public void paint(Graphics g){

		// Calculate cell size, if the cells do not fit evenly adjust size
		int cell_size = (int) GameWindow.height/(dungeon.rooms.length);
		if(cell_size % GameWindow.height != 0){
			cell_size = (int) GameWindow.height/(dungeon.rooms.length - 1);
		}

		// Draw rooms
		for(int y = 0; y < dungeon.rooms.length; y++){
			for(int x = 0; x < dungeon.rooms[0].length; x++){
				switch(dungeon.rooms[y][x].viewable){
				case hidden:
					g.setColor(Room.black);
					break;
				case peeked:
					g.setColor(Room.darkGray);
					break;
				case seen:
					g.setColor(dungeon.rooms[y][x].color);
					break;
				case unknown:
					if(!dungeon.rooms[y][x].isType(Room.RoomType.start)){
						g.setColor(Room.violet);
					} else {
						g.setColor(Room.green);
					}
					break;
				default:
					break;
				}

				g.fillRect(x * cell_size,y * cell_size, cell_size, cell_size);
				g.setColor(Room.black);
				g.drawRect(x * cell_size,y * cell_size, cell_size, cell_size);
			}
		}

		// Draw player
		g.setColor(Room.blue);
		g.fillRect(Main.c.x * cell_size, Main.c.y * cell_size, cell_size, cell_size);
		g.setColor(Room.black);
		g.drawRect(Main.c.x * cell_size, Main.c.y * cell_size, cell_size, cell_size);

//		g.setColor(Room.red);
//		int size = 100;
//		g.drawOval(Main.c.x * cell_size - (size / 2),Main.c.y * cell_size - (size / 2), size, size);

		drawPopup(g);
		
	}


	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			Main.c.move(0, -1);
			Main.gw.repaint();
			break;
		case KeyEvent.VK_DOWN:
			Main.c.move(0, 1);
			Main.gw.repaint();
			break;
		case KeyEvent.VK_LEFT:
			Main.c.move(-1, 0);
			Main.gw.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			Main.c.move(1, 0);
			Main.gw.repaint();
			break;
		}
	}


	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_I:
			Main.openScreen(Main.ip);
			Main.gw.repaint();
			break;
		case KeyEvent.VK_L:
			Main.nextLevel();
			Main.gw.repaint();
			break;
		case KeyEvent.VK_K:
			// TESTING PURPOSES
			Main.openScreen(new FightPanel(Main.c,Monsters.skeleton()));
			Main.gw.repaint();
			break;
		case KeyEvent.VK_C:
			Main.openScreen(Main.sp);
			Main.gw.repaint();
			break;
		case KeyEvent.VK_X:
			Main.restartGame();
			break;
		}
		this.closePopup();
	}

}