import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Images {

	final static BufferedImage
	unknown = get("img/unknown_icon.png"),
	armor_icon = get("img/armor_icon.png"),
	bal_char = get("img/bal_character_icon.png"),
	consumable_icon = get("img/consumable_icon.png"),
	cross = get("img/cross.png"),
	dex_char = get("img/dex_character_icon.png"),
	dex_icon = get("img/dexterity_icon.png"),
	enemy_icon = get("img/enemy_icon.png"),
	fog = get("img/fog.png"),
	head_icon = get("img/head_icon.png"),
	heart_icon = get("img/heart_icon.png"),
	int_char = get("img/int_character_icon.png"),
	int_icon = get("img/inteligence_icon.png"),
	key_icon = get("img/key_icon.png"),
	legs_icon = get("img/legs_icon.png"),
	mana_icon = get("img/mana_icon.png"),
	map_icon = get("img/map_icon.png"),
	ring_icon = get("img/ring2_icon.png"),
	str_char = get("img/str_character_icon.png"),
	str_icon = get("img/strength_icon.png"),
	torso_icon = get("img/torso_icon.png"),
	weapon_icon = get("img/weapon_icon.png"),
	fade_vert = get("img/fade_vert2.png"),
	fade_hor = get("img/fade_hor2.png"),
	fade_diag = get("img/fade_diag.png"),
	hotkeys_default = get("img/default_hotkeys_info.png"),
	dungeon_splash = get("img/dungeonSplash.png")
	;

	static BufferedImage get(String loc){
		try {
			return ImageIO.read(new File(loc));
		} catch (IOException e) {
			System.err.println("Could not find \"" + loc + "\".");
			e.printStackTrace();
		}
		System.err.println("Could not find \"" + loc + "\".");
		return null;
	}

}
