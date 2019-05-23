package logging;

public class Logger {

    public enum Level {
        ALL(0), TRACE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5), FATAL(6), OFF(7);
        private Integer level;
        Level(int level) { this.level = level; }
        public boolean isWorseThan(Level other) { return this.level >= other.level;}
    }

    private static volatile Logger instance = null;
    private Level level;

    public Logger(Level level) {
        this.level = level == null ? Level.ERROR : level;
    }

    public static Logger getSingletonInstance(Level level) {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger(level);
                }
            }
        }
        return instance;
    }

    public void log(Level level, String message) {
        if (level.isWorseThan(this.level)) {
            if (System.err != null && level.isWorseThan(Level.ERROR))
                System.err.println(message);
            else
                System.out.println(message);
        }
    }
}
