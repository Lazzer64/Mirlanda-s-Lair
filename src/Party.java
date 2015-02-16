import java.util.Iterator;


public class Party implements Iterable<Hero>{

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
		String text = "\nParty-------------------------";
		for(Hero i: this){
			text += "\n" + i.title();
		}
		text += "\n------------------------------";
		return text;
	}

	public Iterator<Hero> iterator() {
		return new PartyIterator(this);
	}

}

class PartyIterator implements Iterator<Hero> {

	Party party;
	int current;

	public PartyIterator(Party party){
		this.party = party;
		this.current = -1;
	}

	public boolean hasNext() {
		return (current < party.size - 1);
	}

	public Hero next() {
		current ++;
		return party.get(current);
	}


}

