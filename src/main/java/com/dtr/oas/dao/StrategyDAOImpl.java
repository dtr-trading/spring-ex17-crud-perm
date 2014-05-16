package com.dtr.oas.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dtr.oas.exception.DuplicateStrategyException;
import com.dtr.oas.exception.StrategyNotFoundException;
import com.dtr.oas.model.Strategy;

@Repository
public class StrategyDAOImpl implements StrategyDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void addStrategy(Strategy strategy) throws DuplicateStrategyException {
        List<Strategy> strategies = getStrategies();
        for (Strategy singleStrategy : strategies) {
            if (singleStrategy.getType().equals(strategy.getType()) && singleStrategy.getName().equals(strategy.getName())) {
                String message = "The Strategy [" + singleStrategy.getName() + "] already exists in the system.";
                throw new DuplicateStrategyException(message);
            }
        }
        getCurrentSession().save(strategy);
    }

    @Override
    public void updateStrategy(Strategy strategy) throws StrategyNotFoundException, DuplicateStrategyException {
        Strategy strategyToUpdate = getStrategy(strategy.getId());
        
        // iterated to see if this update is a duplicate
        List<Strategy> strategies = getStrategies();
        for (Strategy singleStrategy : strategies) {
            if (singleStrategy.getType().equals(strategy.getType()) && singleStrategy.getName().equals(strategy.getName())) {
                String message = "The Strategy [" + singleStrategy.getName() + "] already exists in the system.";
                throw new DuplicateStrategyException(message);
            }
        }
        
        // no exception thrown, so it is not a duplicate
        strategyToUpdate.setName(strategy.getName());
        strategyToUpdate.setType(strategy.getType());
        getCurrentSession().update(strategyToUpdate);
    }

    @Override
    public Strategy getStrategy(int id) throws StrategyNotFoundException {
        Strategy strategy = (Strategy) getCurrentSession().get(Strategy.class, id);
        
        if (strategy == null) {
            throw new StrategyNotFoundException("Strategy id [" + id + "] not found in the system.");
        } else {
            return strategy;
        }
    }

    @Override
    public void deleteStrategy(int id) throws StrategyNotFoundException {
        Strategy strategy = getStrategy(id);
        if (strategy != null)
            getCurrentSession().delete(strategy);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Strategy> getStrategies() {
        String hql = "FROM Strategy s ORDER BY s.id";
        return getCurrentSession().createQuery(hql).list();
    }

}
