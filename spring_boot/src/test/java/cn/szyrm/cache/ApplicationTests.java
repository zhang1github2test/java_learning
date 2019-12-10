package cn.szyrm.cache;

import cn.szyrm.cache.bean.Employee;
import cn.szyrm.cache.mapper.EmployeeMapper;
import cn.szyrm.cache.service.IDepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApplicationTests {
    @Resource
    private EmployeeMapper employeeMapper;

    @Test
    void contextLoads() {
    }
    @Test
    void getEmpById(){
        Employee empById = employeeMapper.getEmpById(1);

        assert empById != null;
    }

}
