public abstract class Profession {
	
	static Profession[] default_professions = {new Warrior(), new Mage(), new Ranger()};
	
	String name;
	int strength,dexterity,intelligence;
	int primaryStat;
	Ability[] abilities;
	Equipment weapon;
	
	public Profession(String name, int str, int dex, int intel, Equipment weapon, Ability[] abilities){
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
	
	public void addCombatAction(Ability newAction){
		for(Ability a: abilities){
			if(a.equals(newAction)) return;
		}
		Ability[] newAbilities = new Ability[abilities.length + 1];
		for(int i = 0; i < abilities.length; i++){
			newAbilities[i] = abilities[i];
		}
		newAbilities[abilities.length] = newAction;
		abilities = newAbilities;
	}
	
	public abstract void levelUp(int level);
	public abstract Profession clone();

	public String toString(){
		return name;
	}
	
}