package top.rgb39.ecs.executor;

public interface RuntimeLabel {
    String Startup = "$$Startup";
    String Event = "$$Event";
    String AfterEvent = "$$AfterEvent";
    String BeforeUpdate = "$$BeforeUpdate";
    String Update = "$$Update";
    String AfterUpdate = "$$AfterUpdate";
}
