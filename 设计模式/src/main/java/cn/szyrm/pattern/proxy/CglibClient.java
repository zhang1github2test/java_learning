package cn.szyrm.pattern.proxy;

public class CglibClient {
    public static void main(String[] args) {
        CGLibProxyFactory cgLibProxyFactory = new CGLibProxyFactory(new TeacherDao());
        TeacherDao proxyInstance = (TeacherDao) cgLibProxyFactory.getProxyInstance();

     //   TeacherDao proxyInstance2 =(TeacherDao) cgLibProxyFactory.getProxyInstance2();


        proxyInstance.teach();
        System.out.println(proxyInstance.toString());

     //   proxyInstance2.teach();
        System.out.println(".......");
    }
}
