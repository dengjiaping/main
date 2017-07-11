package cn.ngame.store.game.bean;

import java.io.Serializable;

/**
 * @author zeng
 * @since 2016/12/15
 */
public class GameStrategy implements Serializable {

    private long id;
    private String strategyContent;
    private long gameId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStrategyContent() {
        return strategyContent;
    }

    public void setStrategyContent(String strategyContent) {
        this.strategyContent = strategyContent;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }
}
