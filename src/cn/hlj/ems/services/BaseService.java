package cn.hlj.ems.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cn.hlj.ems.daos.BaseDao;
import cn.hlj.ems.orm.Page;
import cn.hlj.ems.orm.PropertyFilter;

public class BaseService<T> {

	// Spring4提供的泛型注入
	@Autowired
	protected BaseDao<T> baseDao;

	// ---------------------------------Extract-According-To-EmployeeService----------------------------------

	@Transactional(readOnly = true)
	public Page<T> getPage(int pageNo) {
		Page<T> page = baseDao.getPage(pageNo);
		initializePageContent(page.getContent());
		return page;
	}

	protected void initializePageContent(List<T> content) {

	}

	@Transactional(readOnly = true)
	public T get(Integer id) {
		T entity = baseDao.get(id);

		// 在父类中是空的方法. 子类可以根据自己的情况来初始化当前实体类的属性
		initializeEntity(entity);

		return entity;
	}

	protected void initializeEntity(T entity) {

	}

	// ---------------------------------Extract-According-To-EmployeeService----------------------------------

	// -------------------------------Extract-According-To-DepartmentService----------------------------------
	@Transactional
	public List<T> getAll(boolean... cached) {
		List<T> list = null;
		if (cached != null && cached.length == 1) {
			list = baseDao.getAll(cached[0]);
		} else {
			list = baseDao.getAll();
		}
		initializeList(list);
		return list;
	}

	protected void initializeList(List<T> list) {
	}
	// -------------------------------Extract-According-To-DepartmentService----------------------------------

	// -------------------------------Extract-According-To-RoleService----------------------------------------

	@Transactional
	public void save(T entity) {
		baseDao.saveOrUpdate(entity);
	}

	@Transactional(readOnly = true)
	public T getBy(String propertyName, Object propertyVal) {
		return baseDao.getBy(propertyName, propertyVal);
	}

	@Transactional
	public void delete(T entity) {
		baseDao.delete(entity);
	}
	// -------------------------------Extract-According-To-RoleService----------------------------------------

	// -------------------------------Extract-According-To-AuthorityService----------------------------------

	@Transactional(readOnly = true)
	public List<T> getByIsNull(String propertyName) {
		return baseDao.getByNull(propertyName);
	}

	@Transactional(readOnly = true)
	public List<T> getByIsNotNull(String propertyName) {
		return baseDao.getByNotNull(propertyName);
	}
	// -------------------------------Extract-According-To-AuthorityService----------------------------------

	@Transactional(readOnly = true)
	public Page<T> getPage(int pageNo, Map<String, Object> params) {
		// 把params转为PropertyFilter的集合
		List<PropertyFilter> filters = PropertyFilter.parseParamsToPropertyFilters(params);

		// 得到Page对象
		Page<T> page = baseDao.getPage(pageNo, filters);
		initializePageContent(page.getContent());

		return page;
	}

}
