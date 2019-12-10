package cn.szyrm.pattern.proxy;

public class DynamicClient {
    public static void main(String[] args) {
        ITeacherDao teacherDao = new TeacherDao();
        ITeacherDao proxyInstance  = (ITeacherDao) new ProxyFactory(teacherDao).getProxyInstance();
        System.out.println("proxyInstance =" + proxyInstance.getClass());
        proxyInstance.teach();

    }
}
