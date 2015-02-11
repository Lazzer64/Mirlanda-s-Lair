import java.io.File;

public interface Item {

	final static Item none = new NoItem();

	void use(Character target);
	File getIcon();
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

}

enum Consumable implements Item{

	red_potion("Red flask",7,0),
	blue_potion("Blue flask",0,6),
	green_potion("Green flask",5,4),
	orange_potion("Orange flask",7,6);

	String name;
	int hp_heal,mp_heal;
	static File icon = new File("img/consumable_icon.png");

	Consumable(String name, int hp_heal, int mp_heal){
		this.name = name;
		this.hp_heal = hp_heal;
		this.mp_heal = mp_heal;
	}

	public void use(Character target) {
		target.heal(this.hp_heal);
		target.manaHeal(this.mp_heal);
		target.remove(this);
	}

	public File getIcon(){
		return icon;
	}

	public String toString(){
		return name;
	}

}

class Equipment implements Item {
	static Equipment
	stick = new Equipment("Stick", new CombatAction[]{Ability.hit}),
	bone = new Equipment("Bone", new CombatAction[]{Ability.whack}),
	weak_sword = new Equipment("Feeble sword", new CombatAction[]{Ability.slash,Ability.smash}),
	weak_bow = new Equipment("Flimsy bow", new CombatAction[]{Ability.pierce, Ability.snipe}),
	weak_staff = new Equipment("Frail staff", new CombatAction[]{Ability.hit, Magic_Ability.spark, Magic_Ability.lightning}),
	weak_dagger = new Equipment("Paltry Dagger", new CombatAction[]{Ability.slash, Ability.backstab}),
	weak_tome = new Equipment("Fragile Tome", new CombatAction[]{Ability.hit, Magic_Ability.flare, Magic_Ability.fireball}),
	legendary_staff = new LegendaryStaff()
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
}

class LegendaryStaff extends Equipment {

	String actFlavor = "";
	
	public LegendaryStaff(){
		super("Legendary Staff",new CombatAction[]{Ability.hit,Magic_Ability.spark, Magic_Ability.fireball});
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

}