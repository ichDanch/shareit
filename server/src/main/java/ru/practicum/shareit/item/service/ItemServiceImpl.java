package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentsRepository;
import ru.practicum.shareit.item.repository.ItemsRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestsRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemsRepository itemsRepository;
    private final BookingRepository bookingRepository;
    private final UsersRepository usersRepository;
    private final CommentsRepository commentsRepository;
    private final ItemRequestsRepository itemRequestsRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;


    @Autowired
    public ItemServiceImpl(ItemsRepository itemsRepository,
                           BookingRepository bookingRepository,
                           ItemMapper itemMapper,
                           BookingMapper bookingMapper,
                           UsersRepository usersRepository,
                           CommentsRepository commentsRepository,
                           CommentMapper commentMapper, ItemRequestsRepository itemRequestsRepository) {
        this.itemsRepository = itemsRepository;
        this.bookingRepository = bookingRepository;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
        this.usersRepository = usersRepository;
        this.commentsRepository = commentsRepository;
        this.commentMapper = commentMapper;
        this.itemRequestsRepository = itemRequestsRepository;
    }

    @Override
    @Transactional
    public ItemDto saveItem(ItemDto itemDto, long userId) {
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("itemDto available must not be null " +
                    "Method [saveItem] class [ItemServiceImpl] ");
        }
        if (itemDto.getName() == null || itemDto.getName().equals(" ") || itemDto.getName().equals("")) {
            throw new ValidationException("itemDto name must not be null or blank or empty " +
                    "Method [saveItem] class [ItemServiceImpl] ");
        }
        if (itemDto.getDescription() == null) {
            throw new ValidationException("itemDto description must not be null " +
                    "Method [saveItem] class [ItemServiceImpl] ");
        }

        User owner = checkUser(userId);
        Item toItem = itemMapper.toItem(itemDto);
        toItem.setOwner(owner);

        if (itemDto.getRequestId() != 0) {
            ItemRequest itemRequest = itemRequestsRepository.findById(itemDto.getRequestId()).orElseThrow(() ->
                    new NotFoundException(
                            "Does not contain itemRequest with this id or id is invalid " +
                            "Method [saveItem] class [ItemServiceImpl] "+ itemDto.getRequestId())
            );
            toItem.setItemRequest(itemRequest);
        }
        Item item = itemsRepository.save(toItem);
        return itemMapper.toDto(item);
    }

    @Override
    @Transactional
    public ItemDto patchItem(ItemDto itemDto, long itemId, long userId) {
        Item item = checkItem(itemId);

        if (item.getOwner() == null || item.getOwner().getId() != userId) {
            throw new NotFoundException("Only the owner can change item" +
                    "Method [patchItem] class [ItemServiceImpl] ");
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

    @Override
    public List<ItemDto> findAllItemsByOwnerId(int from, int size, Long ownerId) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("from or size are not valid" +
                    "Method [findAllItemsByOwnerId] class [ItemServiceImpl] ");
        }
        User user = checkUser(ownerId);
        Pageable pageWithElements = PageRequest.of(from / size, size, Sort.by("id"));
        Page<Item> page = itemsRepository.findItemsByOwner(user, pageWithElements);
        List<Item> items = page.get().collect(Collectors.toList());

        return items.stream()
                .map(this::setLastAndNextBookingToItem)
                .map(this::setComments)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(long itemId, long userId) {
        Item item = checkItem(itemId);

        ItemDto itemDto = itemMapper.toDto(item);

        if (item.getOwner().getId() == userId) {
            itemDto = setLastAndNextBookingToItem(item);
        }

        return setComments(itemDto);
    }

    @Override
    @Transactional
    public void deleteItemById(long itemId, long userId) {
        Item item = checkItem(itemId);
        checkOwner(userId, item);
        itemsRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> itemsByNameAndDescription(int from, int size, String text) {
        if (text.isBlank()) {
            return new ArrayList<ItemDto>();
        }

        if (from < 0 || size <= 0) {
            throw new ValidationException("from or size are not valid. " +
                    "Method [itemsByNameAndDescription] class [ItemServiceImpl] ");
        }
        Pageable pageWithElements = PageRequest.of(from / size, size, Sort.by("id"));
        Page<Item> page = itemsRepository
                .findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        text, text, pageWithElements);
        List<Item> items = page.get().collect(Collectors.toList());

        return items.stream()
                .map(this::setLastAndNextBookingToItem)
                .map(this::setComments)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto createCommentByUser(CommentDto commentDto, long itemId, long userId) {
        User user = checkUser(userId);
        Item item = checkItem(itemId);
        Booking booking = checkBooking(userId);
        if (commentDto.getText() == null || commentDto.getText().isBlank()) {
            throw new ValidationException("text is null or empty. Method [createCommentByUser] class [ItemServiceImpl] ");
        }
        Comment comment = commentMapper.toComment(commentDto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        commentsRepository.save(comment);

        return commentMapper.toDto(comment);

    }

    private ItemDto setLastAndNextBookingToItem(Item item) {
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

    private ItemDto setComments(ItemDto itemdto) {
        List<CommentDto> commentsDto = commentsRepository.findCommentByItemId(itemdto.getId())
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
        itemdto.setComments(commentsDto);
        return itemdto;
    }

    private void checkOwner(long userId, Item item) {
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Only the owner can change item. " +
                    "Method [checkOwner] class [ItemServiceImpl] " + " userId = " + userId);
        }
    }

    private User checkUser(long userId) {
        return usersRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Does not contain [user] with this id or id is invalid. " +
                        "Method [checkUser] class [ItemServiceImpl]" + " userId = " + userId));
    }

    private Item checkItem(long itemId) {
        return itemsRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Does not contain [item] with this id or id is invalid. " +
                        "Method [checkUser] class [ItemServiceImpl]" + " itemId = " + itemId));
    }

    private Booking checkBooking(long bookerId) {
        return bookingRepository.findBookingByBookerIdAndEndIsBefore(bookerId, LocalDateTime.now())
                .orElseThrow(() ->
                        new ValidationException("Does not contain [booking] with this booker or id is invalid. " +
                                "Method [checkBooking] class [ItemServiceImpl]" + " bookerId = " + bookerId));
    }


}
