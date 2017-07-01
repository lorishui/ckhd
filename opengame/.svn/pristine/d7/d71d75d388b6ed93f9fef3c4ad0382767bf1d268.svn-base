package me.ckhd.opengame.stats.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.stats.dao.GameReportDao;
import me.ckhd.opengame.stats.entity.GameReport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GameReportService extends CrudService<GameReportDao, GameReport> {

	@SuppressWarnings("unchecked")
	public GameReport findData(GameReport entity) throws IOException, ClassNotFoundException {

		GameReport gameReport = dao.findData(entity);
		
		ByteArrayInputStream x = new ByteArrayInputStream(gameReport.getData());
        ObjectInputStream oos = new ObjectInputStream(x);
        
        gameReport.setDataList((List<Map<String,Object>>)oos.readObject());
        return gameReport;
	}

	public int countTask(GameReport entity) {

		return dao.countTask(entity);
	}

	public void updateSucc(GameReport entity) throws IOException {

		ByteArrayOutputStream x = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(x);
        oos.writeObject(entity.getDataList());
        
        entity.setData(x.toByteArray());
		
		dao.updateSucc(entity);
	}

	public void updateFail(GameReport entity) {

		dao.updateFail(entity);
	}

}