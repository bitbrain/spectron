package de.bitbrain.spectron.core;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.spectron.Colors;

public class GameObjectController {

    private final TweenManager tweenManager;

    private Grid grid;

    private GameObjectFactory factory;

    private List<GameObject> players;

    public static enum Move {
        LEFT(-1, 0),
        TOP(0, 1),
        RIGHT(1, 0),
        BOTTOM(0, -1);

        private Vector2 direction;

        Move(int x, int y) {
            this.direction = new Vector2(x, y).nor();
        }

        public Vector2 getDirection() {
            return direction;
        }
    }

    public GameObjectController(Grid grid, TweenManager tweenManager, GameObjectFactory factory) {
        this.grid = grid;
        this.factory = factory;
        this.tweenManager = tweenManager;
        this.players = new ArrayList<GameObject>();
    }

    public void init() {
        players.add(factory.createPlayer(1, 1, Colors.ORANGE));
        players.add(factory.createPlayer(8, 2, Colors.BLUE));
    }

    public void move(int playerId, Move move) {
        if (players.size() > playerId && playerId >= 0) {
            GameObject player = players.get(playerId);
            if (tweenManager.containsTarget(player, GameObjectTween.POS_X)) {
                return;
            }
            if (tweenManager.containsTarget(player, GameObjectTween.POS_Y)) {
                return;
            }
            float targetX = player.getLeft() + move.getDirection().x * grid.getCellSize();
            float targetY = player.getTop() + move.getDirection().y  * grid.getCellSize();
            if (targetX > player.getLeft()) {
                targetX += grid.getOffsetX();
            } else if (targetX < player.getLeft()) {
                targetX -= grid.getOffsetX();
            }
            if (targetY > player.getTop()) {
                targetY += grid.getOffsetY();
            }  else if (targetY < player.getTop()) {
                targetY -= grid.getOffsetY();
            }
            Tween.to(player, GameObjectTween.POS_X, 0.35f).target(targetX).ease(TweenEquations.easeInOutCubic).start(tweenManager);
            Tween.to(player, GameObjectTween.POS_Y, 0.35f).target(targetY).ease(TweenEquations.easeInOutCubic).start(tweenManager);

        }
    }
}