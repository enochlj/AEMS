package cn.hlj.ems.services;

import org.springframework.stereotype.Service;
import cn.hlj.ems.entities.Authority;

@Service
public class AuthorityService extends BaseService<Authority> {
	/*@Autowired
	private AuthorityDao authorityDao;

	@Transactional(readOnly = true)
	public List<Authority> getByIsNull(String propertyName) {
		return authorityDao.getByNull(propertyName);
	}

	@Transactional(readOnly = true)
	public List<Authority> getByIsNotNull(String propertyName) {
		return authorityDao.getByNotNull(propertyName);
	}*/
}
