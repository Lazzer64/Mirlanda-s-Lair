public enum Profession {
	// points add up to 18
	Explorer(6,6,6,Equipment.weak_sword,new CombatAction[]{}),
	Gladiator(10,5,3,Equipment.weak_sword,new CombatAction[]{}),
	Ninja(4,11,3,Equipment.weak_dagger,new CombatAction[]{}),
	Sorcerer(3,3,12,Equipment.weak_staff,new CombatAction[]{}),
	Enchanter(5,3,10,Equipment.weak_tome,new CombatAction[]{}),
	Mage(2,2,14,Equipment.weak_staff,new CombatAction[]{}),
	Ranger(5,10,3,Equipment.weak_bow,new CombatAction[]{});
	
	int strength,dexterity,intelligence;
	CombatAction[] abilities;
	Equipment weapon;
	
	private Profession(int str, int dex, int intel, Equipment weapon, CombatAction[] abilities){
		this.strength = str;
		this.dexterity = dex;
		this.intelligence = intel;
		this.weapon = weapon;
		this.abilities = abilities;
	}
	
}
