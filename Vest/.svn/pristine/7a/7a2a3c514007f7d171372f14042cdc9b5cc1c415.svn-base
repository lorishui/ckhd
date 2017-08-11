package me.ckhd.opengame.common.persistence;

import me.ckhd.opengame.sys.entity.User;
import me.ckhd.opengame.sys.utils.UserUtils;

public class Verdict {
	public static boolean isAllow(String ckappId) {
		User user = UserUtils.getUser();
		if (user == null) {
			return false;
		}
		//单游戏权限
		if (ckappId != null && user.getNo() != null
				&& user.getNo().startsWith("game")) {
			String gameId = user.getNo().substring(4);
			if (!ckappId.equals(gameId)) {
				return false;
			}
		}
		return true;
	}
}
