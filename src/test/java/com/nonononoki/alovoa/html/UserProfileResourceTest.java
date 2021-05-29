package com.nonononoki.alovoa.html;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.nonononoki.alovoa.component.TextEncryptorConverter;
import com.nonononoki.alovoa.entity.User;
import com.nonononoki.alovoa.model.UserDto;
import com.nonononoki.alovoa.repo.ConversationRepository;
import com.nonononoki.alovoa.repo.UserRepository;
import com.nonononoki.alovoa.service.AuthService;
import com.nonononoki.alovoa.service.CaptchaService;
import com.nonononoki.alovoa.service.RegisterService;
import com.nonononoki.alovoa.service.RegisterServiceTest;
import com.nonononoki.alovoa.service.UserService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserProfileResourceTest {
	
	@Autowired
	private RegisterService registerService;

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ConversationRepository conversationRepo;
	
	@Value("${app.first-name.length-max}")
	private int firstNameLengthMax;

	@Value("${app.first-name.length-min}")
	private int firstNameLengthMin;
	
	@Autowired
	private UserProfileResource userProfileResource;

	@MockBean
	private AuthService authService;
	
	private List<User> testUsers;
	
	@Autowired
	private TextEncryptorConverter textEncryptor;
	
	@BeforeEach
	public void before() throws Exception {
		testUsers = RegisterServiceTest.getTestUsers(captchaService, registerService, firstNameLengthMax, firstNameLengthMin);
	}
	
	@AfterEach
	public void after() throws Exception {
		RegisterServiceTest.deleteAllUsers(userService, authService, captchaService, conversationRepo, userRepo);
	}
	
	@Test
	public void test() throws Exception {
		
		User currUser = testUsers.get(0);
		currUser.setLocationLatitude(0.0);
		currUser.setLocationLongitude(0.0);
		User user = testUsers.get(1);
		user.setLocationLatitude(0.0);
		user.setLocationLongitude(0.0);
		userRepo.save(currUser);
		userRepo.save(user);
		userRepo.flush();
		
		Mockito.when(authService.getCurrentUser()).thenReturn(currUser);
		String encodedId = UserDto.encodeId(2, textEncryptor);
		userProfileResource.profileView(encodedId);
	}
}