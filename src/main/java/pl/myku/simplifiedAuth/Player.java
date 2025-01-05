package pl.myku.simplifiedAuth;

public class Player {
    private final net.minecraft.core.entity.player.Player player;
    private boolean authorized = false;
    public Player(net.minecraft.core.entity.player.Player player){
        this.player = player;
    }

    public void authorize() {
        authorized = true;
    }
    public boolean isAuthorized() {
        return authorized;
    }
    public void destroy() { authorized = false; }
}
