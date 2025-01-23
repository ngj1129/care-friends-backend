package hongikchildren.carefriends.videocall;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SignalHandler extends TextWebSocketHandler {

    // 방 별로 연결된 세션 저장 (roomName -> 세션 리스트)
    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New WebSocket connection: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // JSON 메시지를 파싱
        String payload = message.getPayload();
        Map<String, Object> data = parseMessage(payload);

        String type = (String) data.get("type");
        String roomName = (String) data.get("room");
        String target = (String) data.get("target");

        switch (type) {
            case "join_room":
                handleJoinRoom(session, roomName);
                break;
            case "offer":
            case "answer":
            case "candidate":
                sendMessageToRoom(session, roomName, target, payload);
                break;
            default:
                System.out.println("Unknown message type: " + type);
        }
    }

    private void handleJoinRoom(WebSocketSession session, String roomName) throws Exception {
        roomSessions.putIfAbsent(roomName, ConcurrentHashMap.newKeySet());
        roomSessions.get(roomName).add(session);

        System.out.println("User joined room: " + roomName + session.getId());

        // 현재 클라이언트에게 고유 ID 전송
        String myMessage = String.format("{\"type\": \"id\", \"id\": \"%s\"}", session.getId());
        session.sendMessage(new TextMessage(myMessage));


        // 현재 방의 사용자 목록 전달
        Set<WebSocketSession> sessions = roomSessions.get(roomName);
        List<String> userIds = new ArrayList<>();
        for (WebSocketSession s : sessions) {
            if (!s.equals(session)) {
                System.out.println("현재 있는 사람: " + s.getId());
                userIds.add(s.getId());
            }
        }

        // 기존 사용자들에게 새로운 사용자가 들어왔음을 알림
        String newUserMessage = String.format("{\"type\": \"new_user\", \"id\": \"%s\"}", session.getId());
        for (WebSocketSession s : sessions) {
            if (!s.equals(session)) {
                s.sendMessage(new TextMessage(newUserMessage));
            }
        }
    }

    // 동기화 필요
    private synchronized void sendMessageToRoom(WebSocketSession sender, String roomName, String target, String message) throws Exception {
        if (roomName == null || !roomSessions.containsKey(roomName)) {
            System.out.println("Room not found or invalid roomName");
            return;
        }

        Set<WebSocketSession> sessions = roomSessions.get(roomName);
        System.out.println("Sending message to room: " + roomName);
        System.out.println("Message: " + message);

        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                System.out.println("Session name: " + session.getId());
                // target 에게만 메시지 전송
                if (session.isOpen() && session.getId().equals(target) && !session.equals(sender)) {
                    System.out.println("Sending message to: " + session.getId());
                    session.sendMessage(new TextMessage(message));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 세션이 종료되면 모든 방에서 제거
        roomSessions.values().forEach(sessions -> sessions.remove(session));
        System.out.println("WebSocket connection closed: " + session.getId());
    }

    private Map<String, Object> parseMessage(String message) {
        // 간단한 JSON 파싱 (Jackson 라이브러리 사용 추천)
        Map<String, Object> data = new HashMap<>();
        try {
            data = new com.fasterxml.jackson.databind.ObjectMapper().readValue(message, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}

