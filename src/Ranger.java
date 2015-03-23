
public class Ranger extends Profession {

	final static String
	name = "Theif";

	final static int
	baseSTR = 5,
	baseDEX = 10,
	baseINT = 3;

	final static Equipment
	baseWEAPON = Equipment.weak_bow;

	final static CombatAction[] baseABILITIES = {};

	public Ranger() {
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

	public Ranger clone(){
		return new Ranger();
	}
}
