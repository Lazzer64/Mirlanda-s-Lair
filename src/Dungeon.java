import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;


public class Dungeon {

	Room[][] rooms;
	int width,height;
	
	private void setRoom(Room r, int x, int y){
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

		if((y+1) < height && !rooms[y+1][x].isType(Room.RoomType.no_room)){r.add(rooms[y+1][x]);}

		if((y-1) > 0 && !rooms[y-1][x].isType(Room.RoomType.no_room)){r.add(rooms[y-1][x]);}

		if((x+1) < width && !rooms[y][x+1].isType(Room.RoomType.no_room)){r.add(rooms[y][x+1]);}

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

		int rollx = (int) (Math.random() * width);
		int rolly = (int) (Math.random() * height);

		for(int i = 0; i < num; i++){
			while(!rooms[rolly][rollx].isType(Room.RoomType.room)){
				rollx = (int) (Math.random() * width);
				rolly = (int) (Math.random() * height);
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

	static Dungeon parse(File file){
		Dungeon d = new Dungeon();
		int x = 0;
		int y = 0;
		try{

			LineNumberReader reader = new LineNumberReader(new FileReader(file));
			reader.mark(99999);

			int width = 0;
			int height = 0;
			String line = reader.readLine();
			
			// Find Width
			for(int i = 0; i < line.length(); i++){
				if(line.charAt(i) != '\t' && line.charAt(i) != ' '){
					width ++;
				}
			}
			// Find Length
			reader.skip(Long.MAX_VALUE);
			height = reader.getLineNumber() + 1;
			
			d.width = width;
			d.height =height;
			
			d.rooms = new Room[height][width];
			d.initToEmpty();
			reader.reset();
			
			while(line != null){
				line = reader.readLine();
				String s = line;
				for(char i: s.toCharArray()){
					if(i != '\t' && i != ' '){
						Room current = Room.getRoomType(i);
						current.setLocation(x, y);
						d.setRoom(current, x, y);
						x++;
					}
				}
				x = 0;
				y++;
			}

		} catch (FileNotFoundException e) {
			System.err.println("Could not find file " + file);
			e.printStackTrace();
		} catch (Exception e){

		}
		return d;
	}

	void initToEmpty(){
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				rooms[y][x] = Room.getRoomType('X');
			}
		}
	}
	
}



// Dungeon Panel

class DungeonPanel extends GamePanel{

	Dungeon dungeon;
	final int cell_size = 20;
	int fogSize = 400;
	boolean fogEnabled = true;
	boolean miniMapEnabled = false;

	public DungeonPanel(Dungeon d){
		super();
		dungeon = d;
	}

	public void paint(Graphics g){

		g.setColor(Room.black);
		g.fillRect(0, 0, GameWindow.width, GameWindow.height);
		int xCenter = (GameWindow.width - cell_size)/2;
		int yCenter = (GameWindow.height - cell_size)/2;
		int xOff = xCenter -(Main.p.leader.x * cell_size);
		int yOff = yCenter -(Main.p.leader.y * cell_size);

		// Draw rooms
		for(int y = 0; y < dungeon.height; y++){
			for(int x = 0; x < dungeon.width; x++){
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

				g.fillRect(x * cell_size + xOff,y * cell_size + yOff, cell_size, cell_size);
				g.setColor(Room.black);
				g.drawRect(x * cell_size + xOff,y * cell_size + yOff, cell_size, cell_size);
			}
		}

		// Draw player
		g.setColor(Room.blue);
		g.fillRect(Main.p.leader.x * cell_size + xOff, Main.p.leader.y * cell_size + yOff, cell_size, cell_size);
		g.setColor(Room.black);
		g.drawRect(Main.p.leader.x * cell_size + xOff, Main.p.leader.y * cell_size + yOff, cell_size, cell_size);

		if(fogEnabled) drawFog(g);
		if(miniMapEnabled) drawMiniMap(g);

		drawPopup(g);

	}

	void drawMiniMap(Graphics g){
		int xCenter = (GameWindow.width)/2;
		int yCenter = (GameWindow.height)/2;
		int cell_size = this.cell_size/4;
		int xOff = xCenter -(cell_size/2 + Main.p.leader.x * cell_size);
		int yOff = yCenter -(cell_size/2 + Main.p.leader.y * cell_size);
		int alpha = 75; // 0 (transparent) - 255 (opaque)
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0, GameWindow.width, GameWindow.height);

		for(int y = 0; y < dungeon.rooms.length; y++){
			for(int x = 0; x < dungeon.rooms[0].length; x++){

				if(dungeon.rooms[y][x].viewable != Room.Viewable.hidden){
					g.setColor(new Color(255,255,255,alpha));
					g.drawRect(x * cell_size + xOff,y * cell_size + yOff, cell_size, cell_size);
				}
			}

		}

		g.setColor(Room.blue);
		g.fillRect(Main.p.leader.x * cell_size + xOff, Main.p.leader.y * cell_size + yOff, cell_size, cell_size);
		g.setColor(Room.black);
		g.drawRect(Main.p.leader.x * cell_size + xOff, Main.p.leader.y * cell_size + yOff, cell_size, cell_size);
	}

	void drawFog(Graphics g){
		int xCenter = (GameWindow.width - fogSize)/2;
		int yCenter = (GameWindow.height - fogSize)/2;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, xCenter, GameWindow.height); // left black out
		g.fillRect(0, 0, GameWindow.width, yCenter); // top black out
		g.fillRect(xCenter + fogSize, 0, GameWindow.height, GameWindow.width); // right black out
		g.fillRect(0, yCenter + fogSize, GameWindow.width, GameWindow.height); // bottom black out

		try {
			g.drawImage(ImageIO.read(new File("img/fog.png")), xCenter, yCenter, fogSize, fogSize, null);
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
		case KeyEvent.VK_M:
			miniMapEnabled = !miniMapEnabled;
			Main.gw.repaint();
			break;
		}
		this.closePopup();
	}

}