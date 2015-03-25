import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

class Equipment implements Item {
	static Equipment
	weak_sword = new Equipment("Feeble sword", new Ability[]{Ability.Jab, Ability.Slash}),
	weak_bow = new Equipment("Flimsy bow", new Ability[]{Ability.Arrow}),
	weak_staff = new Equipment("Frail staff", new Ability[]{Ability.FireBall})

	;

	String name;
	Ability[] abilities;
	static BufferedImage icon = Images.weapon_icon;

	String flavor = "";

	public Equipment(String name, Ability[] abilities){
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

	public void cast(Ability action, Character caster, Character target){
		
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
	
}
