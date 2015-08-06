import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

class Equipment implements Item {
	final static Equipment
	testItem = new Equipment("Test Weapon", new CombatAction[]{Abilities.inferno}),
	// Useless 
	stick = new Equipment("Stick", new CombatAction[]{Abilities.hit}),
	bone = new Equipment("Bone", new CombatAction[]{Abilities.whack}),
	// Starter 
	weak_sword = new Equipment("Feeble sword", new CombatAction[]{Abilities.slash,Abilities.smash}),
	weak_bow = new Equipment("Flimsy bow", new CombatAction[]{Abilities.pierce, Abilities.snipe}),
	weak_staff = new Equipment("Frail staff", new CombatAction[]{Abilities.hit, Abilities.spark, Abilities.lightning}),
	weak_dagger = new Equipment("Paltry Dagger", new CombatAction[]{Abilities.slash, Abilities.backstab}),
	weak_tome = new Equipment("Fragile Tome", new CombatAction[]{Abilities.hit, Abilities.flare, Abilities.fireball}),
	// Medium TODO
	// Advanced TODO
	// Legendary
	double_cast_staff = new DoubleCastStaff(),
	fire_bow = new FireBow(),
	crit_sword = new CritSword()
	;

	String name;
	CombatAction[] abilities;
	static BufferedImage icon = Images.weapon_icon;

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

	public BufferedImage getIcon() {
		return icon;
	}

	public String toString(){
		return name;
	}
	
	public String getDescription(){
		return name;
		//TODO
	}

	public void drawInfoPanel(Graphics g){
		GamePanel info = new GamePanel(){
			int x = 0, y = 0, size = 200;
			public void paint(Graphics g){
				g.setColor(Color.WHITE);
				g.fillRect(x, y, size, size);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, size, size);
				g.drawImage(icon, x + 3, y + 4, null);
				String text = name + " \n ";
				for(CombatAction a: abilities){
					text += a.toString() + " \n ";
				}
				wrapedText(text, 16, y, size - 16, g);
			}
		};
		
		info.paint(g);
		
	}
	
}

class DoubleCastStaff extends Equipment {

	String actFlavor = "";
	
	public DoubleCastStaff(){
		super("Mirror Staff",new CombatAction[]{Abilities.hit,Abilities.spark, Abilities.fireball});
	}

	public void cast(CombatAction action, Character caster, Character target){
		if(action.getClass() != MagicAbility.class){
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
		super("Wings of the Phoenix", new CombatAction[]{Abilities.pierce, Abilities.snipe});
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
		super("Blade of Fortuna", new CombatAction[]{Abilities.slash, Abilities.smash, Abilities.bladeFlurry});
	}
	
	public void cast(CombatAction action, Character caster, Character target){
		action.use(caster.strength, caster.dexterity * multiplier, caster.intelligence, caster, target);
		actFlavor = action.getFlavorText();
	}

	public String getActionFlavor(){
		return actFlavor;
	}
	public String getDescription(){
		return "A Legnedary Sword that increases the critical strike chance and power of the user.";
	}
	
}