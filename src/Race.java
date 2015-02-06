
public enum Race {
	human("Human"),
	dwarf("Dwarf"),
	elf("Elf"),
	dark_elf("Dark Elf"),
	goblin("Goblin"),
	troll("Troll");
	
	String title;
	
	private Race(String title){
		this.title = title;
	}
	
	public String toString(){
		return title;
	}
}
