package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.User;
import org.telegram.galacticMiniatures.bot.model.UserInfo;
import org.telegram.galacticMiniatures.bot.repository.UserInfoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    public Optional<UserInfo> findByUser(User user) {
        return userInfoRepository.findByUser(user);
    }

    public void save(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }
}