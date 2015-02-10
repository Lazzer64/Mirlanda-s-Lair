
public class Party {

	static final int max_party_size = 3;

	Hero leader;
	Hero[] members;

	int size;

	public Party(Hero leader){
		this.leader = leader;
		members = new Hero[max_party_size];
		members[0] = leader;
		size = 1;
	}

	public boolean add(Hero h){
		if(size < max_party_size){
			members[size] = h;
			size++;
			return true;
		}
		return false;
	}

	public boolean remove(Hero h){
		//TODO
		return false;
	}

	public Hero get(int i){
		if(i < size){
			return members[i];
		}
		return null;
	}
	
	public String toString(){
		String text = "";
		for(int i = 0; i < size; i++){
			text += "|" + members[i].title();
		}
		return text;
	}
}
