package com.kerberos.db.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kerberos.db.model.TGT;

/**
 * @author raunak
 *
 */
@Repository
public class TGTDAOImpl implements ITGTDAO{
	
	private @Autowired SessionFactory sessionFactory;

	@Override
	public void persist(TGT tgt){
		
		Session session = sessionFactory.openSession();
		
		Transaction trx = session.beginTransaction();
		try{
			session.persist(tgt);
		}
		catch(Exception e){
			e.printStackTrace();
			trx.rollback();
		}
		trx.commit();
		session.close();	
	}
	
	@Override
	public void merge(TGT tgt){
		
		Session session = sessionFactory.openSession();
		
		Transaction trx = session.beginTransaction();
		try{
			session.merge(tgt);
		}
		catch(Exception e){
			e.printStackTrace();
			trx.rollback();
		}
		trx.commit();
		session.close();
	}
	
	@Override
	public TGT findTGTforSessionKey(String sessionID){
		TGT tgt = null;
		Session session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		try{
			tgt = (TGT) session.createQuery("from TGT where sessionID = :sessionID").setParameter("sessionID", sessionID).uniqueResult();
			trx.commit();
			session.close();
		}
		catch(Exception e){
			e.printStackTrace();
			trx.rollback();
		}
		return tgt;
	}
	
	@Override
	public TGT findActiveTGTForUsername(String username){
		TGT tgt = null;
		Session session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		try{
			tgt = (TGT) session.createQuery("from TGT where username = :username and isActive = :isActive").setParameter("username", username).setParameter("isActive", true).uniqueResult();
			trx.commit();
			session.close();
		}
		catch(Exception e){
			e.printStackTrace();
			trx.rollback();
		}
		return tgt;
	}

}
