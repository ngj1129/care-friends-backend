package hongikchildren.carefriends;

import hongikchildren.carefriends.caregiver.domain.Caregiver;
import hongikchildren.carefriends.user.domain.Gender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class CarefriendsApplicationTests {

	@Test
	void CaregiverConstructionTest(){
		Caregiver caregiver = Caregiver.builder()
				.name("taesoon")
				.phoneNumber("010-4784-9904")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1999, 4, 24))
				.build();

		String num = caregiver.getPhoneNumber();
		Assertions.assertThat("010-4784-9904").isEqualTo(num);
	}

}
