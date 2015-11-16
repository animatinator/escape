// Constants.java

package escape;


class Constants {
	// Door direction constants
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	
	// Animation type constants
	public static final int ANIMATION_STATIONARY = 0;
	public static final int ANIMATION_WALKING = 1;
	
	// AI state constants
	public static final int AI_STATE_CALM = 0;
	public static final int AI_STATE_ATTACKING = 1;
	
	// Enemy ID constants
	public static final int ENEMY_ZOMBIE = 100;
	public static final int ENEMY_CHASER = 110;
	
	// Enemy image locations
	public static final String ZOMBIE_IMAGEFILE = "zombie.png";
	public static final String CHASER_IMAGEFILE = "zombie.png";
	
	// Effects constants
		// Flash effect
	public static final String FLASH_SMALL = "flashSmall.png";
	public static final String FLASH_MEDIUM = "flashMedium.png";
	public static final String FLASH_LARGE = "flashLarge.png";
	public static final int FLASH_DURATION_GUNFIRE = 3;
		// Line effect
	public static final float LINE_COLOUR_R = 1.0f;
	public static final float LINE_COLOUR_G = 0.8f;
	public static final float LINE_COLOUR_B = 0.7f;
	public static final int LINE_DURATION_GUNFIRE = 5;
	
	// Texture ID constants
		// General ones
	public static final int TEX_INDEX_BLANK = 0;
	public static final int TEX_INDEX_WALL = 1;
	public static final int TEX_INDEX_DOOR_H_O = 2;
	public static final int TEX_INDEX_DOOR_H_C = 3;
	public static final int TEX_INDEX_DOOR_V_O = 4;
	public static final int TEX_INDEX_DOOR_V_C = 5;
	public static final int TEX_INDEX_GENERICCONTAINER = 6;
		// Weapons
	public static final int TEX_INDEX_SWORD = 80;
	public static final int TEX_INDEX_PISTOL = 81;
	public static final int TEX_INDEX_SHOTGUN = 82;
	public static final int TEX_INDEX_MACHINEGUN = 83;
	public static final int TEX_INDEX_ROCKETLAUNCHER = 84;
		// Upgrades
	public static final int TEX_INDEX_LANTERN = 90;
	public static final int TEX_INDEX_BODYARMOUR = 91;
	public static final int TEX_INDEX_RUNNINGSHOES = 92;
	public static final int TEX_INDEX_FISTCHAINS = 93;
		// Floor textures run from 100-255
	public static final int TEX_INDEX_FLOOR_BASE = 100;
	
	// Item ID and data constants
		// Itentifier constants
	public static final int ID_KEY = 0;
	public static final int ID_UPGRADE = 1;
	public static final int ID_WEAPON = 2;
	public static final int ID_SUPPLY = 3;
	
		// Weapons
			// Sword
	public static final int ID_WEAPON_SWORD = 1;
	public static final String SWORD_NAME = "Sword";
	public static final String SWORD_DESCRIPTION = "A slightly rusted sword.";
	public static final double SWORD_DAMAGE = 1.5;
	public static final double SWORD_RANGE = 50.0;
	public static final int SWORD_REPEATDELAY = 20;
	public static final int SWORD_BASEAMMO = -1;
			// Pistol
	public static final int ID_WEAPON_PISTOL = 2;
	public static final String PISTOL_NAME = "Pistol";
	public static final String PISTOL_DESCRIPTION = "A standard-issue, run-of-the-mill pistol.";
	public static final double PISTOL_DAMAGE = 2.5;
	public static final double PISTOL_RANGE = 640.0;
	public static final int PISTOL_REPEATDELAY = 20;
	public static final int PISTOL_BASEAMMO = 15;
			// Shotgun
	public static final int ID_WEAPON_SHOTGUN = 3;
	public static final String SHOTGUN_NAME = "Double-barrelled shotgun";
	public static final String SHOTGUN_DESCRIPTION = "An old wooden shotgun.";
	public static final double SHOTGUN_DAMAGE = 7.0;
	public static final double SHOTGUN_RANGE = 192.0;
	public static final int SHOTGUN_REPEATDELAY = 40;
	public static final int SHOTGUN_BASEAMMO = 7;
			// Machine gun
	public static final int ID_WEAPON_MACHINEGUN = 4;
	public static final String MACHINEGUN_NAME = "Machine gun";
	public static final String MACHINEGUN_DESCRIPTION = "A lightweight machine gun.";
	public static final double MACHINEGUN_DAMAGE = 1.0;
	public static final double MACHINEGUN_RANGE = 600.0;
	public static final int MACHINEGUN_REPEATDELAY = 3;
	public static final int MACHINEGUN_BASEAMMO = 30;
			// Rocket launcher
	public static final int ID_WEAPON_ROCKETLAUNCHER = 5;
	public static final String ROCKETLAUNCHER_NAME = "Rocket launcher";
	public static final String ROCKETLAUNCHER_DESCRIPTION = "A large rocket launcher. It's incredibly heavy.";
	public static final double ROCKETLAUNCHER_DAMAGE = 15.0;
	public static final double ROCKETLAUNCHER_RANGE = 640.0;
	public static final int ROCKETLAUNCHER_REPEATDELAY = 60;
	public static final int ROCKETLAUNCHER_BASEAMMO = 4;
	
		// Upgrades
			// Lantern
	public static final int ID_UPGRADE_LANTERN = 17;
	public static final String LANTERN_NAME = "Lantern";
	public static final String LANTERN_DESCRIPTION = "A shiny golden lantern.";
		// Body armour
	public static final int ID_UPGRADE_BODYARMOUR = 18;
	public static final String BODYARMOUR_NAME = "Body armour";
	public static final String BODYARMOUR_DESCRIPTION = "Some strong-looking body armour. This should help protect against enemies' attacks.";
	public static final double BODYARMOUR_UPGRADE_VALUE = 2.5;
		// Running shoes
	public static final int ID_UPGRADE_RUNNINGSHOES = 19;
	public static final String RUNNINGSHOES_NAME = "Running shoes";
	public static final String RUNNINGSHOES_DESCRIPTION = "Some brand-new running shoes. These will be a lot easier to run in.";
	public static final int RUNNINGSHOES_UPGRADE_VALUE = 10;
		// Fist chains
	public static final int ID_UPGRADE_FISTCHAINS = 20;
	public static final String FISTCHAINS_NAME = "Fist chains";
	public static final String FISTCHAINS_DESCRIPTION = "Wrap them around your fists and you'll be able to inflict significant damage just by punching enemies.";
	
		// Supplies
	public static final int ID_SUPPLY_BANDAGES = 161;
	public static final int ID_SUPPLY_PAINKILLERS = 162;
	public static final int ID_SUPPLY_FIRSTAIDKIT = 163;
	public static final int ID_SUPPLY_PISTOLAMMO_15 = 164;
	public static final int ID_SUPPLY_PISTOLAMMO_30 = 165;
	public static final int ID_SUPPLY_SHOTGUNAMMO_7 = 166;
	public static final int ID_SUPPLY_SHOTGUNAMMO_14 = 167;
	public static final int ID_SUPPLY_SHOTGUNAMMO_21 = 168;
	public static final int ID_SUPPLY_MACHINEGUNAMMO_30 = 169;
	public static final int ID_SUPPLY_MACHINEGUNAMMO_60 = 170;
	public static final int ID_SUPPLY_MACHINEGUNAMMO_90 = 171;
	public static final int ID_SUPPLY_ROCKETLAUNCHEDAMMO_4 = 172;
}