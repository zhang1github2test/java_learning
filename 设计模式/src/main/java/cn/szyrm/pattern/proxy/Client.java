package cn.szyrm.pattern.proxy;

public class Client {
    public static void main(String[] args) {
        ITeacherDao teacherDao = new TeachDaoProxy(new TeacherDao());
        teacherDao.teach();
    }
}
