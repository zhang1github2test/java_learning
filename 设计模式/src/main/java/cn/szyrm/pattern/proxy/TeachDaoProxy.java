package cn.szyrm.pattern.proxy;

public class TeachDaoProxy implements ITeacherDao {
    private  ITeacherDao teacherDao;

    public TeachDaoProxy(ITeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Override
    public void teach() {
        System.out.println("上课前准备工作.....");
        teacherDao.teach();
        System.out.println("上课后清理工作.....");
    }
}
