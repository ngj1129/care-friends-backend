package hongikchildren.carefriends.location.service;


import hongikchildren.carefriends.location.domain.Location;
import hongikchildren.carefriends.friend.domain.Friend;
import hongikchildren.carefriends.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    @Transactional
    public void save(Location location) {
        locationRepository.save(location);
    }

    public List<Location> findAll(Friend friend) {
        return locationRepository.findLocationsByFriend(friend);
    }
}
