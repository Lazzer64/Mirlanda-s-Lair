
public class Ability implements CombatAction {

	// Basic abilities
	static Ability
	hit = new Ability("Hit",false,2,0,0),
	whack = new Ability("Whack",false,2,0,0),
	// Basic Profession abilities
	slash = new Ability("Slash",false,4,0,0),
	smash = new Ability("Smash",false,7,0,6),
	pierce = new Ability("Pierce",false,4,0,0),
	snipe = new Ability("Snipe",false,10,0,8),
	backstab = new Ability("Backstab",false,7,0,6),
	// Medium abilities
	doubleStrike = new MultiStrike("Double Strike",2,2),
	// Advanced abilities
	tripleStrike = new MultiStrike("Triple Strike",3,4),
	// Legendary abilities
	bladeFlurry = new MultiStrike("Blade flurry",5,8)
	;
	
	String name, flavor;
	boolean targeted;
	int damage,heal,cost;

	Ability(String name, boolean targeted, int damage, int heal, int cost){
		this.targeted = targeted;
		this.name = name;
		this.damage = damage;
		this.heal = heal;
		this.cost = cost;
	}

	public void use(int str, int dex, int intel, Character caster, Character target){
		if(target != null){
			flavor = "";
			int use_damage = damage;
			boolean crit = false;
			double roll = Math.random();
			double chance = (dex/50.0);
			double crit_mult = 2 + (dex/10);

			crit = (roll <= chance);

			use_damage += (int) (str/5); // Strength addition to hit
			if(crit){use_damage *= crit_mult; flavor += " *i *cDEX CRIT! *c * ";}


			if(damage > 0 && heal <= 0){ // If damage ability
				target.damage(use_damage);
				flavor += " *b " + name + " * , dealing *cSTR *b " + use_damage + " * *c damage.";
			} 

			else if(heal > 0 && damage <= 0){ // if healing ability
				target.heal(heal);
				flavor += " *b " + name + " * , healing for *cINT " + heal + " *c ";
			} 

			else { // if damage and healing
				target.damage(use_damage);
				caster.heal(heal);
				flavor += " *b " + name + " * , dealing " + use_damage + " damage, and healing for " + heal + ".";
			}
		}
	}

	public void use(Character caster, Character target){
		use(caster.strength, caster.dexterity, caster.intelligence, caster, target);
	}

	public String getFlavorText(){
		return flavor;
	}

	public String toString(){
		return name;
	}

	public int getCost(){
		return cost;
	}

	public boolean targeted(){
		return targeted;
	}

}

class MultiStrike extends Ability {

	int numStrikes;
	static int damage = 2;
	
	MultiStrike(String name, int numStrikes, int cost) {
		super(name, false, damage, 0, cost);
		this.numStrikes = numStrikes;
	}
	
	public void use(int str, int dex, int intel, Character caster, Character target){
		if(target != null){
			flavor = " *b " + name + "  * hitting *b  " + numStrikes + " * times for ";
			
			for(int i = 0; i < numStrikes; i++){
				
				int use_damage = damage;
				boolean crit = false;
				double roll = Math.random();
				double chance = (dex/(50.0 * numStrikes)); // Lower crit chance than normal
				double crit_mult = 2 + (dex/10);
				crit = (roll <= chance);

				use_damage += (int) (str/5); // Strength addition to hit
				if(crit){use_damage *= crit_mult;}
				
				if(i != numStrikes - 1){
					if(crit)flavor += " *i *cDEX CRIT! *c * ";
					flavor += " *cSTR *b " + use_damage + ", * *c ";
				} else {
					flavor += " and ";
					if(crit)flavor += " *i *cDEX CRIT! *c * ";
					flavor += " *cSTR *b " + use_damage + " * *c ";
				}
				target.damage(use_damage);
			}
		}
	}
	
}

