package me.ckhd.opengame.app.entity;

import org.hibernate.validator.constraints.Length;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * 游戏版本 Entity
 * @author wesley
 * @version 2015-06-26
 */
public class GameVersion extends DataEntity<GameVersion> {

	private static final long serialVersionUID = 1L;
	/**
	 * 游戏id
	 */
	private String gameId;
	/**
	 * 游戏version
	 */
	private String version;
	
	/**
	 * 游戏名称
	 */
	private String name;
	
	@Length(min=1, max=64, message="游戏ID长度必须介于 1 和 64 之间")
	public String getGameId() {
		return gameId;
	}
	
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	@Length(min=1, max=10, message="版本长度必须介于 1 和 10 之间")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	@Length(min=1, max=45, message="游戏名称长度必须介于 1 和 45 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
