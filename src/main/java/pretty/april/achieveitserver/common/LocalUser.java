package pretty.april.achieveitserver.common;

import pretty.april.achieveitserver.entity.User;

/**
 * 线程安全的当前登录用户，如果用户未登录，则得到 null
 */
public class LocalUser {

	private static ThreadLocal<User> local = new ThreadLocal<>();

    /**
     * 得到当前登录用户
     *
     * @return user | null
     */
    public static User getLocalUser() {
        return LocalUser.local.get();
    }
}
