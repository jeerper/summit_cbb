package com.summit.service;

import com.summit.domain.User;
import com.summit.domain.UserEnum;
import com.summit.service.user.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * UserService 服務層<br>
 * UserService 實現 UserDetailsService 接口，重寫其方法！這是必須<br>
 *
 * @author yt
 */
@Service("userServiceDetail")
@SuppressWarnings("all")
public class UserServiceDetail implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("进入验证方法");
        System.out.println("username :" + username);
        //User user = userRepository.findByUsername(username);
        User user = null;
        Map<String, Object> js = userService.queryUserByLogin(username);
        System.out.println("js:" + js);
        if (js != null && js.containsKey("data")) {
            JSONArray jsList = (JSONArray) js.get("data");
            if (jsList != null && jsList.size() > 1) {
                JSONObject json = jsList.getJSONObject(1);
                user = new User();
                user.setUsername(json.getString(UserEnum.USERNAME.toString()));
                user.setPassword(json.getString(UserEnum.PASSWORD.toString()));
                user.setAuthorities(userService.getGrantedAuthoritiesByUserName(username));
            }

            System.out.println(user);
        }


        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        Boolean locked = user.isAccountNonLocked();
        return user;
    }

    public User findByUsernameAndPassword(String username, String password) {
        return null;
    }
}
