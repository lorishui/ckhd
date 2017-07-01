package me.ckhd.opengame.app.service;

import java.util.List;

import me.ckhd.opengame.app.dao.APPCkDao;
import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.dao.GameVersionDao;
import me.ckhd.opengame.app.dao.PayCodeConfigDao;
import me.ckhd.opengame.app.dao.PayInfoConfigDao;
import me.ckhd.opengame.app.dao.PayRulesConfigDao;
import me.ckhd.opengame.app.dao.PayRulesDao;
import me.ckhd.opengame.app.dao.ProvinceDao;
import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.entity.GameVersion;
import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.entity.PayRules;
import me.ckhd.opengame.app.entity.PayRulesConfig;
import me.ckhd.opengame.app.entity.Province;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.service.BaseService;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.sys.dao.DictDao;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * APP管理 包括 app、游戏、省份、渠道、开发商等
 * 
 * @author wesley
 * @version 2015-06-25
 */
@Service
@Transactional(readOnly = true)
public class AppService extends BaseService implements InitializingBean {

	@Autowired
	private ProvinceDao provinceDao;
	@Autowired
	private GameVersionDao gameVersionDao;
	@Autowired
	private APPCkDao aPPCkDao;

	@Autowired
	private PayRulesDao payRulesDao;

	@Autowired
	private PayRulesConfigDao payRulesConfigDao;

	@Autowired
	private DictDao dictDao;

	@Autowired
	private PayCodeConfigDao payCodeConfigDao;

	@Autowired
	private PayInfoConfigDao payInfoConfigDao;
	
	@Autowired
	private CfgparamDao cfgparamDao;
	// -- Province Service begin --//

	public Province getProvince(String id) {
		return provinceDao.get(id);
	}

	public Province getProvinceByCode(String code) {
		Province province = new Province();
		province.setCode(code);
		return provinceDao.getByCode(province);
	}

	public Province getProvinceByName(String name) {
		Province province = new Province();
		province.setName(name);
		return provinceDao.getByName(province);
	}

	public List<Province> findProvince(Province province) {
		return provinceDao.findList(province);
	}

	public List<Province> findAllProvince(Province province) {
		return provinceDao.findAllList(province);
	}

	public Page<Province> findProvince(Page<Province> page, Province province) {
		// 设置分页参数
		province.setPage(page);
		// 执行分页查询
		page.setList(provinceDao.findList(province));
		return page;
	}

	@Transactional(readOnly = false)
	public void saveProvince(Province province) {
		if (StringUtils.isBlank(province.getId())) {
			province.preInsert();
			provinceDao.insert(province);
		} else {
			// province.preUpdate();
			provinceDao.update(province);
		}
	}

	@Transactional(readOnly = false)
	public void deleteProvince(Province province) {
		provinceDao.delete(province);
	}

	// -- Province Service end --//

	// -- gameversion Service begin --//

	public GameVersion getGameVersion(String id) {
		return gameVersionDao.get(id);
	}

	public List<GameVersion> findAllGameVersion(GameVersion game) {
		return gameVersionDao.findAllList(game);
	}

	public Page<GameVersion> findGameVersion(Page<GameVersion> page,
			GameVersion gameVersion) {
		// 设置分页参数
		gameVersion.setPage(page);
		// 执行分页查询
		page.setList(gameVersionDao.findList(gameVersion));
		return page;
	}

	@Transactional(readOnly = false)
	public void saveGameVersion(GameVersion gameVersion) {
		if (StringUtils.isBlank(gameVersion.getId())) {
			gameVersion.preInsert();
			gameVersionDao.insert(gameVersion);
		} else {
			gameVersionDao.update(gameVersion);
		}
	}

	@Transactional(readOnly = false)
	public void deleteGameVersion(GameVersion gameVersion) {
		gameVersionDao.delete(gameVersion);
	}

	// -- gameversion Service end --//

	// -- APPCk Service start --//

	public APPCk getAPPCk(String id) {
		return aPPCkDao.get(id);
	}

	public APPCk getAPPCkByName(String name) {
		APPCk aPPCk = new APPCk();
		aPPCk.setName(name);
		return aPPCkDao.getByName(aPPCk);
	}

