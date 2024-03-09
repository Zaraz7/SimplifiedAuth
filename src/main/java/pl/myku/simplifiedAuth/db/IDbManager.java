package pl.myku.simplifiedAuth.db;

public interface IDbManager {
    Boolean loginPlayer(String username, String password);
    Boolean isPlayerRegistered(String username);
    Boolean isSessionValid(String username, String address);
    void addPlayerToDatabase(String username, String password);
    void changePassword(String username, String password);
    void updateLastSeenAndAddress(String username, String address);
    void reloadDb();
}
