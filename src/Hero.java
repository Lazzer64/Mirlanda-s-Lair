import java.util.ArrayList;

public class Hero extends Character{

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
		this.setWeapon(profession.weapon);
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