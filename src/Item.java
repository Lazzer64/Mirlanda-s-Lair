import java.io.File;

public interface Item {

	final static Item none = new NoItem();

	void use(Character target);
	File getIcon();
	String getDescription();
	// TODO getFlavorName()

}

class NoItem implements Item {

	public void use(Character target) {

	}

	public File getIcon() {
		return new File("img/unknown_icon.png");
	}

	public String toString(){
		return "";
	}
	
	public String getDescription(){
		return "Nothing";
	}

}

enum Consumable implements Item{

	red_potion("Red flask",7,0),
	blue_potion("Blue flask",0,6),
	green_potion("Green flask",5,4),
	orange_potion("Orange flask",7,6)
	;

	static Item
	revive_potion = new RevivePotion();

	String name;
	int hp_heal,mp_heal;
	static File icon = new File("img/consumable_icon.png");

	Consumable(String name, int hp_heal, int mp_heal){
		this.name = name;
		this.hp_heal = hp_heal;
		this.mp_heal = mp_heal;
	}

	public void use(Character target) {
		if(!target.dead){
			target.heal(this.hp_heal);
			target.manaHeal(this.mp_heal);
			target.remove(this);
		}
	}

	public File getIcon(){
		return icon;
	}

	public String toString(){
		return name;
	}

	public String getDescription(){
		if(hp_heal > 0 && mp_heal > 0){
			return "Heals target's health and mana.";
		} else if(hp_heal > 0){
			return "Heals target's health";
		} else {
			return "Heals target's mana";
		}

	}

}

class RevivePotion implements Item {

	double percentHealth = .50;

	public void use(Character target) {
		target.revive(percentHealth);
		target.remove(this);
	}

	public File getIcon() {
		return new File("img/consumable_icon.png");
	}

	public String toString(){
		return "Talisman of the Immortal";
	}

	public String getDescription(){
		return "Revives dead target in a weakend state.";
	}

}

enum Head implements Item{
	none("None"),
	cloth("Cloth Helmet");

	String name;
	static File icon = new File("img/head_icon.png");	

	private Head(String name){
		this.name = name;
	}

	@Override
	public void use(Character target) {
		if(target.getClass().equals(Hero.class)){
			if(((Hero)target).head != Head.none){
				((Hero) target).give(((Hero) target).head);
			}
			((Hero) target).setHead(this);
			((Hero) target).remove(this);
		}
	}


	@Override
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

enum Torso implements Item{
	none("None"),
	cloth("Cloth Shirt");

	String name;
	static File icon = new File("img/torso_icon.png");	

	private Torso(String name){
		this.name = name;
	}

	@Override
	public void use(Character target) {
		if(target.getClass().equals(Hero.class)){
			if(((Hero)target).torso != Torso.none){
				((Hero) target).give(((Hero) target).torso);
			}
			((Hero) target).setTorso(this);
			((Hero) target).remove(this);
		}
	}


	@Override
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

enum Legs implements Item{
	none("None"),
	cloth("Cloth Pants");

	String name;
	static File icon = new File("img/legs_icon.png");	

	private Legs(String name){
		this.name = name;
	}

	@Override
	public void use(Character target) {
		if(target.getClass().equals(Hero.class)){
			if(((Hero)target).legs != Legs.none){
				((Hero) target).give(((Hero) target).legs);
			}
			((Hero) target).setLegs(this);
			((Hero) target).remove(this);
		}
	}


	@Override
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

enum Jewelry implements Item{
	none("None", Effect.none),
	copper_ring("Copper Ring", Effect.none),
	heal_ring("Lesser Ring of Rejuvenation", Effect.weak_heal),
	fire_ring("Lesser Ring of Flame", Effect.weak_fire),
	mana_amulet("Lesser Amulet of Spirit", Effect.weak_mana);

	String name;
	Effect effect;
	static File icon = new File("img/ring2_icon.png");	

	private Jewelry(String name, Effect effect){
		this.name = name;
		this.effect = effect;
	}

	@Override
	public void use(Character target) {
		if(target.getClass().equals(Hero.class)){
			if(((Hero)target).jewelry != Jewelry.none){
				((Hero) target).give(((Hero) target).jewelry);
			}
			((Hero) target).setJewelry(this);
			((Hero) target).remove(this);
		}
	}


	@Override
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

enum Map implements Item{

	full_map("Dungeon Map",Room.Viewable.seen),
	half_map("Worn Map",Room.Viewable.peeked);

	String name;
	Room.Viewable view;

	Map(String n, Room.Viewable v){
		name = n;
		view = v;
	}

	File icon = new File("img/map_icon.png");

	public void use(Character target) {
		Main.d.setNotSeenRoomsViewable(view);
		target.remove(this);
	}

	public File getIcon() {
		return icon;
	}

	public String toString(){
		return name;
	}

	public String getDescription(){
		return "Reveals a portion of the map";
	}

}