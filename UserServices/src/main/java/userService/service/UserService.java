//package userService.service;
//
//import userService.entities.UserInfo;
//import userService.entities.UserInfoDto;
//import userService.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.function.Supplier;
//import java.util.function.UnaryOperator;
//
//@Service
//@RequiredArgsConstructor
//public class UserService
//{
//    @Autowired
//    private final UserRepository userRepository;
//
//    public UserInfoDto createOrUpdateUser(UserInfoDto userInfoDto){
//        UnaryOperator<UserInfo> updatingUser = user -> {
//            return userRepository.save(userInfoDto.transformToUserInfo());
//        };
//
//        Supplier<UserInfo> createUser = () -> {
//            return userRepository.save(userInfoDto.transformToUserInfo());
//        };
//
//        UserInfo userInfo = userRepository.findByUserId(userInfoDto.getUserId())
//                .map(updatingUser)
//                .orElseGet(createUser);
//        return new UserInfoDto(
//                userInfo.getUserId(),
//                userInfo.getUsername(),
//                userInfo.getFirstName(),
//                userInfo.getLastName(),
//                userInfo.getPhoneNumber(),
//                userInfo.getEmail(),
//                userInfo.getProfilePic()
//        );
//    }
//
//    public UserInfoDto getUser(UserInfoDto userInfoDto) throws Exception{
//        Optional<UserInfo> userInfoDtoOpt = userRepository.findByUserId(userInfoDto.getUserId());
//        if(userInfoDtoOpt.isEmpty()){
//            throw new Exception("User not found");
//        }
//        UserInfo userInfo = userInfoDtoOpt.get();
//        return new UserInfoDto(
//                userInfo.getUserId(),
//                userInfo.getUsername(),
//                userInfo.getFirstName(),
//                userInfo.getLastName(),
//                userInfo.getPhoneNumber(),
//                userInfo.getEmail(),
//                userInfo.getProfilePic()
//        );
//    }
//
//}

package userService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userService.entities.UserInfo;
import userService.entities.UserInfoDto;
import userService.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoDto createOrUpdateUser(UserInfoDto userInfoDto) {
        UserInfo userInfo = userRepository.findByUserId(userInfoDto.getUserId())
                .map(existingUser -> {
                    // Only update fields that are non-null in the DTO
                    if (userInfoDto.getUsername() != null)
                        existingUser.setUsername(userInfoDto.getUsername());

                    if (userInfoDto.getFirstName() != null)
                        existingUser.setFirstName(userInfoDto.getFirstName());

                    if (userInfoDto.getLastName() != null)
                        existingUser.setLastName(userInfoDto.getLastName());

                    if (userInfoDto.getPhoneNumber() != null)
                        existingUser.setPhoneNumber(userInfoDto.getPhoneNumber());

                    if (userInfoDto.getEmail() != null)
                        existingUser.setEmail(userInfoDto.getEmail());

                    if (userInfoDto.getProfilePic() != null)
                        existingUser.setProfilePic(userInfoDto.getProfilePic());

                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> userRepository.save(userInfoDto.transformToUserInfo()));

        return mapToDto(userInfo);
    }

    public UserInfoDto getUser(UserInfoDto userInfoDto) throws Exception {
        Optional<UserInfo> userInfoOpt = userRepository.findByUserId(userInfoDto.getUserId());
        if (userInfoOpt.isEmpty()) {
            throw new Exception("User not found");
        }
        return mapToDto(userInfoOpt.get());
    }

    private UserInfoDto mapToDto(UserInfo userInfo) {
        return new UserInfoDto(
                userInfo.getUserId(),
                userInfo.getUsername(),
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getPhoneNumber(),
                userInfo.getEmail(),
                userInfo.getProfilePic()
        );
    }
}
