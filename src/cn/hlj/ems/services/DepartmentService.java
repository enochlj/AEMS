package cn.hlj.ems.services;


import org.springframework.stereotype.Service;

import cn.hlj.ems.entities.Department;

@Service
public class DepartmentService extends BaseService<Department> {
/*
	@Autowired
	private DepartmentDao departmentDao;

	@Transactional(readOnly = true)
	public List<Department> getAll() {
		return departmentDao.getAll(true);
	}*/
}
