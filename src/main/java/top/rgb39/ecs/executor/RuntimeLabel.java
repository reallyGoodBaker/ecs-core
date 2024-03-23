package top.rgb39.ecs.executor;

public interface RuntimeLabel {
    public static final String Startup = "$$Startup";
    public static final String Event = "$$Event";
    public static final String BeforeUpdate = "$$BeforeUpdate";
    public static final String Update = "$$Update";
    public static final String AfterUpdate = "$$AfterUpdate";
}