class Magic_Ability implements CombatAction{
	static  Magic_Ability
	weak_heal = new Magic_Ability("Attend Wounds",true,0,5,3),
	flare = new Magic_Ability("Flare",false,6,0,3),
	fireball = new Magic_Ability("Fireball",false,12,0,9),
	spark = new Magic_Ability("Spark",false,5,0,2),
	lightning = new Magic_Ability("Lightning",false,10,0,7);

	String name, flavor;
	boolean targeted;
	int damage,heal,cost;

	Magic_Ability(String name, boolean targeted, int damage, int heal, int cost){
		this.targeted = targeted;
		this.name = name;
		this.damage = damage;
		this.heal = heal;
		this.cost = cost;
	}

	public void use(int str, int dex, int intel, Character caster, Character target){
		if(target != null){
			flavor = "";
			int use_damage = damage;

			use_damage += (int) (intel/5); // Intelligence addition to hit

			if(damage > 0 && heal <= 0){ // If damage ability
				target.damage(use_damage);
				flavor += " *b " + name + " * , dealing *cINT  *b " + use_damage + " * *c damage.";
			} 

			else if(heal > 0 && damage <= 0){ // if healing ability
				target.heal(heal);
				flavor += " *b " + name + " * , healing for *cINT  *b " + heal + " * *c ";
			} 

			else { // if damage and healing
				target.damage(use_damage);
				caster.heal(heal);
				flavor += " *b " + name + " *b , dealing " + use_damage + " damage, and healing for " + heal + ".";
			}
		}
	}

	public void use(Character caster, Character target){
		use(caster.strength, caster.dexterity, caster.intelligence, caster, target);
	}

	public String getFlavorText(){
		return flavor;
	}

	public String toString(){
		return name;
	}

	public int getCost(){
		return cost;
	}

	public boolean targeted(){
		return targeted;
	}
}

class ReviveAction implements CombatAction {

	String flav = "";

	public void use(int str, int dex, int intel, Character caster, Character target) {
		use(caster,target);
	}

	public void use(Character caster, Character target) {
		if(target.dead){
			target.revive(.30);
			flav = " *b resurrect *  on " + target.name + "!";
		} else {
			flav = " resurrect on " + target.name + " but it *cRED failed. *c";
		}
	}

	public int getCost() {
		return 20;
	}

	public String getFlavorText() {
		return flav;
	}

	public boolean targeted() {
		return true;
	}

	public String toString(){
		return "Resurrect";
	}


}

class BagAction implements CombatAction {

	public void use(int str, int dex, int intel, Character caster, Character target) {
		use(caster,target);
	}

	public void use(Character caster,Character target) {
		if(Main.p.inventory.size() > 0){
			Main.openScreen(new CombatInventoryPanel());
		}
	}

	public int getCost(){
		return 0;
	}

	public String getFlavorText(){
		return "opened bag.";
	}

	public String toString(){
		return "Bag";
	}

	public boolean targeted(){
		return false;
	}

}

class RunAction implements CombatAction {

	String flavor;

	public void use(int str, int dex, int intel, Character caster, Character target) {
		double chance = (dex - target.dexterity);
		double roll =  Math.random() * 50;
		if(roll <= chance){
			flavor = "You *b escape! * ";
			Main.openScreen(Main.dp);
			Main.dp.setPopText(" \n You escaped the " + target.name + "!");
			Main.dp.openPopup();
		} else {
			flavor = " *b Flee. * You *cRED *b fail * *c to escape.";
		}
	}

	public void use(Character caster,Character target) {
		use(caster.strength, caster.dexterity, caster.intelligence, caster, target);
	}

	public int getCost(){
		return 0;
	}

	public String getFlavorText(){
		return flavor;
	}

	public String toString(){
		return "Flee";
	}

	public boolean targeted(){
		return false;
	}
}

interface CombatAction{

	CombatAction 
	bag = new BagAction(),
	run = new RunAction();

	public void use(int str, int dex, int intel, Character caster, Character target);
	public void use(Character caster, Character target);
	public int getCost();
	public String getFlavorText();
	public boolean targeted();
}