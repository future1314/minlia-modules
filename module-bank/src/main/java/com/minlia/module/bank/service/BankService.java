package com.minlia.module.bank.service;

import com.github.pagehelper.PageInfo;
import com.minlia.module.bank.bean.domain.BankDO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by garen on 2018/8/10.
 */
public interface BankService {

    BankDO create(BankDO bankDo);

    BankDO update(BankDO bankDo);

    void delete(String number);

    BankDO one(BankDO bankDO);

    List<BankDO> list(BankDO bankDO);

    PageInfo<BankDO> page(BankDO bankDO, Pageable pageable);

}
