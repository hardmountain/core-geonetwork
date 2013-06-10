package org.fao.geonet.kernel.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.fao.geonet.kernel.domain.Operation;
import org.fao.geonet.kernel.domain.ReservedOperation;


/**
 * Implementation for all queries that cannot be automatically generated by Spring-data.
 *
 * @author Jesse
 */
public class OperationRepositoryImpl implements OperationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Operation findReservedOperation(ReservedOperation operation) {
        return entityManager.find(Operation.class, operation.getId());
    }
}