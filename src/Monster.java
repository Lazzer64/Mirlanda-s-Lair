
public class Monster extends Character{

	Ability[] actions;
	LootTable loot;
	int exp;

	public Monster(String name, int strength, int dexterity, int intelligence, int level, Ability[] actions, LootTable loot) {
		super(name, strength, dexterity, intelligence);
		this.actions = actions;
		this.loot = loot;
		this.level = level;
		exp = level;

	}

	public Ability action(){
		// Randomly select an action from the list
		int selected = (int) (Math.random() * (actions.length));
		return actions[selected];
	}

	public int calculateHp(){
		return 5 + (int)(this.strength * 2);
	}
	
	public Item[] getLoot(){
		return new Item[]{loot.rollLoot()};
	}

	public int exp(){
		return exp;
	}

}

class Monsters{

//	static Monster skeleton(int scale){return new Monster("Skeleton",1*scale,0,0,1*scale,new CombatAction[]{Ability.whack},LootTable.skeleton);};
//	static Monster guard(int scale){return new Monster("Guard",3*scale,2*scale,0,1 + 2*scale,new CombatAction[]{Ability.slash},LootTable.gaurd);};
//	static Monster dragon(int scale){return new Monster("Dragon",14*scale,2*scale,10*scale,8 + 3*scale,new CombatAction[]{Magic_Ability.flare,Ability.slash},LootTable.dragon);};
//	static Monster spook(int scale){
//		Monster m = new Monster("Sp00ky Ghost", 50*scale,15*scale,50*scale,40*scale,new CombatAction[]{Magic_Ability.fireball,Ability.snipe}, LootTable.spook);
//		return m;
//		};
}
