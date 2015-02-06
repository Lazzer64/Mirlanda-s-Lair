public interface Effect {

	void use(Character caster, Character target);
	String getFlavor();
	
}

class Effects {
	static Effect
	none = new no_effect(),
	weak_heal = new weak_heal_effect(),
	weak_burn = new weak_fire();
}


class no_effect implements Effect {
	public void use(Character a, Character b){
		
	}
	public String getFlavor() {
		return null;
	}
}

class weak_heal_effect implements Effect {
	
	String name = "heal";
	int healamnt = 1;
	
	public void use(Character caster, Character target) {
		caster.heal(healamnt);
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
	
	public void use(Character caster, Character target) {
		caster.heal(healamnt);
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
	String targetName = "Target";
	
	public void use(Character caster, Character target){
		targetName = target.name;
		target.damage(dmg);
	}
	public String getFlavor(){
		return " burns " + targetName + " for " + dmg + ".";
	}
	
}
