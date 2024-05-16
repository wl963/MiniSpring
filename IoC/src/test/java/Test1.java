import com.mini.beans.BeansException;
import com.mini.context.ClassPathXmlApplicationContext;
import com.mini.test.AService;
import com.mini.test.BaseBaseService;
import com.mini.test.BaseService;
import com.mini.test.People;

public class Test1 {
    public static void main(String[] args) throws ClassNotFoundException, BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml",false);
        AService aservice =(AService) context.getBean("aservice");
        aservice.sayHello();

        BaseBaseService bbs =(BaseBaseService) context.getBean("basebaseservice");
        bbs.getAs().sayHello();

    }
}
