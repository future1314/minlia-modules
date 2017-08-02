package com.minlia.cloud.service;

import com.minlia.cloud.query.body.ApiSearchRequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public interface IOperations<ENTITY extends Persistable,PK extends Serializable> {

    /**
     * findOne
     *
     * @param id
     * @return
     */
    public ENTITY findOne(final PK id);

    /**
     * - contract: if nothing is found, an empty list will be returned to the calling client <br>
     */
    public List<ENTITY> findAll();


    /**
     * @param body
     * @return
     */
    public List<ENTITY> findListByBody(ApiSearchRequestBody body);


    /**
     * is entity exists
     * @param id
     * @return
     */
    Boolean exists(PK id);

    public Page<ENTITY> findAll(Pageable pageable);


    /**
     * @param body
     * @param pageable
     * @return
     */
    public Page<ENTITY> findPageByBody(ApiSearchRequestBody body, Pageable pageable);


    // create
    public ENTITY create(ENTITY resource);


    // update
    public ENTITY update(ENTITY resource);

    // delete
    public void delete(PK id);

    /**
     * deletion in batch
     * @param ids
     */
    public void delete(Iterator<PK> ids);

    /**
     * delete all
     */
    public void deleteAll();

    // count
    public Long count();


}