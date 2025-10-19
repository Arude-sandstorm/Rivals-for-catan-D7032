// java
package ecs.components;

/**
 * Simple control marker: LOCAL or REMOTE.
 */
public class ControlComponent implements Component {
    public enum Type { LOCAL, REMOTE }
    public final Type type;
    public ControlComponent(Type type) { this.type = type; }
}
