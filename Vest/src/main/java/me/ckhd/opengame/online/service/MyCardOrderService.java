package me.ckhd.opengame.online.service;

import org.springframework.stereotype.Service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.online.dao.MyCardOrderDao;
import me.ckhd.opengame.online.entity.MyCardOrder;

@Service
public class MyCardOrderService extends CrudService<MyCardOrderDao, MyCardOrder> {

}
