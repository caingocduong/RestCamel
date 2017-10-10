package com.java.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.java.models.User;

@Repository
public class UserRepositoryImpl implements UserRepository{
	private SessionFactory sessiotnFactory;
	@Autowired
	public UserRepositoryImpl(SessionFactory sessionFactory){
		this.sessiotnFactory = sessionFactory;
	}
	private List<User> personList;
	private void initData(){
		personList = new ArrayList<>();
		User person1 = new User(1,"darren",24);
		User person2 = new User(2,"larry",23);
		personList.add(person1);
		personList.add(person2);
	}
	
	public UserRepositoryImpl(){
		initData();
	}
	
	private Session currentSession(){
		
		return this.sessiotnFactory.getCurrentSession();
	}

	@Override
	public void createPerson(User person) {
		personList.add(person);
		//currentSession().merge(person);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> retrieveAll() {
		
		
		return this.personList;
		//return currentSession().createCriteria(Person.class).list();
	}

	@Override
	public User findById(long id) {
		
		Optional<User> user =  personList.stream().filter(p -> p.getId() == id).findFirst();
		if(user.isPresent()){
			
			return user.get();
		} else {
			
			return null;
		}
		/*Criteria criteria = currentSession().createCriteria(Person.class);
		criteria.add(Restrictions.eq("id", id));
		
		return (Person) criteria.list().get(0);*/
	}

	@Override
	public void update(User person) {
		User p = findById(person.getId());
		personList.get(personList.indexOf(p)).setName(person.getName());
		personList.get(personList.indexOf(p)).setAge(person.getAge());
//		currentSession().update(person);
	}

	@Override
	public void delete(long id) {
		User p = findById(id);
		personList.remove(p);
		/*Person personToDelete = (Person) currentSession().load(Person.class, id);
		currentSession().delete(personToDelete);*/
	}
	
	
}
