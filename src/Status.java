
public enum Status {

	none("None", Effect.none, 0),
	healing("Recovering", Effect.weak_heal, 2),
	burning("Burning", Effect.weak_fire, 3);

	String name;
	Effect effect;
	int turns;

	Status(String name, Effect effect, int turns){
		this.name = name;
		this.effect = effect;
		this.turns = turns;
	}

}
