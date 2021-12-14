package Callbacks;

import Main.SteamWorksController;
import com.codedisaster.steamworks.SteamFriends;
import com.codedisaster.steamworks.SteamFriendsCallback;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

import java.util.HashSet;

/**
 * @Author Bryan Hill
 */

public class SteamFriendsImpl implements SteamFriendsCallback {

    HashSet<SteamID> inDotaClientSet;
    HashSet<SteamID> inDotaMatchSet;

    public SteamFriendsImpl(HashSet<SteamID> inDotaClientSet, HashSet<SteamID> inDotaMatchSet) {
        this.inDotaClientSet = inDotaClientSet;
        this.inDotaMatchSet = inDotaMatchSet;
    }

    @Override
    public void onSetPersonaNameResponse(boolean b, boolean b1, SteamResult steamResult) {

    }

    @Override
    public void onPersonaStateChange(SteamID steamID, SteamFriends.PersonaChange personaChange) {
        // In some instances, this will trigger when one leaves a game.

        // Getting current game.
        SteamFriends.FriendGameInfo info = new SteamFriends.FriendGameInfo();
        SteamWorksController.getInstance().getSteamFriends().getFriendGamePlayed(steamID, info);

        // Adding to dota client set.
        addOrRemoveFromClientSet(info, steamID);
    }

    @Override
    public void onGameOverlayActivated(boolean b) {

    }

    @Override
    public void onGameLobbyJoinRequested(SteamID steamID, SteamID steamID1) {

    }

    @Override
    public void onAvatarImageLoaded(SteamID steamID, int i, int i1, int i2) {

    }

    @Override
    public void onFriendRichPresenceUpdate(SteamID steamID, int i) {
        // This is where game change notifications will take place.

        // Getting current game.
        SteamFriends.FriendGameInfo info = new SteamFriends.FriendGameInfo();
        SteamWorksController.getInstance().getSteamFriends().getFriendGamePlayed(steamID, info);

        // Getting rich context
        String richPresence = SteamWorksController.
                getInstance().
                getSteamFriends().
                getFriendRichPresence(steamID, "steam_display");

        // Adding to dota client set.
        addOrRemoveFromClientSet(info, steamID);

        // If "hero-select" is enabled
        System.out.println(richPresence);
        if (richPresence.contains("Select")) {
            SteamWorksController.getInstance().addToInGameSet(steamID);
            System.out.println(
                    "GameID: " + info.getGameID() + "\n" +
                    "GameIP: " + info.getGameIP() + "\n" +
                    "Game Port: " + info.getGamePort() + "\n" +
                    "Game Lobby: " + info.getSteamIDLobby()
            );
        }
        if (richPresence.equals("Main Menu")) {
            SteamWorksController.getInstance().removeFromInGameSet(steamID);
        }
    }

    @Override
    public void onGameRichPresenceJoinRequested(SteamID steamID, String s) {

    }

    @Override
    public void onGameServerChangeRequested(String s, String s1) {

    }

    /**
     * Method will remove or add users to the Dota 2 client set based on if they are playing the game.
     * @param info Object to information on user's current game they are playing.
     * @param steamID Object identifying the user being examined.
     */
    public void addOrRemoveFromClientSet(SteamFriends.FriendGameInfo info, SteamID steamID) {
        if (info.getGameID() == SteamWorksController.getInstance().DOTA_2_ID) {
            SteamWorksController.getInstance().addToClientSet(steamID);
        } else {
            SteamWorksController.getInstance().removeFromClientSet(steamID);
            SteamWorksController.getInstance().removeFromInGameSet(steamID);
        }
    }

}
