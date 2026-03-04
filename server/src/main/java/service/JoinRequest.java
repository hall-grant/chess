package service;

public record JoinRequest(String authToken, Integer gameID, String playerColor) { }
