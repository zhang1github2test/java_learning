package cn.szyrm.cache.service.impl;

import cn.szyrm.cache.bean.Employee;
import cn.szyrm.cache.mapper.EmployeeMapper;
import cn.szyrm.cache.service.IEmployeeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;
    @Override
    @Cacheable(cacheNames="emp",key = "#id")
    public Employee getEmpById(Integer id) {
        System.out.println("查询id为" + id + "的部门员工");
        return employeeMapper.getEmpById(id);
    }
    @CachePut(cacheNames = "emp",key="#employee.id")
    public Employee updateEmp(Employee employee){
        System.out.println("更新员工信息");
        employeeMapper.updateEmp(employee);
        return employee;
    }
    @CacheEvict(cacheNames = "emp", key="#id")
    public void deleteEmp(Integer id){
        System.out.println("删除员工");
        //   employeeMapper.deleteEmpById(id);
    }
}
