package com.billlog.rest.api.service;

import com.billlog.rest.api.advice.exception.CUserNotFoundException;
import com.billlog.rest.api.mapper.UserMapper;
import com.billlog.rest.api.model.FoodUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    private final ResponseService responseService;

    public CustomUserDetailService(ResponseService responseService) {
        this.responseService = responseService;
    }

    @Override
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {

        FoodUser user = userMapper.findByUsername(user_id);


        if("".equals(user.getUser_idx())){
            throw new CUserNotFoundException();
        }else{
            return user;
        }
    }
}
