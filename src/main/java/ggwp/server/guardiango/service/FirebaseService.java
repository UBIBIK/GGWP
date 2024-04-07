package ggwp.server.guardiango.service;

import ggwp.server.guardiango.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface FirebaseService {
    public String insertUser(User user) throws Exception;
    public User getUserDetail(String id) throws Exception;
    public String updateUser(User user) throws Exception;
    public String deleteUser(String id) throws Exception;
}