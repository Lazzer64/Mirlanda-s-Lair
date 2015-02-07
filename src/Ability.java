
public enum Ability implements CombatAction {

	hit("Hit",false,2,0,0),
	whack("Whack",false,2,0,0),
	slash("Slash",false,4,0,0),
	smash("Smash",false,7,0,6),
	pierce("Pierce",false,4,0,0),
	snipe("Snipe",false,10,0,8),
	backstab("Backstab",false,7,0,6);

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

	public void use(Character caster, Character target){

		flavor = "";
		int use_damage = damage;
		boolean crit = false;
		double roll = Math.random();
		double chance = (caster.dexterity/50.0);
		double crit_mult = 2 + (caster.dexterity/5);

		crit = (roll <= chance);

		use_damage += (int) (caster.strength/5); // Strength addition to hit
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
			flavor += "You used *b " + name + " * , dealing " + use_damage + " damage, and healing for " + heal + ".";
		}

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

enum Magic_Ability implements CombatAction{

	weak_heal("Attend Wounds",true,0,5,3),
	flare("Flare",false,6,0,3),
	fireball("Fireball",false,12,0,9),
	spark("Spark",false,5,0,2),
	lightning("Lightning",false,10,0,7);

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

	public void use(Character caster, Character target){

		flavor = "";
		int use_damage = damage;

		if(targeted){target = caster;}
		
		use_damage += (int) (caster.intelligence/5); // Intelligence addition to hit

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

class BagAction implements CombatAction {

	public void use(Character caster,Character enemy) {
		if(Main.c.inventory.size() > 0){
			Main.openScreen(new CombatInventoryPanel());
		}
	}

	public int getCost(){
		return 0;
	}

	public String getFlavorText(){
		return "opened your bag.";
	}

	public String toString(){
		return "Bag";
	}

	public boolean targeted(){
		return false;
	}

}

interface CombatAction{
	public void use(Character caster, Character enemy);
	public int getCost();
	public String getFlavorText();
	public boolean targeted();
}