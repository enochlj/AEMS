package cn.hlj.ems.daos;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hlj.ems.orm.Page;
import cn.hlj.ems.orm.PropertyFilter;
import cn.hlj.ems.orm.PropertyFilter.MatchType;
import cn.hlj.ems.util.ReflectionUtils;

public class BaseDao<T> {
	private Class<T> entityClass;

	@Autowired
	private SessionFactory sessionFactory;

	public BaseDao() {
		entityClass = ReflectionUtils.getSuperGenericType(getClass());
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Criteria getCriteria() {
		return getSession().createCriteria(entityClass);
	}
	// EmployeeDao的updateVisitedTimes()方法呢?
	// ---------------------------------Extract-According-To-EmployeeDao--------------------------------

	@SuppressWarnings("unchecked")
	public T getBy(String propertyName, Object propertyVal) {
		Criterion criterion = Restrictions.eq(propertyName, propertyVal);

		Criteria criteria = getCriteria().add(criterion);
		return (T) criteria.uniqueResult();
	}

	// Hibernate 的 .cfg.xml 文件中包含了所有的 .hbm.xml 文件
	// 即在 SessionFactory 对象创建的时候, 实际上可以得到所有的实体类的映射信息
	// 所以应该可以得到实体类在映射文件中的所有映射细节. 包括 id 的 name 值
	private String getIdName() {
		// 根据实体类的类型来获取 hibernate 中类的元数据
		ClassMetadata classMetadata = sessionFactory.getClassMetadata(entityClass);
		// 再根据 ClassMetadata 得到映射文件中的细节. 例如 id 的名字
		return classMetadata.getIdentifierPropertyName();
	}

	public long getTotalElements() {
		// 获取实体类的id的属性名
		String idName = getIdName();

		Projection projection = Projections.count(idName);
		Criteria criteria = getCriteria().setProjection(projection);

		return (long) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> getContent(Page<T> page) {
		int firstResult = (page.getPageNo() - 1) * page.getPageSize();
		int maxResults = page.getPageSize();

		return getCriteria().setFirstResult(firstResult).setMaxResults(maxResults).list();
	}

	public Page<T> getPage(int pageNo) {
		Page<T> page = new Page<>();
		page.setPageNo(pageNo);

		long totalElements = getTotalElements();
		page.setTotalElements(totalElements);

		List<T> content = getContent(page);
		page.setContent(content);
		return page;
	}

	@SuppressWarnings("unchecked")
	public T get(Integer id) {
		return (T) getSession().get(entityClass, id);
	}

	public void saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll(boolean... cached) {
		if (cached != null && cached.length == 1) {
			return getCriteria().setCacheable(cached[0]).list();
		}
		return getCriteria().list();
	}

	public void batchSave(List<T> entities) {
		for (int i = 0; i < entities.size(); i++) {
			getSession().save(entities.get(i));

			if ((i + 1) % 50 == 0) {
				getSession().flush();
				getSession().clear();
			}
		}
	}

	// ---------------------------------Extract-According-To-EmployeeDao--------------------------------

	// ---------------------------------Extract-According-To-RoleDao------------------------------------

	@SuppressWarnings("unchecked")
	public List<T> getByIn(String propertyName, List<? extends Object> propertyVals) {
		Criterion criterion = Restrictions.in(propertyName, propertyVals);
		Criteria criteria = getCriteria().add(criterion);
		return criteria.list();
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	// ---------------------------------Extract-According-To-RoleDao------------------------------------

	// -------------------------------Extract-According-To-AuthorityDao----------------------------------

	@SuppressWarnings("unchecked")
	public List<T> getByNull(String propertyName) {
		Criterion criterion = Restrictions.isNull(propertyName);
		Criteria criteria = getCriteria().add(criterion);
		return criteria.setCacheable(true).list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getByNotNull(String propertyName) {
		Criterion criterion = Restrictions.isNotNull(propertyName);
		Criteria criteria = getCriteria().add(criterion);
		return criteria.setCacheable(true).list();
	}

	// -------------------------------Extract-According-To-AuthorityDao----------------------------------

	public Page<T> getPage(int pageNo, List<PropertyFilter> filters) {
		// 把 PropertyFilter 的集合转为 Criterion 的集合.
		// 即一个 PropertyFilter 对应着一个查询条件.
		List<Criterion> criterions = parsePropertyFiltersToCriterions(filters);

		// 2. 查询总的记录数
		Page<T> page = new Page<>();
		page.setPageNo(pageNo);

		long totalElements = getTotalElements(criterions);
		page.setTotalElements(totalElements);

		// 3. 查询当前页面的 Content
		List<T> content = getContent(page, criterions);
		page.setContent(content);

		// 4. 返回
		return page;

	}

	@SuppressWarnings("unchecked")
	private List<T> getContent(Page<T> page, List<Criterion> criterions) {
		int firstResult = (page.getPageNo() - 1) * page.getPageSize();
		int maxResults = page.getPageSize();

		Criteria criteria = getCriteria();

		// 3. 添加查询条件
		for (Criterion criterion : criterions) {
			criteria.add(criterion);
		}

		return criteria.setFirstResult(firstResult).setMaxResults(maxResults).list();
	}

	private long getTotalElements(List<Criterion> criterions) {
		// 1. 获取实体类的 id 的属性名
		String idName = getIdName();

		// 2. 查询 id 的 count
		Projection projection = Projections.count(idName);
		Criteria criteria = getCriteria();

		// 3. 添加查询条件
		for (Criterion criterion : criterions) {
			criteria.add(criterion);
		}

		criteria.setProjection(projection);
		return (long) criteria.uniqueResult();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Criterion> parsePropertyFiltersToCriterions(List<PropertyFilter> filters) {
		List<Criterion> criterions = new ArrayList<>();

		Criterion criterion = null;
		for (PropertyFilter filter : filters) {
			String propertyName = filter.getPropertyName();
			Object propertyValue = filter.getPropertyValue();
			MatchType matchType = filter.getMatchType();
			Class propertyType = filter.getPropertyType();

			// 把页面传入的查询条件的值转为目标类型
			propertyValue = ReflectionUtils.convertValue(propertyValue, propertyType);

			switch (matchType) {
			case EQ:
				criterion = Restrictions.eq(propertyName, propertyValue);
				break;
			case GE:
				criterion = Restrictions.ge(propertyName, propertyValue);
				break;
			case GT:
				criterion = Restrictions.gt(propertyName, propertyValue);
				break;
			case LE:
				criterion = Restrictions.le(propertyName, propertyValue);
				break;
			case LIKE:
				criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE);
				break;
			case LT:
				criterion = Restrictions.lt(propertyName, propertyValue);
				break;
			}

			if (criterion != null) {
				criterions.add(criterion);
			}
		}

		return criterions;
	}
}
