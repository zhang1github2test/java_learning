package cn.szyrm.cache.service;

import cn.szyrm.cache.bean.Employee;

public interface IEmployeeService {
    Employee getEmpById(Integer id);
    Employee updateEmp(Employee employee);
    void deleteEmp(Integer id);
}
