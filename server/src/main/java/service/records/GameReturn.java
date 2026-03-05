package service.records;

// just for listing games without needing to serialize gameName

public record GameReturn(int gameID, String whiteUsername, String blackUsername, String gameName) { }
