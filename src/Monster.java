
public class Monster extends Character{

	CombatAction[] actions;
	LootTable loot;

	public Monster(String name, int strength, int dexterity, int intelligence, CombatAction[] actions, LootTable loot) {
		super(name, strength, dexterity, intelligence);
		this.actions = actions;
		this.loot = loot;
	}

	public CombatAction action(){
		// Randomly select an action from the list
		int selected = (int) (Math.random() * (actions.length));
		return actions[selected];
	}

	public Item[] getLoot(){
		return new Item[]{loot.rollLoot()};
	}

	public int exp(){
		return level;
	}

}

class Monsters{

	static Monster skeleton(){return new Monster("Skeleton",1,0,0,new CombatAction[]{Ability.whack},LootTable.skeleton);};
	static Monster guard(){return new Monster("Guard",3,2,0,new CombatAction[]{Ability.slash},LootTable.gaurd);};
	static Monster dragon(){return new Monster("Dragon",14,2,10,new CombatAction[]{Magic_Ability.flare,Ability.slash},LootTable.dragon);};
	
}

