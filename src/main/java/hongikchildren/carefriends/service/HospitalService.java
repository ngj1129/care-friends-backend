package hongikchildren.carefriends.service;

import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.Hospital;
import hongikchildren.carefriends.domain.Treatment;
import hongikchildren.carefriends.repository.HospitalRepository;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    @Transactional
    public Hospital saveHospital(String name, String address, String phone) {

        Hospital hospital = Hospital.builder()
                .name(name)
                .address(address)
                .phone(phone)
                .build();

        return hospitalRepository.save(hospital);
    }

    public Hospital getHospitalByLinkOrCreate(String link, String title, String address, String telephone, Friend friend) {
        // link로 병원 조회
        return hospitalRepository.findByLink(link)
                .orElseGet(() -> {
                    // 병원이 없을 경우 새 병원 생성
                    Hospital hospital = Hospital.builder()
                            .link(link)
                            .name(title)
                            .address(address)
                            .phone(telephone)
                            .friend(friend)
                            .build();
                    return hospitalRepository.save(hospital);
                });
    }

    public Hospital getHospitalByLink(String link) {
        return hospitalRepository.findByLink(link).orElseThrow(() -> new NoSuchElementException("Hospital not found for link: " + link));
    }

    public List<Hospital> getAllHospitals(Friend friend) {
        return hospitalRepository.findAllByFriend(friend);
    }

    @Transactional
    public void deleteHospital(Long id) { hospitalRepository.deleteById(id); }
}
