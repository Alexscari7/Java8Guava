package designdemo.ProxyMode;

/**
 * @author wusd
 */
public class StaticProxy implements UserService{

    private UserService userService;

    StaticProxy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void save(String user) {
        System.out.println("before method");
        userService.save(user);
        System.out.println("after method");
    }
}
