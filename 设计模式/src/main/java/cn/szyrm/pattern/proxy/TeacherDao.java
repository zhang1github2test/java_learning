package cn.szyrm.pattern.proxy;

public class TeacherDao implements ITeacherDao {
    @Override
    public void teach() {
        System.out.println("老师正式开始上课.....");
    }
}
