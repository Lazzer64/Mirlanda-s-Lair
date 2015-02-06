
public enum Status {

	none("None", Effects.none, 0),
	healing("Recovering", Effects.weak_heal, 2),
	burning("Burning", Effects.weak_fire, 3);

	String name;
	Effect effect;
	int turns;

	Status(String name, Effect effect, int turns){
		this.name = name;
		this.effect = effect;
		this.turns = turns;
	}

}