	public APPCk getAPPCkByAppId(String appid) {
		APPCk aPPCk = new APPCk();
		aPPCk.setCkappId(appid);
		return aPPCkDao.getByAppId(aPPCk);
	}

	public List<APPCk> findAllAPPCks(APPCk aPPCk) {
		return aPPCkDao.findAllList(aPPCk);
	}

	public Page<APPCk> findAPPCk(Page<APPCk> page, APPCk aPPCk) {
		aPPCk.setPage(page);
		page.setList(aPPCkDao.findList(aPPCk));
		return page;

	}

	@Transactional(readOnly = false)
	public void saveAPPCk(APPCk aPPCk) {
		if (StringUtils.isBlank(aPPCk.getId())) {
			aPPCk.preInsert();
			aPPCkDao.insert(aPPCk);
		} else {
			aPPCk.preUpdate();
			aPPCkDao.update(aPPCk);
		}
	}

	@Transactional(readOnly = false)
	public void deleteAPPCk(APPCk aPPCk) {
		aPPCkDao.delete(aPPCk);
	}

	// -- APPCk Service end --//

	public void afterPropertiesSet() throws Exception {
		// blank
	}

	/**
	 * 保存支付通道切换设置
	 * 
	 * @param payRules
	 */
	@Transactional(readOnly = false)
	public void savePayRules(PayRules payRules) {
		if (StringUtils.isBlank(payRules.getId())) {
			payRules.preInsert();
			payRulesDao.insert(payRules);
		} else {
			payRules.preUpdate();
			payRulesDao.update(payRules);
		}
	}

	public PayRules getPayRules(PayRules payRules){
		return payRulesDao.get(payRules);
	}
	
	/**
	 * 获取分页数据
	 * 
	 * @param page
	 * @param payRules
	 * @return
	 */
	public Page<PayRules> findPayRules(Page<PayRules> page, PayRules payRules) {
		// 设置分页参数
		payRules.setPage(page);
		// 执行分页查询
		page.setList(payRulesDao.findList(payRules));
		return page;
	}

	/**
	 * 根据开始时间和结束时间检查此时间段是否有相关数据
	 * 
	 * @param payRules
	 * @return
	 */
	public List<PayRules> checkPayRulesByTime(PayRules payRules) {
		List<PayRules> payRuleList = payRulesDao.checkByTime(payRules);
		if (payRuleList == null || payRuleList.size() <= 0) {
			return null;
		}
		return payRuleList;
	}

	/**
	 * 根据资料查询相关数据
	 * 
	 * @param payRules
	 * @return
	 */
	public List<PayRules> findPayRules(PayRules payRules) {
		return payRulesDao.findList(payRules);
	}

	/**
	 * 删除支付通道切换配置
	 * 
	 * @param payRules
	 */
	@Transactional(readOnly = false)
	public void deletePayRules(PayRules payRules) {
		payRulesDao.delete(payRules);
	}

	/**
	 * 根据资料查询相关数据
	 * 
	 * @param payRules
	 * @return
	 */
	public PayRules findPayRulesById(PayRules payRules) {
		return payRulesDao.get(payRules);
	}

	/**
	 * 获取payRules并且包含config数据
	 * 
	 * @param payRules
	 * @return
	 */
	public PayRules getRulesAndConfig(PayRules payRules) {
		return payRulesDao.getRulesAndConfig(payRules);
	}

	@Transactional(readOnly = false)
	public void saveConfig(PayRulesConfig config) {
		if (StringUtils.isBlank(config.getId())) {
			config.preInsert();
			payRulesConfigDao.insert(config);
		} else {
			config.preUpdate();
			payRulesConfigDao.update(config);
		}
	}

	/**
	 * 删除支付通道切换配置
	 * 
	 * @param payRules
	 */
	@Transactional(readOnly = false)
	public void deletePayRulesConfig(PayRulesConfig payRulesConfig) {
		payRulesConfigDao.delete(payRulesConfig);
	}

	/**
	 * 删除支付通道切换配置
	 * 
	 * @param payRules
	 */
	@Transactional(readOnly = false)
	public void saveIntenerPay(PayRules payRules) {
		payRulesDao.saveInternetPay(payRules);
	}

