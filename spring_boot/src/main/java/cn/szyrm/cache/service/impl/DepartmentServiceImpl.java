package cn.szyrm.cache.service.impl;

import cn.szyrm.cache.bean.Department;
import cn.szyrm.cache.mapper.DepartmentMapper;
import cn.szyrm.cache.service.IDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class DepartmentServiceImpl implements IDepartmentService {
    @Resource
    private DepartmentMapper mapper;
    @Override
    @Transactional
    public void insert() {
        Department de = new Department();
        de.setDepartmentName("警务室");
        mapper.insert(de);
       // int i = 1/0;
    }
}
