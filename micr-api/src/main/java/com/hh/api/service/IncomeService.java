package com.hh.api.service;

public interface IncomeService {
    /*收益计划*/
    void generateIncomePlan();

    /*收益返还到用户的资金账户*/
    void generateIncomeBack();
}
