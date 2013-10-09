package com.me.mygdxgame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class WorldRenderer {

 private World world;
 private OrthographicCamera cam;

 /** for debug rendering **/
 ShapeRenderer debugRenderer = new ShapeRenderer();

 public WorldRenderer(World world) {
  this.world = world;
  this.cam = new OrthographicCamera(10, 7);
  this.cam.position.set(5, 3.5f, 0);
  this.cam.update();
  
  spriteBatch = new SpriteBatch();
  loadTextures();
 }

 
	

	private static final float CAMERA_WIDTH = 10f;
	private static final float CAMERA_HEIGHT = 7f;
	private SpriteBatch spriteBatch;
	private boolean debug = false;
	private int width;
	private int height;
	private float ppuX;	// pixels per unit on the X axis
	private float ppuY;	// pixels per unit on the Y axis
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;
		ppuX = (float)width / CAMERA_WIDTH;
		ppuY = (float)height / CAMERA_HEIGHT;
	}
   private void drawBlocks() {
	  
	          for (Block block : world.getBlocks()) {
	              spriteBatch.draw(blockTexture, block.position.x * ppuX, block.position.y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
	          }
	        
	      }


/** Textures **/
	private Texture bobTexture;
	private Texture blockTexture;
	
	
	
	private static final int        FRAME_COLS = 6;      
    private static final int        FRAME_ROWS = 5;        
    Animation                       walkAnimation;          
    Texture                         walkSheet;              
    TextureRegion[]                 walkFrames;             
    TextureRegion                   currentFrame;           
    
    float stateTime;                                        

	private void loadTextures() {
		walkSheet = new  Texture(Gdx.files.internal("animation_sheet.png"));
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / 
				FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
		walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
                for (int j = 0; j < FRAME_COLS; j++) {
                        walkFrames[index++] = tmp[i][j];
                }
        }
        walkAnimation = new Animation(0.025f, walkFrames);

        stateTime = 0f;
        
        bobTexture = new  Texture(Gdx.files.internal("images/bob.png"));
        blockTexture = new Texture(Gdx.files.internal("images/block.png"));
	}
	public void render() {
		 spriteBatch.begin();
				drawBlocks();
				drawBob();
		 spriteBatch.end();
			
			if (debug)
				drawDebug();
		  }
	private void drawBob() {
		Bob bob = world.getBob();
		
		int facex=1;
		if(bob.facingLeft){
			facex=-1;
		}
		
		if (bob.state==bob.state.WALKING){
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame, bob.position.x * ppuX, bob.position.y * ppuY, facex*Bob.SIZE * ppuX, Bob.SIZE * ppuY);

		}
		else if(bob.state==bob.state.IDLE){
			spriteBatch.draw(bobTexture, bob.position.x * ppuX, bob.position.y * ppuY, facex* Bob.SIZE * ppuX, Bob.SIZE * ppuY);
		}
			
			//spriteBatch.draw(bobTexture, bob.position.x * ppuX, bob.position.y * ppuY, Bob.SIZE * ppuX, Bob.SIZE * ppuY);
		
	}
	private void drawDebug() {
		// render blocks
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);
		for (Block block : world.getBlocks()) {
			Rectangle rect = block.bounds;
			float x1 = block.position.x + rect.x;
			float y1 = block.position.y + rect.y;
			debugRenderer.setColor(new Color(1, 0, 0, 1));
			debugRenderer.rect(x1, y1, rect.width, rect.height);
		}
		// render Bob
		Bob bob = world.getBob();
		Rectangle rect = bob.bounds;
		float x1 = bob.position.x + rect.x;
		float y1 = bob.position.y + rect.y;
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(x1, y1, rect.width, rect.height);
		debugRenderer.end();
	}
 }
 