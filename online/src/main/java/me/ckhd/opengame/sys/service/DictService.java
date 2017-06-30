package me.ckhd.opengame.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.sys.dao.DictDao;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.utils.DictUtils;

 
@Service
@Transactional(readOnly = true)
public class DictService extends CrudService<DictDao, Dict> {
	
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new Dict());
	}

	@Transactional(readOnly = false)
	public void save(Dict dict) {
		super.save(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	@Transactional(readOnly = false)
	public void delete(Dict dict) {
		super.delete(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	public List<Map<String,String>> getList(Dict dict){
		return dao.getList(dict);
	}

}
