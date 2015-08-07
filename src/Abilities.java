import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class Abilities {

	final static Ability
	hit = parse("Hit"),
	whack = parse("Whack"),
	slash = parse("Slash"),
	smash = parse("Smash"),
	pierce = parse("Pierce"),
	snipe = parse("Snipe"),
	backstab = parse("Backstab"),
	doubleStrike = parse("DoubleStrike"),
	tripleStrike  = parse("TripleStrike"),
	bladeFlurry =  parse("BladeFlurry"),
	weak_heal = parse("WeakHeal"),
	flare = parse("Flare"),
	fireball = parse("Fireball"),
	spark = parse("Spark"),
	lightning = parse("Lightning"),
	inferno = parse("Inferno")
	;

	static Ability a;

	static Ability parse(String f){
		return parse(new File("spells/" + f + ".xml"));
	}

	static Ability parse(File f){
		System.out.println("Loading " + f.getName() + "...");
		a = new Ability();
		try {

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			sp.parse(f, new DefaultHandler(){

				String currentValue = "";

				public void startElement(String uri, String localName, String qName, Attributes attributes){
						if(qName.equalsIgnoreCase("ATTACK")) a = new Ability();
						else if(qName.equalsIgnoreCase("SPELL")) a = new MagicAbility();
						else if(qName.equalsIgnoreCase("MULTI")) a = new MultiStrike("MISSING NAME", 0, 0, 0);
						else if(qName.equalsIgnoreCase("RANGE")) a = new RangeOfDamage("MISSING NAME", 0, 0, 0);
				}

				public void endElement(String uri, String localName, String qName){
					if(qName.equalsIgnoreCase("NAME")){
						a.name = currentValue;
					} else if(qName.equalsIgnoreCase("TARGETED")){
						if(currentValue.equalsIgnoreCase("TRUE") || currentValue.equalsIgnoreCase("1")) a.targeted = true;
						else if (currentValue.equalsIgnoreCase("FALSE") || currentValue.equalsIgnoreCase("0")) a.targeted = false;
						else System.err.println("Invalid \"Targeted\" property");
					} else if(qName.equalsIgnoreCase("DAMAGE")){
						int val = Integer.parseInt(currentValue);
						a.damage = val;
					} else if(qName.equalsIgnoreCase("HEAL")){
						int val = Integer.parseInt(currentValue);
						a.heal = val;
					} else if(qName.equalsIgnoreCase("COST")){
						int val = Integer.parseInt(currentValue);
						a.cost = val;
					} else if(qName.equalsIgnoreCase("STRIKES")){
						int val = Integer.parseInt(currentValue);
						((MultiStrike)a).numStrikes = val;
					} else if(qName.equalsIgnoreCase("MIN")){
						int val = Integer.parseInt(currentValue);
						((RangeOfDamage)a).minDamage = val;
					} else if(qName.equalsIgnoreCase("SCALE")){
						int val = Integer.parseInt(currentValue);
						((RangeOfDamage)a).damageRange = val;
					}
					currentValue = "";
				}


				public void characters(char[] ch, int start, int length) {
					currentValue = new String(ch,start,length);
				}
			});

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return a;
	}
}

class Ability implements CombatAction {

	String name, flavor;
	boolean targeted;
	int damage,heal,cost;

	Ability(){
		this.name = "NAME MISSING";
		this.cost = 0;
		this.targeted = false;
		this.damage = 0;
		this.heal = 0;
	}

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

	public String info(){
		String text = "";

		text += name + "\n"
				+ "------\n"
				+ "Cost: " + cost + "\n"
				+ "Targeted: " + targeted + "\n"
				+ "Damage: " + damage + "\n"
				+ "Heal: " + heal + "\n"
				;

		return text;
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

	/**
	 * Create an ability that hits multiple times at the cost of low base damage and lower crit chance.
	 * @param name Name of the Ability
	 * @param baseDmg Base damage that each hit does
	 * @param numStrikes Number of times the ability hits
	 * @param cost Amount of mana it takes to use this ability
	 */
	MultiStrike(String name, int baseDmg, int numStrikes, int cost) {
		super(name, false, baseDmg, 0, cost);
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

class MagicAbility extends Ability {

	MagicAbility(){
		this.name = "NAME MISSING";
		this.cost = 0;
		this.targeted = false;
		this.damage = 0;
		this.heal = 0;
	}

	MagicAbility(String name, boolean targeted, int damage, int heal, int cost){
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
}

class Effect_Ability extends MagicAbility {

	Effect effect;

	Effect_Ability(String name, Effect effect, boolean targeted, int cost) {
		super(name, targeted, 0, 0, cost);
		this.effect = effect;
	}

	public void use(int str, int dex, int intel, Character caster, Character target){
		effect.use(caster, target);
	}
}

class RangeOfDamage extends MagicAbility {

	int minDamage, damageRange;

	RangeOfDamage(String name, int minDamage, int damageRange, int cost) {
		super(name, false, 0, 0, cost);
		this.minDamage = minDamage;
		this.damageRange = damageRange;
	}

	public void use(int str, int dex, int intel, Character caster, Character target) {
		if(target != null){
			flavor = "";

			int use_damage = minDamage;
			use_damage += (int) (intel/5); // Intelligence addition to hit
			double roll = Math.random();
			int percentPower = (int) (roll * 10);
			use_damage += (percentPower * damageRange)/10;
			flavor += " *b " + name + " * at *b " + percentPower + "0% * power hitting for *b *cINT " + use_damage + " * *c ";
			target.damage(use_damage);
		}
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