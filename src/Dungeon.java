import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.imageio.ImageIO;


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





// Dungeon Panel

class DungeonPanel extends GamePanel{

	Dungeon dungeon;
	int cell_size;
	boolean fogEnabled = false;

	public DungeonPanel(Dungeon d){
		super();
		dungeon = d;
		
		// Calculate cell size, if the cells do not fit evenly adjust size
		cell_size = (int) GameWindow.height/(dungeon.rooms.length);
		if(cell_size % GameWindow.height != 0){
			cell_size = (int) GameWindow.height/(dungeon.rooms.length + 1);
		}
	}

	public void paint(Graphics g){
		
		g.setColor(Room.black);
		g.fillRect(0, 0, GameWindow.width, GameWindow.height);
		
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
		g.fillRect(Main.p.leader.x * cell_size, Main.p.leader.y * cell_size, cell_size, cell_size);
		g.setColor(Room.black);
		g.drawRect(Main.p.leader.x * cell_size, Main.p.leader.y * cell_size, cell_size, cell_size);

		if(fogEnabled){
			drawFog(g);
		}

		drawPopup(g);

	}

	void drawFog(Graphics g){
		int fogSize = 200;
		int center_x = Main.p.leader.x * cell_size + cell_size/2 - (fogSize/2);
		int center_y = Main.p.leader.y * cell_size + cell_size/2 - (fogSize/2);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, center_x, GameWindow.height); // left black out
		g.fillRect(0, 0, GameWindow.width, center_y); // top black out
		g.fillRect(center_x + fogSize, 0, GameWindow.height, GameWindow.width); // right black out
		g.fillRect(0, center_y + fogSize, GameWindow.width, GameWindow.height); // bottom black out

		try {
			g.drawImage(ImageIO.read(new File("img/fog.png")), center_x, center_y, fogSize, fogSize, null);
		} catch (IOException e) {e.printStackTrace();}

	}


	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			Main.p.leader.move(0, -1);
			Main.gw.repaint();
			break;
		case KeyEvent.VK_DOWN:
			Main.p.leader.move(0, 1);
			Main.gw.repaint();
			break;
		case KeyEvent.VK_LEFT:
			Main.p.leader.move(-1, 0);
			Main.gw.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			Main.p.leader.move(1, 0);
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
			Main.openScreen(new FightPanel(Main.p.members,new Monster[] {Monsters.skeleton(),Monsters.skeleton()}));
			Main.gw.repaint();
			break;
		case KeyEvent.VK_P:
			// TESTING PURPOSES
			Main.openScreen(new FightPanel(Main.p.members, new Monster[] {Monsters.spook()}));
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