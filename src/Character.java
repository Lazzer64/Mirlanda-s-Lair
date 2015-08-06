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
	boolean dead = false;

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
		return new CombatAction[]{Abilities.hit};
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
		return 10 + (int)(this.strength * 2);
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
		dead = true;
		health = 0;
		mana = 0;
	}

	public void revive(double percentHealth){
		dead = false;
		heal((int)(max_health*percentHealth));
	}

	public void revive(int amntHealth){
		dead = false;
		heal(amntHealth);
	}

	public String getHpText() {
		if(!dead){
			return health + "/" + max_health;
		}
		return "DEAD";
	}
	
	public String getMpText() {
		if(mana > 0){
			return mana + "/" + max_mana;
		}
		return "Empty";
	}

	public String title(){
		return name + " the " + race + " " + profession + " (" + level + ")";
	}

	/**
	 * Finds the primary stat of the Character based on the profession.
	 * @return 
	 * 0: Balanced, 
	 * 1: Strength, 
	 * 2: Dexterity, 
	 * 3: Intelligence.
	 */
	public int getPrimaryStat(){
		return this.profession.primaryStat;
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