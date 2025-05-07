public class BeatmapImporter {
    private BeatmapLoader beatmapLoader;
    private Game game;

    public BeatmapImporter(Game game) {
        this.game = game;
        this.beatmapLoader = new BeatmapLoader();
    }

    public void importBeatmap() {
        beatmapLoader.loadBeatmapWithFileChooser(game);
    }
}