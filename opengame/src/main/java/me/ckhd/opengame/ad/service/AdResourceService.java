package me.ckhd.opengame.ad.service;

import me.ckhd.opengame.ad.dao.AdResourceDao;
import me.ckhd.opengame.ad.entity.AdResource;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;

@Service
public class AdResourceService extends  CrudService<AdResourceDao, AdResource>{

}
