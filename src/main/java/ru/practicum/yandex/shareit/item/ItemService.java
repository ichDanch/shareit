package ru.practicum.yandex.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.booking.BookingMapper;
import ru.practicum.yandex.shareit.booking.BookingRepository;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.yandex.shareit.booking.model.Booking;
import ru.practicum.yandex.shareit.exceptions.NotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.item.dto.CommentDto;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.model.Comment;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.UserService;
import ru.practicum.yandex.shareit.user.UsersRepository;
import ru.practicum.yandex.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ItemService {
    private final ItemsRepository itemsRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final UsersRepository usersRepository;
    private final CommentsRepository commentsRepository;
    private final CommentMapper commentMapper;


    @Autowired
    public ItemService(ItemsRepository itemsRepository,
                       UserService userService,
                       BookingRepository bookingRepository,
                       ItemMapper itemMapper,
                       BookingMapper bookingMapper,
                       UsersRepository usersRepository,
                       CommentsRepository commentsRepository,
                       CommentMapper commentMapper) {
        this.itemsRepository = itemsRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
        this.usersRepository = usersRepository;
        this.commentsRepository = commentsRepository;
        this.commentMapper = commentMapper;
    }

    @Transactional
    public ItemDto saveItem(ItemDto itemDto, long userId) {
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("itemDto available must not be null");
        }
        if (itemDto.getName() == null || itemDto.getName().equals(" ") || itemDto.getName().equals("")) {
            throw new ValidationException("itemDto name must not be null or blank or empty");
        }
        if (itemDto.getDescription() == null) {
            throw new ValidationException("itemDto description must not be null");
        }

        User owner = userService.findById(userId);
        Item toItem = itemMapper.toItem(itemDto);
        toItem.setOwner(owner);
        Item item = itemsRepository.save(toItem);
        return itemMapper.toDto(item);
    }

    @Transactional
    public ItemDto patchItem(ItemDto itemDto, long itemId, long userId) {
        Item item = itemsRepository.findById(itemId)
                .orElseThrow(() ->
                        new NotFoundException("Does not contain item with this id or id is invalid " + itemId));

        if (item.getOwner() == null || item.getOwner().getId() != userId) {
            throw new NotFoundException("Only the owner can change item");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item patchedItem = itemsRepository.save(item);

        return itemMapper.toDto(patchedItem);
    }

    public List<ItemDto> findAllItemsByOwnerId(Long ownerId) {
        userService.findById(ownerId);
        List<Item> items = itemsRepository.findItemsByOwnerId(ownerId);

        return items.stream()
                .map(this::setLastAndNextBookingToItem)
                .map(this::setComments)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    public ItemDto findById(long itemId, long userId) {
        Item item = itemsRepository.findById(itemId)
                .orElseThrow(() ->
                        new NotFoundException("Does not contain item with this id or id is invalid " + itemId));

        ItemDto itemDto = itemMapper.toDto(item);

        if (item.getOwner().getId() == userId) {
           itemDto = setLastAndNextBookingToItem(item);
        }

        return setComments(itemDto);
    }
    public ItemDto setLastAndNextBookingToItem(Item item) {
        ItemDto itemDto = itemMapper.toDto(item);
        List<Booking> bookings = bookingRepository.findBookingsByItemId(item.getId());

        BookingDtoToItem lastBooking = bookings.stream()
                .sorted(Comparator.comparing(Booking::getEnd).reversed())
                .filter(b -> LocalDateTime.now().isAfter(b.getEnd()))
                .map(bookingMapper::toBookingDtoToItem)
                .findFirst()
                .orElse(null);

        BookingDtoToItem nextBooking = bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .filter(b -> LocalDateTime.now().isBefore(b.getStart()))
                .map(bookingMapper::toBookingDtoToItem)
                .findFirst()
                .orElse(null);

        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        return itemDto;
    }

    @Transactional
    public void deleteItemById(long itemId, long userId) {
        Item item = itemsRepository.findById(itemId)
                .orElseThrow(() ->
                        new NotFoundException("Does not contain item with this id or id is invalid " + itemId));
        checkOwner(userId, item);
        itemsRepository.deleteById(itemId);
    }

    public List<Item> findAllItems() {
        return itemsRepository.findAll();
    }

    public List<Item> itemsByNameAndDescription(String text) {
        if (text.isBlank()) {
            return new ArrayList<Item>();
        }
        return findAllItems()
                .stream()
                .filter(u -> u.getAvailable() == true)
                .filter(u ->
                        u.getName().toLowerCase().contains(text.toLowerCase())
                                || u.getDescription().toLowerCase().contains((text.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createCommentByUser(CommentDto commentDto, long itemId, long userId) {
        User user = usersRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Does not contain user with this id or id is invalid " + userId));
        Item item = itemsRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Does not contain item with this id or id is invalid " + userId));
        Booking booking = bookingRepository.findBookingByBookerIdAndEndIsBefore(userId, LocalDateTime.now())
                .orElseThrow(() ->
                        new ValidationException("Does not contain booking with this booker or id is invalid " + userId));
        if (commentDto.getText() == null || commentDto.getText().isBlank()) {
            throw new ValidationException("text is null or empty");
        }
        Comment comment = commentMapper.toComment(commentDto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        commentsRepository.save(comment);

        return commentMapper.toDto(comment);

    }

    public ItemDto setComments(ItemDto itemdto) {
        List<CommentDto> commentsDto = commentsRepository.findCommentByItemId(itemdto.getId())
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
        itemdto.setComments(commentsDto);
        return itemdto;
    }

    private void checkOwner(long userId, Item item) {
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Only the owner can change item");
        }
    }


}
