package com.rohan.econome;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Level implements Screen {

	protected TiledMap map;
	protected TiledMapTileLayer bg;
	protected TiledMapTileLayer fg;
	protected TiledMapTileLayer oj;
	protected TiledMapTileLayer oh;
	protected TiledMapTileLayer zones;
	protected OrthogonalTiledMapRenderer mapRenderer;
	private String regionName;
	protected ArrayList<Rectangle> colliders;
	protected ArrayList<Item> itemsOnScreen;
	protected ArrayList<ComponentEntity> critters;
	protected ArrayList<Zone> zoneArray;

	protected OrthographicCamera cam;

	protected Player player;
	protected GameStateManager gsm;

	protected Music levelMusic;

	protected ArrayList<TiledMap> level;
	protected DialogueHandler dh;

	protected SpriteBatch sb;
	private Jukebox jukebox;
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

	private World world;

	public Level(GameStateManager inputGSM, String inputLevel) {
		gsm = inputGSM;
		sb = gsm.getAM().getSB();
		jukebox = gsm.getAM().getJukebox();

		cam = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		// No need for the two below, Don't try to add them
		// Each ui element will have their own stage which will be
		// pushed and popped in the GSM.
		// stage = new Stage();
		// Gdx.input.setInputProcessor(stage);

		level = new ArrayList<TiledMap>();
		critters = new ArrayList<ComponentEntity>();
		itemsOnScreen = new ArrayList<Item>();
		zoneArray = new ArrayList<Zone>();
		dh = new DialogueHandler();
		regionName = "Spawntopia";
		world = new World(new Vector2(0, -98f), true);

		areaLoad(inputLevel);

		player = new Player(30, 120, this);

		Chicken chicken = new Chicken(30, 120, this);
		addEntity(chicken);

	}

	public void addEntity(ComponentEntity e) {
		critters.add(e);
	}

	public World getWorld() {
		return world;
	}

	public void areaLoad(String inputString) {
		System.out.println("Level.areaLoad() is beginning");
		map = makeTileMap(inputString);
		mapRenderer = new OrthogonalTiledMapRenderer(map, sb);

		// mapRenderer.setView(cam);

	}

	public String getRegionName() {
		return regionName;
	}

	public void cameraHandler() {

		cam.position.set(player.getX()
				+ player.getTextureRegion().getRegionWidth() / 2, player.getY()
				+ player.getTextureRegion().getRegionHeight() / 2, 0);
		if (cam.position.x < 0 + cam.viewportWidth / 2) {
			cam.position.x = cam.viewportWidth / 2;
		}
		if (cam.position.x > (map.getProperties().get("width", Integer.class) * map
				.getProperties().get("tilewidth", Integer.class))
				- cam.viewportWidth / 2) {
			cam.position.x = (map.getProperties().get("width", Integer.class) * map
					.getProperties().get("tilewidth", Integer.class))
					- cam.viewportWidth / 2;
		}
		if (cam.position.y < 0 + cam.viewportHeight / 2) {
			cam.position.y = cam.viewportHeight / 2;
		}
		if (cam.position.y > (map.getProperties().get("height", Integer.class) * map
				.getProperties().get("tileheight", Integer.class))
				- cam.viewportHeight / 2) {
			cam.position.y = (map.getProperties().get("height", Integer.class) * map
					.getProperties().get("tileheight", Integer.class))
					- cam.viewportHeight / 2;
		}
		cam.update();
	}

	@Override
	public void render(float delta) {
		update();
		drawMap();
	}

	public void drawMap() {

		// System.out.println("Level.drawMap() is drawing");

		mapRenderer.setView(cam);

		sb.begin();

		mapRenderer.renderTileLayer(bg);
		mapRenderer.renderTileLayer(fg);
		mapRenderer.renderTileLayer(oj);
		mapRenderer.renderTileLayer(zones);

		for (Item i : itemsOnScreen) {
			sb.draw(i.getTexture(), i.getLocation().x, i.getLocation().y);
		}

		sb.draw(player.getFrame(), player.getX()
				- player.getFrame().getRegionWidth() / 2, player.getY());

		for (ComponentEntity e : critters) {
			sb.draw(e.getTexture(), e.getX(), e.getY());
		}

		ArrayList<DustCloud> tempCloudList = player.getDustList();
		for (DustCloud d : tempCloudList) {
			sb.draw(d.getTexture(), d.getX(), d.getY(), 8, 8);
		}

		mapRenderer.renderTileLayer(oh);
		sb.end();
	}

	public void entityUpdate() {
		for (ComponentEntity e : critters) {
			e.update();
		}
	}

	public Camera getCamera() {
		return cam;
	}

	public ArrayList<Rectangle> getColliders() {
		return colliders;
	}

	public ArrayList<ComponentEntity> getCritters() {
		return critters;
	}

	public MapProperties getCurrentMapProperties() {
		return map.getProperties();
	}

	public ArrayList<Item> getItems() {
		return itemsOnScreen;
	}

	public Player getPlayer() {
		return player;
	}

	public SpriteBatch getSB() {
		return sb;
	}

	public SpriteBatch getSpriteBatch() {
		return sb;
	}

	public ArrayList<Zone> getZones() {
		return zoneArray;
	}

	public void itemUpdate() {
		for (Item i : itemsOnScreen) {
			i.handleInteraction(player.getInteraction());
		}
	}

	public TiledMap makeTileMap(String inputString) {
		TiledMap tileMap = new TmxMapLoader().load(inputString);
		colliders = new ArrayList<Rectangle>();
		bg = (TiledMapTileLayer) tileMap.getLayers().get("Background");
		fg = (TiledMapTileLayer) tileMap.getLayers().get("Foreground");
		oj = (TiledMapTileLayer) tileMap.getLayers().get("Objects");
		oh = (TiledMapTileLayer) tileMap.getLayers().get("Overhead");
		zones = (TiledMapTileLayer) tileMap.getLayers().get("Zones");

		for (int row = 0; row < oj.getHeight(); row++) {
			for (int col = 0; col < oj.getWidth(); col++) {
				Cell cell = oj.getCell(col, row);
				if (cell == null)
					continue;
				if (cell.getTile() == null)
					continue;

				Rectangle r = new Rectangle(col * oj.getTileWidth(), row
						* oj.getTileHeight(), oj.getTileWidth(),
						oj.getTileHeight());
				colliders.add(r);
			}
		}

		for (int row = 0; row < zones.getHeight(); row++) {
			for (int col = 0; col < zones.getWidth(); col++) {
				Cell cell = zones.getCell(col, row);
				if (cell == null)
					continue;
				if (cell.getTile() == null)
					continue;
				Rectangle r = new Rectangle(col * zones.getTileWidth(), row
						* zones.getTileHeight(), zones.getTileWidth(),
						zones.getTileHeight());

				String cellType = (String) cell.getTile().getProperties()
						.get("Type");
				if (cellType != null) {
					System.out.println("Found cell Type: " + cellType);
					switch (cellType) {
					case "Teleporter":
						System.out
								.println("The destination for the cell on this map is : ");
						System.out.println(cell.getTile().getProperties()
								.get("Destination"));
						System.out.println("The type is : ");
						System.out.println(cell.getTile().getProperties()
								.get("MapType"));
						TeleZone t = new TeleZone(r, (String) cell.getTile()
								.getProperties().get("Destination"),
								(String) cell.getTile().getProperties()
										.get("mapType"));
						zoneArray.add(t);
						break;
					case "Ladder":
						System.out.println("Adding a ladder zone");
						LadderZone l = new LadderZone(r);
						zoneArray.add(l);
						break;
					default:
						continue;
					}
				}
			}
		}

		return tileMap;
	}

	public void setPlayer(Player inputPlayer) {
		player = inputPlayer;
	}

	public void setZoneArray(ArrayList<Zone> inputZones) {
		this.zoneArray = inputZones;
	}

	public void startMusic() {
		levelMusic.play();
	}

	public void stopMusic() {
		levelMusic.stop();
	}

	public void stopPlayer() {
		player.fullStop();
	}

	public void update() {
		player.update();
		entityUpdate();
		cameraHandler();

		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}