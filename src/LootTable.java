import java.util.Hashtable;

public class LootTable extends Hashtable<Item, Double>{

	static final LootTable
	treasure = new LootTable() {{
		put(Consumable.red_potion, .60);
		put(Consumable.blue_potion, .35);
		put(Equipment.stick, .05);
	}},
	
	skeleton = new LootTable() {{
		put(Consumable.red_potion, .70);
		put(Equipment.bone, .30);
	}},

	gaurd = new LootTable() {{
		put(Consumable.red_potion, .60);
		put(Consumable.blue_potion, .40);
	}},

	dragon = new LootTable() {{
		put(Jewelry.burn_ring, .50);
		put(Jewelry.heal_ring, .50);
	}};

	
	public Item rollLoot(){
		double roll = Math.random();
		return getLootAt(roll);
	}

	private Item getLootAt(Double roll){
		double sum = 0;
		for(Item i: keySet()){
			sum += this.get(i);
			if(roll <= sum){
				return i;
			}
		}
		return null;
	}

}
