import java.util.ArrayList;


public class Warrior extends Profession {

	final static String
	name = "Warrior";
	
	final static int
	baseSTR = 10,
	baseDEX = 5,
	baseINT = 3;
	
	final static Equipment
	baseWEAPON = Equipment.weak_sword;
	
	final static CombatAction[] baseABILITIES = {};
	
	public Warrior() {
		super(name, baseSTR, baseDEX, baseINT, baseWEAPON, baseABILITIES);
	}
	
	public void levelUp(int level){
		switch(level){
		case 9:
		case 8:
		case 7:
		case 6:
		case 5:		
		case 4:
		case 3:
		case 2:	
		}
	}	
	
	public Warrior clone(){
		return new Warrior();
	}
}
