// TODO replace ability
import java.awt.Color;
import java.awt.Graphics;

public class Attack {

	private final static boolean X = true, O = false, C = false;
	final static int RIGHT = 0, UP = 90, LEFT = 180, DOWN = 270;

	final static Attack 
	FireBall = new Attack( new boolean[][] {
			{O,O,O,O,O,O,O,X,O},
			{O,O,O,O,C,O,X,X,X},
			{O,O,O,O,O,O,O,X,O},
	}),

	Slash = new Attack( new boolean[][] {
			{O,O,C,X,X}
	}),

	Arrow = new Attack( new boolean[][]{
			{O,O,O,O,C,O,O,X,X}
	});

	final boolean[][] range;
	boolean[][] currentRange;

	public Attack(boolean[][] range){

		if(range.length % 2 == 0 || range[0].length % 2 == 0){
			System.err.println("Error: " + this + " should have a proper center.");
		}

		int size = Math.max(range.length, range[0].length);

		int yAdj = (size - range.length)/2;

		this.range = new boolean[size][size];

		for(int y = 0; y < range.length; y++){
			for(int x = 0; x < range[0].length; x++){
				this.range[y + yAdj][x] = range[y][x];
			}
		}

		this.currentRange = this.range;

	}

	public void draw(Graphics g){
		int 
		cell_size = DungeonPanel.cell_size,
		xCenter = (GameWindow.width - cell_size)/2,
		yCenter = (GameWindow.height - cell_size)/2,
		aY = yCenter - cell_size * (range.length/2);

		for(boolean[] row: currentRange){
			int aX = xCenter - cell_size * (range.length/2);
			for(boolean b: row){
				if(b){
					g.setColor(new Color(255,100,100,50));
					g.fillRect(aX, aY, cell_size, cell_size);
					g.setColor(Color.RED);
					g.drawRect(aX, aY, cell_size, cell_size);
				}
				aX += cell_size;
			}
			aY += cell_size;
		}
	}

	public void rotate(int orientation){
		boolean[][] temp = new boolean[range.length][range.length];
		for(int y = 0; y < range.length; y++){
			for(int x = 0; x < range[0].length; x++){
				switch(orientation){
				case UP:
					temp[y][x] = range[x][(range.length - 1) - y];
					break;
				case DOWN:
					temp[y][x] = range[x][y];
					break;
				case LEFT:
					temp[y][x] = range[y][(range[0].length - 1) - x];
					break;
				case RIGHT:
					temp[y][x] = range[y][x];
					break;
				}
			}
		}
		currentRange = temp;

	}
}
