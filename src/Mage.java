import java.util.ArrayList;


public class Mage extends Profession {

	final static String
	name = "Sorcerer";
	
	final static int
	baseSTR = 3,
	baseDEX = 5,
	baseINT = 10;
	
	final static Equipment
	baseWEAPON = Equipment.weak_staff;
	
	final static CombatAction[] baseABILITIES = {};
	
	public Mage() {
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
	
	public Mage clone(){
		return new Mage();
	}
}
