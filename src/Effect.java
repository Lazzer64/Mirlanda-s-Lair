public interface Effect {

	static Effect
	none = new no_effect(),
	weak_heal = new weak_heal_effect(),
	medium_heal = new med_heal_effect(),
	weak_fire = new weak_fire(),
	weak_burn = new weak_burn(),
	weak_mana = new weak_mana_heal();
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

class weak_heal_effect implements Effect {

	String name = "heal";
	int healamnt = 1;
	
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

class med_heal_effect implements Effect{
	String name = "heal";
	int healamnt = 3;
	
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

class weak_fire implements Effect {

	int dmg = 2;
	int chance = 50;
	String targetName = "Target";
	String flavor = "";
	
	boolean roll(){
		double roll = Math.random();
		if(roll <= chance/100.0){
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

class weak_burn implements Effect {

	String targetName = "Target";

	
	public boolean use(Character caster, Character target){
		target.setStatus(Status.burning);
		return true;
	}
	public String getFlavor(){
		return targetName + " was burned." ;
	}
}

class weak_mana_heal implements Effect {

	int healAmnt = 1;
	
	public boolean use(Character caster, Character target) {
		caster.manaHeal(healAmnt);
		return true;
	}

	public String getFlavor() {
		return " heals for " + healAmnt + " *cBLUE mana. *c ";
	}
	
}
