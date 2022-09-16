package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.ItemDto;

import java.util.List;
import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItemRequest(ItemRequestDto itemRequestDto, long userId) {
        return post("", userId, itemRequestDto);
    }

//    public ResponseEntity<Object> patchRequest(ItemDto itemDto, long itemId, long userId) {
//        return patch("/" + itemId, userId, itemDto);
//    }
//
//    public ResponseEntity<Object> getRequestById(long itemId, long userId) {
//        return get("/" + itemId, userId);
//    }
//
//    public ResponseEntity<Object> getAllItemsByOwnerId(int from, int size, long ownerId) {
//        Map<String, Object> parameters = Map.of(
//                "from", from,
//                "size", size
//        );
//        return get("?from={from}&size={size}", ownerId, parameters);
//    }
//
//    public ResponseEntity<Object> searchItemsByNameAndDescription(long from, long size, String text) {
//        Map<String, Object> parameters = Map.of(
//                "text", text,
//                "from", from,
//                "size", size
//        );
//        return get("/search?text={text}&from={from}&size={size}", null, parameters);
//    }
//
//    public ResponseEntity<Object> deleteItem(long itemId, long userId) {
//        return delete("/" + itemId, userId);
//    }
//
//    public ResponseEntity<Object> createComment(CommentDto commentDto, long itemId, long userId) {
//        return post("/" + itemId + "/comment", userId, commentDto);
//    }

    public ResponseEntity<Object> findRequestsByOwnerId(long ownerId) {
        return get("",ownerId);
    }

    public ResponseEntity<Object> findRequestById(long userId, long requestId) {
        return get("/" + requestId, userId);

    }

    public ResponseEntity<Object> findAllRequests(long from, long size, long userId) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from=" + from + "&size=" + size, userId, parameters);
    }
}
