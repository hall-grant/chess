package service.records;

public record JoinRequest(String authToken, Integer gameID, String playerColor) { }
