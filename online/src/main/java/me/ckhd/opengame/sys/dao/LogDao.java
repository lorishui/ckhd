/**
 * Copyright &copy; 2015-2018 <a href="http://www.ckhd.me/">创酷互动</a> All rights reserved.
 */
package me.ckhd.opengame.sys.dao;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.sys.entity.Log;

import org.springframework.stereotype.Repository;

/**
 * 日志DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@Repository
public interface LogDao extends CrudDao<Log> {

}
