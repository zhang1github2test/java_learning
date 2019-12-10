package cn.szyrm.cache.controller;

import cn.szyrm.cache.bean.Employee;
import cn.szyrm.cache.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService service;
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable("id") Integer id){
           return service.getEmpById(id);
    }
    @GetMapping("/update")
    public Employee updateEmployee(Employee employee){
        return service.updateEmp(employee);
    }
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Integer id){
        service.deleteEmp(id);
    }
}