	public List<PayRules> getRulesAndConfigByTime(PayRules payRules) {
		return payRulesDao.getRulesAndConfigByTime(payRules);
	}

	public List<PayRules> getAllRulesAndConfigByTime(PayRules payRules) {
		return payRulesDao.getAllRulesAndConfigByTime(payRules);
	}

	public List<PayRulesConfig> getPayRulesConfig(PayRulesConfig payRulesConfig){
		return payRulesConfigDao.getPayRulesConfigByPayRulesId(payRulesConfig);
	}
	// TODO 网游计费点信息

	/**
	 * 获取计费点列表
	 * 
	 * @param page
	 * @param payInfoConfig
	 * @return
	 */
	public Page<PayCodeConfig> findPage(Page<PayCodeConfig> page,
			PayCodeConfig payCodeConfig) {
		payCodeConfig.setPage(page);
		page.setList(payCodeConfigDao.findList(payCodeConfig));
		return page;
	}

	/**
	 * 保存支付信息
	 * 
	 * @param payInfoConfig
	 */
	@Transactional(readOnly = false)
	public void savePayCodeConfig(PayCodeConfig payCodeConfig) {
		if (StringUtils.isBlank(payCodeConfig.getId())) {
			payCodeConfig.preInsert();
			payCodeConfigDao.insert(payCodeConfig);
		} else {
			payCodeConfig.preUpdate();
			payCodeConfigDao.update(payCodeConfig);
		}
	}

	/**
	 * 删除支付配置信息
	 * 
	 * @param payInfoConfig
	 */
	@Transactional(readOnly = false)
	public void delPayCodeConfig(PayCodeConfig payCodeConfig) {
		payCodeConfigDao.delete(payCodeConfig);
	}

	/**
	 * 删除支付配置信息
	 * 
	 * @param payInfoConfig
	 */
	@Transactional(readOnly = false)
	public PayCodeConfig getPayCodeConfig(PayCodeConfig payCodeConfig) {
		return payCodeConfigDao.get(payCodeConfig);
	}

	/**
	 * 检测唯一
	 * @param payCodeConfig
	 * @return
	 */
	public int checkPayCodeOnly(PayCodeConfig payCodeConfig) {
		return payCodeConfigDao.checkOnly(payCodeConfig);
	}

	// TODO 网游计费信息

	/**
	 * 获取支付信息列表
	 * 
	 * @param page
	 * @param payInfoConfig
	 * @return
	 */
	public Page<PayInfoConfig> findPage(Page<PayInfoConfig> page,
			PayInfoConfig payInfoConfig) {
		payInfoConfig.setPage(page);
		page.setList(payInfoConfigDao.findList(payInfoConfig));
		return page;
	}

	/**
	 * 保存支付基本信息
	 * 
	 * @param payInfoConfig
	 */
	@Transactional(readOnly = false)
	public void savePayInfoConfig(PayInfoConfig payInfoConfig) {
		if (StringUtils.isBlank(payInfoConfig.getId())) {
			payInfoConfig.preInsert();
			payInfoConfigDao.insert(payInfoConfig);
		} else {
			payInfoConfig.preUpdate();
			payInfoConfigDao.update(payInfoConfig);
		}
	}

	/**
	 * 删除支付配置信息
	 * 
	 * @param payInfoConfig
	 */
	@Transactional(readOnly = false)
	public void delPayInfoConfig(PayInfoConfig payInfoConfig) {
		payInfoConfigDao.delete(payInfoConfig);
	}

	/**
	 * 获取支付配置信息
	 * 
	 * @param payInfoConfig
	 */
	@Transactional(readOnly = false)
	public PayInfoConfig getPayInfoConfig(PayInfoConfig payInfoConfig) {
		return payInfoConfigDao.get(payInfoConfig);
	}
	
	/**
	 * 检测唯一
	 * @param payCodeConfig
	 * @return
	 */
	public int checkPayInfoOnly(PayInfoConfig payInfoConfig) {
		return payInfoConfigDao.checkOnly(payInfoConfig);
	}
	
	
	public List<Cfgparam> findAdList(){
		return cfgparamDao.findList(new Cfgparam());
	}
}
