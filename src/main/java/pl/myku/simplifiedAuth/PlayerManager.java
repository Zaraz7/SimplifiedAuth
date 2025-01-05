package pl.myku.simplifiedAuth;

import java.util.HashMap;

public class PlayerManager extends HashMap<String, Player>{
    public Player get(net.minecraft.core.entity.player.Player player){
        String username = player.username;
        if (containsKey(username)){
            return super.get(username);
        }
        Player newPlayer = new Player(player);
        put(username, newPlayer);
        return newPlayer;
    }
}
