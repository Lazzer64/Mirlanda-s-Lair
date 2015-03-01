public interface Effect {

	static Effect
	none = new no_effect(),
	weak_heal = new HealEffect(1),
	medium_heal = new HealEffect(3),
	weak_fire = new FireEffect(2,.5),
	weak_burn = new BurnEffect(),
	weak_mana = new ManaHeal(1);
	//TODO Fear - Lower opponents health the more likely to make enemy flee. Grant rewards.
	
	boolean use(Character caster, Character target);
	String getFlavor();
	
}

class no_effect implements Effect {

	public boolean use(Character a, Character b){
		return false;
	}
	public String getFlavor() {
		return null;
	}
}

class HealEffect implements Effect {

	String name = "heal";
	int healamnt;
	
	public HealEffect(int healamnt){
		this.healamnt = healamnt;
	}
	
	public boolean use(Character caster, Character target) {
		caster.heal(healamnt);
		return true;
	}

	public String getFlavor() {
		return " healed for " + healamnt + ".";
	}

	public String toString(){
		return name;
	}

}

class FireEffect implements Effect {

	int dmg;
	double chance;
	String targetName = "Target";
	String flavor = "";
	
	public FireEffect(int dmg, double chance){
		this.dmg = dmg;
		this.chance = chance;
	}
	
	boolean roll(){
		double roll = Math.random();
		if(roll <= chance){
			return true;
		}
		flavor = "";
		return false;
	}

	public boolean use(Character caster, Character target){
		targetName = target.name;
		if(roll()){
			target.damage(dmg);
			return true;
		}
		return false;
	}
	public String getFlavor(){
		return " *i *cRED burns * *c " + targetName + " for *cRED  *b " + dmg + " * *c .";
	}
}

class BurnEffect implements Effect {

	String targetName = "Target";

	public boolean use(Character caster, Character target){
		target.setStatus(Status.burning);
		return true;
	}
	public String getFlavor(){
		return targetName + " was burned." ;
	}
}

class ManaHeal implements Effect {

	int healAmnt;
	
	public ManaHeal(int healAmnt){
		this.healAmnt = healAmnt;
	}
	
	public boolean use(Character caster, Character target) {
		caster.manaHeal(healAmnt);
		return true;
	}

	public String getFlavor() {
		return " heals for " + healAmnt + " *cBLUE mana. *c ";
	}
}