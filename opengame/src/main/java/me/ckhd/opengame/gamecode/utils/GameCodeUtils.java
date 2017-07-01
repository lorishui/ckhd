package me.ckhd.opengame.gamecode.utils;

import java.util.ArrayList;

import me.ckhd.opengame.gamecode.dao.GameCodeDao;
import me.ckhd.opengame.gamecode.entity.GameCode;

public class GameCodeUtils {
	
	private static ArrayList<GameCode> list = new ArrayList<GameCode>();
	
	public static GameCode getCode(GameCodeDao gameCodeDao){
		synchronized ("a") {
			if(list.size() == 0){
				list = gameCodeDao.getCodes();
			}
			GameCode gameCode = list.get(0);
			list.remove(0);
			return gameCode;
		}
	}
}
