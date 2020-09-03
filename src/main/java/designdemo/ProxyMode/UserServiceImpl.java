package designdemo.ProxyMode;

/**
 * @author wusd
 * @description 空
 * @create 2020/09/02 18:25
 */
public class UserServiceImpl implements UserService {
    @Override
    public void save(String user) {
        System.out.println("保存" + user + "成功");
    }
}
