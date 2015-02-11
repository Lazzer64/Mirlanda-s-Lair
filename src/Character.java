import java.util.ArrayList;

public class Character {

	String name;
	int max_health,max_mana,health,mana;
	int strength,dexterity,intelligence;
	int x,y;
	int level = 1;
	Race race;
	Profession profession;
	Inventory inventory;
	Status status = Status.none;
	int statusTurns = 0;


	/**
	 * Create a character with given Name, Strength, Dexterity, and Intelligence.
	 * @param name Character's name in a String
	 * @param strength Strength value of the character
	 * @param dexterity Dexterity value of the character
	 * @param intelligence Intelligence value of the character
	 */
	public Character(String name,int strength, int dexterity, int intelligence){

		this.name = name;
		this.strength = strength;
		this.dexterity = dexterity;
		this.intelligence = intelligence;
		this.max_health = calculateHp();
		this.health = max_health;
		this.max_mana = calculateMp();
		this.mana = max_mana;
		this.inventory = new Inventory();
		this.x = 0;
		this.y = 0;

	}

	/**
	 * Create a character with given Name, Strength, Dexterity, Intelligence, and inventory;
	 * @param name Character's name in a String
	 * @param strength Strength value of the character
	 * @param dexterity Dexterity value of the character
	 * @param intelligence Intelligence value of the character
	 * @param inventory Inventory to be cloned to the character
	 */
	public Character(String name,int strength, int dexterity, int intelligence,Inventory inventory){

		this.name = name;
		this.strength = strength;
		this.dexterity = dexterity;
		this.intelligence = intelligence;
		this.max_health = calculateHp();
		this.health = max_health;
		this.max_mana = calculateMp();
		this.mana = max_mana;
		this.inventory = (Inventory) inventory.clone();
		this.x = 0;
		this.y = 0;

	}

	/**
	 * Create a character given a name, profession, and race.
	 * @param name Character's name in a String 
	 * @param profession Profession of the character
	 * @param race Race of the character
	 */
	public Character(String name, Race race, Profession profession){

		this.name = name;
		this.profession = profession;
		this.race = race;
		this.strength = profession.strength;
		this.dexterity = profession.dexterity;
		this.intelligence = profession.intelligence;
		this.max_health = calculateHp();
		this.health = max_health;
		this.max_mana = calculateMp();
		this.mana = max_mana;
		this.inventory = new Inventory();
		this.x = 0;
		this.y = 0;

	}

	public void move(int x, int y){

		if(this.x + x > 0 && this.x + x < Main.d.rooms.length && this.y + y > 0 && this.y + y < Main.d.rooms.length){
			if(!Main.d.rooms[this.y + y][this.x + x].isType(Room.RoomType.no_room)){
				this.x += x;
				this.y += y;
			}
		}
	}

	public void setLocation(int x , int y){
		this.x = x;
		this.y = y;
	}

	public Room getCurrentRoom(){
		return Main.d.rooms[y][x];
	}

	public void give(Item i){
		if(inventory.containsKey(i)){
			inventory.put(i, inventory.get(i)+1);
		} else {
			inventory.put(i, 1);
		}
	}

	public void give(Item[] i){
		for(Item e: i){
			give(e);
		}
	}

	public void remove(Item i){
		if(inventory.get(i) > 1){
			inventory.put(i, inventory.get(i) - 1);
		} else {
			inventory.remove(i);
		}
	}

	public CombatAction[] getCombatActions(){
		//TODO abilities for non heroes
		return new CombatAction[]{Ability.hit};
	}

	public String useCombatAction(CombatAction ability, Character target){
		if(mana >= ability.getCost()){
			manaBurn(ability.getCost());
			ability.use(this,target);
			return ability.getFlavorText();
		}
		return "No mana";
	}

	public int calculateHp(){
		return 15 + (int)(this.strength * 2);
	}

	public int calculateMp(){
		return this.intelligence * 2;
	}

	public void damage(int damage){
		this.health -= damage;		
		if(this.health <= 0){
			death();
		}
	}

	public void heal(int heal){
		this.health += heal;
		if(this.health > this.max_health){
			this.health = max_health;
		}

	}

	public void manaHeal(int heal){
		this.mana += heal;
		if(this.mana> this.max_mana){
			this.mana = max_mana;
		}
	}

	public void manaBurn(int burn){
		this.mana -= burn;
	}

	public void setStatus(Status status){
		this.status = status;
		this.statusTurns = status.turns;
	}
	
	public void tickStatus(){
		if(statusTurns > 0){
			statusTurns--;
		} else {
			status = Status.none;
		}
	}
	
	public void use(Item i){
		i.use(this);
	}

	public void death(){
		//System.out.println("RIP in peace " + name + ".");
	}

	public String title(){
		return name + " the " + race + " " + profession + " (" + level + ")";
	}

	public String toString(){
		String ret = "";

		ret += title() + ":\n";
		ret += "Hp: " + health + "/" + max_health + "\n";
		ret += "Mp: " + mana + "/" + max_mana + "\n";
		ret += "----\n";
		ret += "Str: " + strength + "\n";
		ret += "Dex: " + dexterity + "\n";
		ret += "Int: " + intelligence + "\n";
		ret += "----\n";
		ret += inventory;

		return ret;
	}

}

