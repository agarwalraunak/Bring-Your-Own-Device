package com.kerberos.db.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kerberos.db.model.ServiceTicket;
import com.kerberos.db.model.TGT;

/**
 * @author raunak
 *
 */
@Repository
public class ServiceTicketDAOImpl implements IServiceTicketDAO{

	private @Autowired SessionFactory sessionFactory;
	
	@Override
	public void persist(ServiceTicket serviceTicket) {

		Session session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		try {
			session.save(serviceTicket);
		} catch (Exception e) {
			e.printStackTrace();
			trx.rollback();
		}
		trx.commit();
		session.close();
	}

	@Override
	public ServiceTicket findServiceTicketByID(String serviceSessionID) {

		ServiceTicket serviceTicket = null;
		Session session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		try {
			serviceTicket = (ServiceTicket) session.createQuery("from ServiceTicket where serviceSessionID = :serviceSessionID").setParameter("serviceSessionID", serviceSessionID).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			trx.rollback();
		}
		trx.commit();
		session.close();
		
		return serviceTicket;
	}

	@Override
	@SuppressWarnings(value="unchecked")
	public ServiceTicket findActiveServiceTicketByTGTAndServiceName(TGT tgt, String serviceName){
		ServiceTicket serviceTicket = null;
		Session session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		try {
			List<ServiceTicket> serviceTickets = (List<ServiceTicket>) session.createQuery("from ServiceTicket where tgt.id = :tgtID and serviceName = :serviceName and isActive = :isActive order by id")
					.setParameter("tgtID", tgt.getId()).setParameter("serviceName", serviceName).setParameter("isActive", true).list();
			//Deactivate if more than one active ticket is found
			if (serviceTickets.size() > 0) {
				//Get the last service ticket
				serviceTicket = serviceTickets.get(serviceTickets.size()-1);
				for (ServiceTicket ticket : serviceTickets) {
					if (ticket.getId() != serviceTicket.getId()) {
						ticket.setActive(false);
						session.update(ticket);
					}
				}
				if (serviceTicket.getExpiresOn().before(new Date())){
					serviceTicket.setActive(false);
					session.update(serviceTicket);
					serviceTicket = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			trx.rollback();
		}
		trx.commit();
		session.close();
		
		return serviceTicket;
	}

	@Override
	public void merge(ServiceTicket serviceTicket) {
		Session session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		try {
			session.merge(serviceTicket);
		} catch (Exception e) {
			e.printStackTrace();
			trx.rollback();
		}
		trx.commit();
		session.close();		
	}
}
