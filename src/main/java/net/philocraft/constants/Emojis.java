package net.philocraft.constants;

public enum Emojis {
    
    MUSIC_PAUSE("<:Pause:1044661653247098930>"),
    MUSIC_SKIP("<:Skip:846709694973607956>"),
    MUSIC_RESUME("<:Resume:1044661652068511784>"),
    MUSIC_LOOP("<:Loop:936942637473226752>"),
    MUSIC_LEAVE("<:Leave:937006597782269954>"),
    MUSIC_JOIN("<:Join:937006558699724801>"),
    MUSIC_ERROR("<:Cross:969148307123359754>"),
    MUSIC_SHUFFLE("<:Shuffle:936980188623958046>"),
    MUSIC_STOP("<:Stop:846712942221459478>");

    private String emoji;

    private Emojis(String emoji) {
        this.emoji = emoji;
    }

    public String get() {
        return this.emoji;
    }
}
