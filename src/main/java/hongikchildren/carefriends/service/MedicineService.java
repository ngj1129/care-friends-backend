package hongikchildren.carefriends.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //클래스 단위에서는 readonly이고 조회 기능 이외에는 @Transactional 붙여야함.
public class MedicineService {

}
