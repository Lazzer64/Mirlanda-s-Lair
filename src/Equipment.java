import java.io.File;

class Equipment implements Item {
	static Equipment
	// Useless 
	stick = new Equipment("Stick", new CombatAction[]{Ability.hit}),
	bone = new Equipment("Bone", new CombatAction[]{Ability.whack}),
	// Starter 
	weak_sword = new Equipment("Feeble sword", new CombatAction[]{Ability.slash,Ability.smash}),
	weak_bow = new Equipment("Flimsy bow", new CombatAction[]{Ability.pierce, Ability.snipe}),
	weak_staff = new Equipment("Frail staff", new CombatAction[]{Ability.hit, Magic_Ability.spark, Magic_Ability.lightning}),
	weak_dagger = new Equipment("Paltry Dagger", new CombatAction[]{Ability.slash, Ability.backstab}),
	weak_tome = new Equipment("Fragile Tome", new CombatAction[]{Ability.hit, Magic_Ability.flare, Magic_Ability.fireball}),
	// Medium TODO
	// Advanced TODO
	// Legendary
	double_cast_staff = new DoubleCastStaff(),
	fire_bow = new FireBow(),
	crit_sword = new CritSword()
	;

	String name;
	CombatAction[] abilities;
	static File icon = new File("img/weapon_icon.png");

	String flavor = "";

	public Equipment(String name, CombatAction[] abilities){
		this.name = name;
		this.abilities = abilities;
	}

	public void use(Character target) {
		if(target.getClass().equals(Hero.class)){
			((Hero) target).give(((Hero) target).weapon);
			((Hero) target).setWeapon(this);
			((Hero) target).remove(this);
		}
	}

	public void cast(CombatAction action, Character caster, Character target){
		action.use(caster, target);
		flavor = action.getFlavorText();
	}

	public String getActionFlavor(){
		return flavor;
	}

	public File getIcon() {
		return icon;
	}

	public String toString(){
		return name;
	}
	
	public String getDescription(){
		return name;
		//TODO
	}
	
}

class DoubleCastStaff extends Equipment {

	String actFlavor = "";
	
	public DoubleCastStaff(){
		super("Mirror Staff",new CombatAction[]{Ability.hit,Magic_Ability.spark, Magic_Ability.fireball});
	}

	public void cast(CombatAction action, Character caster, Character target){
		if(action.getClass() != Magic_Ability.class){
			action.use(caster, target); 
			actFlavor = action.getFlavorText();
		}else{
			action.use(caster, target);
			actFlavor = action.getFlavorText();
			action.use(caster, target);
			actFlavor += " \n *b Staff *  recasts " + action.getFlavorText();
		}
	}

	public String getActionFlavor(){
		return actFlavor;
	}
	public String getDescription(){
		return "A Legendary Staff that mirrors spells the user casts.";
	}

}

class FireBow extends Equipment {

	String actFlavor = "";
	int fireMult = 2;
	
	public FireBow() {
		super("Wings of the Phoenix", new CombatAction[]{Ability.pierce, Ability.snipe});
	}
	
	public void cast(CombatAction action, Character caster, Character target){
		int fireDmg = fireMult * caster.level;
		action.use(caster, target);
		new FireEffect(fireDmg,1).use(caster, target);
		actFlavor = action.getFlavorText();
		actFlavor = actFlavor.substring(0, actFlavor.length()-1); // remove period
		actFlavor += " and *cRED *i scolding * *c the target for *cRED " + fireDmg + ". *c ";
	}

	public String getActionFlavor(){
		return actFlavor;
	}
	public String getDescription(){
		return "A Legnedary Bow that that was crafted with the feathers of a phoenix.";
	}
	
}

class CritSword extends Equipment {

	String actFlavor = "";
	int multiplier = 3;
	
	
	public CritSword() {
		super("Blade of Fortuna", new CombatAction[]{Ability.slash, Ability.smash, Ability.bladeFlurry});
	}
	
	public void cast(CombatAction action, Character caster, Character target){
		action.use(1, caster.dexterity * multiplier, caster.intelligence, caster, target);
		actFlavor = action.getFlavorText();
	}

	public String getActionFlavor(){
		return actFlavor;
	}
	public String getDescription(){
		return "A Legnedary Sword that increases the critical strike chance and power of the user.";
	}
	
}