class Hero extends Character{

	Equipment weapon;
	Head head = Head.none;
	Torso torso = Torso.cloth;
	Jewelry jewelry = Jewelry.none;
	Legs legs = Legs.cloth;
	int exp = 0;
	CombatAction[] defaultActions = {Magic_Ability.weak_heal, CombatAction.bag, CombatAction.run};


	public Hero(String name, int strength, int dexterity, int intelligence) {
		super(name, strength, dexterity, intelligence);
	}

	public Hero(String name, int strength, int dexterity, int intelligence,
			Inventory inventory) {
		super(name, strength, dexterity, intelligence, inventory);
	}

	public Hero(String name, Race race, Profession profession) {
		super(name,race,profession);
	}
	
	public String useCombatAction(CombatAction ability, Character target){
		if(mana >= ability.getCost()){
			manaBurn(ability.getCost());
			weapon.cast(ability,this,target);
			return weapon.getActionFlavor();
		}
		return "No mana";
	}

	public void move(int x, int y){

		if(this.x + x > 0 && this.x + x < Main.d.rooms.length && this.y + y > 0 && this.y + y < Main.d.rooms.length){
			if(!Main.d.rooms[this.y + y][this.x + x].isType(Room.RoomType.no_room)){
				this.x += x;
				this.y += y;
				Room.encounter(this.getCurrentRoom().type);
			}
		}
		revealRooms();
	}

	public void grantExp(int amount){
		exp += amount;
		checkLevelUp();
	}

	public void checkLevelUp(){

		if(exp > 85 && level < 9){
			levelUp(9);
		} else if(exp > 64 && level < 8){
			levelUp(8);
		} else if(exp > 46 && level < 7){
			levelUp(7);
		} else if(exp > 32 && level < 6){
			levelUp(6);
		} else if(exp > 20 && level < 5){
			levelUp(5);
		} else if(exp > 12 && level < 4){
			levelUp(4);
		} else if(exp > 6 && level < 3){
			levelUp(3);				
		} else if(exp > 2 && level < 2){
			levelUp(2);
		} 

	}

	public void levelUp(int level){
		// Level up rewards
		switch(level){
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
		case 9:
			break;
		}
		this.level = level;
		this.strength = this.profession.strength + (level * 1);
		this.dexterity = this.profession.dexterity + (level * 1);
		this.intelligence = this.profession.intelligence + (level * 1);
		this.max_health = this.calculateHp();
		this.health = max_health;
		this.max_mana = this.calculateMp();
		this.mana = max_mana;
	}

	public void setWeapon(Equipment w){
		this.weapon = w;
	}

	public void setHead(Head w){
		this.head = w;
	}

	public void setTorso(Torso w){
		this.torso = w;
	}

	public void setJewelry(Jewelry w){
		this.jewelry = w;
	}

	public void setLegs(Legs w){
		this.legs = w;
	}	

	public CombatAction[] getCombatActions(){

		ArrayList<CombatAction> act = new ArrayList<CombatAction>();

		for(CombatAction a: weapon.abilities){act.add(a);}
		for(CombatAction a: profession.abilities){act.add(a);}
		for(CombatAction a: defaultActions){act.add(a);}

		CombatAction[] acts = new CombatAction[act.size()];
		for(int i = 0; i < act.size(); i++){
			acts[i] = act.get(i);
		}

		return acts;
	}

	public void revealRooms(){
		Main.d.rooms[this.y][this.x].viewable = Room.Viewable.seen;
		switch(race){
		case dark_elf: // Dark elves can see 3 units away from them.
			for(Room r: Main.d.getSurrounding(this.x, this.y, 3)){changeRoomState(r);}
			break;
		case elf:// Elves can see the contents of adjacent rooms.
			for(Room r: Main.d.getSurrounding(this.x, this.y, 2)){changeRoomState(r);}
			for(Room r: Main.d.getSurrounding(this.x, this.y)){r.viewable = Room.Viewable.seen;} 
			break;
		case dwarf: // Dwarfs see the entire floor but cannot see or remember where they have been.
			Main.d.setAllRoomsViewable(Room.Viewable.peeked);
			break;
		case goblin:
			for(Room[] y: Main.d.rooms){
				for(Room r: y){
					if(!r.isType(Room.RoomType.room) && !r.isType(Room.RoomType.horizontal_hidden) && !r.isType(Room.RoomType.vertical_hidden) && !r.isType(Room.RoomType.no_room)){
						r.viewable = Room.Viewable.unknown;
					}
				}

			}
			for(Room r: Main.d.getSurrounding(this.x, this.y, 1)){changeRoomState(r);}
			break;
		case troll: // Troll has a circular fog around the character
			for(Room r: Main.d.getSurrounding(this.x, this.y, 2)){changeRoomState(r);}
			Main.dp.fogEnabled = true;
			break;
		default: // Other races see 2 units ahead
			for(Room r: Main.d.getSurrounding(this.x, this.y, 2)){changeRoomState(r);} 
		}

	}

	public void changeRoomState(Room r){
		switch(r.viewable){
		case seen:
			break;
		case hidden:
			r.viewable = Room.Viewable.peeked;
			break;
		case peeked:
			break;
		case unknown:
			break;
		default:
			break;
		}
	}
}