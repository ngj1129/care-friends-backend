package hongikchildren.carefriends.service;

import hongikchildren.carefriends.domain.*;
import hongikchildren.carefriends.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true) //클래스 단위에서는 readonly이고 조회 기능 이외에는 @Transactional 붙여야함.
@RequiredArgsConstructor //lombok으로 생성자 자동 의존성 주입 -> 바꾸자고 제안하기
public class MedicineService {

    private final MedicineRepository medicineRepository;
    //private final ScheduleRepository scheduleRepository;

    /*
    @Id @GeneratedValue
    @Column(name="taskId")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="scheduleId")
    private Schedule schedule;

    private Long groupId;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalTime startTime;
    private LocalTime signalTime;
    private String title;
    private String location;
    private String memo;
     */

    //등록 시 id 반환과 엔티티 반환 차이
    //인자 받는 방법: Medicine 객체 or 모든 필드
    @Transactional
    public Medicine saveMedicine(String name, TakeTime takeTime, LocalDate takeStart, LocalDate takeEnd, int numPerDay, String imgUrl, String caution, String effect) {
        Medicine medicine = Medicine.builder()
                .name(name)
                .takeTime(takeTime)
                .takeStart(takeStart)
                .takeEnd(takeEnd)
                .numPerDay(numPerDay)
                .imgUrl(imgUrl)
                .caution(caution)
                .effect(effect)
                .build();


        /**
         * 자동 Task 추가
         */
        for (LocalDate date = takeStart; !date.isAfter(takeEnd); date = date.plusDays(1)) {
            Task task = Task.builder()
                    .day(date)
                    .title(name)
                    .taskType(TaskType.MEDICINE)
                    .status(Status.YET)
                    .startTime(LocalTime.now())
                    .signalTime(LocalTime.now())
                    .build();


//            Schedule schedule;
//            if (scheduleRepository.findByDay(date).isPresent()) {
//                //원래 있던 스케줄 가져옴
//                schedule = scheduleRepository.findByDay(date).get();
//            }
//            else {
//                //스케줄 새로 생성
//                schedule = Schedule.builder()
//                        .day(date)
//                        .build();
//                scheduleRepository.save(schedule);
//            }
//            schedule.addTask(task);
//            task.setSchedule(schedule);
        }
        return medicineRepository.save(medicine);
    }

    //프렌즈 -> 약 조회
    public List<Medicine> getAllMedicines(Friend friend) {
        return medicineRepository.findByFriend(friend);
    }

    //업데이트

    //삭제
    @Transactional
    public void deleteMedicine(Long id) { medicineRepository.deleteById(id); }

}
