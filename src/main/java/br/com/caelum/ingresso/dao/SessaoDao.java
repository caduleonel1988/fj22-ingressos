package br.com.caelum.ingresso.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;

@Repository
public class SessaoDao {
	
	@PersistenceContext
	private EntityManager manager;
	
	// method to save the session
	public void save(Sessao sessao) {
		manager.persist(sessao);
	}
	
	// method to list all sessions
	public List<Sessao> buscaSessoesDaSala(Sala sala) {
		return manager.createQuery("select s from Session s where s.sala = :sala", Sessao.class)
				.setParameter("sala", sala)
				.getResultList();
	}

}
