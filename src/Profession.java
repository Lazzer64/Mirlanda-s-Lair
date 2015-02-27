public class Profession {
	// points add up to 18
	static public Profession
	explorer = new Profession("Explorer",6,6,6,Equipment.weak_sword,new CombatAction[]{}),
	gladiator = new Profession("Gladiator",10,5,3,Equipment.weak_sword,new CombatAction[]{}),
	thief = new Profession("Thief",4,11,3,Equipment.weak_dagger,new CombatAction[]{}),
	sorcerer = new Profession("Sorcerer",3,3,12,Equipment.weak_staff,new CombatAction[]{}),
	enchanter = new Profession("Enchanter",5,3,10,Equipment.weak_tome,new CombatAction[]{}),
	mage = new Profession("Mage",2,2,14,Equipment.weak_staff,new CombatAction[]{}),
	ranger = new Profession("Ranger",5,10,3,Equipment.weak_bow,new CombatAction[]{}),
	cleric = new Profession("Cleric", 2, 2, 14, Equipment.weak_staff, new CombatAction[]{new ReviveAction()})
	;
	
	static Profession[] default_professions = {explorer,gladiator,thief,sorcerer,enchanter,mage,ranger};
	
	String name;
	int strength,dexterity,intelligence;
	int primaryStat;
	CombatAction[] abilities;
	Equipment weapon;
	
	public Profession(String name, int str, int dex, int intel, Equipment weapon, CombatAction[] abilities){
		this.name = name;
		this.strength = str;
		this.dexterity = dex;
		this.intelligence = intel;
		this.weapon = weapon;
		this.abilities = abilities;
		
		int maxStat = Math.max(Math.max(str, dex), intel);
		if(maxStat == str && maxStat == dex && maxStat == intel){
			primaryStat = 0;
		} else if(maxStat == str) {
			primaryStat = 1;
		} else if(maxStat == dex) {
			primaryStat = 2;
		} else if(maxStat == intel){
			primaryStat = 3;
		}
		
	}
	

	public String toString(){
		return name;
	}
	
}