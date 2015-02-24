import java.io.File;

class Equipment implements Item {
	static Equipment
	stick = new Equipment("Stick", new CombatAction[]{Ability.hit}),
	bone = new Equipment("Bone", new CombatAction[]{Ability.whack}),
	weak_sword = new Equipment("Feeble sword", new CombatAction[]{Ability.slash,Ability.smash}),
	weak_bow = new Equipment("Flimsy bow", new CombatAction[]{Ability.pierce, Ability.snipe}),
	weak_staff = new Equipment("Frail staff", new CombatAction[]{Ability.hit, Magic_Ability.spark, Magic_Ability.lightning}),
	weak_dagger = new Equipment("Paltry Dagger", new CombatAction[]{Ability.slash, Ability.backstab}),
	weak_tome = new Equipment("Fragile Tome", new CombatAction[]{Ability.hit, Magic_Ability.flare, Magic_Ability.fireball}),
	double_cast_staff = new DoubleCastStaff(),
	fire_bow = new FireBow()
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
	int fireAmnt = 20;
	
	public FireBow() {
		super("Phoenix Strike", new CombatAction[]{Ability.pierce, Ability.snipe});
	}
	
	public void cast(CombatAction action, Character caster, Character target){
		action.use(caster, target);
		new FireEffect(fireAmnt,1).use(caster, target);
		actFlavor = action.getFlavorText();
		actFlavor = actFlavor.substring(0, actFlavor.length()-1); // remove period
		actFlavor += " and *cRED *i scolding * *c the target for *cRED " + fireAmnt + ". *c ";
	}

	public String getActionFlavor(){
		return actFlavor;
	}
	public String getDescription(){
		return "A Legnedary Bow that ignites the arrows it launches.";
	}
	
}