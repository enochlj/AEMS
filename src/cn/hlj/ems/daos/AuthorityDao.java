package cn.hlj.ems.daos;

import org.springframework.stereotype.Repository;

import cn.hlj.ems.entities.Authority;

@Repository
public class AuthorityDao extends BaseDao<Authority> {
/*
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Criteria getCriteria() {
		return getSession().createCriteria(Authority.class);
	}

	@SuppressWarnings("unchecked")
	public List<Authority> getByNull(String propertyName) {
		Criterion criterion = Restrictions.isNull(propertyName);
		Criteria criteria = getCriteria().add(criterion);
		return criteria.setCacheable(true).list();
	}

	@SuppressWarnings("unchecked")
	public List<Authority> getByNotNull(String propertyName) {
		Criterion criterion = Restrictions.isNotNull(propertyName);
		Criteria criteria = getCriteria().add(criterion);
		return criteria.setCacheable(true).list();
	}
	*/
}
