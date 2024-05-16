import com.mini.beans.BeansException;
import com.mini.context.ClassPathXmlApplicationContext;
import com.mini.test.BaseBaseService;
import com.mini.test.BaseService;

public class Test1 {
    public static void main(String[] args) throws ClassNotFoundException, BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        BaseService bservice =(BaseService) context.getBean("bservice");
        bservice.sayHello();

        BaseBaseService bbs =(BaseBaseService) context.getBean("bbs");
        bbs.sayHello();
    }
}
