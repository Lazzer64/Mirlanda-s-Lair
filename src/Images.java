import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {

	final static BufferedImage
	unknown = get("unknown_icon.png"),
	armor_icon = get("armor_icon.png"),
	bal_char = get("bal_character_icon.png"),
	consumable_icon = get("consumable_icon.png"),
	cross = get("cross.png"),
	dex_char = get("dex_character_icon.png"),
	dex_icon = get("dexterity_icon.png"),
	enemy_icon = get("enemy_icon.png"),
	fog = get("fog.png"),
	head_icon = get("head_icon.png"),
	heart_icon = get("heart_icon.png"),
	int_char = get("int_character_icon.png"),
	int_icon = get("inteligence_icon.png"),
	key_icon = get("key_icon.png"),
	legs_icon = get("legs_icon.png"),
	mana_icon = get("mana_icon.png"),
	map_icon = get("map_icon.png"),
	ring_icon = get("ring2_icon.png"),
	str_char = get("str_character_icon.png"),
	str_icon = get("strength_icon.png"),
	torso_icon = get("torso_icon.png"),
	weapon_icon = get("weapon_icon.png"),
	fade_vert = get("fade_vert2.png"),
	fade_hor = get("fade_hor2.png"),
	fade_diag = get("fade_diag.png"),
	hotkeys_default = get("default_hotkeys_info.png"),
	dungeon_splash = get("dungeonSplash.png")
	;

	static BufferedImage get(String loc){
		System.out.println("Loading " + loc + "... ");
		try {
			return ImageIO.read(new File("img/" + loc));
		} catch (IOException e) {
			System.err.println("Could not find \"" + loc + "\".");
			e.printStackTrace();
		}
		System.err.println("Could not find \"" + loc + "\".");
		return null;
	}

}