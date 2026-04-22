package funs.gamez;

import com.gamez.tap_gopher.collections.StarCollection;

/**
 * @ProjectName: TapGophers
 * @Package: funs.gamez
 * @ClassName: IGameController
 * @Description: {@link StarCollection}
 */
public interface IGameController {
    /**
     * 是否消费游戏结束的事件
     * false : GAME_OVER
     * true : GAME_PAUSE
     * @return
     */
    boolean onGameOverConfirm();
}
