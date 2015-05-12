package com.rohan.econome;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class PlayScreen implements Screen {

	SpriteBatch sb;
	Jukebox jukebox;
	Stage stage;
	GameStateManager gsm;
	Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

	private String currentMap;
	private Level currentLevel;
	protected TiledMap map;
	protected TiledMapTileLayer bg;
	protected TiledMapTileLayer fg;
	protected TiledMapTileLayer oj;
	protected TiledMapTileLayer oh;
	protected TiledMapTileLayer zones;
	protected OrthogonalTiledMapRenderer mapRenderer;
	protected ArrayList<Rectangle> colliders;
	protected ArrayList<Item> itemsOnScreen;
	protected ArrayList<Entity> critters;
	protected ArrayList<Zone> zoneArray;
	private Camera cam;

	private Player player;
	private BitmapFont chatFont;
	private ArrayList<TiledMap> level;
	private DialogueHandler dh;

	public PlayScreen(String inputLevel) {

		// Super spooky
		gsm = AssetManager.getGSM();

		sb = gsm.getAM().getSB();
		jukebox = gsm.getAM().getJukebox();

		stage = new Stage();
		cam = stage.getCamera();
		Gdx.input.setInputProcessor(stage);

		level = new ArrayList<TiledMap>();
		critters = new ArrayList<Entity>();
		itemsOnScreen = new ArrayList<Item>();
		zoneArray = new ArrayList<Zone>();
		dh = new DialogueHandler();
		chatFont = new BitmapFont();
		areaLoad(inputLevel);

		player = new Player(30, 30, this);

	}

	@Override
	public void show() {

		sb = gsm.getAM().getSB();
		jukebox = gsm.getAM().getJukebox();

		stage = new Stage();
		cam = stage.getCamera();
		Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void render(float delta) {
		drawMap();

	}

	public ArrayList<Rectangle> getColliders() {
		return colliders;
	}

	public ArrayList<Entity> getCritters() {
		return critters;
	}

	public ArrayList<Zone> getZones() {
		return zoneArray;
	}

	public ArrayList<Item> getItems() {
		return itemsOnScreen;
	}

	public MapProperties getCurrentMapProperties() {
		return map.getProperties();
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

	public void drawMap() {
		sb.begin();
		mapRenderer.setView((OrthographicCamera) cam);

		mapRenderer.renderTileLayer(bg);
		mapRenderer.renderTileLayer(fg);
		mapRenderer.renderTileLayer(oj);
		mapRenderer.renderTileLayer(zones);

		for (Item i : itemsOnScreen) {
			sb.draw(i.getTexture(), i.getLocation().x, i.getLocation().y);
		}

		sb.draw(player.getFrame(), player.getX()
				- player.getFrame().getRegionWidth() / 2, player.getY());

		for (Entity e : critters) {
			sb.draw(e.getTexture(), e.getX(), e.getY());
		}

		mapRenderer.renderTileLayer(oh);
		sb.end();
	}

	public void addEntity(Entity e) {
		critters.add(e);
	}

	public void areaLoad(String inputString) {
		System.out.println("Level.areaLoad() is beginning");
		map = makeTileMap(inputString);
		mapRenderer = new OrthogonalTiledMapRenderer(map, sb);

		// mapRenderer.setView(cam);

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
}
