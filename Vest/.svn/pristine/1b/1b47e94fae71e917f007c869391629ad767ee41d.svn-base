package me.ckhd.opengame.query.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QueryService {

	@Autowired
	private SqlSession sqlSession;
	
	private String prefix = getClass().getName() + ".";
	
	private String getStatement(String statement) { return prefix + statement; }
	
	public <T>T selectOne(String statement) {
		return sqlSession.selectOne(getStatement(statement));
	}
	
	public <T>T selectOne(String statement, Object parameter) {
		return sqlSession.selectOne(getStatement(statement), parameter);
	}
	
	public <E>List<E> selectList(String statement) {
		return sqlSession.selectList(getStatement(statement));
	}
	
	public <E>List<E> selectList(String statement, Object parameter) {
		return sqlSession.selectList(getStatement(statement), parameter);
	}
	
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return sqlSession.selectList(getStatement(statement), parameter, rowBounds);
	}
	
	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return sqlSession.selectMap(getStatement(statement), mapKey);
	}
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return sqlSession.selectMap(getStatement(statement), parameter, mapKey);
	}
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return sqlSession.selectMap(getStatement(statement), parameter, mapKey, rowBounds);
	}
	public void select(String statement, Object parameter, ResultHandler handler) {
		sqlSession.select(getStatement(statement), parameter, handler);
	}
	public void select(String statement, ResultHandler handler) {
		sqlSession.select(getStatement(statement), handler);
	}
	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		sqlSession.select(getStatement(statement), parameter, rowBounds, handler);
	}

}
