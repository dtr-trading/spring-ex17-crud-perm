package com.dtr.oas.service;

import java.util.List;

import com.dtr.oas.dao.StrategyDAO;
import com.dtr.oas.exception.DuplicateStrategyException;
import com.dtr.oas.exception.StrategyNotFoundException;
import com.dtr.oas.model.Strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StrategyServiceImpl implements StrategyService {

    @Autowired
    private StrategyDAO strategyDAO;

    @Override
    public void addStrategy(Strategy strategy) throws DuplicateStrategyException {
        strategyDAO.addStrategy(strategy);
    }

    @Override
    public void updateStrategy(Strategy strategy) throws StrategyNotFoundException, DuplicateStrategyException{
        strategyDAO.updateStrategy(strategy);
    }

    @Override
    public Strategy getStrategy(int id) throws StrategyNotFoundException {
        return strategyDAO.getStrategy(id);
    }

    @Override
    public void deleteStrategy(int id) throws StrategyNotFoundException {
        strategyDAO.deleteStrategy(id);
    }

    @Override
    public List<Strategy> getStrategies() {
        return strategyDAO.getStrategies();
    }

}
