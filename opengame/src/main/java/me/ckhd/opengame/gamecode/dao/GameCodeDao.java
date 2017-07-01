package me.ckhd.opengame.gamecode.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.gamecode.entity.GameCode;
import me.ckhd.opengame.gamecode.entity.GameCodeLog;
import me.ckhd.opengame.gamecode.entity.GameCodeVer;

import org.apache.ibatis.annotations.Param;
import org.apache.tools.ant.types.resources.selectors.Date;
import org.springframework.stereotype.Repository;

@Repository
public interface GameCodeDao extends CrudDao<GameCode> {

	GameCode findGameCodeByCodeAndCkAppId(@Param("code")String code,@Param("ckAppId")String ckAppId);

	List<Map<String, String>> findAllCkApp();

	ArrayList<GameCode> getCodes();

	void log(GameCodeLog gameCodelog);

	GameCodeLog checkPhoneNum(GameCodeLog gcl);

	GameCode getCode(@Param("randomInt")Integer randomInt, @Param("ckAppId")String ckAppId);

	int saveGameCodeVer(GameCodeVer gcv);

	List<GameCodeVer> findGCVList(GameCodeVer gcv);

	void updateGCV(GameCodeVer gcv);

	List<Object> getUsedNum(@Param("ckAppId")String ckAppId);
	
	
}
