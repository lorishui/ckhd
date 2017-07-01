package com.chkd.count.count.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chkd.count.common.utils.LogUtils;

@Component("curd")
public class OnlineCurdDataHandle {
	
	@Autowired
	private JdbcTemplate onlineTemplate;
	
	@Autowired
	private JdbcTemplate eventTemplate;
	
	public List<Map<String,Object>> getListData(String sql){
		return eventTemplate.queryForList(sql);
	}
	
	@Transactional(readOnly=false)
	public boolean save(List<String> sqlList){
		try{
			for(String sql:sqlList){
				onlineTemplate.execute(sql);
			}
			return true;
		}catch(Throwable e){
			LogUtils.log.error("insert list error!", e);
		}
		return false;
	}
	
	@Transactional(readOnly=false)
	public boolean save(String sql){
		try{
			onlineTemplate.execute(sql);
			return true;
		}catch(Throwable e){
			LogUtils.log.error("insert list error!", e);
		}
		return false;
	}
	
	@Transactional(readOnly=false)
	public boolean saveAndprepare(final String sql,String time){
		try{
			class ExecutePreparedStatementCallback implements PreparedStatementCallback<Object>, SqlProvider {
					private String time;
					public void setTime(String time) {
						this.time=time;
					}
					public String getSql() {
						return sql;
					}
					public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
						ps.setString(1, this.time);
						ps.execute();
						return null;
					}
				}
			ExecutePreparedStatementCallback action = new ExecutePreparedStatementCallback();
			action.setTime(time);
			onlineTemplate.execute(sql, action);
			return true;
		}catch(Throwable e){
			LogUtils.log.error("insert list error!", e);
		}
		return false;
	}
	
	public List<Map<String,Object>> getOnlineListData(String sql){
		return onlineTemplate.queryForList(sql);
	}
	
	/**
	 * @param sql
	 * @param args
	 */
	public int update(String sql, Object... args) {
		return onlineTemplate.update(sql, args);
	}
}
