package co.odsilvert.dsmz.util;

import org.bukkit.entity.Player;

/**
 * 
 * A data class used in bandage item for checking players and their associated ointments used
 *
 */
public class PlayerDataClass {
	
	private Player player;
	private boolean redOintment;
	private boolean greenOintment;
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public boolean getRedOintment() {
		return redOintment;
	}
	public void setRedOintment(boolean redOintment) {
		this.redOintment = redOintment;
	}
	public boolean getGreenOintment() {
		return greenOintment;
	}
	public void setGreenOintment(boolean greenOintment) {
		this.greenOintment = greenOintment;
	}
	
	public PlayerDataClass(Player player, boolean redOintment, boolean greenOintment) {
		this.player = player;
		this.redOintment = redOintment;
		this.greenOintment = greenOintment;
	}

}
