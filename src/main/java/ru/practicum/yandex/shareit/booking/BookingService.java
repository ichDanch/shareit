package ru.practicum.yandex.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.booking.model.Booking;
import ru.practicum.yandex.shareit.exceptions.ItemNotFoundException;
import ru.practicum.yandex.shareit.item.model.Item;

@Service
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }
    public Booking findById(long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ItemNotFoundException("Does not contain booking with this id or id is invalid " + id));
    }
}
