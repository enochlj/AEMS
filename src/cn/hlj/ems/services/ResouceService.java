package cn.hlj.ems.services;

import org.springframework.stereotype.Service;

import cn.hlj.ems.entities.Resource;

@Service
public class ResouceService extends BaseService<Resource> {

	/*@Autowired
	private ResourceDao resourceDao;

	@Transactional(readOnly = true)
	public List<Resource> getAll() {
		return resourceDao.getAll();
	}*/
}
