package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
   private Texture playerImage;
   private Texture enemyImage;
   private SpriteBatch batch;
   private OrthographicCamera camera;
   private Rectangle player;
   private Rectangle enemy;
   private Array<Rectangle> enemies;
   private long lastDropTime;
   private  int time = 900000000;
   private int score = 0;
   private BitmapFont font;
   private int tap = 0;

   @Override
   public void create() {
       font = new BitmapFont();
       font.setColor(Color.WHITE);
       font.getData().setScale(5, 5);

      playerImage = new Texture(Gdx.files.internal("red.png"));
      enemyImage = new Texture(Gdx.files.internal("yellow.jpg"));

      // create the camera and the SpriteBatch
      camera = new OrthographicCamera();
      camera.setToOrtho(false, 480, 800);
      batch = new SpriteBatch();

      player = new Rectangle();
      player.x = 0;
      player.y = 20;
      player.width = 64;
      player.height = 64;

       enemies = new Array<Rectangle>();
       spawnEnemies();
   }

    private void spawnEnemies() {
        Rectangle enemy = new Rectangle();
        enemy.x = MathUtils.random(0, 480-350);
        enemy.y = 800;
        enemy.width = 350;
        enemy.height = 64;
        enemies.add(enemy);
        lastDropTime = TimeUtils.nanoTime();
    }



   @Override
   public void render() {
       Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
       Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

       camera.update();
       batch.setProjectionMatrix(camera.combined);

       batch.begin();
       batch.draw(playerImage, player.x, player.y);
       for (Rectangle enemy : enemies) {
           batch.draw(enemyImage, enemy.x, enemy.y);
       }
       batch.end();

       if (Gdx.input.justTouched()) {
           Vector3 touchPos = new Vector3();
           if (tap == 1) {
               tap = 0;
           } else {
               tap = 1;
           }
       }
       if (tap == 0) {
           player.x += 800 * Gdx.graphics.getRawDeltaTime();
       } else {
           player.x -= 800 * Gdx.graphics.getRawDeltaTime();
       }

       if (player.x < 0) player.x = 0;
       if (player.x > 480 - 64) player.x = 480 - 64;


       if (TimeUtils.nanoTime() - lastDropTime > time) {
           spawnEnemies();

       }

       for (Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext(); ) {
           Rectangle enemy = iter.next();
           enemy.y -= 300 * Gdx.graphics.getDeltaTime();
           if (enemy.y + 64 < 0) iter.remove();


       }
   }
   
   @Override
   public void dispose() {
      playerImage.dispose();
      enemyImage.dispose();
   }
}