package me.ckhd.opengame.online.dao;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.online.entity.MyCardOrder;

@Repository
public interface MyCardOrderDao extends CrudDao<MyCardOrder> {

}
